package kosanku.dao;

import kosanku.model.RoomStatus;
import kosanku.model.Room;
import kosanku.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO {
    /* Insert creates its own id */
    public boolean insert(Room room) {
        String sql = "INSERT INTO rooms (room_number, price, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setDouble(2, room.getPrice());
            pstmt.setString(3, room.getStatus().getDbEnumValue());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) room.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean insertWithId(Room room) {
        String sql = "INSERT INTO rooms (id, room_number, price, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, room.getId());
            pstmt.setString(2, room.getRoomNumber());
            pstmt.setDouble(3, room.getPrice());
            pstmt.setString(4, room.getStatus().getDbEnumValue());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public Optional<Room> findById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public Optional<Room> findByRoomNumber(String room_number) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room_number);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public List<Room> findAll() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean update(Room room) {
        String sql = "UPDATE rooms SET room_number = ?, price = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setDouble(2, room.getPrice());
            pstmt.setString(3, room.getStatus().getDbEnumValue());
            pstmt.setInt(4, room.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        return new Room(
            rs.getInt("id"),
            rs.getString("room_number"),
            rs.getDouble("price"),
            RoomStatus.fromDbToJavaConstant(rs.getString("status"))
        );
    }

    /** Locking read: a second transaction on the same room waits here until the first commits. */
    public Optional<RoomStatus> findStatusForUpdate(Connection conn, int roomId) throws SQLException {
        String sql = "SELECT status FROM rooms WHERE id = ? FOR UPDATE";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.of(RoomStatus.fromDbToJavaConstant(rs.getString("status"))) : Optional.empty();
            }
        }
    }

    public boolean updateStatus(Connection conn, int roomId, RoomStatus status) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE rooms SET status = ? WHERE id = ?")) {
            pstmt.setString(1, status.getDbEnumValue());
            pstmt.setInt(2, roomId);
            return pstmt.executeUpdate() > 0;
        }
    }
}