package kosanku.dao;

import kosanku.model.BillStatus;
import kosanku.model.Bill;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillDAO {

    private static final String COLUMNS = "id, contract_id, bulan_tagihan, total_tagihan, status";

    // MySQL error codes
    private static final int ER_DUP_ENTRY = 1062;           // UNIQUE (contract_id, bulan_tagihan) violated
    private static final int ER_ROW_IS_REFERENCED = 1451;   // RESTRICT: bill still has contracts
    private static final int ER_NO_REFERENCED_ROW = 1452;   // contract_id does not exist

    public boolean insert(Bill bill) {
        String sql = "INSERT INTO bills (contract_id, bulan_tagihan, total_tagihan, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, bill.getContractId());
            pstmt.setDate(2, new java.sql.Date(bill.getBulanTagihan().getTime()));
            pstmt.setDouble(3, bill.getTotalTagihan());
            pstmt.setString(4, bill.getStatus().getDbEnumValue());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) bill.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    public Optional<Bill> findById(int id) {
        String sql = "SELECT " + COLUMNS + " FROM bills WHERE id = ?";
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

    public List<Bill> findAll() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT " + COLUMNS + " FROM bills ORDER BY bulan_tagihan DESC, id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logError(e);
        }
        return list;
    }

    /** All bills of one contract, oldest month first. */
    public List<Bill> findByContractId(int contractId) {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT " + COLUMNS + " FROM bills WHERE contract_id = ? ORDER BY bulan_tagihan";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contractId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logError(e);
        }
        return list;
    }

    /** All bills with a given status, e.g. every unpaid bill. */
    public List<Bill> findByStatus(BillStatus status) {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT " + COLUMNS + " FROM bills WHERE status = ? ORDER BY bulan_tagihan";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.getDbEnumValue());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logError(e);
        }
        return list;
    }

    /** Lets the UI show a friendly message before inserting. The UNIQUE constraint is still the real guard. */
    public boolean existsForMonth(int contractId, java.util.Date bulanTagihan) {
        String sql = "SELECT 1 FROM bills WHERE contract_id = ? AND bulan_tagihan = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contractId);
            pstmt.setDate(2, new java.sql.Date(bulanTagihan.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    public boolean update(Bill bill) {
        String sql = "UPDATE bills SET contract_id = ?, bulan_tagihan = ?, total_tagihan = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bill.getContractId());
            pstmt.setDate(2, new java.sql.Date(bill.getBulanTagihan().getTime()));
            pstmt.setDouble(3, bill.getTotalTagihan());
            pstmt.setString(4, bill.getStatus().getDbEnumValue());
            pstmt.setInt(5, bill.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    /** Updates only the status, using its own connection. */
    public boolean updateStatus(int id, BillStatus status) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return updateStatus(conn, id, status);
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    /**
     * Updates only the status on a connection you pass in, so it can run in the same
     * transaction as a payment insert. The caller owns commit/rollback.
     */
    public boolean updateStatus(Connection conn, int id, BillStatus status) throws SQLException {
        String sql = "UPDATE bills SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.getDbEnumValue());
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM bills WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    private Bill mapRow(ResultSet rs) throws SQLException {
        return new Bill(
            rs.getInt("id"),
            rs.getInt("contract_id"),
            rs.getDate("bulan_tagihan"),
            rs.getDouble("total_tagihan"),
            BillStatus.fromDbToJavaConstant(rs.getString("status"))
        );
    }

    /** Short message for the errors we expect, full stack trace for anything else. */
    private void logError(SQLException e) {
        switch (e.getErrorCode()) {
            case ER_DUP_ENTRY:
                System.err.println("Tagihan untuk kontrak dan bulan tersebut sudah ada.");
                break;
            case ER_NO_REFERENCED_ROW:
                System.err.println("Kontrak tidak ditemukan.");
                break;
            case ER_ROW_IS_REFERENCED:
                System.err.println("Tagihan masih memiliki kontrak, tidak bisa dihapus.");
                break;
            default:
                e.printStackTrace();
        }
    }
}
