![alt text](AsylumDiagram.png?raw=true)
# Sistem Manajemen Asylum (Tugas Praktikum JDBC - UG6)

Aplikasi ini memiliki sistem *Role-Based Access Control* (RBAC) yang ketat:

1. **Role Dokter:**
   * Menambahkan rekam medis baru untuk pasien (Insert).
   * Mengedit diagnosis rekam medis (Update).
   * Menghapus rekam medis (Delete).
   * Melihat semua data rekam medis di sistem (Read All).
   * Mencari data rekam medis berdasarkan sebagian nama pasien (Search menggunakan `LIKE`).
   * Mendaftarkan akun Dokter/Pasien baru dari dalam sistem.

2. **Role Pasien:**
   * Hanya dapat melihat riwayat rekam medis (Read Specific).
