package kosanku.service;

import java.util.List;
import kosanku.dao.ExpenseDAO;
import kosanku.model.Expense;

public class ExpenseService {
    private final ExpenseDAO expenseDAO;

    public ExpenseService(ExpenseDAO expenseDAO) {
        if (expenseDAO == null) {
            throw new IllegalArgumentException("ExpenseDAO wajib diisi.");
        }
        this.expenseDAO = expenseDAO;
    }

    public List<Expense> getAll() {
        return expenseDAO.findAll();
    }

    public void create(Expense expense) {
        validate(expense);
        if (!expenseDAO.insert(expense)) {
            throw new IllegalStateException("Gagal menyimpan pengeluaran.");
        }
    }

    public void edit(Expense expense) {
        validate(expense);
        if (expense.getId() <= 0) {
            throw new IllegalArgumentException("ID pengeluaran tidak valid.");
        }
        if (!expenseDAO.update(expense)) {
            throw new IllegalStateException("Gagal mengubah pengeluaran.");
        }
    }

    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID pengeluaran tidak valid.");
        }
        if (!expenseDAO.delete(id)) {
            throw new IllegalStateException("Gagal menghapus pengeluaran.");
        }
    }

    private void validate(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Data pengeluaran wajib diisi.");
        }
        if (expense.getExpenseDate() == null) {
            throw new IllegalArgumentException("Tanggal wajib diisi.");
        }
        if (expense.getDeskripsi() == null || expense.getDeskripsi().isBlank()) {
            throw new IllegalArgumentException("Deskripsi wajib diisi.");
        }
        if (!Double.isFinite(expense.getJumlah()) || expense.getJumlah() <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        }
    }
}