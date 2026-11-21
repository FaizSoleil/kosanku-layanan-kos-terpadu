package kosanku.service;

import kosanku.dao.UserDAO;
import kosanku.model.User;
import kosanku.util.PasswordUtil;

public class AuthService {
    private final UserDAO usersDAO;

    public AuthService(UserDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public User login(String username, String password) {
        // 1. Validasi input kosong
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong!");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong!");
        }

        // 2. Cari user dari database via UserDAO[cite: 7]
        User user = usersDAO.findByUsername(username.trim())
                .orElseThrow(() -> new IllegalArgumentException("Username atau password salah!"));

        // 3. Verifikasi hash password (SHA-256 + salt)
        String hashedInput = PasswordUtil.hashSHA256(password);
        if (!hashedInput.equals(user.getPassword())) {
            throw new IllegalArgumentException("Username atau password salah!");
        }

        return user;
    }
}