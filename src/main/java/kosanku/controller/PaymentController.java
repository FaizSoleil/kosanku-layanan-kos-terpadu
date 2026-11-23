package kosanku.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import kosanku.model.Payment;
import kosanku.service.PaymentService;

public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void create(String billId, String paymentDate, String jumlah) throws SQLException {
        paymentService.create(toPayment(0, billId, paymentDate, jumlah));
    }

    public void edit(int id, String billId, String paymentDate, String jumlah) throws SQLException {
        paymentService.update(toPayment(id, billId, paymentDate, jumlah));
    }

    public void delete(int id) throws SQLException {
        paymentService.delete(id);
    }

    private Payment toPayment(int id, String billId, String paymentDate, String jumlah) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setBillId(parseBillId(billId));
        payment.setPaymentDate(parseDate(paymentDate));
        payment.setJumlah(parseJumlah(jumlah));
        return payment;
    }

    private int parseBillId(String value) {
        try {
            return Integer.parseInt(clean(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID tagihan harus berupa angka bulat.");
        }
    }

    private java.sql.Date parseDate(String value) {
        try {
            return java.sql.Date.valueOf(LocalDate.parse(clean(value)));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Tanggal bayar harus berformat yyyy-MM-dd, contoh 2026-11-23.");
        }
    }

    private double parseJumlah(String value) {
        try {
            return Double.parseDouble(clean(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Jumlah harus berupa angka, contoh 1500000 atau 1500000.50.");
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}