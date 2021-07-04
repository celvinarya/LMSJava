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
public class Invoice extends Return{
    private String id_transaksi,tanggal_transaksi;
    private int daysCount, hargaDenda, totalDenda;

    public Invoice(int id_peminjaman, int id_buku, int id_user, String tanggal_pengembalian, String tanggal_transaksi,int daysCount, int hargaDenda) {
        super(id_peminjaman, id_buku, id_user, tanggal_pengembalian);
        this.tanggal_transaksi= tanggal_transaksi;
        this.daysCount = daysCount;
        this.hargaDenda=hargaDenda;
    }
    public Invoice(int id_peminjaman, int id_buku, int id_user, String tanggal_pengembalian, String tanggal_transaksi, int hargaDenda) {
        super(id_peminjaman, id_buku, id_user, tanggal_pengembalian);
        this.tanggal_transaksi= tanggal_transaksi;
        this.daysCount = daysCount;
        this.hargaDenda=hargaDenda;
        this.totalDenda= daysCount * hargaDenda;
    }
    public Invoice(){
        
    }
    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public void setHargaDenda(int hargaDenda) {
        this.hargaDenda = hargaDenda;
    }


    public String getId_transaksi() {
        return id_transaksi;
    }

    public String getTanggal_transaksi() {
        return tanggal_transaksi;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public int getDenda() {
        return hargaDenda;
    }

    public int getTotalDenda() {
        int totalDenda = this.hargaDenda * daysCount; 
        return totalDenda;
    }
    
}
