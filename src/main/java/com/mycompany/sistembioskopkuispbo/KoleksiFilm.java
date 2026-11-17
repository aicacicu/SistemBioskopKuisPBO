package com.mycompany.sistembioskopkuispbo;
import java.io.Serializable;

public abstract class KoleksiFilm implements Serializable {
    private String kodeObjek;
    private String namaJadwal;
    private StudioBioskop studio;

    public KoleksiFilm(String kodeObjek, String namaJadwal, int kapasitas) {
        this.kodeObjek = kodeObjek;
        this.namaJadwal = namaJadwal;
        this.studio = new StudioBioskop(kapasitas);
    }

    public String getKodeObjek() {
        return kodeObjek;
    }

    public String getNamaJadwal() {
        return namaJadwal;
    }

    public int getSisaKursi() {
        return studio.getSisaKursi();
    }

    public boolean cekKetersediaan(int jumlah) {
        return studio.cekKetersediaan(jumlah);
    }

    public void kurangiKursi(int jumlah) {
        studio.kurangiKursi(jumlah);
    }

    public abstract double hitungRating();
}