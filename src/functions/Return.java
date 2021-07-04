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
public class Return {
    private int id_peminjaman, id_buku, id_user;
    private String status, tanggal_peminjaman, tanggal_pengembalian, note;

    public Return(int id_peminjaman, int id_buku, int id_user, String status, String tanggal_peminjaman, String tanggal_pengembalian, String note) {
        this.id_peminjaman = id_peminjaman;
        this.id_buku = id_buku;
        this.id_user = id_user;
        this.status = status;
        this.tanggal_peminjaman = tanggal_peminjaman;
        this.tanggal_pengembalian = tanggal_pengembalian;
        this.note = note;
    }
    public Return(int id_peminjaman, int id_buku, int id_user, String tanggal_pengembalian){
        this.id_peminjaman = id_peminjaman;
        this.id_buku = id_buku;
        this.id_user = id_user;
        this.tanggal_pengembalian = tanggal_pengembalian;
    }
    public Return(){
        
    }
    public String getNote() {
        return note;
    }
    
    public int getId_peminjaman() {
        return id_peminjaman;
    }

    public int getId_buku() {
        return id_buku;
    }

    public int getId_user() {
        return id_user;
    }

    public String getStatus() {
        return status;
    }

    public String getTanggal_peminjaman() {
        return tanggal_peminjaman;
    }

    public String getTanggal_pengembalian() {
        return tanggal_pengembalian;
    }
    
}
