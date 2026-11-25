package kosanku.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import kosanku.dao.PaymentDAO;
import kosanku.model.Payment;
import kosanku.dao.BillDAO;
import kosanku.model.Bill;
import kosanku.model.BillStatus;

public class PaymentService {

    private static final double MAX_JUMLAH = 9_999_999_999.99; // DECIMAL(12, 2)

    private final PaymentDAO paymentDAO;
    private final BillDAO billDAO;
    private final BillService billService;

    public PaymentService(PaymentDAO paymentDAO) {
        this(paymentDAO, new BillDAO(), new BillService());
    }

    public PaymentService(PaymentDAO paymentDAO, BillDAO billDAO, BillService billService) {
        this.paymentDAO = paymentDAO;
        this.billDAO = billDAO;
        this.billService = billService;
    }

    private void checkBill(Payment payment) {
        billDAO.findById(payment.getBillId()).orElseThrow(() -> new IllegalArgumentException("Tagihan dengan ID " + payment.getBillId() + " tidak ditemukan."));
    }

    public void create(Payment payment) throws SQLException {
        validate(payment);
        checkBill(payment);
        if (!paymentDAO.insert(payment)) {
            throw new IllegalStateException("Pembayaran gagal disimpan.");
        }
        billService.recalculateStatus(payment.getBillId());
    }

    public void update(Payment payment) throws SQLException {
        if (payment.getId() <= 0) {
            throw new IllegalArgumentException("ID pembayaran tidak valid.");
        }
        validate(payment);
        Payment old = paymentDAO.findById(payment.getId())
            .orElseThrow(() -> new IllegalStateException("Pembayaran tidak ditemukan."));
        checkBill(payment);
        if (!paymentDAO.update(payment)) {
            throw new IllegalStateException("Pembayaran gagal diperbarui.");
        }
        billService.recalculateStatus(payment.getBillId());
        if (old.getBillId() != payment.getBillId()) {
            billService.recalculateStatus(old.getBillId());   // payment moved to another bill
        }
    }

    public void delete(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID pembayaran tidak valid.");
        }
        Payment old = paymentDAO.findById(id).orElseThrow(() -> new IllegalStateException("Pembayaran tidak ditemukan."));
        if (!paymentDAO.delete(id)) {
            throw new IllegalStateException("Pembayaran gagal dihapus.");
        }
        billService.recalculateStatus(old.getBillId());
    }

    // private void checkBill(Payment payment) throws SQLException {
    //     Bill bill = billDAO.findById(payment.getBillId())
    //         .orElseThrow(() -> new IllegalArgumentException("Tagihan dengan ID " + payment.getBillId() + " tidak ditemukan."));
    //     BigDecimal total = BigDecimal.valueOf(bill.getTotalTagihan());
    //     BigDecimal paidByOthers = paymentDAO.sumByBillId(payment.getBillId(), payment.getId());
    //     if (paidByOthers.add(BigDecimal.valueOf(payment.getJumlah())).compareTo(total) > 0) {
    //         throw new IllegalArgumentException("Jumlah melebihi sisa tagihan (" + total.subtract(paidByOthers).toPlainString() + ").");
    //     }
    // }

    private void refreshBillStatus(int billId) throws SQLException {
        Bill bill = billDAO.findById(billId).orElse(null);
        if (bill == null) return;
        BigDecimal paid = paymentDAO.sumByBillId(billId, 0);
        BigDecimal total = BigDecimal.valueOf(bill.getTotalTagihan());

        BillStatus status;
        if (paid.signum() == 0) status = BillStatus.BELUM_DIBAYAR;
        else if (paid.compareTo(total) >= 0) status = BillStatus.LUNAS;
        else status = BillStatus.SEBAGIAN;

        billDAO.updateStatus(billId, status);
    }

    private void validate(Payment payment) {
        if (payment.getBillId() <= 0) {
            throw new IllegalArgumentException("ID tagihan harus lebih dari 0.");
        }
        if (payment.getPaymentDate() == null) {
            throw new IllegalArgumentException("Tanggal bayar wajib diisi.");
        }
        double jumlah = payment.getJumlah();
        if (Double.isNaN(jumlah) || Double.isInfinite(jumlah)) {
            throw new IllegalArgumentException("Jumlah tidak valid.");
        }
        // Match the DECIMAL(12, 2) column before checking the > 0 rule
        jumlah = BigDecimal.valueOf(jumlah).setScale(2, RoundingMode.HALF_UP).doubleValue();
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        }
        if (jumlah > MAX_JUMLAH) {
            throw new IllegalArgumentException("Jumlah terlalu besar.");
        }
        payment.setJumlah(jumlah);
    }
}