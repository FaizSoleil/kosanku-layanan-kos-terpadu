package kosanku.dao;

import kosanku.model.Contracts;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContractsDAO {

    public boolean insert(Contracts contract) {
        String sql = "INSERT INTO contracts (tenant_id, room_id, tanggal_masuk, tanggal_keluar, deposit, tanggal_jatuh_tempo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, contract.getTenantId());
            pstmt.setInt(2, contract.getRoomId());
            pstmt.setDate(3, new java.sql.Date(contract.getTanggalMasuk().getTime()));
            pstmt.setDate(4, contract.getTanggalKeluar() != null ? new java.sql.Date(contract.getTanggalKeluar().getTime()) : null);
            pstmt.setDouble(5, contract.getDeposit());
            pstmt.setDate(6, new java.sql.Date(contract.getTanggalJatuhTempo().getTime()));

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) contract.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public Optional<Contracts> findById(int id) {
        String sql = "SELECT id, tenant_id, room_id, tanggal_masuk, tanggal_keluar, deposit, tanggal_jatuh_tempo FROM contracts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public List<Contracts> findAll() {
        List<Contracts> list = new ArrayList<>();
        String sql = "SELECT id, tenant_id, room_id, tanggal_masuk, tanggal_keluar, deposit, tanggal_jatuh_tempo FROM contracts";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean update(Contracts contract) {
        String sql = "UPDATE contracts SET tenant_id = ?, room_id = ?, tanggal_masuk = ?, tanggal_keluar = ?, deposit = ?, tanggal_jatuh_tempo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contract.getTenantId());
            pstmt.setInt(2, contract.getRoomId());
            pstmt.setDate(3, new java.sql.Date(contract.getTanggalMasuk().getTime()));
            pstmt.setDate(4, contract.getTanggalKeluar() != null ? new java.sql.Date(contract.getTanggalKeluar().getTime()) : null);
            pstmt.setDouble(5, contract.getDeposit());
            pstmt.setDate(6, new java.sql.Date(contract.getTanggalJatuhTempo().getTime()));
            pstmt.setInt(7, contract.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM contracts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Contracts mapRow(ResultSet rs) throws SQLException {
        return new Contracts(
            rs.getInt("tenant_id"),
            rs.getInt("room_id"),
            rs.getDate("tanggal_masuk"),
            rs.getDate("tanggal_keluar"),
            rs.getDouble("deposit"),
            rs.getDate("tanggal_jatuh_tempo")
        );
        // Note: Remember to call setId using the setter if your constructor doesn't set it.
    }
}