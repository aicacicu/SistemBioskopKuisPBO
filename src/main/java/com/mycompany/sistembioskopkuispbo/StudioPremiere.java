package com.mycompany.sistembioskopkuispbo;
public class StudioPremiere extends KoleksiFilm implements Borrowable, Rateable {
    public StudioPremiere(String kode, String nama, int kapasitas) {
        super(kode, nama, kapasitas);
    }

    public boolean cekKetersediaan(int jumlah) {
        return super.cekKetersediaan(jumlah);
    }

    public double hitungRating() {
        return 5.0;
    }
}