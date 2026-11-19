package kosanku.model;

public enum RoomStatus {
    TERSEDIA("Tersedia"),
    TERISI("Terisi"),
    PERBAIKAN("Perbaikan");

    private final String dbValue;

    RoomStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    /** The exact string stored in the MySQL ENUM column. */
    public String getDbEnumValue() {
        return dbValue;
    }

    /** Converts a database string to the enum. Throws on unknown values so mistakes surface early. */
    public static RoomStatus fromDbToJavaConstant(String value) {
        for (RoomStatus s : values()) {
            if (s.dbValue.equals(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown room status: " + value);
    }

    /** JComboBox and JTable will display the label, e.g. "Tersedia". */
    @Override
    public String toString() {
        return dbValue;
    }
}

/*
Note for future me:
Keep in mind:
1. In Java code, use the enum (RoomStatus.TERISI). Never use raw strings like "Terisi".
2. When talking to MySQL, convert: getDbEnumValue() going in, fromDbToJavaConstant() coming out.
3. Keep SQL ENUM and text label storen inside the constant spelled exactly the same. This is the one rule you have to maintain by hand. If you ever add a status, you edit two places: the SQL ENUM and the Java enum, with matching text.

methods:
getDbEnumValue() says “give me the text MySQL knows this by”, and fromDbToJavaConstant() says “given MySQL’s text, find the Java constant”.

Contoh penggunaan:
RoomDAO maprows():
private Rooms mapRow(ResultSet rs) throws SQLException {
        return new Rooms(
            rs.getInt("id"),
            rs.getString("room_number"),
            rs.getString("type"),
            rs.getDouble("price"),
            rs.getString("facilities"),
            RoomStatus.fromDb(rs.getString("status"))
        );
    }

RoomDAO update():
public boolean update(Rooms room) {
        String sql = "UPDATE rooms SET room_number = ?, type = ?, price = ?, facilities = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setDouble(3, room.getPrice());
            pstmt.setString(4, room.getFacilities());
            pstmt.setString(5, room.getStatus().getDbValue()); <-- EXAMPLE HERE
            pstmt.setInt(6, room.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
*/