package kosanku.dao;

import kosanku.model.ContractStatus;
import kosanku.model.Contract;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContractDAO {

    public Optional<Contract> findById(int id) {
        String sql = "SELECT * FROM contracts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public List<Contract> findAll() {
        List<Contract> list = new ArrayList<>();
        String sql = "SELECT * FROM contracts ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean update(Contract contract) {
        String sql = "UPDATE contracts SET tenant_id = ?, room_id = ?, tanggal_masuk = ?, tanggal_keluar = ?, deposit = ?, tanggal_jatuh_tempo = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contract.getTenantId());
            pstmt.setInt(2, contract.getRoomId());
            pstmt.setDate(3, new java.sql.Date(contract.getTanggalMasuk().getTime()));
            pstmt.setDate(4, contract.getTanggalKeluar() != null ? new java.sql.Date(contract.getTanggalKeluar().getTime()) : null);
            pstmt.setDouble(5, contract.getDeposit());
            pstmt.setDate(6, new java.sql.Date(contract.getTanggalJatuhTempo().getTime()));
            pstmt.setString(7, contract.getStatus().getDbEnumValue());
            pstmt.setInt(8, contract.getId());
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

    public boolean hasActiveContractForRoom(int roomId, int excludedContractId) {
        String sql = "SELECT 1 FROM contracts "
                   + "WHERE room_id = ? AND status = 'Aktif' AND id <> ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, excludedContractId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Contract mapRow(ResultSet rs) throws SQLException {
        return new Contract(
            rs.getInt("id"),
            rs.getInt("tenant_id"),
            rs.getInt("room_id"),
            rs.getDate("tanggal_masuk"),
            rs.getDate("tanggal_keluar"),
            rs.getDouble("deposit"),
            rs.getDate("tanggal_jatuh_tempo"),
            ContractStatus.fromDbToJavaConstant(rs.getString("status"))
        );
        // Note: Remember to call setId using the setter if your constructor doesn't set it.
    }

    public boolean insert(Connection conn, Contract contract) throws SQLException {
        String sql = "INSERT INTO contracts (tenant_id, room_id, tanggal_masuk, tanggal_keluar, deposit, tanggal_jatuh_tempo, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, contract.getTenantId());
            pstmt.setInt(2, contract.getRoomId());
            pstmt.setDate(3, new java.sql.Date(contract.getTanggalMasuk().getTime()));
            pstmt.setDate(4, new java.sql.Date(contract.getTanggalKeluar().getTime()));
            pstmt.setDouble(5, contract.getDeposit());
            pstmt.setDate(6, new java.sql.Date(contract.getTanggalJatuhTempo().getTime()));
            pstmt.setString(7, contract.getStatus().getDbEnumValue());
            if (pstmt.executeUpdate() > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) contract.setId(keys.getInt(1));
                }
                return true;
            }
            return false;
        }
    }

    public boolean insert(Contract contract) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return insert(conn, contract);
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public Optional<Contract> findById(Connection conn, int id) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM contracts WHERE id = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public boolean hasActiveContractForRoom(Connection conn, int roomId, int excludedContractId) throws SQLException {
        String sql = "SELECT 1 FROM contracts WHERE room_id = ? AND status = 'Aktif' AND id <> ? LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, excludedContractId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean updateStatus(Connection conn, int id, ContractStatus status) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE contracts SET status = ? WHERE id = ?")) {
            pstmt.setString(1, status.getDbEnumValue());
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}