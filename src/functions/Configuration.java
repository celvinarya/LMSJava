package functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author celvin
 */
public class Configuration {
    private static Connection MYSQLConnection;
    
    public static Connection configDB()throws SQLException{
        try{
            String url = "jdbc:mysql://localhost/librarymanagement";
            String user = "root";
            String pass = "";
           
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            MYSQLConnection = DriverManager.getConnection(url, user, pass);// melakukan koneksi ke database
        }
        catch(SQLException e){
            System.err.println("Koneksi ke database gagal " + e.getMessage());
        }
        return MYSQLConnection;
    }
    
}
