package kosanku.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import kosanku.dao.BillDAO;
import kosanku.dao.ContractDAO;
import kosanku.dao.RoomDAO;
import kosanku.dao.TenantDAO;
import kosanku.model.Contract;
import kosanku.model.ContractStatus;
import kosanku.model.Room;
import kosanku.model.RoomStatus;
import kosanku.model.Tenant;
import kosanku.util.Tx;

public class ContractService {
    private static final double MAX_DEPOSIT = 9_999_999_999.99;

    private final ContractDAO contractDAO;
    private final TenantDAO tenantDAO;
    private final RoomDAO roomDAO;
    private final BillDAO billDAO;

    public ContractService(ContractDAO contractDAO) {
        if (contractDAO == null) {
            throw new IllegalArgumentException("ContractDAO wajib diisi.");
        }
        this.contractDAO = contractDAO;
        this.tenantDAO = new TenantDAO();
        this.roomDAO = new RoomDAO();
        this.billDAO = new BillDAO();
    }

    ContractService(ContractDAO contractDAO, TenantDAO tenantDAO, RoomDAO roomDAO, BillDAO billDAO) {
        if (contractDAO == null || tenantDAO == null || roomDAO == null || billDAO == null) {
            throw new IllegalArgumentException("Semua DAO wajib diisi.");
        }
        this.contractDAO = contractDAO;
        this.tenantDAO = tenantDAO;
        this.roomDAO = roomDAO;
        this.billDAO = billDAO;
    }

    public List<Contract> getAll() {
        return contractDAO.findAll();
    }

