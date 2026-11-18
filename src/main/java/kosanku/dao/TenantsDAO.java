package kosanku.dao;

import kosanku.model.Tenants;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TenantsDAO {

    public boolean insert(Tenants tenant) {
        String sql = "INSERT INTO tenants (name, email, phone_number) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenant.getName());
            pstmt.setString(2, tenant.getEmail());
            pstmt.setString(3, tenant.getPhoneNumber());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public Optional<Tenants> findByEmail(String email) {
        String sql = "SELECT id, name, email, phone_number FROM tenants WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public List<Tenants> findAll() throws SQLException {
        List<Tenants> list = new ArrayList<>();
        String sql = "SELECT * FROM tenants";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw e; } //throw biar propagasi ke atas.
        return list;
    }

    public boolean update(Tenants tenant) {
        String sql = "UPDATE tenants SET name = ?, phone_number = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenant.getName());
            pstmt.setString(2, tenant.getPhoneNumber());
            pstmt.setString(3, tenant.getEmail());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(String email) {
        String sql = "DELETE FROM tenants WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Tenants mapRow(ResultSet rs) throws SQLException {
        return new Tenants(rs.getInt("id"), rs.getString("gov_id_number") ,rs.getString("name"), rs.getString("email"), rs.getString("phone_number"));
    }
}