package kosanku.dao;

import kosanku.model.Payments;
import kosanku.model.RoomStatus;
import kosanku.model.Rooms;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentsDAO {

    public boolean insert(Payments payment) {
        String sql = "INSERT INTO payments (payment_date, jumlah) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(payment.getPaymentDate().getTime()));
            pstmt.setDouble(2, payment.getJumlah());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<Payments> findAll() throws SQLException {
        List<Payments> list = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) { throw e; }
        return list;
    }

    private Payments mapRow(ResultSet rs) throws SQLException {
        return new Payments(
            rs.getInt("id"),
            rs.getInt("bill_id"),
            rs.getDate("payment_data"),
            rs.getDouble("jumlah")
        );
    }
}