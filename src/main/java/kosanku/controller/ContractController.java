package kosanku.controller;

import java.util.Date;
import java.util.List;
import kosanku.model.Contract;
import kosanku.model.ContractStatus;
import kosanku.service.ContractService;

public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        if (contractService == null) {
            throw new IllegalArgumentException("ContractService wajib diisi.");
        }
        this.contractService = contractService;
    }

    public List<Contract> loadAll() {
        return contractService.getAll();
    }

    public void create(String tenantId, String roomId,
                   String tanggalMasuk, String tanggalKeluar,
                   String deposit, String tanggalJatuhTempo,
                   ContractStatus status) {
        contractService.create(buildContract(
            0, tenantId, roomId, tanggalMasuk, tanggalKeluar,
            deposit, tanggalJatuhTempo, status
        )
        );
    }

    public void edit(int id, String tenantId, String roomId,
                   String tanggalMasuk, String tanggalKeluar,
                   String deposit, String tanggalJatuhTempo,
                   ContractStatus status) {
        contractService.edit(buildContract(
            id, tenantId, roomId, tanggalMasuk, tanggalKeluar,
            deposit, tanggalJatuhTempo, status
        )
        );
    }

    public void delete(int id) {
        contractService.delete(id);
    }

    private Contract buildContract(int id, String tenantId, String roomId,
                               String tanggalMasuk, String tanggalKeluar,
                               String deposit, String tanggalJatuhTempo,
                               ContractStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status kontrak wajib dipilih.");
        }

        Contract contract = new Contract();

        contract.setId(id);
        contract.setTenantId(parseInteger(tenantId, "ID penyewa"));
        contract.setRoomId(parseInteger(roomId, "ID kamar"));
        contract.setTanggalMasuk(parseDate(tanggalMasuk, "Tanggal masuk"));
        contract.setTanggalKeluar(parseDate(tanggalKeluar, "Tanggal keluar"));
        contract.setTanggalJatuhTempo(parseDate(tanggalJatuhTempo, "Tanggal jatuh tempo"));
        contract.setDeposit(parseDeposit(deposit));
        contract.setStatus(status);

        return contract;
    }

    private int parseInteger(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " wajib diisi.");
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                throw new IllegalArgumentException(fieldName + " tidak valid.");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " harus berupa angka bulat.", e);
        }
    }

    private double parseDeposit(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Deposit wajib diisi.");
        }
        try {
            double deposit = Double.parseDouble(value.trim());
            if (!Double.isFinite(deposit)) {
                throw new IllegalArgumentException("Deposit tidak valid.");
            }
            if (deposit < 0) {
                throw new IllegalArgumentException("Deposit tidak boleh negatif.");
            }
            return deposit;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Deposit harus berupa angka, contoh 1500000 atau 1500000.50.",
                e
            );
        }
    }

    private Date parseDate(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " wajib diisi.");
        }

        try {
            return java.sql.Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                fieldName + " harus berformat yyyy-MM-dd, contoh 2026-11-24.",
                e
            );
        }
    }

    public void end(int id, ContractStatus finalStatus) {
        contractService.endContract(id, finalStatus);
    }
}
