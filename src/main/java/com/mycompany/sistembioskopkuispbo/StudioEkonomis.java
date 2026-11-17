package com.mycompany.sistembioskopkuispbo;
public class StudioEkonomis extends KoleksiFilm implements Borrowable, Rateable {
    public StudioEkonomis(String kode, String nama, int kapasitas) {
        super(kode, nama, kapasitas);
    }

    public boolean cekKetersediaan(int jumlah) {
        return super.cekKetersediaan(jumlah);
    }

    public double hitungRating() {
        return 3.0;
    }
}