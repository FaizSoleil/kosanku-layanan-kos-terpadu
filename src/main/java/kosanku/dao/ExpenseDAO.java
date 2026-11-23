package kosanku.dao;

import kosanku.model.Expense;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public boolean insert(Expense expense) {
        String sql = "INSERT INTO expenses (expense_date, kategori, jumlah) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, new java.sql.Date(expense.getExpenseDate().getTime()));
            pstmt.setString(2, expense.getDeskripsi());
            pstmt.setDouble(3, expense.getJumlah());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) expense.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Expense expense) {
        String sql = "UPDATE expenses SET expense_date = ?, kategori = ?, jumlah = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(expense.getExpenseDate().getTime()));
            pstmt.setString(2, expense.getDeskripsi());
            pstmt.setDouble(3, expense.getJumlah());
            pstmt.setInt(4, expense.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<Expense> findAll() {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT id, expense_date, kategori, jumlah FROM expenses";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Expense mapRow(ResultSet rs) throws SQLException {
        return new Expense(
            rs.getInt("id"),
            rs.getDate("expense_date"),
            rs.getString("kategori"),
            rs.getDouble("jumlah")
        );
    }
}