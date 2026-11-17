/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package kosanku;

import kosanku.controller.AuthController;
import kosanku.dao.UsersDAO;
import kosanku.service.AuthService;
import kosanku.ui.LoginForm;
import kosanku.util.PasswordUtil;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Cetak hash password 'admin123' ke Output NetBeans
        System.out.println("Hash admin123: " + PasswordUtil.hashSHA256("admin123"));

        SwingUtilities.invokeLater(() -> {
            UsersDAO usersDAO = new UsersDAO();
            AuthService authService = new AuthService(usersDAO);
            AuthController authController = new AuthController(authService);

            LoginForm loginForm = new LoginForm(authController);
            authController.setLoginView(loginForm);

            loginForm.setVisible(true);
        });
    }
}