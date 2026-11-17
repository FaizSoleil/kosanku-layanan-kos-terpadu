package kosanku.service;

import kosanku.dao.UsersDAO;
import kosanku.model.Users;
import kosanku.util.PasswordUtil;

public class AuthService {
    private final UsersDAO usersDAO;

    public AuthService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Users login(String username, String password) {
        // 1. Validasi input kosong
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong!");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong!");
        }

        // 2. Cari user dari database via UsersDAO[cite: 7]
        Users user = usersDAO.findByUsername(username.trim())
                .orElseThrow(() -> new IllegalArgumentException("Username atau password salah!"));

        // 3. Verifikasi hash password (SHA-256 + salt)
        String hashedInput = PasswordUtil.hashSHA256(password);
        if (!hashedInput.equals(user.getPassword())) {
            throw new IllegalArgumentException("Username atau password salah!");
        }

        return user;
    }
}