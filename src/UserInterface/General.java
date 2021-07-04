
package UserInterface;

import functions.Book;
import functions.Configuration;
import functions.DatabaseOperations;
import functions.Invoice;
import functions.User;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;

public class General extends javax.swing.JFrame {

    //MyVariable
    private Color mouseHover = new Color(62,88,114);
    private Color colorBase = new Color(47,67,86);
    private Color statusBorrowColorBase = new Color(27,86,124);
    private Color statusBorrowOK = new Color(17,124,103);
    private Color statusBorrowNotOK = new Color(124,37,27);
    
    //removePanel
    private void removePanel(){
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
    }
    //addPanel
    private void addPanel(JPanel panel){
        mainPanel.add(panel);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
    private void loadDashboard(){
        try{
            int id_user=DatabaseOperations.getUsers("SELECT * FROM users WHERE username='"+UsernameLabel.getText()+"'").get(0).getId_user();
            int booksBorrowed=0;
            int booksReturned=0;
            int countBorrowed=DatabaseOperations.getReturned("SELECT * FROM returned WHERE status='Borrowed' AND id_user="+id_user+"").size();
            if(countBorrowed>0){
                booksBorrowed=countBorrowed;
            }
            int countReturned=DatabaseOperations.getReturned("SELECT * FROM returned WHERE status='Returned' AND id_user="+id_user+"").size();
            if(countReturned>0){
                booksReturned=countReturned;
            }
            dashBooksBorrowedText.setText(String.valueOf(booksBorrowed));
            dashBooksReturnedText.setText(String.valueOf(booksReturned));
        }catch(IndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(this, "No User Detected!");
        }
    }
    private String changeDateFormat(String date){
        String string = date;
        String[] parts = string.split("-");
        String day = parts[0]; 
        String month = parts[1]; 
        String year = parts[2];
        
        String result = year+"-"+month+"-"+day;
        
        return result;
    }
    private void buildBorrowTable(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Book's Title");
        model.addColumn("Borrow Date");
        model.addColumn("Returned Date");
        model.addColumn("Note");
        String textQuery ="SELECT * FROM users WHERE username = '"+UsernameLabel.getText()+"'";
        int id_user = DatabaseOperations.getUsers(textQuery).get(0).getId_user();
        String query = "SELECT * FROM returned WHERE id_user="+id_user+" AND status = 'Borrowed'";
        Object[] rowData=new Object[4];
        int valLength=DatabaseOperations.getReturned(query).size();
        for(int i = 0; i<valLength;i++){
            int id_buku=DatabaseOperations.getReturned(query).get(i).getId_buku();
            String booksQuery = "SELECT * FROM books  WHERE id_buku="+id_buku+"";
            rowData[0]=DatabaseOperations.getBooks(booksQuery).get(0).getJudul_buku();
            rowData[1]=DatabaseOperations.getReturned(query).get(i).getTanggal_peminjaman();
            rowData[2]=DatabaseOperations.getReturned(query).get(i).getTanggal_pengembalian();
            rowData[3]=DatabaseOperations.getReturned(query).get(i).getNote();
            model.addRow(rowData);
        }
        borrowTable.setModel(model);
    }
    private void buildReturnedTable(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Book's Title");
        model.addColumn("Borrow Date");
        model.addColumn("Returned Date");
        model.addColumn("Note");
        String textQuery ="SELECT * FROM users WHERE username = '"+UsernameLabel.getText()+"'";
        int id_user = DatabaseOperations.getUsers(textQuery).get(0).getId_user();
        String query = "SELECT * FROM returned WHERE id_user="+id_user+" AND status='Returned'";
        Object[] rowData=new Object[4];
        int valLength=DatabaseOperations.getReturned(query).size();
        for(int i = 0; i<valLength;i++){
            int id_buku=DatabaseOperations.getReturned(query).get(i).getId_buku();
            String booksQuery = "SELECT * FROM books  WHERE id_buku="+id_buku+"";
            rowData[0]=DatabaseOperations.getBooks(booksQuery).get(0).getJudul_buku();
            rowData[1]=DatabaseOperations.getReturned(query).get(i).getTanggal_peminjaman();
            rowData[2]=DatabaseOperations.getReturned(query).get(i).getTanggal_pengembalian();
            rowData[3]=DatabaseOperations.getReturned(query).get(i).getNote();
            model.addRow(rowData);
        }
        returnedTable.setModel(model);
    }
    private void buildBookTable(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Code");
        model.addColumn("Title");
        model.addColumn("Genre");
        model.addColumn("Author");
        model.addColumn("Publisher");
        model.addColumn("Year");
        model.addColumn("Available");
        String query = "SELECT * FROM books";
        Object[] rowData=new Object[7];
        int id_buku;
        int booksLength=DatabaseOperations.getBooks(query).size();
        for(int i = 0; i<booksLength;i++){
            id_buku=DatabaseOperations.getBooks(query).get(i).getId_buku();
            //Get Stok Borrowed
            String querr = "SELECT * FROM returned WHERE id_buku = "+id_buku+" AND status='Borrowed'";
            int stokBorrowed = DatabaseOperations.getReturned(querr).size();
            //End--
            rowData[0]=DatabaseOperations.getBooks(query).get(i).getKode_buku();
            rowData[1]=DatabaseOperations.getBooks(query).get(i).getJudul_buku();
            rowData[2]=DatabaseOperations.getBooks(query).get(i).getGenre_buku();
            rowData[3]=DatabaseOperations.getBooks(query).get(i).getPenulis_buku();
            rowData[4]=DatabaseOperations.getBooks(query).get(i).getPenerbit_buku();
            rowData[5]=DatabaseOperations.getBooks(query).get(i).getTahun_terbit();
            rowData[6]=DatabaseOperations.getBooks(query).get(i).getStok()-stokBorrowed;
            model.addRow(rowData);
        }
        booksTable.setModel(model);
    }
    private void rebuildBookTableKeyword(String keyword){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID ");
        model.addColumn("Code");
        model.addColumn("Title");
        model.addColumn("Genre");
        model.addColumn("Author");
        model.addColumn("Publisher");
        model.addColumn("Year");
        model.addColumn("Stock");
  
        Object[] rowData=new Object[8];
        int booksLength=DatabaseOperations.findBooks(keyword).size();
        for(int i = 0; i<booksLength;i++){
            rowData[0]=DatabaseOperations.findBooks(keyword).get(i).getId_buku();
            rowData[1]=DatabaseOperations.findBooks(keyword).get(i).getKode_buku();
            rowData[2]=DatabaseOperations.findBooks(keyword).get(i).getJudul_buku();
            rowData[3]=DatabaseOperations.findBooks(keyword).get(i).getGenre_buku();
            rowData[4]=DatabaseOperations.findBooks(keyword).get(i).getPenulis_buku();
            rowData[5]=DatabaseOperations.findBooks(keyword).get(i).getPenerbit_buku();
            rowData[6]=DatabaseOperations.findBooks(keyword).get(i).getTahun_terbit();
            rowData[7]=DatabaseOperations.findBooks(keyword).get(i).getStok();
            model.addRow(rowData);
        }
        booksTable.setModel(model);
    }
    private void resetBookForm(){
        kodeBukuText.setText("");
        judulBukuText.setText("");
        genreBukuText.setText("");
        penulisBukuText.setText("");
        penerbitBukuText.setText("");
        tahunTerbitBukuText.setText("");
        stokBukuText.setText("");
    }
    private void resetReturnForm(){
        returnBookTitleText.setText("");
        returnBorrowDateText.setText("");
        returnReturnDateText.setText("");
        returnNoteTextArea.setText("");
    }
    public void resetBorrowForm(){
        booksTitleBorrowedText.setText("");
        borrowDateText.setText("");
        returnDateText.setText("");
        noteBorrowText.setText("");
        expDayText.setText("");
        fineText.setText("");
    }
    public General(String username) {
        initComponents();
        UsernameLabel.setText(username);
        loadDashboard();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bodyPanel = new javax.swing.JPanel();
        sideNav = new javax.swing.JPanel();
        btnDashboard = new javax.swing.JLabel();
        btnReturned = new javax.swing.JLabel();
        core = new javax.swing.JLabel();
        account = new javax.swing.JLabel();
        btnBorrow = new javax.swing.JLabel();
        transaction = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();
        btnBooks = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        dashboardPanel = new javax.swing.JPanel();
        headerLabelDashboard = new javax.swing.JLabel();
        mainCardPanel = new javax.swing.JPanel();
        panelTotalBooks3 = new javax.swing.JPanel();
        dashBooksBorrowedLabel = new javax.swing.JLabel();
        dashBooksBorrowedText = new javax.swing.JLabel();
        panelTotalBooks4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        dashBooksReturnedText = new javax.swing.JLabel();
        borrowPanel = new javax.swing.JPanel();
        headerLabelBorrow = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        booksTitleBorrowedLabel = new javax.swing.JLabel();
        booksTitleBorrowedText = new javax.swing.JTextField();
        borrowDateLabel = new javax.swing.JLabel();
        borrowDateText = new javax.swing.JTextField();
        returnDateLabel = new javax.swing.JLabel();
        returnDateText = new javax.swing.JTextField();
        noteBorrowLabel = new javax.swing.JLabel();
        expDayLabel = new javax.swing.JLabel();
        expDayText = new javax.swing.JTextField();
        fineLabel = new javax.swing.JLabel();
        fineText = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        noteBorrowText = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        borrowTable = new javax.swing.JTable();
        returnPanel = new javax.swing.JPanel();
        headerLabelReturned = new javax.swing.JLabel();
        JscrollPane4 = new javax.swing.JScrollPane();
        returnedTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        returnBookTitleText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        returnBorrowDateText = new javax.swing.JTextField();
        returnReturnDateText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        returnNoteTextArea = new javax.swing.JTextArea();
        booksPanel = new javax.swing.JPanel();
        headerLabelBooks = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        booksTable = new javax.swing.JTable();
        booksKeyword = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        kodeBukuText = new javax.swing.JTextField();
        judulBukuText = new javax.swing.JTextField();
        kodeBukulLabel = new javax.swing.JLabel();
        judulBukuLabel = new javax.swing.JLabel();
        genreBukuLabel = new javax.swing.JLabel();
        genreBukuText = new javax.swing.JTextField();
        penulisBukuText = new javax.swing.JTextField();
        penulisBukuLabel = new javax.swing.JLabel();
        penerbitBukuLabel = new javax.swing.JLabel();
        penerbitBukuText = new javax.swing.JTextField();
        stokBukuText = new javax.swing.JTextField();
        stokBukuLabel = new javax.swing.JLabel();
        tahunTerbitBukuLabel = new javax.swing.JLabel();
        tahunTerbitBukuText = new javax.swing.JTextField();
        navigasi = new javax.swing.JPanel();
        userTypeLabel = new javax.swing.JLabel();
        BrandManagement = new javax.swing.JLabel();
        BrandLibrary = new javax.swing.JLabel();
        UsernameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Library Management System");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        bodyPanel.setBackground(new java.awt.Color(52, 73, 94));
        bodyPanel.setMinimumSize(new java.awt.Dimension(1220, 700));
        bodyPanel.setPreferredSize(new java.awt.Dimension(1220, 700));
        bodyPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sideNav.setBackground(new java.awt.Color(42, 59, 76));
        sideNav.setForeground(new java.awt.Color(255, 255, 255));
        sideNav.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDashboard.setBackground(new java.awt.Color(47, 67, 86));
        btnDashboard.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(236, 240, 241));
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/dashboard.png"))); // NOI18N
        btnDashboard.setText(" Dashboard");
        btnDashboard.setOpaque(true);
        btnDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDashboardMouseExited(evt);
            }
        });
        sideNav.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 49, 220, 58));

        btnReturned.setBackground(new java.awt.Color(47, 67, 86));
        btnReturned.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnReturned.setForeground(new java.awt.Color(236, 240, 241));
        btnReturned.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnReturned.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/returned.png"))); // NOI18N
        btnReturned.setText("Returned");
        btnReturned.setOpaque(true);
        btnReturned.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReturnedMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReturnedMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReturnedMouseExited(evt);
            }
        });
        sideNav.add(btnReturned, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 220, 58));

        core.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        core.setForeground(new java.awt.Color(236, 240, 241));
        core.setText("    CORE");
        sideNav.add(core, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 11, 220, 32));

        account.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        account.setForeground(new java.awt.Color(236, 240, 241));
        account.setText("    ACCOUNT");
        sideNav.add(account, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 220, 32));

        btnBorrow.setBackground(new java.awt.Color(47, 67, 86));
        btnBorrow.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnBorrow.setForeground(new java.awt.Color(236, 240, 241));
        btnBorrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnBorrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/borrow.png"))); // NOI18N
        btnBorrow.setText("Borrow");
        btnBorrow.setOpaque(true);
        btnBorrow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBorrowMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBorrowMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBorrowMouseExited(evt);
            }
        });
        sideNav.add(btnBorrow, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 151, 220, 58));

        transaction.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        transaction.setForeground(new java.awt.Color(236, 240, 241));
        transaction.setText("    TRANSACTIONS");
        sideNav.add(transaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 113, 220, 32));

        btnLogOut.setBackground(new java.awt.Color(47, 67, 86));
        btnLogOut.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnLogOut.setForeground(new java.awt.Color(236, 240, 241));
        btnLogOut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exit.png"))); // NOI18N
        btnLogOut.setText("LogOut");
        btnLogOut.setOpaque(true);
        btnLogOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogOutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogOutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogOutMouseExited(evt);
            }
        });
        sideNav.add(btnLogOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 220, 58));

        settings.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        settings.setForeground(new java.awt.Color(236, 240, 241));
        settings.setText("    SETTINGS");
        sideNav.add(settings, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 220, 32));

        btnBooks.setBackground(new java.awt.Color(47, 67, 86));
        btnBooks.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnBooks.setForeground(new java.awt.Color(236, 240, 241));
        btnBooks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnBooks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/books.png"))); // NOI18N
        btnBooks.setText("Books");
        btnBooks.setOpaque(true);
        btnBooks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBooksMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBooksMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBooksMouseExited(evt);
            }
        });
        sideNav.add(btnBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 220, 58));

        bodyPanel.add(sideNav, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 98, 220, 600));

        mainPanel.setBackground(new java.awt.Color(236, 240, 241));
        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(236, 240, 241)));
        mainPanel.setLayout(new java.awt.CardLayout());

        headerLabelDashboard.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerLabelDashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabelDashboard.setText("Dashboard");

        panelTotalBooks3.setBackground(new java.awt.Color(63, 121, 193));

        dashBooksBorrowedLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dashBooksBorrowedLabel.setForeground(new java.awt.Color(255, 255, 255));
        dashBooksBorrowedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashBooksBorrowedLabel.setText("Books Borrowed");

        dashBooksBorrowedText.setBackground(new java.awt.Color(63, 121, 193));
        dashBooksBorrowedText.setFont(new java.awt.Font("Segoe UI", 1, 100)); // NOI18N
        dashBooksBorrowedText.setForeground(new java.awt.Color(255, 255, 255));
        dashBooksBorrowedText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashBooksBorrowedText.setText("0");

        javax.swing.GroupLayout panelTotalBooks3Layout = new javax.swing.GroupLayout(panelTotalBooks3);
        panelTotalBooks3.setLayout(panelTotalBooks3Layout);
        panelTotalBooks3Layout.setHorizontalGroup(
            panelTotalBooks3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalBooks3Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(dashBooksBorrowedText, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashBooksBorrowedLabel)
                .addGap(33, 33, 33))
        );
        panelTotalBooks3Layout.setVerticalGroup(
            panelTotalBooks3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks3Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(dashBooksBorrowedText, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashBooksBorrowedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        panelTotalBooks4.setBackground(new java.awt.Color(244, 129, 19));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Books Returned");

        dashBooksReturnedText.setFont(new java.awt.Font("Segoe UI", 1, 100)); // NOI18N
        dashBooksReturnedText.setForeground(new java.awt.Color(255, 255, 255));
        dashBooksReturnedText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashBooksReturnedText.setText("0");

        javax.swing.GroupLayout panelTotalBooks4Layout = new javax.swing.GroupLayout(panelTotalBooks4);
        panelTotalBooks4.setLayout(panelTotalBooks4Layout);
        panelTotalBooks4Layout.setHorizontalGroup(
            panelTotalBooks4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalBooks4Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(dashBooksReturnedText, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(36, 36, 36))
        );
        panelTotalBooks4Layout.setVerticalGroup(
            panelTotalBooks4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks4Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(dashBooksReturnedText, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout mainCardPanelLayout = new javax.swing.GroupLayout(mainCardPanel);
        mainCardPanel.setLayout(mainCardPanelLayout);
        mainCardPanelLayout.setHorizontalGroup(
            mainCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainCardPanelLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(panelTotalBooks3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(panelTotalBooks4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(354, Short.MAX_VALUE))
        );
        mainCardPanelLayout.setVerticalGroup(
            mainCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainCardPanelLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(mainCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelTotalBooks4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelTotalBooks3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(286, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabelDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(mainCardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabelDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainCardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.add(dashboardPanel, "card2");

        headerLabelBorrow.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerLabelBorrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabelBorrow.setText("Borrow");

        booksTitleBorrowedLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        booksTitleBorrowedLabel.setText("Book's Title");

        booksTitleBorrowedText.setEditable(false);
        booksTitleBorrowedText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        borrowDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        borrowDateLabel.setText("Borrow Date");

        borrowDateText.setEditable(false);
        borrowDateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        returnDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        returnDateLabel.setText("Return Date");

        returnDateText.setEditable(false);
        returnDateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        noteBorrowLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        noteBorrowLabel.setText("Note");

        expDayLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        expDayLabel.setText("Expiration Day");

        expDayText.setEditable(false);
        expDayText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        fineLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fineLabel.setText("Fine");

        fineText.setEditable(false);
        fineText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        noteBorrowText.setEditable(false);
        noteBorrowText.setColumns(20);
        noteBorrowText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        noteBorrowText.setRows(5);
        jScrollPane3.setViewportView(noteBorrowText);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(booksTitleBorrowedLabel)
                    .addComponent(borrowDateLabel)
                    .addComponent(returnDateLabel)
                    .addComponent(noteBorrowLabel)
                    .addComponent(expDayLabel)
                    .addComponent(fineLabel))
                .addGap(69, 69, 69)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fineText)
                    .addComponent(expDayText)
                    .addComponent(jScrollPane3)
                    .addComponent(booksTitleBorrowedText, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borrowDateText)
                    .addComponent(returnDateText))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(booksTitleBorrowedLabel)
                    .addComponent(booksTitleBorrowedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(borrowDateLabel)
                    .addComponent(borrowDateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnDateLabel)
                    .addComponent(returnDateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(noteBorrowLabel)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expDayLabel)
                    .addComponent(expDayText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fineLabel)
                    .addComponent(fineText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(217, 217, 217))
        );

        borrowTable.setAutoCreateRowSorter(true);
        borrowTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        borrowTable.getTableHeader().setResizingAllowed(false);
        borrowTable.getTableHeader().setReorderingAllowed(false);
        borrowTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borrowTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(borrowTable);

        javax.swing.GroupLayout borrowPanelLayout = new javax.swing.GroupLayout(borrowPanel);
        borrowPanel.setLayout(borrowPanelLayout);
        borrowPanelLayout.setHorizontalGroup(
            borrowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borrowPanelLayout.createSequentialGroup()
                .addGroup(borrowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(borrowPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(headerLabelBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(borrowPanelLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)))
                .addGap(14, 14, 14))
        );
        borrowPanelLayout.setVerticalGroup(
            borrowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borrowPanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(headerLabelBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(borrowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(161, Short.MAX_VALUE))
        );

        mainPanel.add(borrowPanel, "card2");

        headerLabelReturned.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerLabelReturned.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabelReturned.setText("Returned");

        returnedTable.setAutoCreateRowSorter(true);
        returnedTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        returnedTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        returnedTable.getTableHeader().setResizingAllowed(false);
        returnedTable.getTableHeader().setReorderingAllowed(false);
        returnedTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnedTableMouseClicked(evt);
            }
        });
        JscrollPane4.setViewportView(returnedTable);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Book's title");

        returnBookTitleText.setEditable(false);
        returnBookTitleText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Borrow Date");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Return Date");

        returnBorrowDateText.setEditable(false);
        returnBorrowDateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        returnReturnDateText.setEditable(false);
        returnReturnDateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Note");

        returnNoteTextArea.setEditable(false);
        returnNoteTextArea.setColumns(20);
        returnNoteTextArea.setRows(5);
        jScrollPane4.setViewportView(returnNoteTextArea);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(returnReturnDateText, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(returnBorrowDateText, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(returnBookTitleText, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addComponent(returnBookTitleText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returnBorrowDateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returnReturnDateText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
        );

        javax.swing.GroupLayout returnPanelLayout = new javax.swing.GroupLayout(returnPanel);
        returnPanel.setLayout(returnPanelLayout);
        returnPanelLayout.setHorizontalGroup(
            returnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(returnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(returnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(returnPanelLayout.createSequentialGroup()
                        .addComponent(headerLabelReturned, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(returnPanelLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(JscrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
        );
        returnPanelLayout.setVerticalGroup(
            returnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(returnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabelReturned, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(returnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JscrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        mainPanel.add(returnPanel, "card2");

        booksPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerLabelBooks.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerLabelBooks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabelBooks.setText("Books");
        booksPanel.add(headerLabelBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 86, 37));

        booksTable.setAutoCreateRowSorter(true);
        booksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        booksTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        booksTable.getTableHeader().setResizingAllowed(false);
        booksTable.getTableHeader().setReorderingAllowed(false);
        booksTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                booksTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(booksTable);

        booksPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 679, -1));

        booksKeyword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        booksKeyword.setText("Search");
        booksKeyword.setToolTipText("Search");
        booksKeyword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                booksKeywordMouseClicked(evt);
            }
        });
        booksKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                booksKeywordKeyPressed(evt);
            }
        });
        booksPanel.add(booksKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 40, 190, -1));

        kodeBukuText.setEditable(false);
        kodeBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        judulBukuText.setEditable(false);
        judulBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        kodeBukulLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kodeBukulLabel.setText("Code");

        judulBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        judulBukuLabel.setText("Title");

        genreBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        genreBukuLabel.setText("Genre");

        genreBukuText.setEditable(false);
        genreBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        penulisBukuText.setEditable(false);
        penulisBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        penulisBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        penulisBukuLabel.setText("Author");

        penerbitBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        penerbitBukuLabel.setText("Publisher");

        penerbitBukuText.setEditable(false);
        penerbitBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        stokBukuText.setEditable(false);
        stokBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        stokBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stokBukuLabel.setText("Stock Available");

        tahunTerbitBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tahunTerbitBukuLabel.setText("Year of publication");

        tahunTerbitBukuText.setEditable(false);
        tahunTerbitBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(penerbitBukuLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tahunTerbitBukuLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                        .addGap(36, 36, 36)
                        .addComponent(stokBukuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tahunTerbitBukuText)
                        .addGap(36, 36, 36)
                        .addComponent(stokBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(kodeBukuText, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(judulBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(judulBukuText)
                                .addComponent(genreBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(genreBukuText)
                                .addComponent(penulisBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addComponent(penulisBukuText)
                                .addComponent(penerbitBukuText, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(kodeBukulLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(kodeBukulLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kodeBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(judulBukuLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(judulBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genreBukuLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genreBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penulisBukuLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penulisBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penerbitBukuLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penerbitBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tahunTerbitBukuLabel)
                    .addComponent(stokBukuLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tahunTerbitBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stokBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(78, Short.MAX_VALUE))
        );

        booksPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 280, 420));

        mainPanel.add(booksPanel, "card2");

        bodyPanel.add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 1000, 600));

        navigasi.setBackground(new java.awt.Color(42, 59, 76));
        navigasi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userTypeLabel.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        userTypeLabel.setForeground(new java.awt.Color(236, 240, 241));
        userTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        userTypeLabel.setText("General");
        navigasi.add(userTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 40, 170, 40));

        BrandManagement.setFont(new java.awt.Font("Segoe UI", 1, 42)); // NOI18N
        BrandManagement.setForeground(new java.awt.Color(236, 240, 241));
        BrandManagement.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BrandManagement.setText("Library");
        navigasi.add(BrandManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 170, 50));

        BrandLibrary.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        BrandLibrary.setForeground(new java.awt.Color(236, 240, 241));
        BrandLibrary.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BrandLibrary.setText("Management");
        navigasi.add(BrandLibrary, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 170, 50));

        UsernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        UsernameLabel.setForeground(new java.awt.Color(236, 240, 241));
        UsernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        UsernameLabel.setText("Username");
        navigasi.add(UsernameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 10, 180, 50));

        bodyPanel.add(navigasi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -2, 1220, 100));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    //MOUSE HOVER
    private void btnDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashboardMouseEntered
        btnDashboard.setBackground(mouseHover);
    }//GEN-LAST:event_btnDashboardMouseEntered

    private void btnDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashboardMouseExited
        btnDashboard.setBackground(colorBase);
    }//GEN-LAST:event_btnDashboardMouseExited

    private void btnDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashboardMouseClicked
        loadDashboard();
        removePanel();
        addPanel(dashboardPanel);
    }//GEN-LAST:event_btnDashboardMouseClicked

    private void btnBorrowMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrowMouseEntered
        btnBorrow.setBackground(mouseHover);
    }//GEN-LAST:event_btnBorrowMouseEntered

    private void btnBorrowMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrowMouseExited
        btnBorrow.setBackground(colorBase);
    }//GEN-LAST:event_btnBorrowMouseExited

    private void btnReturnedMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReturnedMouseEntered
        btnReturned.setBackground(mouseHover);
    }//GEN-LAST:event_btnReturnedMouseEntered

    private void btnReturnedMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReturnedMouseExited
        btnReturned.setBackground(colorBase);
    }//GEN-LAST:event_btnReturnedMouseExited

    private void btnLogOutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutMouseEntered
        btnLogOut.setBackground(mouseHover);
    }//GEN-LAST:event_btnLogOutMouseEntered

    private void btnLogOutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutMouseExited
        btnLogOut.setBackground(colorBase);
    }//GEN-LAST:event_btnLogOutMouseExited

    private void btnBooksMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBooksMouseEntered
        btnBooks.setBackground(mouseHover);
    }//GEN-LAST:event_btnBooksMouseEntered

    private void btnBooksMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBooksMouseExited
        btnBooks.setBackground(colorBase);
    }//GEN-LAST:event_btnBooksMouseExited

    private void btnBorrowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrowMouseClicked
        removePanel();
        addPanel(borrowPanel);
        resetBorrowForm();
        buildBorrowTable();
    }//GEN-LAST:event_btnBorrowMouseClicked

    private void btnReturnedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReturnedMouseClicked
        removePanel();
        addPanel(returnPanel);
        resetReturnForm();
        buildReturnedTable();
    }//GEN-LAST:event_btnReturnedMouseClicked

    private void btnBooksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBooksMouseClicked
        removePanel();
        addPanel(booksPanel);
        resetBookForm();
        buildBookTable();
    }//GEN-LAST:event_btnBooksMouseClicked

    private void btnLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutMouseClicked
        int dialogBtn = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit", dialogBtn);
        if(dialogResult==0){
            Login logout = new Login();
            logout.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnLogOutMouseClicked

    private void booksKeywordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_booksKeywordMouseClicked
        booksKeyword.setText("");
    }//GEN-LAST:event_booksKeywordMouseClicked

    private void booksTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_booksTableMouseClicked
        int baris = booksTable.rowAtPoint(evt.getPoint());
        String Code = booksTable.getValueAt(baris, 0).toString();
        kodeBukuText.setText(Code);
        String Title = booksTable.getValueAt(baris, 1).toString();
        judulBukuText.setText(Title);
        String genre = booksTable.getValueAt(baris, 2).toString();
        genreBukuText.setText(genre);
        String author = booksTable.getValueAt(baris, 3).toString();
        penulisBukuText.setText(author);
        String publisher = booksTable.getValueAt(baris, 4).toString();
        penerbitBukuText.setText(publisher);
        String year = booksTable.getValueAt(baris, 5).toString();
        tahunTerbitBukuText.setText(year);
        String stock = booksTable.getValueAt(baris, 6).toString();
        stokBukuText.setText(stock);
    }//GEN-LAST:event_booksTableMouseClicked

    private void booksKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_booksKeywordKeyPressed
        if(evt.getKeyCode()==10){
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(booksKeyword.getText());
            boolean b = m.find();
            if (b){
               JOptionPane.showMessageDialog(this, "There is a special character in my fields");
            }else{
                String keyword= booksKeyword.getText();
                rebuildBookTableKeyword(keyword);
            }
        }
    }//GEN-LAST:event_booksKeywordKeyPressed

    private void returnedTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnedTableMouseClicked
        int baris = returnedTable.rowAtPoint(evt.getPoint());
        String booksTitle = returnedTable.getValueAt(baris, 0).toString();
        returnBookTitleText.setText(booksTitle);
        String borrowDate = returnedTable.getValueAt(baris, 1).toString();
        returnBorrowDateText.setText(borrowDate);
        String returnDate = returnedTable.getValueAt(baris, 2).toString();
        returnReturnDateText.setText(returnDate);
        String note = returnedTable.getValueAt(baris, 3).toString();
        returnNoteTextArea.setText(note);
    }//GEN-LAST:event_returnedTableMouseClicked

    private void borrowTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowTableMouseClicked
        int baris = borrowTable.rowAtPoint(evt.getPoint());
        String booksTitle = borrowTable.getValueAt(baris, 0).toString();
        booksTitleBorrowedText.setText(booksTitle);
        String borrowDate = borrowTable.getValueAt(baris, 1).toString();
        borrowDateText.setText(borrowDate);
        String returnDate = borrowTable.getValueAt(baris, 2).toString();
        returnDateText.setText(returnDate);
        String note = borrowTable.getValueAt(baris, 3).toString();
        noteBorrowText.setText(note);
        
        //Calculate fine
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateNow = currentDateTime.format(formatter);
                        
        String dateobj1= returnDateText.getText();
        String dateobj2= dateNow;
        String dateBeforeString = changeDateFormat(dateobj1);
        String dateAfterString = dateobj2;

        //Parsing the date
        LocalDate dateBefore = LocalDate.parse(dateBeforeString);
        LocalDate dateAfter = LocalDate.parse(dateAfterString);

        //calculating number of days in between
        long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
        int daysCount= (int)noOfDaysBetween;
        Invoice invoice = new Invoice();
        invoice.setDaysCount(daysCount);
        invoice.setHargaDenda(3000);
        int fine = invoice.getTotalDenda();
        //End
        
        
        expDayText.setText(String.valueOf(daysCount));
        fineText.setText(String.valueOf(fine));
    }//GEN-LAST:event_borrowTableMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(General.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(General.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(General.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(General.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new General("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BrandLibrary;
    private javax.swing.JLabel BrandManagement;
    private javax.swing.JScrollPane JscrollPane4;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JLabel account;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JTextField booksKeyword;
    private javax.swing.JPanel booksPanel;
    private javax.swing.JTable booksTable;
    private javax.swing.JLabel booksTitleBorrowedLabel;
    private javax.swing.JTextField booksTitleBorrowedText;
    private javax.swing.JLabel borrowDateLabel;
    private javax.swing.JTextField borrowDateText;
    private javax.swing.JPanel borrowPanel;
    private javax.swing.JTable borrowTable;
    private javax.swing.JLabel btnBooks;
    private javax.swing.JLabel btnBorrow;
    private javax.swing.JLabel btnDashboard;
    private javax.swing.JLabel btnLogOut;
    private javax.swing.JLabel btnReturned;
    private javax.swing.JLabel core;
    private javax.swing.JLabel dashBooksBorrowedLabel;
    private javax.swing.JLabel dashBooksBorrowedText;
    private javax.swing.JLabel dashBooksReturnedText;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel expDayLabel;
    private javax.swing.JTextField expDayText;
    private javax.swing.JLabel fineLabel;
    private javax.swing.JTextField fineText;
    private javax.swing.JLabel genreBukuLabel;
    private javax.swing.JTextField genreBukuText;
    private javax.swing.JLabel headerLabelBooks;
    private javax.swing.JLabel headerLabelBorrow;
    private javax.swing.JLabel headerLabelDashboard;
    private javax.swing.JLabel headerLabelReturned;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel judulBukuLabel;
    private javax.swing.JTextField judulBukuText;
    private javax.swing.JTextField kodeBukuText;
    private javax.swing.JLabel kodeBukulLabel;
    private javax.swing.JPanel mainCardPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel navigasi;
    private javax.swing.JLabel noteBorrowLabel;
    private javax.swing.JTextArea noteBorrowText;
    private javax.swing.JPanel panelTotalBooks3;
    private javax.swing.JPanel panelTotalBooks4;
    private javax.swing.JLabel penerbitBukuLabel;
    private javax.swing.JTextField penerbitBukuText;
    private javax.swing.JLabel penulisBukuLabel;
    private javax.swing.JTextField penulisBukuText;
    private javax.swing.JTextField returnBookTitleText;
    private javax.swing.JTextField returnBorrowDateText;
    private javax.swing.JLabel returnDateLabel;
    private javax.swing.JTextField returnDateText;
    private javax.swing.JTextArea returnNoteTextArea;
    private javax.swing.JPanel returnPanel;
    private javax.swing.JTextField returnReturnDateText;
    private javax.swing.JTable returnedTable;
    private javax.swing.JLabel settings;
    private javax.swing.JPanel sideNav;
    private javax.swing.JLabel stokBukuLabel;
    private javax.swing.JTextField stokBukuText;
    private javax.swing.JLabel tahunTerbitBukuLabel;
    private javax.swing.JTextField tahunTerbitBukuText;
    private javax.swing.JLabel transaction;
    private javax.swing.JLabel userTypeLabel;
    // End of variables declaration//GEN-END:variables
}
