package com.mycompany.sistembioskopkuispbo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SistemBioskop implements Serializable {
    private String namaBioskop;
    private HashMap<String, Penonton> daftarPenonton;
    private HashMap<String, KoleksiFilm> daftarJadwal;
    private ArrayList<Transaksi> riwayatTransaksi;
    private ArrayList<String> detailPenolakan;

    public SistemBioskop(String namaBioskop) {
        this.namaBioskop = namaBioskop;
        daftarPenonton = new HashMap<>();
        daftarJadwal = new HashMap<>();
        riwayatTransaksi = new ArrayList<>();
        detailPenolakan = new ArrayList<>();
    }

    public void tambahPenonton(String id, String nama) {
        daftarPenonton.put(id, new Penonton(id, nama));
        System.out.println("[BERHASIL] Penonton " + nama + " berhasil didaftarkan.");
    }

    public void tambahJadwalFilm(String kode, String nama, int kapasitas) {
        KoleksiFilm film;

        if (kode.equals("OBJ-A")) {
            film = new StudioPremiere(kode, nama, kapasitas);
        } else if (kode.equals("OBJ-B")) {
            film = new StudioReguler(kode, nama, kapasitas);
        } else {
            film = new StudioEkonomis(kode, nama, kapasitas);
        }

        daftarJadwal.put(kode, film);
        System.out.println("[BERHASIL] Jadwal " + nama + " berhasil ditambahkan.");
    }

    private String ambilNilai(String data, String label) throws TransaksiGagalException {
        String[] bagian = data.trim().split(":", 2);

        if (bagian.length != 2 || !bagian[0].trim().equals(label)) {
            throw new TransaksiGagalException("Format " + label + " tidak sesuai.");
        }

        return bagian[1].trim();
    }

    private void prosesTransaksi(String id, String kode, int jumlah)
            throws TransaksiGagalException {

        if (jumlah <= 0) {
            throw new TransaksiGagalException("Jumlah tiket harus lebih dari 0.");
        }

        Penonton penonton = daftarPenonton.get(id);

        if (penonton == null) {
            throw new TransaksiGagalException("ID penonton " + id + " tidak ditemukan.");
        }

        KoleksiFilm film = daftarJadwal.get(kode);

        if (film == null) {
            throw new TransaksiGagalException("Kode jadwal " + kode + " tidak ditemukan.");
        }

        if (!film.cekKetersediaan(jumlah)) {
            throw new TransaksiGagalException(
                "Kursi tidak mencukupi untuk " + kode + ". Tersedia: " + film.getSisaKursi()
            );
        }

        film.kurangiKursi(jumlah);

        riwayatTransaksi.add(new Transaksi(
            id, penonton.getNama(), kode, jumlah, "BERHASIL", ""
        ));

        System.out.println(
            "[TRANSAKSI BERHASIL] " + penonton.getNama()
            + " memesan " + jumlah + " tiket di "
            + film.getNamaJadwal()
            + ". Sisa kursi: " + film.getSisaKursi()
        );
    }

    public void jalankanTransaksiDariFile(String namaFile) throws Exception {
        Scanner scanner = new Scanner(new FileInputStream(namaFile));
        int nomorBaris = 0;
        int berhasil = 0;
        int ditolak = 0;

        try {
            while (scanner.hasNextLine()) {
                String baris = scanner.nextLine().trim();

                if (baris.isEmpty()) {
                    continue;
                }

                nomorBaris++;

                String id = "";
                String kode = "";
                int jumlah = 0;

                try {
                    String[] data = baris.split(";");

                    id = ambilNilai(data[0], "ID");
                    kode = ambilNilai(data[1], "KD");
                    jumlah = Integer.parseInt(ambilNilai(data[2], "JML"));

                    prosesTransaksi(id, kode, jumlah);
                    berhasil++;

                } catch (TransaksiGagalException | NumberFormatException e) {
                    ditolak++;

                    String alasan = e instanceof NumberFormatException
                        ? "Jumlah tiket harus berupa angka."
                        : e.getMessage();

                    String nama = daftarPenonton.containsKey(id)
                        ? daftarPenonton.get(id).getNama()
                        : id;

                    riwayatTransaksi.add(new Transaksi(
                        id, nama, kode, jumlah, "DITOLAK", alasan
                    ));

                    detailPenolakan.add(
                        "Baris " + nomorBaris + " : " + alasan
                    );

                    System.out.println(
                        "[TRANSAKSI DITOLAK] Baris " + nomorBaris + ": " + alasan
                    );
                }
            }
        } finally {
            scanner.close();
        }

        System.out.println("\nPembacaan file transaksi selesai.");
        System.out.println("Total transaksi berhasil: " + berhasil);
        System.out.println("Total transaksi ditolak: " + ditolak);
    }

    public void lihatRekapTransaksi() {
        System.out.println("\n========== HASIL TRANSAKSI ==========\n");
        System.out.printf("%-5s %-10s %-8s %-8s %-10s%n",
            "No.", "Penonton", "Kode", "Jumlah", "Status");
        System.out.println("----------------------------------------------");

        int berhasil = 0;
        int ditolak = 0;

        for (int i = 0; i < riwayatTransaksi.size(); i++) {
            Transaksi transaksi = riwayatTransaksi.get(i);

            System.out.printf("%-5d %-10s %-8s %-8d %-10s%n",
                i + 1,
                transaksi.getNamaPenonton(),
                transaksi.getKodeObjek(),
                transaksi.getJumlahTiket(),
                transaksi.getStatus()
            );

            if (transaksi.getStatus().equals("BERHASIL")) {
                berhasil++;
            } else {
                ditolak++;
            }
        }

        System.out.println("----------------------------------------------");
        System.out.println("\nKeterangan:");
        System.out.println("[" + berhasil + "] Transaksi berhasil");
        System.out.println("[" + ditolak + "] Transaksi ditolak");

        System.out.println("\n========== DETAIL PENOLAKAN ==========\n");

        if (detailPenolakan.isEmpty()) {
            System.out.println("Tidak ada transaksi yang ditolak.");
        } else {
            for (String detail : detailPenolakan) {
                System.out.println(detail);
            }
        }

        System.out.println("\n======================================");
    }

    public void simpanLaporanAkhir() {
        lihatRekapTransaksi();

        try {
            ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream("snapshot.obj")
            );

            output.writeObject(this);
            output.close();

            System.out.println("[BERHASIL] Data berhasil diserialisasi ke snapshot.obj.");
        } catch (Exception e) {
            System.out.println("[GAGAL] Serialisasi data: " + e.getMessage());
        }

        try {
            ArrayList<String> daftarID = new ArrayList<>(daftarPenonton.keySet());

            for (int i = 0; i < daftarID.size() - 1; i++) {
                for (int j = 0; j < daftarID.size() - i - 1; j++) {
                    if (daftarID.get(j).compareTo(daftarID.get(j + 1)) > 0) {
                        String sementara = daftarID.get(j);
                        daftarID.set(j, daftarID.get(j + 1));
                        daftarID.set(j + 1, sementara);
                    }
                }
            }

            FileWriter writer = new FileWriter("laporan.txt");

            writer.write("========================================\n");
            writer.write("       LAPORAN SISTEM BIOSKOP\n");
            writer.write("========================================\n");
            writer.write("Nama Bioskop: " + namaBioskop + "\n\n");
            writer.write("DAFTAR ID PENONTON:\n");

            for (String id : daftarID) {
                writer.write(id + "\n");
            }

            writer.close();

            System.out.println("[BERHASIL] Laporan berhasil disimpan ke laporan.txt.");
        } catch (Exception e) {
            System.out.println("[GAGAL] Membuat laporan: " + e.getMessage());
        }

        System.out.println("========================================");
        System.out.println("           PROSES SELESAI");
        System.out.println("========================================");
    }
}