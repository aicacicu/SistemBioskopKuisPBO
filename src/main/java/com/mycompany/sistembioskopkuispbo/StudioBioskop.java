package com.mycompany.sistembioskopkuispbo;
import java.io.Serializable;

public class StudioBioskop implements Serializable {
    private int kapasitas;
    private int sisaKursi;

    public StudioBioskop(int kapasitas) {
        this.kapasitas = kapasitas;
        this.sisaKursi = kapasitas;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public int getSisaKursi() {
        return sisaKursi;
    }

    public boolean cekKetersediaan(int jumlah) {
        return jumlah > 0 && jumlah <= sisaKursi;
    }

    public void kurangiKursi(int jumlah) {
        sisaKursi -= jumlah;
    }
}