    public void create(Contract contract) {
        validate(contract);
        boolean active = contract.getStatus() == ContractStatus.AKTIF;

        // Read-only check, done outside the transaction because TenantDAO has no Connection overload.
        Tenant tenant = tenantDAO.findById(contract.getTenantId()).orElseThrow(() -> new IllegalArgumentException("Penyewa tidak ditemukan."));
        if (!tenant.isvisible()) {
            throw new IllegalArgumentException("Penyewa tidak aktif.");
        }

        Tx.run(conn -> {
            // Lock the room row FIRST so two simultaneous contracts for one room are serialized.
            RoomStatus roomStatus = roomDAO.findStatusForUpdate(conn, contract.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Kamar tidak ditemukan."));

            if (active) {
                if (roomStatus == RoomStatus.PERBAIKAN) {
                    throw new IllegalArgumentException("Kamar sedang dalam perbaikan.");
                }
                if (roomStatus == RoomStatus.TERISI) {
                    throw new IllegalArgumentException("Kamar sedang terisi.");
                }
                // Guard against drift: status says free, but an active contract exists.
                if (contractDAO.hasActiveContractForRoom(conn, contract.getRoomId(), 0)) {
                    throw new IllegalArgumentException("Kamar ini masih memiliki kontrak aktif.");
                }
            }

            if (!contractDAO.insert(conn, contract)) {
                throw new IllegalStateException("Gagal menyimpan kontrak.");
            }
            if (active && !roomDAO.updateStatus(conn, contract.getRoomId(), RoomStatus.TERISI)) {
                throw new IllegalStateException("Status kamar gagal diperbarui."); // rolls back the insert too
            }
        });
    }

    public void endContract(int contractId, ContractStatus finalStatus) {
        if (contractId <= 0) {
            throw new IllegalArgumentException("ID kontrak tidak valid.");
        }
        if (finalStatus != ContractStatus.SELESAI && finalStatus != ContractStatus.DIBATALKAN) {
            throw new IllegalArgumentException("Kontrak hanya dapat diakhiri dengan status Selesai atau Dibatalkan.");
        }

        Tx.run(conn -> {
            Contract existing = contractDAO.findById(conn, contractId)
                .orElseThrow(() -> new IllegalArgumentException("Kontrak tidak ditemukan."));
            if (existing.getStatus() != ContractStatus.AKTIF) {
                throw new IllegalArgumentException("Hanya kontrak aktif yang dapat diakhiri.");
            }
            if (finalStatus == ContractStatus.SELESAI
                    && billDAO.hasUnsettledBills(conn, contractId)) {
                throw new IllegalArgumentException("Kontrak masih memiliki tagihan yang belum lunas.");
            }

            if (!contractDAO.updateStatus(conn, contractId, finalStatus)) {
                throw new IllegalStateException("Gagal mengakhiri kontrak.");
            }

            // Free the room, but only if it is TERISI. Never overwrite PERBAIKAN.
            RoomStatus roomStatus = roomDAO.findStatusForUpdate(conn, existing.getRoomId()).orElse(null);
            if (roomStatus == RoomStatus.TERISI
                    && !contractDAO.hasActiveContractForRoom(conn, existing.getRoomId(), contractId)) {
                roomDAO.updateStatus(conn, existing.getRoomId(), RoomStatus.TERSEDIA);
            }
        });
    }

    public void edit(Contract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Data kontrak wajib diisi.");
        }
        if (contract.getId() <= 0) {
            throw new IllegalArgumentException("ID kontrak tidak valid.");
        }
        validate(contract);

        Contract existing = contractDAO.findById(contract.getId())
            .orElseThrow(() -> new IllegalStateException("Kontrak tidak ditemukan."));

        Tenant tenant = tenantDAO.findById(contract.getTenantId())
            .orElseThrow(() -> new IllegalArgumentException("Penyewa tidak ditemukan."));
        if (!tenant.isvisible()) {
            throw new IllegalArgumentException("Penyewa tidak aktif.");
        }

        Room room = roomDAO.findById(contract.getRoomId())
            .orElseThrow(() -> new IllegalArgumentException("Kamar tidak ditemukan."));
        if (room.getStatus() == RoomStatus.PERBAIKAN
                && contract.getStatus() == ContractStatus.AKTIF) {
            throw new IllegalArgumentException("Kamar sedang dalam perbaikan.");
        }
        if (contract.getStatus() == ContractStatus.AKTIF
                && contractDAO.hasActiveContractForRoom(contract.getRoomId(), contract.getId())) {
            throw new IllegalArgumentException("Kamar ini masih memiliki kontrak aktif.");
        }

        if (!contractDAO.update(contract)) {
            throw new IllegalStateException("Gagal mengubah kontrak.");
        }
        updateRoomStatus(existing, contract);
    }

    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID kontrak tidak valid.");
        }

        Contract existing = contractDAO.findById(id)
            .orElseThrow(() -> new IllegalStateException("Kontrak tidak ditemukan."));
        if (!contractDAO.delete(id)) {
            throw new IllegalStateException(
                "Kontrak tidak dapat dihapus. Kemungkinan masih memiliki tagihan.");
        }
        if (existing.getStatus() == ContractStatus.AKTIF) {
            setRoomAvailableIfNoActiveContract(existing.getRoomId());
        }
    }

    private void validate(Contract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Data kontrak wajib diisi.");
        }
        if (contract.getTenantId() <= 0) {
            throw new IllegalArgumentException("ID penyewa tidak valid.");
        }
        if (contract.getRoomId() <= 0) {
            throw new IllegalArgumentException("ID kamar tidak valid.");
        }
        if (contract.getTanggalMasuk() == null) {
            throw new IllegalArgumentException("Tanggal masuk wajib diisi.");
        }
        if (contract.getTanggalKeluar() == null) {
            throw new IllegalArgumentException("Tanggal keluar wajib diisi.");
        }
        if (contract.getTanggalJatuhTempo() == null) {
            throw new IllegalArgumentException("Tanggal jatuh tempo wajib diisi.");
        }
        if (contract.getStatus() == null) {
            throw new IllegalArgumentException("Status kontrak wajib dipilih.");
        }
        if (!contract.getTanggalJatuhTempo().after(contract.getTanggalMasuk())) {
            throw new IllegalArgumentException("Tanggal jatuh tempo harus setelah tanggal masuk.");
        }
        if (!contract.getTanggalKeluar().after(contract.getTanggalJatuhTempo())) {
            throw new IllegalArgumentException("Tanggal keluar harus setelah tanggal jatuh tempo.");
        }
        if (!Double.isFinite(contract.getDeposit()) || contract.getDeposit() < 0) {
            throw new IllegalArgumentException("Deposit tidak valid.");
        }

        double deposit = BigDecimal.valueOf(contract.getDeposit())
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
        if (deposit > MAX_DEPOSIT) {
            throw new IllegalArgumentException("Deposit terlalu besar.");
        }
        contract.setDeposit(deposit);
    }

    private void updateRoomStatus(Contract existing, Contract updated) {
        if (existing.getRoomId() != updated.getRoomId()) {
            setRoomAvailableIfNoActiveContract(existing.getRoomId());
        }

        if (updated.getStatus() == ContractStatus.AKTIF) {
            roomDAO.findById(updated.getRoomId()).ifPresent(room -> {
                room.setStatus(RoomStatus.TERISI);
                roomDAO.update(room);
            });
        } else {
            setRoomAvailableIfNoActiveContract(updated.getRoomId());
        }
    }

    private void setRoomAvailableIfNoActiveContract(int roomId) {
        if (contractDAO.hasActiveContractForRoom(roomId, 0)) {
            return;
        }
        roomDAO.findById(roomId).ifPresent(room -> {
            room.setStatus(RoomStatus.TERSEDIA);
            roomDAO.update(room);
        });
    }
}
