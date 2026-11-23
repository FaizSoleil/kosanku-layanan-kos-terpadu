package kosanku.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import kosanku.model.Expense;
import kosanku.service.ExpenseService;
import kosanku.ui.panel.ExpensePanel;

public class ExpenseController {
    private final ExpenseService expenseService;
    private ExpensePanel expenseView;

    public ExpenseController(ExpenseService expenseService) {
        if (expenseService == null) {
            throw new IllegalArgumentException("ExpenseService wajib diisi.");
        }
        this.expenseService = expenseService;
    }

    public void setExpenseView(ExpensePanel expenseView) {
        this.expenseView = expenseView;
    }

    public List<Expense> loadAll() {
        return expenseService.getAll();
    }

    public void create(Date expenseDate, String deskripsi, String jumlahText) {
        expenseService.create(buildExpense(null, expenseDate, deskripsi, jumlahText));
    }

    public void create(Date expenseDate, String deskripsi, double jumlah) {
        expenseService.create(buildExpense(null, expenseDate, deskripsi, jumlah));
    }

    public void create(String tanggalText, String deskripsi, String jumlahText) {
        expenseService.create(buildExpense(null, parseDate(tanggalText), deskripsi, jumlahText));
    }

    public void edit(int id, Date expenseDate, String deskripsi, String jumlahText) {
        expenseService.edit(buildExpense(id, expenseDate, deskripsi, jumlahText));
    }

    public void edit(int id, Date expenseDate, String deskripsi, double jumlah) {
        expenseService.edit(buildExpense(id, expenseDate, deskripsi, jumlah));
    }

    public void edit(int id, String tanggalText, String deskripsi, String jumlahText) {
        expenseService.edit(buildExpense(id, parseDate(tanggalText), deskripsi, jumlahText));
    }

    public void delete(int id) {
        expenseService.delete(id);
    }

    private Expense buildExpense(Integer id, Date expenseDate, String deskripsi, String jumlahText) {
        if (jumlahText == null) {
            throw new IllegalArgumentException("Jumlah harus berupa angka.");
        }
        final double jumlah;
        try {
            jumlah = Double.parseDouble(jumlahText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Jumlah harus berupa angka.");
        }
        return buildExpense(id, expenseDate, deskripsi, jumlah);
    }

    private Expense buildExpense(Integer id, Date expenseDate, String deskripsi, double jumlah) {
        Expense expense = new Expense();
        expense.setId(id == null ? 0 : id);
        expense.setExpenseDate(expenseDate);
        expense.setDeskripsi(deskripsi == null ? null : deskripsi.trim());
        expense.setJumlah(jumlah);
        return expense;
    }

    private Date parseDate(String tanggalText) {
        if (tanggalText == null || tanggalText.trim().isEmpty()) {
            throw new IllegalArgumentException("Tanggal wajib diisi.");
        }
        String value = tanggalText.trim();
        for (String pattern : new String[]{"yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yy"}) {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            formatter.setLenient(false);
            try {
                return formatter.parse(value);
            } catch (ParseException ignored) {
                // Coba format tanggal berikutnya.
            }
        }
        throw new IllegalArgumentException("Format tanggal tidak valid. Gunakan yyyy-MM-dd.");
    }
}