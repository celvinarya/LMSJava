/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

/**
 *
 * @author celvin
 */
public class Book {
    private String kode_buku, judul_buku, genre_buku, penulis_buku, penerbit_buku, tahun_terbit;
    private int stok,id_buku;

    public Book(int id_buku, String kode_buku, String judul_buku, String genre_buku, String penulis_buku, String penerbit_buku, String tahun_terbit, int stok) {
        this.kode_buku = kode_buku;
        this.judul_buku = judul_buku;
        this.genre_buku = genre_buku;
        this.penulis_buku = penulis_buku;
        this.penerbit_buku = penerbit_buku;
        this.tahun_terbit = tahun_terbit;
        this.stok = stok;
        this.id_buku = id_buku;
    }

    public Book(String kode_buku, String judul_buku, String genre_buku, String penulis_buku, String penerbit_buku, String tahun_terbit, int stok) {
        this.kode_buku = kode_buku;
        this.judul_buku = judul_buku;
        this.genre_buku = genre_buku;
        this.penulis_buku = penulis_buku;
        this.penerbit_buku = penerbit_buku;
        this.tahun_terbit = tahun_terbit;
        this.stok = stok;
    }

    public String getKode_buku() {
        return kode_buku;
    }

    public String getJudul_buku() {
        return judul_buku;
    }

    public String getGenre_buku() {
        return genre_buku;
    }

    public String getPenulis_buku() {
        return penulis_buku;
    }

    public String getPenerbit_buku() {
        return penerbit_buku;
    }

    public String getTahun_terbit() {
        return tahun_terbit;
    }

    public int getStok() {
        return stok;
    }

    public int getId_buku() {
        return id_buku;
    }
    
    
}
