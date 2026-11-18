package kosanku.test;

/***
 *
 * Make sure MySQL is running!
 * And youv'e ran schema.sql and seed.sql too!
 * Else all tests would fail automatically (obv :p)
 */

import kosanku.dao.*;
import kosanku.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("=== STARTING KOSANKU DAO TESTS ===\n");

        RoomsDAO roomsDAO = new RoomsDAO();
        System.out.println("Testing RoomsDAO...");
        Rooms testRoom = new Rooms("A101", "Deluxe", 1500000.0, "AC, WiFi, Hot Water", "Available");
        boolean roomInserted = roomsDAO.insert(testRoom);
        System.out.println("-> Insert Room: " + (roomInserted ? "SUCCESS (ID: " + testRoom.getId() + ")" : "FAILED"));

        if (roomInserted) {
            Optional<Rooms> foundRoom = roomsDAO.findById(testRoom.getId());
            System.out.println("-> Find Room by ID: " + (foundRoom.isPresent() ? "FOUND (" + foundRoom.get().getRoomNumber() + ")" : "NOT FOUND"));

            List<Rooms> allRooms = roomsDAO.findAll();
            System.out.println("-> Total Rooms in DB: " + allRooms.size());
        }

        TenantsDAO tenantsDAO = new TenantsDAO();
        System.out.println("\nTesting TenantsDAO...");
        String testEmail = "budi@example.com";
        Tenants testTenant = new Tenants(1, "Budi Santoso", testEmail, "081234567890");
        
        tenantsDAO.delete(testEmail);
        
        boolean tenantInserted = tenantsDAO.insert(testTenant);
        System.out.println("-> Insert Tenant: " + (tenantInserted ? "SUCCESS" : "FAILED"));

        if (tenantInserted) {
            Optional<Tenants> foundTenant = tenantsDAO.findByEmail(testEmail);
            System.out.println("-> Find Tenant by Email: " + (foundTenant.isPresent() ? "FOUND (" + foundTenant.get().getName() + ")" : "NOT FOUND"));
        }

        UsersDAO usersDAO = new UsersDAO();
        System.out.println("\nTesting UsersDAO...");
        String testUsername = "admin_test";
        Users testUser = new Users(testUsername, "password123", "System Administrator");

        usersDAO.delete(testUsername); // Clean up
        boolean userInserted = usersDAO.insert(testUser);
        System.out.println("-> Insert User: " + (userInserted ? "SUCCESS" : "FAILED"));

        if (userInserted) {
            Optional<Users> foundUser = usersDAO.findByUsername(testUsername);
            System.out.println("-> Find User by Username: " + (foundUser.isPresent() ? "FOUND" : "NOT FOUND"));
            
            usersDAO.delete(testUsername);
        }

        System.out.println("\n=== TESTS COMPLETED ===");
    }
}