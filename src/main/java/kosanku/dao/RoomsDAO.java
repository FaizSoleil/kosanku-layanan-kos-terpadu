package kosanku.dao;

import kosanku.model.RoomStatus;
import kosanku.model.Rooms;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomsDAO {
    // MySQL error codes
    private static final int ER_DUP_ENTRY = 1062;          // id_number or email already used
    private static final int ER_ROW_IS_REFERENCED = 1451;  // RESTRICT: tenant still has contracts

    public boolean insert(Rooms room) {
        String sql = "INSERT INTO rooms (room_number, price, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setDouble(3, room.getPrice());
            pstmt.setString(5, room.getStatus().getDbEnumValue());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) room.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) { logError(e);; }
        return false;
    }

    public Optional<Rooms> findById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { logError(e);; }
        return Optional.empty();
    }

    public List<Rooms> findAll() {
        List<Rooms> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean update(Rooms room) {
        String sql = "UPDATE rooms SET room_number = ?, price = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setDouble(2, room.getPrice());
            pstmt.setString(3, room.getStatus().getDbEnumValue());
            pstmt.setInt(4, room.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { logError(e); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { logError(e); }
        return false;
    }

    private Rooms mapRow(ResultSet rs) throws SQLException {
        return new Rooms(
            rs.getInt("id"),
            rs.getString("room_number"),
            rs.getDouble("price"),
            RoomStatus.fromDbToJavaConstant(rs.getString("status"))
        );
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