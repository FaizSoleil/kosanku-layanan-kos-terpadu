package kosanku.dao;

import kosanku.model.Expenses;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpensesDAO {

    public boolean insert(Expenses expense) {
        String sql = "INSERT INTO expenses (expense_date, kategori, jumlah) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(expense.getExpenseDate().getTime()));
            pstmt.setString(2, expense.getKategori());
            pstmt.setDouble(3, expense.getJumlah());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<Expenses> findAll() throws SQLException {
        List<Expenses> list = new ArrayList<>();
        String sql = "SELECT expense_date, kategori, jumlah FROM expenses";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Expenses(
                    rs.getInt("id"),
                    rs.getDate("expense_date"),
                    rs.getString("kategori"),
                    rs.getDouble("jumlah")
                ));
            }
        } catch (SQLException e) { throw e; }
        return list;
    }
}