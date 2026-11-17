package com.mycompany.sistembioskopkuispbo;
import java.io.Serializable;

public class Transaksi implements Serializable {
    private String idPenonton;
    private String namaPenonton;
    private String kodeObjek;
    private int jumlahTiket;
    private String status;
    private String alasan;

    public Transaksi(String idPenonton, String namaPenonton, String kodeObjek,
                     int jumlahTiket, String status, String alasan) {
        this.idPenonton = idPenonton;
        this.namaPenonton = namaPenonton;
        this.kodeObjek = kodeObjek;
        this.jumlahTiket = jumlahTiket;
        this.status = status;
        this.alasan = alasan;
    }

    public String getIdPenonton() {
        return idPenonton;
    }

    public String getNamaPenonton() {
        return namaPenonton;
    }

    public String getKodeObjek() {
        return kodeObjek;
    }

    public int getJumlahTiket() {
        return jumlahTiket;
    }

    public String getStatus() {
        return status;
    }

    public String getAlasan() {
        return alasan;
    }
}