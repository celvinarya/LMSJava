/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author celvin
 */
public class DatabaseOperations {
    
    public static void addUser(String firstName, String lastName, String username, String password, String user_type){
        String fullName = firstName+" "+lastName;
        try{
            String query="INSERT INTO users(name,username,password,user_type) VALUES('"+fullName+"','"+username+"','"+password+"','"+user_type+"')";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public void tampilkanDataUsers(JTable userTable, String sql){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID ");
        model.addColumn("Name");
        model.addColumn("Username");
        model.addColumn("Tipe User");
        
        try{
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            Statement stm = conn.createStatement();//membuat sebuah statement
            ResultSet res = stm.executeQuery(sql);//Execute Sql querry
            while (res.next()){
                model.addRow(new Object[]{res.getString("id"),res.getString("name"),res.getString("username"),res.getString("user_type")});
                //atau bisa juga res.getString("nama_barang");
            }
            userTable.setModel(model);
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static int updateUser(String id, String name, String user_type){
        int count = 0;
        try{
            String query="UPDATE users SET name = '"+name+"', user_type='"+user_type+"' WHERE id = "+id+"";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
            count = 1;
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
            count = 0;
        }
        return count;
    }
    public static int deleteUser(String id){
        int count = 0;
        try{
            String query="DELETE FROM users WHERE id = "+id+"";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
        return count;
    }public static ArrayList<User> getUsers(String query){
        ArrayList<User> user = new ArrayList<User>();
        try{
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            Statement stm = conn.createStatement();//membuat sebuah statement
            ResultSet res = stm.executeQuery(query);//Execute Sql querry
            User u;
            while(res.next()){ 
                String string = res.getString("name");
                String[] parts = string.split(" ");
                String firstName = parts[0];
                String lastName = parts[1];
                u = new User(
                        firstName,
                        lastName,
                        res.getString("username"),
                        res.getString("password"),
                        res.getInt("id"),
                        res.getString("user_type")
                );
               user.add(u);
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
        return user;
    }
    public static ArrayList<Book> getBooks(String query){
        ArrayList<Book> buku = new ArrayList<Book>();
        try{
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            Statement stm = conn.createStatement();//membuat sebuah statement
            ResultSet res = stm.executeQuery(query);//Execute Sql querry
            Book b;
            while(res.next()){        
                b = new Book(
                        res.getInt("id_buku"),
                        res.getString("kode_buku"),
                        res.getString("judul_buku"),
                        res.getString("genre_buku"),
                        res.getString("penulis_buku"),
                        res.getString("penerbit_buku"),
                        res.getString("tahun_terbit"),
                        res.getInt("stok")
                );
               buku.add(b);
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
        return buku;
    }
    public static ArrayList<Book> findBooks(String keyword){
        ArrayList<Book> buku = new ArrayList<Book>();
        try{
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            Statement stm = conn.createStatement();//membuat sebuah statement
            ResultSet res = stm.executeQuery("SELECT * FROM books WHERE id_buku LIKE '%"+keyword+"%' OR judul_buku LIKE '%"+keyword+"%' OR genre_buku LIKE '%"+keyword+"%' OR penulis_buku LIKE '%"+keyword+"%' OR penerbit_buku LIKE '%"+keyword+"%' OR tahun_terbit LIKE '%"+keyword+"%'");//Execute Sql querry
            Book b;
            while(res.next()){        
                b = new Book(
                        res.getInt("id_buku"),
                        res.getString("kode_buku"),
                        res.getString("judul_buku"),
                        res.getString("genre_buku"),
                        res.getString("penulis_buku"),
                        res.getString("penerbit_buku"),
                        res.getString("tahun_terbit"),
                        res.getInt("stok")
                );
               buku.add(b);
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
        return buku;
    }
    public static ArrayList<Book> findBooksTitle(String keyword){
        ArrayList<Book> buku = new ArrayList<Book>();
        try{
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            Statement stm = conn.createStatement();//membuat sebuah statement
            ResultSet res = stm.executeQuery("SELECT * FROM books WHERE judul_buku LIKE '%"+keyword+"%'");//Execute Sql querry
            Book b;
            while(res.next()){        
                b = new Book(
                        res.getInt("id_buku"),
                        res.getString("kode_buku"),
                        res.getString("judul_buku"),
                        res.getString("genre_buku"),
                        res.getString("penulis_buku"),
                        res.getString("penerbit_buku"),
                        res.getString("tahun_terbit"),
                        res.getInt("stok")
                );
               buku.add(b);
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
        return buku;
    }
    public static void addBook(String code, String judul, String genre, String penulis, String penerbit, String tahunTerbit, int stock){
        try{
            String query="INSERT INTO books(kode_buku, judul_buku, genre_buku, penulis_buku, penerbit_buku, tahun_terbit, stok) VALUES('"+code+"','"+judul+"','"+genre+"','"+penulis+"','"+penerbit+"','"+tahunTerbit+"','"+stock+"')";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static void updateBuku(String id, String code, String judul, String genre, String penulis, String penerbit, String tahunTerbit, String stock){
        try{
            String query="UPDATE books SET kode_buku = '"+code+"', judul_buku='"+judul+"', genre_buku='"+genre+"', penulis_buku='"+penulis+"', penerbit_buku='"+penerbit+"', tahun_terbit='"+tahunTerbit+"', stok='"+stock+"'  WHERE id_buku = "+id+"";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static void deleteBuku(String id){
        try{
            String query="DELETE FROM books WHERE id_buku = "+id+"";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static void borrowBook(int id_buku, int id_user, String status, Date dt_1, Date dt_2, String note){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String tanggal_peminjaman = sdf.format(dt_1);
            String tanggal_pengembalian = sdf.format(dt_2);
            String query="INSERT INTO returned(id_buku, id_user, status, tanggal_peminjaman, tanggal_pengembalian, note) VALUES("+id_buku+","+id_user+",'"+status+"','"+tanggal_peminjaman+"','"+tanggal_pengembalian+"','"+note+"')";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static ArrayList<Return> getReturned(String query){
        ArrayList<Return> returnBook = new ArrayList<Return>();
        try{
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            Statement stm = conn.createStatement();//membuat sebuah statement
            ResultSet res = stm.executeQuery(query);//Execute Sql querry
            Return r;
            while(res.next()){        
                r = new Return(
                        res.getInt("id_peminjaman"),
                        res.getInt("id_buku"),
                        res.getInt("id_user"),
                        res.getString("status"),
                        res.getString("tanggal_peminjaman"),
                        res.getString("tanggal_pengembalian"),
                        res.getString("note")
                );
               returnBook.add(r);
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
        return returnBook;
    }
    public static ArrayList<Return> findReturned(String keyword){
        ArrayList<Return> returned = new ArrayList<Return>();
        try{
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            Statement stm = conn.createStatement();//membuat sebuah statement
            ResultSet res = stm.executeQuery("SELECT * FROM returned WHERE id_buku LIKE '%"+keyword+"%' OR id_user LIKE '%"+keyword+"%' OR status LIKE '%"+keyword+"%' OR tanggal_peminjaman LIKE '%"+keyword+"%' OR tanggal_pengembalian LIKE '%"+keyword+"%' OR id_peminjaman LIKE '%"+keyword+"%'");//Execute Sql querry
            Return r;
            while(res.next()){        
                r = new Return(
                        res.getInt("id_peminjaman"),
                        res.getInt("id_buku"),
                        res.getInt("id_user"),
                        res.getString("status"),
                        res.getString("tanggal_peminjaman"),
                        res.getString("tanggal_pengembalian"),
                        res.getString("note")
                );
               returned.add(r);
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
        return returned;
    }
    public static void deleteReturned(String id){
        try{
            String query="DELETE FROM returned WHERE id_peminjaman = "+id+"";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static void updateStatusReturned(int id){
        try{
            String query="UPDATE returned SET status = 'Returned' WHERE id_peminjaman = "+id+"";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static void updateStatusLost(String id){
        try{
            String query="UPDATE returned SET status = 'Lost' WHERE id_peminjaman = "+id+"";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static void updateStokLost(int id_buku, int stokBukuFinal){
        try{
            String query="UPDATE books SET stok = "+stokBukuFinal+" WHERE id_buku = "+id_buku+"";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    public static void addInvoice(int id_buku, int id_anggota, int id_librarian, int denda, String tanggal_transaksi){
        try{
            String query="INSERT INTO invoice(id_buku, id_anggota, id_librarian, denda, tanggal_transaksi) VALUES("+id_buku+","+id_anggota+","+id_librarian+","+denda+",'"+tanggal_transaksi+"')";
            Connection conn = Configuration.configDB();//koneksi ke database melalui method kelas configuration
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.execute();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
}
