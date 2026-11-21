package kosanku.dao;

import kosanku.model.Tenants;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class TenantsDAO {
    private static final String COLUMNS = "id, id_number, name, email, phone_number, is_visible";

    // MySQL error codes
    private static final int ER_DUP_ENTRY = 1062;          // id_number or email already used
    private static final int ER_ROW_IS_REFERENCED = 1451;  // RESTRICT: tenant still has contracts

    public boolean insert(Tenants tenant) {
        String sql = "INSERT INTO tenants (id_number, name, email, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tenant.getIdNumber());
            pstmt.setString(2, tenant.getName());
            pstmt.setString(3, emptyToNull(tenant.getEmail()));
            pstmt.setString(4, tenant.getPhoneNumber());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) tenant.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    // The find methods do NOT filter by is_visible, so you can still find a deactivated tenant
    // (for example to reactivate them, or to explain why a NIK/email is already taken).
    public Optional<Tenants> findById(int id) {
        String sql = "SELECT " + COLUMNS + " FROM tenants WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            logError(e);
        }
        return Optional.empty();
    }

    public Optional<Tenants> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) return Optional.empty();
        String sql = "SELECT " + COLUMNS + " FROM tenants WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            logError(e);
        }
        return Optional.empty();
    }

    public Optional<Tenants> findByIdNumber(String idNumber) {
        String sql = "SELECT " + COLUMNS + " FROM tenants WHERE id_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            logError(e);
        }
        return Optional.empty();
    }

    /** visible tenants only (what the main list screen should show). */
    public List<Tenants> findAll() {
        return findAll(false);
    }

    /** Pass true to also include deactivated tenants (for a "show invisible" checkbox). */
    public List<Tenants> findAll(boolean includeInvisible) {
        List<Tenants> list = new ArrayList<>();
        String sql = "SELECT " + COLUMNS + " FROM tenants"
                   + (includeInvisible ? "" : " WHERE is_visible = TRUE")
                   + " ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logError(e);
        }
        return list;
    }

    /** Updates by id, because email is optional and cannot identify a row. */
    public boolean update(Tenants tenant) {
        String sql = "UPDATE tenants SET id_number = ?, name = ?, email = ?, phone_number = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenant.getIdNumber());
            pstmt.setString(2, tenant.getName());
            pstmt.setString(3, emptyToNull(tenant.getEmail()));
            pstmt.setString(4, tenant.getPhoneNumber());
            pstmt.setInt(5, tenant.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    /** True if the tenant has any contract at all (visible or finished). */
    public boolean hasContracts(int tenantId) {
        String sql = "SELECT 1 FROM contracts WHERE tenant_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tenantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); //returns true if the new current row is valid.
            }
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    /** True if the tenant has a contract with status 'Aktif'. */
    public boolean hasVisibleContract(int tenantId) {
        String sql = "SELECT 1 FROM contracts WHERE tenant_id = ? AND status = 'Aktif' LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tenantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    /**
     * Real DELETE. Only works for tenants with no contracts at all;
     * otherwise the RESTRICT foreign key refuses and this returns false.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM tenants WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    /** Soft delete: hides the tenant but keeps their history. Refused while they have an visible contract. */
    public boolean deactivate(int id) {
        if (hasVisibleContract(id)) {
            System.err.println("Penyewa masih memiliki kontrak aktif. Akhiri kontrak terlebih dahulu.");
            return false;
        }
        return setvisible(id, false);
    }

    public boolean reactivate(int id) {
        return setvisible(id, true);
    }

    private boolean setvisible(int id, boolean visible) {
        String sql = "UPDATE tenants SET is_visible = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, visible);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    private Tenants mapRow(ResultSet rs) throws SQLException {
        return new Tenants(
            rs.getInt("id"),
            rs.getString("id_number"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone_number"),
            rs.getBoolean("is_visible")
        );
    }

    /** The email column is UNIQUE and nullable: an empty string would collide, so store NULL instead. */
    private String emptyToNull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }

    /** Short message for the errors we expect, full stack trace for anything else. */
    private void logError(SQLException e) {
        switch (e.getErrorCode()) {
            case ER_DUP_ENTRY:
                System.err.println("NIK atau email sudah terdaftar.");
                break;
            case ER_ROW_IS_REFERENCED:
                System.err.println("Penyewa masih memiliki kontrak, tidak bisa dihapus. Nonaktifkan saja.");
                break;
            default:
                e.printStackTrace();
        }
    }
}