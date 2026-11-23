package kosanku.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import kosanku.dao.BillDAO;
import kosanku.model.Bill;
import kosanku.model.BillStatus;

public class BillService {

    private static final double MAX_TOTAL = 9_999_999_999.99;

    private final BillDAO billDAO;

    public BillService(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    public void create(Bill bill) {
        validate(bill);
        if (billDAO.existsForMonth(bill.getContractId(), bill.getBulanTagihan())) {
            throw new IllegalArgumentException("Tagihan untuk kontrak ini pada bulan tersebut sudah ada.");
        }
        bill.setStatus(BillStatus.BELUM_DIBAYAR);
        if (!billDAO.insert(bill)) {
            throw new IllegalStateException("Tagihan gagal disimpan. Pastikan ID kontrak ada.");
        }
    }

    public void update(Bill bill) {
        if (bill.getId() <= 0) {
            throw new IllegalArgumentException("ID tagihan tidak valid.");
        }
        validate(bill);

        Bill existing = billDAO.findById(bill.getId())
            .orElseThrow(() -> new IllegalStateException("Tagihan tidak ditemukan."));

        boolean keyChanged = existing.getContractId() != bill.getContractId()
            || !toLocalDate(existing.getBulanTagihan()).equals(toLocalDate(bill.getBulanTagihan()));
        if (keyChanged && billDAO.existsForMonth(bill.getContractId(), bill.getBulanTagihan())) {
            throw new IllegalArgumentException("Tagihan untuk kontrak ini pada bulan tersebut sudah ada.");
        }

        bill.setStatus(existing.getStatus());
        if (!billDAO.update(bill)) {
            throw new IllegalStateException("Tagihan gagal diperbarui. Pastikan ID kontrak ada.");
        }
    }

    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID tagihan tidak valid.");
        }
        if (billDAO.findById(id).isEmpty()) {
            throw new IllegalStateException("Tagihan tidak ditemukan.");
        }
        if (!billDAO.delete(id)) {
            throw new IllegalStateException("Tagihan gagal dihapus. Kemungkinan sudah memiliki pembayaran.");
        }
    }

    private void validate(Bill bill) {
        if (bill.getContractId() <= 0) {
            throw new IllegalArgumentException("ID kontrak harus lebih dari 0.");
        }
        if (bill.getBulanTagihan() == null) {
            throw new IllegalArgumentException("Bulan tagihan wajib diisi.");
        }
        double total = bill.getTotalTagihan();
        if (Double.isNaN(total) || Double.isInfinite(total)) {
            throw new IllegalArgumentException("Total tagihan tidak valid.");
        }
        total = BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        if (total <= 0) {
            throw new IllegalArgumentException("Total tagihan harus lebih dari 0.");
        }
        if (total > MAX_TOTAL) {
            throw new IllegalArgumentException("Total tagihan terlalu besar.");
        }
        bill.setTotalTagihan(total);
    }

    private LocalDate toLocalDate(java.util.Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}