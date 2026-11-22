package kosanku.dao;

import kosanku.model.Expense;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public boolean insert(Expense expense) {
        String sql = "INSERT INTO expenses (expense_date, kategori, jumlah) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(expense.getExpenseDate().getTime()));
            pstmt.setString(2, expense.getDeskripsi());
            pstmt.setDouble(3, expense.getJumlah());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<Expense> findAll() {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT id, expense_date, deskripsi, jumlah FROM expenses";
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
            rs.getString("deskripsi"),
            rs.getDouble("jumlah")
        );
    }
}