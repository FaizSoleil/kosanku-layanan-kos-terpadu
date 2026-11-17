package kosanku.dao;

import kosanku.model.Bills;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillsDAO {

    public boolean insert(Bills bill) {
        String sql = "INSERT INTO bills (contract_id, bulan_tagihan, total_tagihan, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, bill.getContractId());
            pstmt.setDate(2, new java.sql.Date(bill.getBulanTagihan().getTime()));
            pstmt.setDouble(3, bill.getTotalTagihan());
            pstmt.setString(4, bill.getStatus());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) bill.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public Optional<Bills> findById(int id) {
        String sql = "SELECT id, contract_id, bulan_tagihan, total_tagihan, status FROM bills WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public List<Bills> findAll() {
        List<Bills> list = new ArrayList<>();
        String sql = "SELECT id, contract_id, bulan_tagihan, total_tagihan, status FROM bills";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean update(Bills bill) {
        String sql = "UPDATE bills SET contract_id = ?, bulan_tagihan = ?, total_tagihan = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bill.getContractId());
            pstmt.setDate(2, new java.sql.Date(bill.getBulanTagihan().getTime()));
            pstmt.setDouble(3, bill.getTotalTagihan());
            pstmt.setString(4, bill.getStatus());
            pstmt.setInt(5, bill.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM bills WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Bills mapRow(ResultSet rs) throws SQLException {
        Bills bill = new Bills(
            rs.getInt("contract_id"),
            rs.getDate("bulan_tagihan"),
            rs.getDouble("total_tagihan"),
            rs.getString("status")
        );
        bill.setId(rs.getInt("id"));
        return bill;
    }
}