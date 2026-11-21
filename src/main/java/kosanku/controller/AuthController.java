package kosanku.controller;

import kosanku.model.User;
import kosanku.service.AuthService;
import kosanku.ui.DashboardForm;
import kosanku.ui.LoginForm;

import javax.swing.JOptionPane;

public class AuthController {
    private final AuthService authService;
    private LoginForm loginView;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void setLoginView(LoginForm loginView) {
        this.loginView = loginView;
    }

    public void handleLogin(String username, String password) {
        try {
            User loggedInUser = authService.login(username, password);

            JOptionPane.showMessageDialog(loginView,
                "Selamat datang, " + loggedInUser.getNamaLengkap() + "!",
                "Login Berhasil",
                JOptionPane.INFORMATION_MESSAGE);

            if (loginView != null) {
                loginView.dispose();
            }

            // Buka form Dasbor setelah login berhasil
            DashboardForm dashboard = new DashboardForm(loggedInUser);
            dashboard.setVisible(true);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(loginView,
                ex.getMessage(),
                "Login Gagal",
                JOptionPane.WARNING_MESSAGE);
            if (loginView != null) {
                loginView.resetPasswordField();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(loginView,
                "Terjadi kesalahan sistem: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}