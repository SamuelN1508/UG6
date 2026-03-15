package org.rplbo.app;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rplbo.app.Manager.UserManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    private UserManager userManager;
    private static int skorAkhir = 0; // Variabel static untuk menyimpan total nilai

    @BeforeEach
    public void setUp() {
//        DBConnectionManager.createTables();
        Connection testConn = DBConnectionManager.getConnection();
        userManager = new UserManager(testConn);
    }

    @AfterEach
    public void tearDown() {
        String hapusDataTest = "DELETE FROM users WHERE username LIKE 'test_%'";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(hapusDataTest)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal membersihkan data test: " + e.getMessage());
        }
    }

    // Method ini otomatis dijalankan di akhir setelah SEMUA test selesai
    @AfterAll
    public static void cetakNilai() {
        System.out.println("\n=========================================");
        System.out.println("  TOTAL NILAI USER MANAGER : " + skorAkhir + " / 100");
        System.out.println("=========================================\n");
    }

    // --- SKENARIO TESTING ---

    @Test
    public void testRegistrasiBerhasil() {
        boolean hasil = userManager.registerUser("test_andi", "rahasia123", "andi@mail.com", "pasien");
        assertTrue(hasil, "Registrasi seharusnya berhasil");

        skorAkhir += 25; // Hanya tereksekusi jika assertTrue lolos
        System.out.println("[✔] testRegistrasiBerhasil (+25 Poin)");
    }

    @Test
    public void testRegistrasiUsernameKembarGagal() {
        userManager.registerUser("test_budi", "pass", "budi@mail.com", "dokter");
        boolean hasilKedua = userManager.registerUser("test_budi", "passbeda", "budi2@mail.com", "pasien");
        assertFalse(hasilKedua, "Registrasi kedua harusnya gagal");

        skorAkhir += 25;
        System.out.println("[✔] testRegistrasiUsernameKembarGagal (+25 Poin)");
    }

    @Test
    public void testLoginBerhasil() {
        userManager.registerUser("test_cindy", "pass123", "cindy@mail.com", "pasien");
        boolean statusLogin = userManager.authenticateUser("test_cindy", "pass123");
        assertTrue(statusLogin, "Login harusnya berhasil");

        skorAkhir += 25;
        System.out.println("[✔] testLoginBerhasil (+25 Poin)");
    }

    @Test
    public void testGetRoleBerhasil() {
        userManager.registerUser("test_dokter1", "123", "doc@mail.com", "dokter");
        String role = userManager.getUserRole("test_dokter1");
        assertEquals("dokter", role, "Role harusnya 'dokter'");

        skorAkhir += 25;
        System.out.println("[✔] testGetRoleBerhasil (+25 Poin)");
    }
}