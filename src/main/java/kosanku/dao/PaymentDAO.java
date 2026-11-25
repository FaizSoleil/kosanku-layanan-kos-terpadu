package kosanku.dao;

import kosanku.model.Payment;
import kosanku.util.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDAO {

    public boolean insert(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (bill_id, payment_date, jumlah) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payment.getBillId());
            pstmt.setDate(2, new java.sql.Date(payment.getPaymentDate().getTime()));
            pstmt.setBigDecimal(3, BigDecimal.valueOf(payment.getJumlah()));
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean update(Payment payment) throws SQLException {
        String sql = "UPDATE payments SET bill_id = ?, payment_date = ?, jumlah = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payment.getBillId());
            pstmt.setDate(2, new java.sql.Date(payment.getPaymentDate().getTime()));
            pstmt.setBigDecimal(3, BigDecimal.valueOf(payment.getJumlah()));
            pstmt.setInt(4, payment.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Payment> findAll() throws SQLException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) { throw e; }
        return list;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("id"),
            rs.getInt("bill_id"),
            rs.getDate("payment_date"),
            rs.getDouble("jumlah")
        );
    }

    public Optional<Payment> findById(int id) throws SQLException {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public BigDecimal sumByBillId(int billId, int excludePaymentId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(jumlah), 0) FROM payments WHERE bill_id = ? AND id <> ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, billId);
            pstmt.setInt(2, excludePaymentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getBigDecimal(1);
            }
        }
    }

    // PaymentDAO
    public BigDecimal sumByBillId(Connection conn, int billId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(jumlah), 0) FROM payments WHERE bill_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBigDecimal(1);
            }
        }
    }
}