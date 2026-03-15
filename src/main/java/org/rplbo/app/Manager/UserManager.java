package org.rplbo.app.Manager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    private Connection connection;

    // Constructor untuk menerima koneksi dari Main / DBConfig
    public UserManager(Connection connection) {
        this.connection = connection;
    }

    // Constructor kosong (opsional, tapi jika dipakai pastikan kamu nge-set koneksinya nanti)
    public UserManager() {
    }

    // --- FITUR REGISTER (INSERT) ---
    public boolean registerUser(String username, String password, String email, String role) {
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, role);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Akan return true jika ada baris yang berhasil ditambahkan

        } catch (SQLException e) {
            System.err.println("Error saat registrasi: " + e.getMessage());
            return false;
        }
    }

    // --- FITUR UPDATE PROFIL (UPDATE) ---
    public boolean updateProfile(String username, String password, String email) {
        // Mengupdate password dan email berdasarkan username
        String query = "UPDATE users SET password = ?, email = ? WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, password);
            stmt.setString(2, email);
            stmt.setString(3, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Akan return true jika data berhasil diubah

        } catch (SQLException e) {
            System.err.println("Error saat update profil: " + e.getMessage());
            return false;
        }
    }

    // --- FITUR LOGIN (SELECT) ---
    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Eksekusi query dan tampung hasilnya di ResultSet
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Jika rs.next() bernilai true, berarti kombinasi user & pass ditemukan
            }

        } catch (SQLException e) {
            System.err.println("Error saat autentikasi: " + e.getMessage());
            return false;
        }
    }

    public String getUserRole(String username) {
        String query = "SELECT role FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role"); // Ambil role-nya
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ambil role: " + e.getMessage());
        }
        return "guest"; // Default jika terjadi error
    }
}