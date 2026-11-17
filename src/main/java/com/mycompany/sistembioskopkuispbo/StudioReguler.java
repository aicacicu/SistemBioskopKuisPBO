package com.mycompany.sistembioskopkuispbo;
public class StudioReguler extends KoleksiFilm implements Borrowable, Rateable {
    public StudioReguler(String kode, String nama, int kapasitas) {
        super(kode, nama, kapasitas);
    }

    public boolean cekKetersediaan(int jumlah) {
        return super.cekKetersediaan(jumlah);
    }

    public double hitungRating() {
        return 4.0;
    }
}