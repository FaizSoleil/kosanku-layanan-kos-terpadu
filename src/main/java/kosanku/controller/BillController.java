package kosanku.controller;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import kosanku.model.Bill;
import kosanku.service.BillService;

public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    public void create(String contractId, String bulanTagihan, String totalTagihan) {
        billService.create(toBill(0, contractId, bulanTagihan, totalTagihan));
    }

    public void edit(int id, String contractId, String bulanTagihan, String totalTagihan) {
        billService.update(toBill(id, contractId, bulanTagihan, totalTagihan));
    }

    public void delete(int id) {
        billService.delete(id);
    }

    private Bill toBill(int id, String contractId, String bulanTagihan, String totalTagihan) {
        Bill bill = new Bill();
        bill.setId(id);
        bill.setContractId(parseContractId(contractId));
        bill.setBulanTagihan(parseMonth(bulanTagihan));
        bill.setTotalTagihan(parseTotal(totalTagihan));
        return bill;
    }

    private int parseContractId(String value) {
        try {
            return Integer.parseInt(clean(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID kontrak harus berupa angka bulat.");
        }
    }

    private java.sql.Date parseMonth(String value) {
        try {
            return java.sql.Date.valueOf(YearMonth.parse(clean(value)).atDay(1));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Bulan tagihan harus berformat yyyy-MM, contoh 2026-11.");
        }
    }

    private double parseTotal(String value) {
        try {
            return Double.parseDouble(clean(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Total tagihan harus berupa angka, contoh 1500000 atau 1500000.50.");
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}