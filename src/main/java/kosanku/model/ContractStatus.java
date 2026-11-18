package kosanku.model;

public enum ContractStatus {
    AKTIF("Aktif"),
    SELESAI("Selesai"),
    DIBATALKAN("Dibatalkan");

    private final String dbValue;

    ContractStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    /** The exact string stored in the MySQL ENUM column. */
    public String getDbEnumValue() {
        return dbValue;
    }

    /** Converts a database string to the enum. Throws on unknown values so mistakes surface early. */
    public static ContractStatus fromDbToJavaConstant(String value) {
        for (ContractStatus s : values()) {
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
