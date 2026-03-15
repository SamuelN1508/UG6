package org.rplbo.app;

import org.rplbo.app.Data.RekamMedis;
import org.rplbo.app.Manager.RekamMedisManager;
import org.rplbo.app.Manager.UserManager;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class AsylumSystem {

    private Connection connection;
    private UserManager userManager;
    // Tambahkan manager lain di sini nantinya:
    private RekamMedisManager rekamMedisManager;
    // private BangsalManager bangsalManager;

    public AsylumSystem() {
        // 1. Koneksi Terpusat
        connection = DBConnectionManager.getConnection();

        // 2. Passing koneksi ke Manager
        this.userManager = new UserManager(connection);

         this.rekamMedisManager = new RekamMedisManager(connection);
        System.out.println("[Sistem] Berhasil terhubung ke Database Asylum!");
    }

    // ==========================================
    // MENU LEVEL 1: SEBELUM LOGIN
    // ==========================================
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Sistem Manajemen Mental Asylum ===");
            System.out.println("1. Login (Dokter / Pasien)");
            System.out.println("0. Keluar Aplikasi");
            System.out.print("Pilih menu: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    prosesLogin(scanner);
                    break;
                case 0:
                    System.out.println("Menutup sistem. Goodbye!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void prosesLogin(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // 1. Cek Login (True / False)
        if (userManager.authenticateUser(username, password)) {
            // 2. Jika sukses, ambil role-nya dari database
            String roleAktif = userManager.getUserRole(username);
            String roleKapital = roleAktif.substring(0, 1).toUpperCase() + roleAktif.substring(1);
            System.out.println("\nLogin Berhasil! Selamat datang, " + roleKapital + " "+ username);

            // 3. Arahkan menu sesuai role
            if (roleAktif.equalsIgnoreCase("dokter")) {
                menuDokter(scanner, username); // Cukup oper String username-nya saja
            } else if (roleAktif.equalsIgnoreCase("pasien")) {
                menuPasien(scanner, username);
            }
        } else {
            System.out.println("Login Gagal! Username atau password salah.");
        }
    }

    private void prosesRegistrasi(Scanner scanner) {
        System.out.println("\n=== Registrasi Akun Baru ===");
        System.out.print("Username : ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Role (dokter / pasien): ");
        String role = scanner.nextLine().toLowerCase(); // Ubah ke huruf kecil untuk mempermudah validasi

        try {
            // 1. Validasi Input Role: Lempar Exception jika salah
            if (!role.equals("dokter") && !role.equals("pasien")) {
                throw new IllegalArgumentException("Role tidak valid! Hanya boleh memilih 'dokter' atau 'pasien'.");
            }

            // 2. Jika lolos validasi, panggil UserManager
            if (userManager.registerUser(username, password, email, role)) {
                System.out.println("Registrasi berhasil! Silakan login.");
            } else {
                System.out.println("Registrasi gagal. Pastikan username belum terpakai.");
            }

        } catch (IllegalArgumentException e) {
            // 3. Tangkap Exception dan tampilkan pesan error
            System.err.println("[Error] " + e.getMessage());
        }
    }

    // ==========================================
    // MENU LEVEL 2: SETELAH LOGIN (BEDA ROLE)
    // ==========================================

    private void menuDokter(Scanner scanner, String dokter) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MENU DOKTER: " + dokter + " ===");
            System.out.println("1. Tambah Rekam Medis (Insert)");
            System.out.println("2. Registrasi Pasien/Dokter Baru");
            System.out.println("3. Edit Rekam Medis (Update)");
            System.out.println("4. Hapus Rekam Medis (Delete)");
            System.out.println("5. Lihat Semua Rekam Medis (Get All)");
            System.out.println("6. Cari Pasien (Search)");
            System.out.println("0. Logout");
            System.out.print("Pilih: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    tambahRekamMedis(scanner, dokter);
                    break;
                case 2:
                    prosesRegistrasi(scanner);
                    break;
                case 3:
                    editRekamMedis(scanner);
                    break;
                case 4:
                    hapusRekamMedis(scanner);
                    break;
                case 5:
                    allPasien();
                    break;
                case 6:
                    cariPasien(scanner);
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void tambahRekamMedis(Scanner scanner, String usernameDokter) {
        System.out.println("\n=== TAMBAH REKAM MEDIS ===");
        System.out.print("Masukkan Nama Pasien: ");
        String namaPasien = scanner.nextLine();
        System.out.print("Masukkan Tanggal (Contoh: 15-03-2026): ");
        String tanggal = scanner.nextLine();
        System.out.print("Masukkan Diagnosis/Catatan Medis: ");
        String diagnosis = scanner.nextLine();

        // Menggunakan usernameDokter otomatis dari sesi login
        if (rekamMedisManager.tambahRekamMedis(usernameDokter, namaPasien, diagnosis, tanggal)) {
            System.out.println("Sukses! Rekam medis berhasil disimpan.");
        } else {
            System.out.println("Gagal menyimpan rekam medis. Pastikan nama pasien benar.");
        }
    }

    private void editRekamMedis(Scanner scanner) {
        System.out.println("\n=== EDIT REKAM MEDIS ===");
        System.out.print("Masukkan ID Rekam Medis yang ingin diubah: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Masukkan Diagnosis Baru: ");
        String diagnosisBaru = scanner.nextLine();

        if (rekamMedisManager.editRekamMedis(id, diagnosisBaru)) {
            System.out.println("Rekam medis berhasil diubah!");
        } else {
            System.out.println("Gagal mengubah. Pastikan ID benar.");
        }
    }

    private void hapusRekamMedis(Scanner scanner) {
        System.out.println("\n=== HAPUS REKAM MEDIS ===");
        System.out.print("Masukkan ID Rekam Medis yang ingin dihapus: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (rekamMedisManager.hapusRekamMedis(id)) {
            System.out.println("Rekam medis berhasil dihapus!");
        } else {
            System.out.println("Gagal menghapus. Pastikan ID benar.");
        }
    }

    private void allPasien() {
        List<RekamMedis> list = rekamMedisManager.getAllRekamMedis();
        System.out.println("\n=== DAFTAR SEMUA REKAM MEDIS ===");
        if (list.isEmpty()) {
            System.out.println("Belum ada data.");
        } else {
            for (RekamMedis rm : list) {
                System.out.println("ID: " + rm.getId() + " | Pasien: " + rm.getNamaPasien() +
                        " | Dokter: dr. " + rm.getNamaDokter() + " | Tgl: " + rm.getTanggal());
                System.out.println("Diagnosis: " + rm.getDiagnosis());
                System.out.println("-------------------------------------------------");
            }
        }
    }

    private void cariPasien(Scanner scanner) {
        System.out.println("\n=== CARI DATA REKAM MEDIS PASIEN ===");
        System.out.print("Masukkan nama pasien yang dicari: ");
        String keyword = scanner.nextLine();

        // Panggil fungsi search dari manager
        List<RekamMedis> list = rekamMedisManager.cariRekamMedisPasien(keyword);

        System.out.println("\n=== HASIL PENCARIAN UNTUK '" + keyword + "' ===");

        if (list.isEmpty()) {
            System.out.println("Tidak ada rekam medis yang ditemukan untuk pasien tersebut.");
        } else {
            for (RekamMedis rm : list) {
                System.out.println("ID: " + rm.getId() + " | Pasien: " + rm.getNamaPasien() +
                        " | Dokter: dr. " + rm.getNamaDokter() + " | Tgl: " + rm.getTanggal());
                System.out.println("Diagnosis: " + rm.getDiagnosis());
                System.out.println("-------------------------------------------------");
            }
        }
    }


    private void menuPasien(Scanner scanner, String pasien) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MENU PASIEN: " + pasien + " ===");
            System.out.println("1. Update Profil Saya (Edit DB)");
            System.out.println("2. Lihat Riwayat Rekam Medis Saya (Read)");
            System.out.println("0. Logout");
            System.out.print("Pilih: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    editRekamMedis(scanner);
                    break;
                case 2:
                    cariPasien(scanner);
                    break;
                case 0:
                    back = true;
                    break;
                default: System.out.println("Pilihan tidak valid.");
            }
        }
    }
    public static void main(String[] args) throws IOException {
        AsylumSystem asylumSystem = new AsylumSystem();
        asylumSystem.start();
    }
}