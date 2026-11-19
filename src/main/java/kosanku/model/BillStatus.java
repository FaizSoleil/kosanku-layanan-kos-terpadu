package kosanku.model;

public enum BillStatus {
    BELUM_DIBAYAR("Belum Dibayar"),
    SEBAGIAN("Sebagian"),
    LUNAS("Lunas");

    private final String dbValue;

    BillStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    /** The exact string stored in the MySQL ENUM column. */
    public String getDbEnumValue() {
        return dbValue;
    }

    /** Converts a database string to the enum. Throws on unknown values so mistakes surface early. */
    public static BillStatus fromDbToJavaConstant(String value) {
        for (BillStatus s : values()) {
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

*/