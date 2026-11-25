package kosanku.util;

import java.sql.Connection;
import java.sql.SQLException;

//Transaction helper class
public final class Tx {
    private Tx() {}

    @FunctionalInterface
    public interface Work {
        void run(Connection conn) throws SQLException;
    }

    /** Runs all DAO calls in one transaction: all succeed or none do. */
    public static void run(Work work) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                work.run(conn);
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e; // validation errors (IllegalArgumentException) pass through unchanged
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Kesalahan database: " + e.getMessage(), e);
        }
    }
}