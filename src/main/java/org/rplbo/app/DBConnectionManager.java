package org.rplbo.app;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionManager {
    private static final String DB_URL = "jdbc:sqlite:Asylum.db";
    private static Connection connection;

    private DBConnectionManager() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // --- METHOD BARU UNTUK MEMBUAT TABEL OTOMATIS ---
//    public static void createTables() {
        // Query untuk tabel Users
        // Catatan: SQLite menggunakan AUTOINCREMENT, bukan AUTO_INCREMENT seperti MySQL
//        String tableUsers = "CREATE TABLE IF NOT EXISTS users ("
//                + "email TEXT PRIMARY KEY, "
//                + "username TEXT NOT NULL UNIQUE, "
//                + "password TEXT NOT NULL, "
//                + "role TEXT CHECK( role IN ('dokter', 'pasien') ) NOT NULL"
//                + ");";
//
//        // Query untuk tabel Rekam Medis (Contoh relasi untuk fitur CRUD Dokter/Pasien)
//        String tableRekamMedis = "CREATE TABLE IF NOT EXISTS rekam_medis ("
//                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + "nama_pasien INTEGER NOT NULL, "
//                + "nama_dokter INTEGER NOT NULL, "
//                + "diagnosis TEXT NOT NULL, "
//                + "tanggal TEXT NOT NULL"
//                + ");";

//        try (Connection conn = getConnection();
//             Statement stmt = conn.createStatement()) {
//
//            // Eksekusi pembuatan tabel
//            stmt.execute(tableUsers);
//            stmt.execute(tableRekamMedis);
//            System.out.println("[Database] Tabel berhasil dicek/dibuat di Asylum.db!");
//
//        } catch (SQLException e) {
//            System.err.println("[Database Error] Gagal membuat tabel: " + e.getMessage());
//        }
    }
