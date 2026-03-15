package org.rplbo.app.Data;

public class RekamMedis {
    private int id;
    private String namaDokter;
    private String namaPasien;
    private String diagnosis;
    private String tanggal;

    // Constructor
    public RekamMedis(int id, String namaDokter, String namaPasien, String diagnosis, String tanggal) {
        this.id = id;
        this.namaDokter = namaDokter;
        this.namaPasien = namaPasien;
        this.diagnosis = diagnosis;
        this.tanggal = tanggal;
    }

    // Getter untuk dipanggil di menu utama nanti
    public int getId() { return id; }
    public String getNamaDokter() { return namaDokter; }
    public String getNamaPasien() { return namaPasien; }
    public String getDiagnosis() { return diagnosis; }
    public String getTanggal() { return tanggal; }
}