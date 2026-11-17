/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package kosanku;

import kosanku.util.DatabaseConnection;
import java.sql.Connection;

/**
 *
 *
 */
public class Main {

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("terhubung");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
