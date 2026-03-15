package org.rplbo.app;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rplbo.app.Data.RekamMedis;
import org.rplbo.app.Manager.RekamMedisManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RekamMedisManagerTest {

    private RekamMedisManager rekamMedisManager;
    private static int skorAkhir = 0; // Variabel static untuk total nilai

    @BeforeEach
    public void setUp() {
//        DBConnectionManager.createTables();
        Connection testConn = DBConnectionManager.getConnection();
        rekamMedisManager = new RekamMedisManager(testConn);
    }

    @AfterEach
    public void tearDown() {
        String hapusDataTest = "DELETE FROM rekam_medis WHERE nama_pasien LIKE 'TestPasien_%'";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(hapusDataTest)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal membersihkan data test: " + e.getMessage());
        }
    }

    // Method rekap nilai di akhir
    @AfterAll
    public static void cetakNilai() {
        System.out.println("\n=========================================");
        System.out.println("  TOTAL NILAI REKAM MEDIS : " + skorAkhir + " / 100");
        System.out.println("=========================================\n");
    }

    // --- SKENARIO TESTING ---

    @Test
    public void testTambahRekamMedisBerhasil() {
        boolean hasil = rekamMedisManager.tambahRekamMedis("dr. Strange", "TestPasien_Budi", "Insomnia", "15-03-2026");
        assertTrue(hasil, "Harusnya berhasil ditambah");

        skorAkhir += 25;
        System.out.println("[✔] testTambahRekamMedisBerhasil (+25 Poin)");
    }

    @Test
    public void testCariRekamMedisPasien() {
        rekamMedisManager.tambahRekamMedis("dr. Tirta", "TestPasien_Caca", "Flu", "16-03-2026");
        List<RekamMedis> hasilCari = rekamMedisManager.cariRekamMedisPasien("TestPasien_Caca");

        assertFalse(hasilCari.isEmpty(), "Data harusnya ditemukan");
        assertEquals("TestPasien_Caca", hasilCari.get(0).getNamaPasien(), "Nama harus sesuai");

        skorAkhir += 25;
        System.out.println("[✔] testCariRekamMedisPasien (+25 Poin)");
    }

    @Test
    public void testEditRekamMedisBerhasil() {
        rekamMedisManager.tambahRekamMedis("dr. Boyke", "TestPasien_Deni", "Sakit Perut", "17-03-2026");
        List<RekamMedis> list = rekamMedisManager.cariRekamMedisPasien("TestPasien_Deni");
        int idTarget = list.get(0).getId();

        boolean hasilEdit = rekamMedisManager.editRekamMedis(idTarget, "Sudah Sembuh");
        assertTrue(hasilEdit, "Edit harus return true");

        List<RekamMedis> listSetelahEdit = rekamMedisManager.cariRekamMedisPasien("TestPasien_Deni");
        assertEquals("Sudah Sembuh", listSetelahEdit.get(0).getDiagnosis(), "Diagnosis harus berubah");

        skorAkhir += 25;
        System.out.println("[✔] testEditRekamMedisBerhasil (+25 Poin)");
    }

    @Test
    public void testHapusRekamMedisBerhasil() {
        rekamMedisManager.tambahRekamMedis("dr. Terawan", "TestPasien_Eko", "Cuci Otak", "18-03-2026");
        List<RekamMedis> list = rekamMedisManager.cariRekamMedisPasien("TestPasien_Eko");
        int idTarget = list.get(0).getId();

        boolean hasilHapus = rekamMedisManager.hapusRekamMedis(idTarget);
        assertTrue(hasilHapus, "Hapus harus return true");

        List<RekamMedis> listSetelahDihapus = rekamMedisManager.cariRekamMedisPasien("TestPasien_Eko");
        assertTrue(listSetelahDihapus.isEmpty(), "List harus kosong setelah dihapus");

        skorAkhir += 25;
        System.out.println("[✔] testHapusRekamMedisBerhasil (+25 Poin)");
    }
}