
package UserInterface;

import functions.Book;
import functions.Configuration;
import functions.DatabaseOperations;
import functions.Invoice;
import functions.User;
import java.awt.Color;
import java.awt.event.KeyEvent;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Librarian extends javax.swing.JFrame {

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
    //reset userForm
    private void resetUserForm(){
        idText.setText("");
        namaText.setText("");
        usernameText.setText("");
    }
    private void loadDashboard(){
        int totalBooks=0;
        int totalUsersAccount=0;
        int booksBorrowed=0;
        int booksReturned=0;
        int countBooks=DatabaseOperations.getBooks("SELECT * FROM books").size();
        if(countBooks>0){
            totalBooks=countBooks;
        }
        int countUsers=DatabaseOperations.getUsers("SELECT * FROM users").size();
        if(countUsers>0){
            totalUsersAccount=countUsers;
        }
        int countBorrowed=DatabaseOperations.getReturned("SELECT * FROM returned WHERE status='Borrowed'").size();
        if(countBorrowed>0){
            booksBorrowed=countBorrowed;
        }
        int countReturned=DatabaseOperations.getReturned("SELECT * FROM returned WHERE status='Returned'").size();
        if(countReturned>0){
            booksReturned=countReturned;
        }
        dashTotalBooksText.setText(String.valueOf(totalBooks));
        dashTotalAccountText.setText(String.valueOf(totalUsersAccount));
        dashBooksBorrowedText.setText(String.valueOf(booksBorrowed));
        dashBooksReturnedText.setText(String.valueOf(booksReturned));
    }
    private void buildReturnedTable(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Book ID");
        model.addColumn("User ID");
        model.addColumn("Status");
        model.addColumn("Date Borrow");
        model.addColumn("Date Return");
        model.addColumn("Note");
        String query = "SELECT * FROM returned";
        Object[] rowData=new Object[7];
        int valLength=DatabaseOperations.getReturned(query).size();
        for(int i = 0; i<valLength;i++){
            rowData[0]=DatabaseOperations.getReturned(query).get(i).getId_peminjaman();
            rowData[1]=DatabaseOperations.getReturned(query).get(i).getId_buku();
            rowData[2]=DatabaseOperations.getReturned(query).get(i).getId_user();
            rowData[3]=DatabaseOperations.getReturned(query).get(i).getStatus();
            rowData[4]=DatabaseOperations.getReturned(query).get(i).getTanggal_peminjaman();
            rowData[5]=DatabaseOperations.getReturned(query).get(i).getTanggal_pengembalian();
            rowData[6]=DatabaseOperations.getReturned(query).get(i).getNote();
            model.addRow(rowData);
        }
        returnedTable.setModel(model);
    }
    private void rebuildReturnedTable(String keyword){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Book ID");
        model.addColumn("User ID");
        model.addColumn("Status");
        model.addColumn("Date Borrow");
        model.addColumn("Date Return");
        model.addColumn("Note");
        Object[] rowData=new Object[7];
        int valLength=DatabaseOperations.findReturned(keyword).size();
        for(int i = 0; i<valLength;i++){
            rowData[0]=DatabaseOperations.findReturned(keyword).get(i).getId_peminjaman();
            rowData[1]=DatabaseOperations.findReturned(keyword).get(i).getId_buku();
            rowData[2]=DatabaseOperations.findReturned(keyword).get(i).getId_user();
            rowData[3]=DatabaseOperations.findReturned(keyword).get(i).getStatus();
            rowData[4]=DatabaseOperations.findReturned(keyword).get(i).getTanggal_peminjaman();
            rowData[5]=DatabaseOperations.findReturned(keyword).get(i).getTanggal_pengembalian();
            rowData[6]=DatabaseOperations.findReturned(keyword).get(i).getNote();
            model.addRow(rowData);
        }
        returnedTable.setModel(model);
    }
    
    private void buildBookTable(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID ");
        model.addColumn("Code");
        model.addColumn("Title");
        model.addColumn("Genre");
        model.addColumn("Author");
        model.addColumn("Publisher");
        model.addColumn("Year");
        model.addColumn("Stock");
        String query = "SELECT * FROM books";
        Object[] rowData=new Object[8];
        int booksLength=DatabaseOperations.getBooks(query).size();
        for(int i = 0; i<booksLength;i++){
            rowData[0]=DatabaseOperations.getBooks(query).get(i).getId_buku();
            rowData[1]=DatabaseOperations.getBooks(query).get(i).getKode_buku();
            rowData[2]=DatabaseOperations.getBooks(query).get(i).getJudul_buku();
            rowData[3]=DatabaseOperations.getBooks(query).get(i).getGenre_buku();
            rowData[4]=DatabaseOperations.getBooks(query).get(i).getPenulis_buku();
            rowData[5]=DatabaseOperations.getBooks(query).get(i).getPenerbit_buku();
            rowData[6]=DatabaseOperations.getBooks(query).get(i).getTahun_terbit();
            rowData[7]=DatabaseOperations.getBooks(query).get(i).getStok();
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
    private void resetaddBookForm(){
        addCodeBukuText.setText("");
        addTitleBukuText.setText("");
        addGenreBukuText.setText("");
        addAuthorBukuText.setText("");
        addPublisherBukuText.setText("");
        addYearBukuText.setText("");
        addStockBukuText.setText("");
    }
    private void resetBorrowForm(){
        enterBookIDText.setText("");
        enterUsernameText.setText("");
        statusBorrowLabel.setText("Yes or No");
        statusBorrowLabel.setForeground(statusBorrowColorBase);
        enterBorrowingDate.setCalendar(null);
        enterReturnDate.setCalendar(null);
        noteTextArea.setText("");
    }
    private void resetBookForm(){
        idBukuText.setText("");
        kodeBukuText.setText("");
        judulBukuText.setText("");
        genreBukuText.setText("");
        penulisBukuText.setText("");
        penerbitBukuText.setText("");
        tahunTerbitBukuText.setText("");
        stokBukuText.setText("");
    }
    private void resetPaymentPanel(){
        idPeminjamanText.setText("");
        bookIDText.setText("");
        bookTitleText.setText("");
        userIDText.setText("");
        usernamePayText.setText("");
        exipiryDateText.setText("");
        returnDateText.setText("");
        fineText.setText("");
    }
    private boolean checkNullBookForm(){
        if(idBukuText.getText().isEmpty() || kodeBukuText.getText().isEmpty() || judulBukuText.getText().isEmpty() || genreBukuText.getText().isEmpty() || penulisBukuText.getText().isEmpty() || penerbitBukuText.getText().isEmpty() || tahunTerbitBukuText.getText().isEmpty() || stokBukuText.getText().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    private boolean checkNullReturnForm(){
        if(returnIDText.getText().isEmpty() || returnBookIDText.getText().isEmpty() || returnBookTitleText.getText().isEmpty() || returnUserIDText.getText().isEmpty() || returnUsernameText.getText().isEmpty() || returnStatusText.getText().isEmpty() || returnBorrowDateText.getText().isEmpty() || returnReturnDateText.getText().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    private void resetBookInfoPanel(){
        idBookInfo.setText("");
        codeBookInfo.setText("");
        titleBookInfo.setText("");
        genreBookInfo.setText("");
        authorBookInfo.setText("");
        yearBookInfo.setText("");
        publisherBookInfo.setText("");
    }
    private void resetUserInfoPanel(){
        idUserInfo.setText("");
        nameidUserInfo.setText("");
        usernameidUserInfo.setText("");
        usertypeidUserInfo.setText("");
    }
    private void resetReturnForm(){
        returnIDText.setText("");
        returnBookIDText.setText("");
        returnBookTitleText.setText("");
        returnUserIDText.setText("");
        returnUsernameText.setText("");
        returnStatusText.setText("");
        returnBorrowDateText.setText("");
        returnReturnDateText.setText("");
        returnNoteTextArea.setText("");
        returnKeyword.setText("Search");
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
    private String changeDateFormat2(String date){
        String string = date;
        String[] parts = string.split("-");
        String year = parts[0]; 
        String month = parts[1]; 
        String day = parts[2];
        
        String result = day+"-"+month+"-"+year;
        
        return result;
    }
    private void inputNumberOnly(KeyEvent evt, JTextField textfield){
        if (evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' || evt.getKeyCode()==8 || evt.getKeyCode()==10) {
            textfield.setEditable(true);
        }else {
            textfield.setEditable(false);
            JOptionPane.showMessageDialog(this, "Please input value in number");
        }
    }
    public Librarian(String username) {
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
        btnUsers = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();
        btnBooks = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        dashboardPanel = new javax.swing.JPanel();
        headerLabelDashboard = new javax.swing.JLabel();
        mainCardPanel = new javax.swing.JPanel();
        panelTotalBooks = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        dashTotalBooksText = new javax.swing.JLabel();
        panelTotalBooks2 = new javax.swing.JPanel();
        dashTotalAccountLabel = new javax.swing.JLabel();
        dashTotalAccountText = new javax.swing.JLabel();
        panelTotalBooks3 = new javax.swing.JPanel();
        dashBooksBorrowedLabel = new javax.swing.JLabel();
        dashBooksBorrowedText = new javax.swing.JLabel();
        panelTotalBooks4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        dashBooksReturnedText = new javax.swing.JLabel();
        borrowPanel = new javax.swing.JPanel();
        headerLabelBorrow = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        headerBookInfo1 = new javax.swing.JLabel();
        headerBookInfo2 = new javax.swing.JLabel();
        headerBookInfo3 = new javax.swing.JLabel();
        headerBookInfo4 = new javax.swing.JLabel();
        headerBookInfo5 = new javax.swing.JLabel();
        enterBookIDText = new javax.swing.JTextField();
        enterUsernameText = new javax.swing.JTextField();
        statusBorrowLabel = new javax.swing.JLabel();
        enterBorrowingDate = new com.toedter.calendar.JDateChooser();
        enterReturnDate = new com.toedter.calendar.JDateChooser();
        resetBorrow = new javax.swing.JButton();
        submitBorrow = new javax.swing.JButton();
        headerBookInfo17 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        noteTextArea = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        headerBookInfo = new javax.swing.JLabel();
        headerBookInfo6 = new javax.swing.JLabel();
        headerBookInfo7 = new javax.swing.JLabel();
        headerBookInfo8 = new javax.swing.JLabel();
        headerBookInfo9 = new javax.swing.JLabel();
        headerBookInfo10 = new javax.swing.JLabel();
        headerBookInfo11 = new javax.swing.JLabel();
        headerBookInfo12 = new javax.swing.JLabel();
        idBookInfo = new javax.swing.JTextField();
        codeBookInfo = new javax.swing.JTextField();
        titleBookInfo = new javax.swing.JTextField();
        genreBookInfo = new javax.swing.JTextField();
        authorBookInfo = new javax.swing.JTextField();
        yearBookInfo = new javax.swing.JTextField();
        publisherBookInfo = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        headerUserInfo = new javax.swing.JLabel();
        headerBookInfo13 = new javax.swing.JLabel();
        nameidUserInfo = new javax.swing.JTextField();
        headerBookInfo14 = new javax.swing.JLabel();
        usernameidUserInfo = new javax.swing.JTextField();
        headerBookInfo15 = new javax.swing.JLabel();
        idUserInfo = new javax.swing.JTextField();
        headerBookInfo16 = new javax.swing.JLabel();
        usertypeidUserInfo = new javax.swing.JTextField();
        returnPanel = new javax.swing.JPanel();
        headerLabelReturned = new javax.swing.JLabel();
        returnTable = new javax.swing.JScrollPane();
        returnedTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        returnIDText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        returnBookIDText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        returnBookTitleText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        returnUserIDText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        returnUsernameText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        returnStatusText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        returnBorrowDateText = new javax.swing.JTextField();
        returnReturnDateText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        returnNoteTextArea = new javax.swing.JTextArea();
        btnReturn = new javax.swing.JButton();
        btnLostReturn = new javax.swing.JButton();
        btnDeleteReturn = new javax.swing.JButton();
        returnKeyword = new javax.swing.JTextField();
        booksPanel = new javax.swing.JPanel();
        headerLabelBooks = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        booksTable = new javax.swing.JTable();
        btnAddBook = new javax.swing.JButton();
        booksKeyword = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        idBukuText = new javax.swing.JTextField();
        idBukulLabel = new javax.swing.JLabel();
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
        btnResetBuku = new javax.swing.JButton();
        btnDeleteBuku = new javax.swing.JButton();
        btnSaveBuku = new javax.swing.JButton();
        tahunTerbitBukuLabel = new javax.swing.JLabel();
        tahunTerbitBukuText = new javax.swing.JTextField();
        usersPanel = new javax.swing.JPanel();
        headerLabelUsers = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        keywordUser = new javax.swing.JTextField();
        btnAddUser = new javax.swing.JButton();
        namaUserLabel = new javax.swing.JLabel();
        tipeUserLabel = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();
        namaText = new javax.swing.JTextField();
        usernameText = new javax.swing.JTextField();
        userTypeCB = new javax.swing.JComboBox<>();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        idUserLabel = new javax.swing.JLabel();
        idText = new javax.swing.JTextField();
        addUserPanel = new javax.swing.JPanel();
        headerLabelAddUser = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        firstNameText = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        lastNameText = new javax.swing.JTextField();
        addUsernameLabel = new javax.swing.JLabel();
        addUsernameText = new javax.swing.JTextField();
        addPasswordLabel = new javax.swing.JLabel();
        btnSubmitAddUser = new javax.swing.JButton();
        usertypeCB = new javax.swing.JComboBox<>();
        usertypeLabel = new javax.swing.JLabel();
        addPasswordText = new javax.swing.JPasswordField();
        addBookPanel = new javax.swing.JPanel();
        headerAddBookLabel = new javax.swing.JLabel();
        addTitleBukuLabel = new javax.swing.JLabel();
        addGenreBukuLabel = new javax.swing.JLabel();
        addTitleBukuText = new javax.swing.JTextField();
        addGenreBukuText = new javax.swing.JTextField();
        addAuthorBukuLabel = new javax.swing.JLabel();
        addAuthorBukuText = new javax.swing.JTextField();
        addPublisherBukuLabel = new javax.swing.JLabel();
        addPublisherBukuText = new javax.swing.JTextField();
        addYearBukuLabel = new javax.swing.JLabel();
        addYearBukuText = new javax.swing.JTextField();
        addStockBukuLabel = new javax.swing.JLabel();
        addStockBukuText = new javax.swing.JTextField();
        btnSubmitAddBook = new javax.swing.JButton();
        addCodeBukuText = new javax.swing.JTextField();
        addCodeBukuLabel = new javax.swing.JLabel();
        paymentPanel = new javax.swing.JPanel();
        headerLabelPayment = new javax.swing.JLabel();
        bookIDLabel = new javax.swing.JLabel();
        bookIDText = new javax.swing.JTextField();
        bookTitleLabel = new javax.swing.JLabel();
        bookTitleText = new javax.swing.JTextField();
        userIDLabel = new javax.swing.JLabel();
        userIDText = new javax.swing.JTextField();
        usernamePayLabel = new javax.swing.JLabel();
        usernamePayText = new javax.swing.JTextField();
        exipiryDateLabel = new javax.swing.JLabel();
        exipiryDateText = new javax.swing.JTextField();
        returnDateLabel = new javax.swing.JLabel();
        returnDateText = new javax.swing.JTextField();
        fineLabel = new javax.swing.JLabel();
        fineText = new javax.swing.JTextField();
        btnPay = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        idPeminjamanLabel = new javax.swing.JLabel();
        idPeminjamanText = new javax.swing.JTextField();
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
        sideNav.add(account, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 220, 32));

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
        sideNav.add(btnLogOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 220, 58));

        btnUsers.setBackground(new java.awt.Color(47, 67, 86));
        btnUsers.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnUsers.setForeground(new java.awt.Color(236, 240, 241));
        btnUsers.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/users.png"))); // NOI18N
        btnUsers.setText("Users");
        btnUsers.setOpaque(true);
        btnUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUsersMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUsersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUsersMouseExited(evt);
            }
        });
        sideNav.add(btnUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 220, 58));

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

        panelTotalBooks.setBackground(new java.awt.Color(63, 121, 193));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Total Books");

        dashTotalBooksText.setFont(new java.awt.Font("Segoe UI", 1, 100)); // NOI18N
        dashTotalBooksText.setForeground(new java.awt.Color(255, 255, 255));
        dashTotalBooksText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashTotalBooksText.setText("0");

        javax.swing.GroupLayout panelTotalBooksLayout = new javax.swing.GroupLayout(panelTotalBooks);
        panelTotalBooks.setLayout(panelTotalBooksLayout);
        panelTotalBooksLayout.setHorizontalGroup(
            panelTotalBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalBooksLayout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(panelTotalBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooksLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooksLayout.createSequentialGroup()
                        .addComponent(dashTotalBooksText, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48))))
        );
        panelTotalBooksLayout.setVerticalGroup(
            panelTotalBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooksLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(dashTotalBooksText, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        panelTotalBooks2.setBackground(new java.awt.Color(24, 178, 137));

        dashTotalAccountLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dashTotalAccountLabel.setForeground(new java.awt.Color(255, 255, 255));
        dashTotalAccountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashTotalAccountLabel.setText("Total Accounts");

        dashTotalAccountText.setFont(new java.awt.Font("Segoe UI", 1, 100)); // NOI18N
        dashTotalAccountText.setForeground(new java.awt.Color(255, 255, 255));
        dashTotalAccountText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashTotalAccountText.setText("0");

        javax.swing.GroupLayout panelTotalBooks2Layout = new javax.swing.GroupLayout(panelTotalBooks2);
        panelTotalBooks2.setLayout(panelTotalBooks2Layout);
        panelTotalBooks2Layout.setHorizontalGroup(
            panelTotalBooks2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalBooks2Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(panelTotalBooks2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks2Layout.createSequentialGroup()
                        .addComponent(dashTotalAccountText, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks2Layout.createSequentialGroup()
                        .addComponent(dashTotalAccountLabel)
                        .addGap(18, 18, 18))))
        );
        panelTotalBooks2Layout.setVerticalGroup(
            panelTotalBooks2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks2Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(dashTotalAccountText, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashTotalAccountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        panelTotalBooks3.setBackground(new java.awt.Color(244, 129, 19));

        dashBooksBorrowedLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dashBooksBorrowedLabel.setForeground(new java.awt.Color(255, 255, 255));
        dashBooksBorrowedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashBooksBorrowedLabel.setText("Books Borrowed");

        dashBooksBorrowedText.setFont(new java.awt.Font("Segoe UI", 1, 100)); // NOI18N
        dashBooksBorrowedText.setForeground(new java.awt.Color(255, 255, 255));
        dashBooksBorrowedText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dashBooksBorrowedText.setText("0");

        javax.swing.GroupLayout panelTotalBooks3Layout = new javax.swing.GroupLayout(panelTotalBooks3);
        panelTotalBooks3.setLayout(panelTotalBooks3Layout);
        panelTotalBooks3Layout.setHorizontalGroup(
            panelTotalBooks3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks3Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(panelTotalBooks3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTotalBooks3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(dashBooksBorrowedLabel))
                    .addComponent(dashBooksBorrowedText, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );
        panelTotalBooks3Layout.setVerticalGroup(
            panelTotalBooks3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks3Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(dashBooksBorrowedText, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashBooksBorrowedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        panelTotalBooks4.setBackground(new java.awt.Color(214, 182, 113));

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks4Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(panelTotalBooks4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTotalBooks4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel16))
                    .addComponent(dashBooksReturnedText, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        panelTotalBooks4Layout.setVerticalGroup(
            panelTotalBooks4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalBooks4Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
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
                .addGap(42, 42, 42)
                .addComponent(panelTotalBooks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelTotalBooks2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelTotalBooks3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelTotalBooks4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        mainCardPanelLayout.setVerticalGroup(
            mainCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainCardPanelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(mainCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelTotalBooks4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelTotalBooks3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelTotalBooks2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelTotalBooks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(268, Short.MAX_VALUE))
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

        borrowPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerLabelBorrow.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerLabelBorrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabelBorrow.setText("Borrow");
        borrowPanel.add(headerLabelBorrow, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 105, 37));

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        headerBookInfo1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        headerBookInfo1.setText("Is This Book Available?");

        headerBookInfo2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        headerBookInfo2.setText("Enter Book ID");

        headerBookInfo3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        headerBookInfo3.setText("Enter User ID");

        headerBookInfo4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        headerBookInfo4.setText("Borrowing date");

        headerBookInfo5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        headerBookInfo5.setText("Return date");

        enterBookIDText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        enterBookIDText.setToolTipText("Press enter to get book info");
        enterBookIDText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enterBookIDTextKeyPressed(evt);
            }
        });

        enterUsernameText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        enterUsernameText.setToolTipText("Press enter to get user info");
        enterUsernameText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enterUsernameTextKeyPressed(evt);
            }
        });

        statusBorrowLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        statusBorrowLabel.setForeground(new java.awt.Color(27, 86, 124));
        statusBorrowLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusBorrowLabel.setText("Yes or No");

        resetBorrow.setBackground(new java.awt.Color(27, 86, 124));
        resetBorrow.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        resetBorrow.setForeground(new java.awt.Color(255, 255, 255));
        resetBorrow.setText("Reset");
        resetBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBorrowActionPerformed(evt);
            }
        });

        submitBorrow.setBackground(new java.awt.Color(17, 124, 103));
        submitBorrow.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        submitBorrow.setForeground(new java.awt.Color(255, 255, 255));
        submitBorrow.setText("Submit");
        submitBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBorrowActionPerformed(evt);
            }
        });

        headerBookInfo17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        headerBookInfo17.setText("Note");

        noteTextArea.setColumns(20);
        noteTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        noteTextArea.setRows(5);
        noteTextArea.setToolTipText("Add a note for user");
        jScrollPane3.setViewportView(noteTextArea);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addComponent(enterReturnDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(enterBorrowingDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(resetBorrow)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(submitBorrow))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(headerBookInfo1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(headerBookInfo4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(headerBookInfo5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(headerBookInfo17, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(headerBookInfo2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(76, 76, 76)
                                        .addComponent(headerBookInfo3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(statusBorrowLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(enterBookIDText, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(enterUsernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 38, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBookInfo2)
                    .addComponent(headerBookInfo3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enterBookIDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enterUsernameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBookInfo1)
                    .addComponent(statusBorrowLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(headerBookInfo4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enterBorrowingDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerBookInfo5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enterReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerBookInfo17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submitBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        borrowPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 54, -1, -1));

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        headerBookInfo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        headerBookInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerBookInfo.setText("Book Info");

        headerBookInfo6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo6.setText("ID");

        headerBookInfo7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo7.setText("Code");

        headerBookInfo8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo8.setText("Title");

        headerBookInfo9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo9.setText("Genre");

        headerBookInfo10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo10.setText("Author");

        headerBookInfo11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo11.setText("Publisher");

        headerBookInfo12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo12.setText("Year");

        idBookInfo.setEditable(false);
        idBookInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        codeBookInfo.setEditable(false);
        codeBookInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        titleBookInfo.setEditable(false);
        titleBookInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        genreBookInfo.setEditable(false);
        genreBookInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        authorBookInfo.setEditable(false);
        authorBookInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        yearBookInfo.setEditable(false);
        yearBookInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        publisherBookInfo.setEditable(false);
        publisherBookInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 24, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(headerBookInfo6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(headerBookInfo8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(headerBookInfo9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(headerBookInfo10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(headerBookInfo11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(idBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(headerBookInfo7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codeBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(titleBookInfo)
                    .addComponent(genreBookInfo)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(authorBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(headerBookInfo12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(yearBookInfo))
                    .addComponent(publisherBookInfo))
                .addGap(90, 90, 90))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(204, 204, 204)
                .addComponent(headerBookInfo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(headerBookInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(headerBookInfo6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(idBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(codeBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(headerBookInfo7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBookInfo8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titleBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBookInfo9)
                    .addComponent(genreBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBookInfo10)
                    .addComponent(authorBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(headerBookInfo12)
                    .addComponent(yearBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBookInfo11)
                    .addComponent(publisherBookInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );

        borrowPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(478, 54, -1, 250));

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        headerUserInfo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        headerUserInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerUserInfo.setText("User Info");

        headerBookInfo13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo13.setText("ID");

        nameidUserInfo.setEditable(false);
        nameidUserInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        headerBookInfo14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo14.setText("Username");

        usernameidUserInfo.setEditable(false);
        usernameidUserInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        headerBookInfo15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo15.setText("Name");

        idUserInfo.setEditable(false);
        idUserInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        headerBookInfo16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        headerBookInfo16.setText("User Type");

        usertypeidUserInfo.setEditable(false);
        usertypeidUserInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(219, 219, 219)
                        .addComponent(headerUserInfo))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(headerBookInfo14)
                            .addComponent(usernameidUserInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(headerBookInfo16)
                            .addComponent(usertypeidUserInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(idUserInfo)
                                    .addGap(18, 18, 18)
                                    .addComponent(nameidUserInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(headerBookInfo13)
                                    .addGap(328, 328, 328)
                                    .addComponent(headerBookInfo15))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(headerUserInfo)
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBookInfo13)
                    .addComponent(headerBookInfo15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idUserInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameidUserInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerBookInfo14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameidUserInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerBookInfo16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usertypeidUserInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        borrowPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, 552, -1));

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
        returnTable.setViewportView(returnedTable);

        returnIDText.setEditable(false);
        returnIDText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("ID");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Book ID");

        returnBookIDText.setEditable(false);
        returnBookIDText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Book's title");

        returnBookTitleText.setEditable(false);
        returnBookTitleText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("User ID");

        returnUserIDText.setEditable(false);
        returnUserIDText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Username");

        returnUsernameText.setEditable(false);
        returnUsernameText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Status");

        returnStatusText.setEditable(false);
        returnStatusText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

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

        btnReturn.setBackground(new java.awt.Color(17, 124, 103));
        btnReturn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReturn.setForeground(new java.awt.Color(255, 255, 255));
        btnReturn.setText("Return");
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnActionPerformed(evt);
            }
        });

        btnLostReturn.setBackground(new java.awt.Color(122, 122, 0));
        btnLostReturn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLostReturn.setForeground(new java.awt.Color(255, 255, 255));
        btnLostReturn.setText("Lost");
        btnLostReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLostReturnActionPerformed(evt);
            }
        });

        btnDeleteReturn.setBackground(new java.awt.Color(124, 37, 27));
        btnDeleteReturn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeleteReturn.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteReturn.setText("Delete");
        btnDeleteReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteReturnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(returnBookIDText, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(returnBookTitleText, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(returnIDText, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(returnUserIDText, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(returnStatusText, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(returnUsernameText))))
                .addGap(10, 10, 10))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(returnBorrowDateText, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(returnReturnDateText)
                        .addGap(14, 14, 14))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDeleteReturn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLostReturn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReturn)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(returnIDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(returnBookIDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(returnBookTitleText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnUserIDText, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnUsernameText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(returnStatusText, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnBorrowDateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnReturnDateText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLostReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        returnKeyword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        returnKeyword.setText("Search");
        returnKeyword.setToolTipText("Press enter to search with keyword");
        returnKeyword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnKeywordMouseClicked(evt);
            }
        });
        returnKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                returnKeywordKeyPressed(evt);
            }
        });

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(returnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(returnKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(returnTable, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))))
        );
        returnPanelLayout.setVerticalGroup(
            returnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(returnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabelReturned, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(returnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(returnPanelLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(returnKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(returnTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(returnPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(69, Short.MAX_VALUE))
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

        btnAddBook.setBackground(new java.awt.Color(42, 59, 76));
        btnAddBook.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddBook.setForeground(new java.awt.Color(255, 255, 255));
        btnAddBook.setText("Add Book");
        btnAddBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddBookActionPerformed(evt);
            }
        });
        booksPanel.add(btnAddBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 30, -1, -1));

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
        booksPanel.add(booksKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, 190, -1));

        idBukuText.setEditable(false);
        idBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        idBukulLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        idBukulLabel.setText("ID");

        kodeBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        judulBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        kodeBukulLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kodeBukulLabel.setText("Code");

        judulBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        judulBukuLabel.setText("Title");

        genreBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        genreBukuLabel.setText("Genre");

        genreBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        penulisBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        penulisBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        penulisBukuLabel.setText("Author");

        penerbitBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        penerbitBukuLabel.setText("Publisher");

        penerbitBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        stokBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stokBukuText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stokBukuTextKeyPressed(evt);
            }
        });

        stokBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stokBukuLabel.setText("Stock");

        btnResetBuku.setBackground(new java.awt.Color(27, 86, 124));
        btnResetBuku.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnResetBuku.setForeground(new java.awt.Color(255, 255, 255));
        btnResetBuku.setText("Reset");
        btnResetBuku.setPreferredSize(new java.awt.Dimension(84, 26));
        btnResetBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetBukuActionPerformed(evt);
            }
        });

        btnDeleteBuku.setBackground(new java.awt.Color(124, 37, 27));
        btnDeleteBuku.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDeleteBuku.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteBuku.setText("Delete");
        btnDeleteBuku.setPreferredSize(new java.awt.Dimension(84, 26));
        btnDeleteBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBukuActionPerformed(evt);
            }
        });

        btnSaveBuku.setBackground(new java.awt.Color(17, 124, 103));
        btnSaveBuku.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSaveBuku.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveBuku.setText("Save");
        btnSaveBuku.setPreferredSize(new java.awt.Dimension(84, 26));
        btnSaveBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveBukuActionPerformed(evt);
            }
        });

        tahunTerbitBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tahunTerbitBukuLabel.setText("Year of publication");

        tahunTerbitBukuText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tahunTerbitBukuText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tahunTerbitBukuTextKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(idBukulLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(stokBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(penerbitBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(penulisBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(genreBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(judulBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(kodeBukulLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tahunTerbitBukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kodeBukuText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(judulBukuText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genreBukuText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(penulisBukuText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(penerbitBukuText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stokBukuText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tahunTerbitBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnResetBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(btnDeleteBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSaveBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idBukulLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kodeBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kodeBukulLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(judulBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(judulBukuLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(genreBukuLabel)
                    .addComponent(genreBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(penulisBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(penulisBukuLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(penerbitBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(penerbitBukuLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tahunTerbitBukuLabel)
                    .addComponent(tahunTerbitBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stokBukuLabel)
                    .addComponent(stokBukuText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResetBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSaveBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 104, Short.MAX_VALUE))
        );

        booksPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 290, 420));

        mainPanel.add(booksPanel, "card2");

        usersPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        usersPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerLabelUsers.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerLabelUsers.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabelUsers.setText("Users");
        usersPanel.add(headerLabelUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 78, 37));

        userTable.setAutoCreateRowSorter(true);
        userTable.setBorder(new javax.swing.border.MatteBorder(null));
        userTable.setModel(new javax.swing.table.DefaultTableModel(
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
        userTable.setRowHeight(25);
        userTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setResizingAllowed(false);
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(userTable);

        usersPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(438, 87, 550, 310));

        keywordUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        keywordUser.setText("Search");
        keywordUser.setToolTipText("Press enter for searching a name or username");
        keywordUser.setName(""); // NOI18N
        keywordUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keywordUserMouseClicked(evt);
            }
        });
        keywordUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                keywordUserKeyPressed(evt);
            }
        });
        usersPanel.add(keywordUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 50, 240, 30));
        keywordUser.getAccessibleContext().setAccessibleName("");

        btnAddUser.setBackground(new java.awt.Color(42, 59, 76));
        btnAddUser.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddUser.setForeground(new java.awt.Color(255, 255, 255));
        btnAddUser.setText("Add User");
        btnAddUser.setBorder(null);
        btnAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUserActionPerformed(evt);
            }
        });
        usersPanel.add(btnAddUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 50, 120, 30));

        namaUserLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        namaUserLabel.setText("Name");
        usersPanel.add(namaUserLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 60, 30));

        tipeUserLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tipeUserLabel.setText("Tipe User");
        usersPanel.add(tipeUserLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 60, 30));

        userNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        userNameLabel.setText("Username");
        usersPanel.add(userNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 70, 30));

        namaText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usersPanel.add(namaText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 290, 30));

        usernameText.setEditable(false);
        usernameText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usersPanel.add(usernameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 290, 30));

        userTypeCB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        userTypeCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Librarian", "General" }));
        usersPanel.add(userTypeCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 220, 290, 30));

        btnSimpan.setBackground(new java.awt.Color(17, 124, 103));
        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Save");
        btnSimpan.setPreferredSize(new java.awt.Dimension(84, 26));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        usersPanel.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 280, 90, 40));

        btnHapus.setBackground(new java.awt.Color(124, 37, 27));
        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Delete");
        btnHapus.setPreferredSize(new java.awt.Dimension(84, 26));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        usersPanel.add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, 90, 40));

        btnReset.setBackground(new java.awt.Color(27, 86, 124));
        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setText("Reset");
        btnReset.setPreferredSize(new java.awt.Dimension(84, 26));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        usersPanel.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 90, 40));

        idUserLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        idUserLabel.setText("ID");
        usersPanel.add(idUserLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 60, 30));

        idText.setEditable(false);
        idText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usersPanel.add(idText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 290, 30));

        mainPanel.add(usersPanel, "card2");

        headerLabelAddUser.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerLabelAddUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabelAddUser.setText("Add User");

        firstNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        firstNameLabel.setText("First Name");

        firstNameText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        lastNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lastNameLabel.setText("Last Name");

        lastNameText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        addUsernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addUsernameLabel.setText("Username");

        addUsernameText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        addPasswordLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addPasswordLabel.setText("Password");

        btnSubmitAddUser.setBackground(new java.awt.Color(17, 124, 103));
        btnSubmitAddUser.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSubmitAddUser.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmitAddUser.setText("Submit");
        btnSubmitAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitAddUserActionPerformed(evt);
            }
        });

        usertypeCB.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        usertypeCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "General", "Librarian" }));

        usertypeLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        usertypeLabel.setText("User type");

        addPasswordText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addPasswordText.setText("jPasswordField1");

        javax.swing.GroupLayout addUserPanelLayout = new javax.swing.GroupLayout(addUserPanel);
        addUserPanel.setLayout(addUserPanelLayout);
        addUserPanelLayout.setHorizontalGroup(
            addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addUserPanelLayout.createSequentialGroup()
                .addGroup(addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addUserPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(headerLabelAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addUserPanelLayout.createSequentialGroup()
                        .addGap(278, 278, 278)
                        .addGroup(addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSubmitAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(usertypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(usertypeCB, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(addPasswordText, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(firstNameLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(firstNameText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                                    .addComponent(lastNameText, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addUsernameText, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lastNameLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addUsernameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addPasswordLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(355, Short.MAX_VALUE))
        );
        addUserPanelLayout.setVerticalGroup(
            addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabelAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(firstNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(firstNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lastNameLabel)
                .addGap(11, 11, 11)
                .addComponent(lastNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addUsernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addUsernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(addPasswordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addPasswordText, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usertypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usertypeCB, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSubmitAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        mainPanel.add(addUserPanel, "card2");

        addBookPanel.setPreferredSize(new java.awt.Dimension(990, 397));
        addBookPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerAddBookLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerAddBookLabel.setText("Add Book");
        addBookPanel.add(headerAddBookLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 21, -1, 39));

        addTitleBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addTitleBukuLabel.setText("Title");
        addBookPanel.add(addTitleBukuLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 117, -1, 39));

        addGenreBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addGenreBukuLabel.setText("Genre");
        addBookPanel.add(addGenreBukuLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 162, -1, 39));

        addTitleBukuText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addBookPanel.add(addTitleBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 121, 255, -1));

        addGenreBukuText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addBookPanel.add(addGenreBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 166, 255, -1));

        addAuthorBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addAuthorBukuLabel.setText("Author");
        addBookPanel.add(addAuthorBukuLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 207, -1, 39));

        addAuthorBukuText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addBookPanel.add(addAuthorBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 211, 255, -1));

        addPublisherBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addPublisherBukuLabel.setText("Publisher");
        addBookPanel.add(addPublisherBukuLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 252, -1, 39));

        addPublisherBukuText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addBookPanel.add(addPublisherBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 252, 255, -1));

        addYearBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addYearBukuLabel.setText("Year of Publication");
        addBookPanel.add(addYearBukuLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 297, -1, 39));

        addYearBukuText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addYearBukuText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addYearBukuTextKeyPressed(evt);
            }
        });
        addBookPanel.add(addYearBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 301, 255, -1));

        addStockBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addStockBukuLabel.setText("Stock");
        addBookPanel.add(addStockBukuLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 342, -1, 39));

        addStockBukuText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addStockBukuText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addStockBukuTextKeyPressed(evt);
            }
        });
        addBookPanel.add(addStockBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 346, 255, -1));

        btnSubmitAddBook.setBackground(new java.awt.Color(17, 124, 103));
        btnSubmitAddBook.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSubmitAddBook.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmitAddBook.setText("Submit");
        btnSubmitAddBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitAddBookActionPerformed(evt);
            }
        });
        addBookPanel.add(btnSubmitAddBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(399, 392, -1, -1));

        addCodeBukuText.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addCodeBukuText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addCodeBukuTextKeyPressed(evt);
            }
        });
        addBookPanel.add(addCodeBukuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 76, 255, -1));

        addCodeBukuLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addCodeBukuLabel.setText("Code");
        addBookPanel.add(addCodeBukuLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 72, -1, 39));

        mainPanel.add(addBookPanel, "card8");

        headerLabelPayment.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        headerLabelPayment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabelPayment.setText("Payment");

        bookIDLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bookIDLabel.setText("Book ID");

        bookIDText.setEditable(false);
        bookIDText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        bookTitleLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bookTitleLabel.setText("Book's title");

        bookTitleText.setEditable(false);
        bookTitleText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        userIDLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        userIDLabel.setText("User ID");

        userIDText.setEditable(false);
        userIDText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        usernamePayLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usernamePayLabel.setText("Username");

        usernamePayText.setEditable(false);
        usernamePayText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        exipiryDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        exipiryDateLabel.setText("Exipiry Date");

        exipiryDateText.setEditable(false);
        exipiryDateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        returnDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        returnDateLabel.setText("Return Date(now)");

        returnDateText.setEditable(false);
        returnDateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        fineLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fineLabel.setText("Fine(IDR)");

        fineText.setEditable(false);
        fineText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnPay.setBackground(new java.awt.Color(17, 124, 103));
        btnPay.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPay.setForeground(new java.awt.Color(255, 255, 255));
        btnPay.setText("Pay");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(124, 37, 27));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        idPeminjamanLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        idPeminjamanLabel.setText("ID");

        idPeminjamanText.setEditable(false);
        idPeminjamanText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout paymentPanelLayout = new javax.swing.GroupLayout(paymentPanel);
        paymentPanel.setLayout(paymentPanelLayout);
        paymentPanelLayout.setHorizontalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabelPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bookIDLabel)
                    .addComponent(bookTitleLabel)
                    .addComponent(userIDLabel)
                    .addComponent(usernamePayLabel)
                    .addComponent(exipiryDateLabel)
                    .addComponent(returnDateLabel)
                    .addComponent(fineLabel)
                    .addComponent(idPeminjamanLabel))
                .addGap(29, 29, 29)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPay))
                    .addComponent(bookTitleText, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(bookIDText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                        .addComponent(idPeminjamanText, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(userIDText, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernamePayText, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exipiryDateText, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnDateText, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fineText, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        paymentPanelLayout.setVerticalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabelPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idPeminjamanLabel)
                    .addComponent(idPeminjamanText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bookIDLabel)
                    .addComponent(bookIDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bookTitleLabel)
                    .addComponent(bookTitleText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userIDLabel)
                    .addComponent(userIDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernamePayLabel)
                    .addComponent(usernamePayText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exipiryDateLabel)
                    .addComponent(exipiryDateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnDateLabel)
                    .addComponent(returnDateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fineLabel)
                    .addComponent(fineText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPay)
                    .addComponent(btnCancel))
                .addContainerGap(232, Short.MAX_VALUE))
        );

        mainPanel.add(paymentPanel, "card2");

        bodyPanel.add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 1000, 600));

        navigasi.setBackground(new java.awt.Color(42, 59, 76));
        navigasi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userTypeLabel.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        userTypeLabel.setForeground(new java.awt.Color(236, 240, 241));
        userTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        userTypeLabel.setText("Librarian");
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

    private void btnUsersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsersMouseEntered
        btnUsers.setBackground(mouseHover);
    }//GEN-LAST:event_btnUsersMouseEntered

    private void btnUsersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsersMouseExited
        btnUsers.setBackground(colorBase);
    }//GEN-LAST:event_btnUsersMouseExited

    private void btnBooksMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBooksMouseEntered
        btnBooks.setBackground(mouseHover);
    }//GEN-LAST:event_btnBooksMouseEntered

    private void btnBooksMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBooksMouseExited
        btnBooks.setBackground(colorBase);
    }//GEN-LAST:event_btnBooksMouseExited

    private void btnBorrowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrowMouseClicked
        removePanel();
        resetBorrowForm();
        resetBookInfoPanel();
        resetUserInfoPanel();
        addPanel(borrowPanel);
    }//GEN-LAST:event_btnBorrowMouseClicked

    private void btnReturnedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReturnedMouseClicked
        removePanel();
        addPanel(returnPanel);
        buildReturnedTable();
        resetReturnForm();
    }//GEN-LAST:event_btnReturnedMouseClicked

    private void btnBooksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBooksMouseClicked
        removePanel();
        addPanel(booksPanel);
        resetBookForm();
        buildBookTable();
    }//GEN-LAST:event_btnBooksMouseClicked

    private void btnUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsersMouseClicked
        removePanel();
        resetUserForm();
        addPanel(usersPanel);
        DatabaseOperations allUser = new DatabaseOperations();
        allUser.tampilkanDataUsers(userTable, "SELECT * FROM users");
    }//GEN-LAST:event_btnUsersMouseClicked

    private void btnLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutMouseClicked
        int dialogBtn = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit", dialogBtn);
        if(dialogResult==0){
            Login logout = new Login();
            logout.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnLogOutMouseClicked

    private void btnAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUserActionPerformed
        firstNameText.setText("");
        lastNameText.setText("");
        addUsernameText.setText("");
        addPasswordText.setText("");
        removePanel();
        addPanel(addUserPanel);
    }//GEN-LAST:event_btnAddUserActionPerformed

    private void keywordUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_keywordUserMouseClicked
        keywordUser.setText("");
    }//GEN-LAST:event_keywordUserMouseClicked

    private void keywordUserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keywordUserKeyPressed
        if(evt.getKeyCode()==10){
            String keyword= keywordUser.getText();
            DatabaseOperations keywordUser = new DatabaseOperations();
            keywordUser.tampilkanDataUsers(userTable, "SELECT * FROM users WHERE name LIKE '%"+keyword+"%' OR username LIKE '%"+keyword+"%' OR user_type LIKE '%"+keyword+"%'");
        }
    }//GEN-LAST:event_keywordUserKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetUserForm();        
    }//GEN-LAST:event_btnResetActionPerformed

    private void userTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTableMouseClicked
        int baris = userTable.rowAtPoint(evt.getPoint());
        //ID user
        String ID = userTable.getValueAt(baris, 0).toString();
        idText.setText(ID);
        //nama user
        String nama = userTable.getValueAt(baris, 1).toString();
        namaText.setText(nama);
        //username user
        String usrnm = userTable.getValueAt(baris, 2).toString();
        usernameText.setText(usrnm);
        String usrtype = userTable.getValueAt(baris, 3).toString();
        userTypeCB.setSelectedItem(usrtype);
    }//GEN-LAST:event_userTableMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(!namaText.getText().isEmpty() && !idText.getText().isEmpty()){
            Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(namaText.getText());
            boolean b = m.find();
                if (b){
                   JOptionPane.showMessageDialog(this, "Please input string!");
                }else{
                int dialogBtn = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to change this user?", "EDIT", dialogBtn);
                if(dialogResult==0){
                    DatabaseOperations.updateUser(idText.getText(), namaText.getText(), userTypeCB.getSelectedItem().toString());
                    JOptionPane.showMessageDialog(this, "Successfully edited data!");
                    removePanel();
                    resetUserForm();
                    addPanel(usersPanel);
                    DatabaseOperations allUser = new DatabaseOperations();
                    allUser.tampilkanDataUsers(userTable, "SELECT * FROM users");
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select the user to edit");
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if(!namaText.getText().isEmpty()){
            int dialogBtn = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "DELETE", dialogBtn);
            if(dialogResult==0){
                DatabaseOperations.deleteUser(idText.getText());
                JOptionPane.showMessageDialog(this, "Successfully deleted user!");
                removePanel();
                resetUserForm();
                addPanel(usersPanel);
                DatabaseOperations allUser = new DatabaseOperations();
                allUser.tampilkanDataUsers(userTable, "SELECT * FROM users");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select the user to delete");
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnSubmitAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitAddUserActionPerformed
        if(!(firstNameText.getText().trim().isEmpty()||lastNameText.getText().trim().isEmpty()||addUsernameText.getText().trim().isEmpty()||(addPasswordText.getText().isEmpty()))){
            Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(firstNameText.getText()+lastNameText.getText());
            boolean b = m.find();
            if (b){
                JOptionPane.showMessageDialog(this, "Please enter a string in the name field!");
            }
            else{
                Pattern q = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher n = q.matcher(addUsernameText.getText());
                boolean a = n.find();
                if (a){
                    JOptionPane.showMessageDialog(this, "Invalid value in username fields! Please input in string or number");
                }else{
        //            Cek apakah username sudah digunakan
                    int usersCount = DatabaseOperations.getUsers("SELECT * FROM users WHERE username = '"+addUsernameText.getText().trim()+"'").size();
        //            End CEK
                    if(usersCount > 0){
                        JOptionPane.showMessageDialog(this, "Username has been used! Please choose another username.");
                    }else{
                        User addnewuser = new User();
                        addnewuser.setFirstName(firstNameText.getText().trim());
                        addnewuser.setLastName(lastNameText.getText().trim());
                        addnewuser.setUsername(addUsernameText.getText().trim());
                        addnewuser.setPassword(addPasswordText.getText());
                        if(usertypeCB.getSelectedIndex()==0){
                            addnewuser.setUser_type("General");
                        }else{
                            addnewuser.setUser_type("Librarian");
                        }
                        DatabaseOperations.addUser(addnewuser.getFirstName(), addnewuser.getLastName(), addnewuser.getUsername(), addnewuser.getPassword(), addnewuser.getUser_type());
                        JOptionPane.showMessageDialog(this, "User added successfully!");
                        removePanel();
                        resetUserForm();
                        addPanel(usersPanel);
                        DatabaseOperations allUser = new DatabaseOperations();
                        allUser.tampilkanDataUsers(userTable, "SELECT * FROM users");
                    }
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please fill in all fields");
        }
    }//GEN-LAST:event_btnSubmitAddUserActionPerformed

    private void btnResetBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetBukuActionPerformed
        resetBookForm();
    }//GEN-LAST:event_btnResetBukuActionPerformed

    private void btnDeleteBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBukuActionPerformed
        if(checkNullBookForm()){
            int dialogBtn = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "WARNING", dialogBtn);
            if(dialogResult==0){
                DatabaseOperations.deleteBuku(idBukuText.getText());
                JOptionPane.showMessageDialog(this, "Successfully deleted data!");
                removePanel();
                addPanel(booksPanel);
                resetBookForm();
                buildBookTable();
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select the book to be deleted!");
        }
    }//GEN-LAST:event_btnDeleteBukuActionPerformed

    private void btnSaveBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveBukuActionPerformed
        if(checkNullBookForm()){
            if(tahunTerbitBukuText.getText().length()!=4){
                JOptionPane.showMessageDialog(this, "Please enter the year in 4 digits");
            }else{
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(kodeBukuText.getText()+judulBukuText.getText()+genreBukuText.getText()+penulisBukuText.getText()+penerbitBukuText.getText());
                boolean b = m.find();
                if (b){
                   JOptionPane.showMessageDialog(this, "There is a special character in my fields");
                }else{
                    int dialogBtn = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to edit this book?", "WARNING", dialogBtn);
                    if(dialogResult==0){
                        DatabaseOperations.updateBuku(idBukuText.getText(), kodeBukuText.getText(), judulBukuText.getText(), genreBukuText.getText(), penulisBukuText.getText(), penerbitBukuText.getText(), tahunTerbitBukuText.getText(), stokBukuText.getText());
                        JOptionPane.showMessageDialog(this, "Successfully edited data!");
                        removePanel();
                        addPanel(booksPanel);
                        resetBookForm();
                        buildBookTable();
                    }
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select the book to be edited!");
        }
    }//GEN-LAST:event_btnSaveBukuActionPerformed

    private void btnAddBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddBookActionPerformed
        resetaddBookForm();
        removePanel();
        addPanel(addBookPanel);
    }//GEN-LAST:event_btnAddBookActionPerformed

    private void btnSubmitAddBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitAddBookActionPerformed
        if(!(addTitleBukuText.getText().isEmpty() || addGenreBukuText.getText().isEmpty() || addAuthorBukuText.getText().isEmpty() || addPublisherBukuText.getText().isEmpty() || addYearBukuText.getText().isEmpty() || addStockBukuText.getText().isEmpty() || addCodeBukuText.getText().isEmpty())){
            if(addYearBukuText.getText().length()!=4){
                JOptionPane.showMessageDialog(this, "Please enter the year in 4 digits");
            }else{
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(addCodeBukuText.getText()+addTitleBukuText.getText()+addGenreBukuText.getText()+addAuthorBukuText.getText()+addPublisherBukuText.getText()+addYearBukuText.getText()+addStockBukuText.getText());
                boolean b = m.find();
                if (b){
                   JOptionPane.showMessageDialog(this, "There is a special character in my fields");
                }else{
                    try{
                        int stok = Integer. parseInt(addStockBukuText.getText());
                        Book addNewBook = new Book(addCodeBukuText.getText(),addTitleBukuText.getText(),addGenreBukuText.getText(),addAuthorBukuText.getText(),addPublisherBukuText.getText(),addYearBukuText.getText(),stok);
                        DatabaseOperations.addBook(addNewBook.getKode_buku(), addNewBook.getJudul_buku(), addNewBook.getGenre_buku(), addNewBook.getPenulis_buku(), addNewBook.getPenerbit_buku(), addNewBook.getTahun_terbit(), addNewBook.getStok());
                        JOptionPane.showMessageDialog(this, "Successfully added data!");
                        removePanel();
                        addPanel(booksPanel);
                        resetBookForm();
                        resetaddBookForm();
                        buildBookTable();
                    }catch(NumberFormatException e){
                        System.out.println("Error : "+e);
                    }
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please fill in all the fields!");
        }
    }//GEN-LAST:event_btnSubmitAddBookActionPerformed

    private void booksKeywordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_booksKeywordMouseClicked
        booksKeyword.setText("");
    }//GEN-LAST:event_booksKeywordMouseClicked

    private void booksTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_booksTableMouseClicked
        int baris = booksTable.rowAtPoint(evt.getPoint());
        String ID = booksTable.getValueAt(baris, 0).toString();
        idBukuText.setText(ID);
        String Code = booksTable.getValueAt(baris, 1).toString();
        kodeBukuText.setText(Code);
        String Title = booksTable.getValueAt(baris, 2).toString();
        judulBukuText.setText(Title);
        String genre = booksTable.getValueAt(baris, 3).toString();
        genreBukuText.setText(genre);
        String author = booksTable.getValueAt(baris, 4).toString();
        penulisBukuText.setText(author);
        String publisher = booksTable.getValueAt(baris, 5).toString();
        penerbitBukuText.setText(publisher);
        String year = booksTable.getValueAt(baris, 6).toString();
        tahunTerbitBukuText.setText(year);
        String stock = booksTable.getValueAt(baris, 7).toString();
        stokBukuText.setText(stock);
    }//GEN-LAST:event_booksTableMouseClicked

    private void booksKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_booksKeywordKeyPressed
        if(evt.getKeyCode()==10){
            String keyword= booksKeyword.getText();
            rebuildBookTableKeyword(keyword);
        }
    }//GEN-LAST:event_booksKeywordKeyPressed

    private void enterBookIDTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_enterBookIDTextKeyPressed
        inputNumberOnly(evt, enterBookIDText);
        if(evt.getKeyCode()==10){
            resetBookInfoPanel();
            String id_buku = enterBookIDText.getText();
            String query="SELECT * FROM books WHERE id_buku = "+id_buku+"";
            int booksLength=DatabaseOperations.getBooks(query).size();
            if(booksLength > 0 ){
                String[] hasil = new String[8];
                hasil[0] = String.valueOf(DatabaseOperations.getBooks(query).get(0).getId_buku());
                hasil[1] = DatabaseOperations.getBooks(query).get(0).getKode_buku();
                hasil[2] = DatabaseOperations.getBooks(query).get(0).getJudul_buku();
                hasil[3] = DatabaseOperations.getBooks(query).get(0).getGenre_buku();
                hasil[4] = DatabaseOperations.getBooks(query).get(0).getPenerbit_buku();
                hasil[5] = DatabaseOperations.getBooks(query).get(0).getPenulis_buku();
                hasil[6] = DatabaseOperations.getBooks(query).get(0).getTahun_terbit();
                hasil[7] = String.valueOf(DatabaseOperations.getBooks(query).get(0).getStok());
                    
                idBookInfo.setText(hasil[0]);
                codeBookInfo.setText(hasil[1]);
                titleBookInfo.setText(hasil[2]);
                genreBookInfo.setText(hasil[3]);
                publisherBookInfo.setText(hasil[4]);
                authorBookInfo.setText(hasil[5]);
                yearBookInfo.setText(hasil[6]);
                
                //Get Stok Borrowed
                String querr = "SELECT * FROM returned WHERE id_buku = "+hasil[0]+" AND status='Borrowed'";
                int stokBorrowed = DatabaseOperations.getReturned(querr).size();
                //End--
                int stok = DatabaseOperations.getBooks(query).get(0).getStok();
                int finalStok = stok - stokBorrowed;
                if(finalStok > 0){
                    statusBorrowLabel.setText("Yes(Available: "+finalStok+")");
                    statusBorrowLabel.setForeground(statusBorrowOK);
                }else{
                    statusBorrowLabel.setText("No");
                    statusBorrowLabel.setForeground(statusBorrowNotOK);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Book ID not available");
            }
        }
    }//GEN-LAST:event_enterBookIDTextKeyPressed

    private void enterUsernameTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_enterUsernameTextKeyPressed
        inputNumberOnly(evt, enterUsernameText);
        if(evt.getKeyCode()==10){
            resetUserInfoPanel();
            String keyword_user = enterUsernameText.getText();
            String query="SELECT * FROM users WHERE id = "+keyword_user+"";
            int usersLength=DatabaseOperations.getUsers(query).size();
            if(usersLength > 0 ){
                String[] hasil = new String[5];
                hasil[0] = String.valueOf(DatabaseOperations.getUsers(query).get(0).getId_user());
                hasil[1] = DatabaseOperations.getUsers(query).get(0).getFirstName();
                hasil[2] = DatabaseOperations.getUsers(query).get(0).getLastName();
                hasil[3] = DatabaseOperations.getUsers(query).get(0).getUsername();
                hasil[4] = DatabaseOperations.getUsers(query).get(0).getPassword();

                String name=hasil[1]+" "+hasil[2];
                String user_type="";
                user_type = DatabaseOperations.getUsers(query).get(0).getUser_type();
                idUserInfo.setText(hasil[0]);
                nameidUserInfo.setText(name);
                usernameidUserInfo.setText(hasil[3]);
                usertypeidUserInfo.setText(user_type);
                
            }else{
                JOptionPane.showMessageDialog(this, "User not available");
            }
        }
    }//GEN-LAST:event_enterUsernameTextKeyPressed

    private void resetBorrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBorrowActionPerformed
        resetBookInfoPanel();
        resetUserInfoPanel();
        resetBorrowForm();
    }//GEN-LAST:event_resetBorrowActionPerformed

    private void submitBorrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBorrowActionPerformed
        if(!(enterBookIDText.getText().isEmpty() || enterUsernameText.getText().isEmpty() || statusBorrowLabel.getText()=="Yes or No" || enterBorrowingDate.getCalendar()==null || enterReturnDate.getCalendar()==null)){
            if(statusBorrowLabel.getText()=="Yes"){
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateBorrow = sdf.format(enterBorrowingDate.getDate());
                String dateReturn = sdf.format(enterReturnDate.getDate());
                SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy");
                try{
                    Date dt_1 = objSDF.parse(dateBorrow);
                    Date dt_2 = objSDF.parse(dateReturn);
                    if(dt_1.compareTo(dt_2)<=0){
                        String note = noteTextArea.getText();
                        DatabaseOperations.borrowBook(Integer.valueOf(enterBookIDText.getText()), Integer.valueOf(enterUsernameText.getText()), "Borrowed", dt_1, dt_2, note);
                        JOptionPane.showMessageDialog(this, "Successfully added data!");
                        resetBookInfoPanel();
                        resetUserInfoPanel();
                        resetBorrowForm();
                    }else{
                        JOptionPane.showMessageDialog(this, "The borrowing date must not exceed the return date!");
                    }
                }catch(ParseException e){
                    System.out.println("Error : "+e);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Sorry, the book is not available");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please fill in all the fields!");
        }
    }//GEN-LAST:event_submitBorrowActionPerformed

    private void returnKeywordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnKeywordMouseClicked
        returnKeyword.setText("");
    }//GEN-LAST:event_returnKeywordMouseClicked

    private void returnKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returnKeywordKeyPressed
        if(evt.getKeyCode()==10){
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(returnKeyword.getText());
            boolean b = m.find();
            if (b){
               JOptionPane.showMessageDialog(this, "There is a special character in my fields");
            }
            String keyword= returnKeyword.getText();
            rebuildReturnedTable(keyword);
        }
    }//GEN-LAST:event_returnKeywordKeyPressed

    private void returnedTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnedTableMouseClicked
        int baris = returnedTable.rowAtPoint(evt.getPoint());
        String ID = returnedTable.getValueAt(baris, 0).toString();
        returnIDText.setText(ID);
        String bookID= returnedTable.getValueAt(baris, 1).toString();
        returnBookIDText.setText(bookID);
        String userID= returnedTable.getValueAt(baris, 2).toString();
        returnUserIDText.setText(userID);
        String status= returnedTable.getValueAt(baris, 3).toString();
        returnStatusText.setText(status);
        String dateBorrow= returnedTable.getValueAt(baris, 4).toString();
        returnBorrowDateText.setText(dateBorrow);
        String dateReturn= returnedTable.getValueAt(baris, 5).toString();
        returnReturnDateText.setText(dateReturn);
        String note= returnedTable.getValueAt(baris, 6).toString();
        returnNoteTextArea.setText(note);
        
        try{
            String bookTitle = DatabaseOperations.getBooks("SELECT * FROM books WHERE id_buku="+bookID+"").get(0).getJudul_buku();
            returnBookTitleText.setText(bookTitle);
        }catch (IndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(this, "Buku dengan ID Buku '"+bookID+"' tidak tersedia. Anda boleh menghapus data peminjaman ini");
            returnBookIDText.setText("Data Unavailable");
            returnBookTitleText.setText("Data Unavailable");
        }
        
        try{
            String username = DatabaseOperations.getUsers("SELECT * FROM users WHERE id="+userID+"").get(0).getUsername();
            returnUsernameText.setText(username);
        }catch (IndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(this, "User dengan ID user '"+userID+"' tidak tersedia. Anda boleh menghapus data peminjaman ini");
            returnUserIDText.setText("Data Unavailable");
            returnUsernameText.setText("Data Unavailable");
        }
    }//GEN-LAST:event_returnedTableMouseClicked

    private void btnDeleteReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteReturnActionPerformed
        if(checkNullReturnForm()){
            int dialogBtn = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this data?", "WARNING", dialogBtn);
            if(dialogResult==0){
                DatabaseOperations.deleteReturned(returnIDText.getText());
                JOptionPane.showMessageDialog(this, "Successfully deleted data!");
                removePanel();
                addPanel(returnPanel);
                buildReturnedTable();
                resetReturnForm();
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select the data to be deleted!");
        }
    }//GEN-LAST:event_btnDeleteReturnActionPerformed

    private void btnLostReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLostReturnActionPerformed
        if(checkNullReturnForm()){
            String status = returnStatusText.getText();
            if(status.equals("Lost") || status.equals("Returned")){
                JOptionPane.showMessageDialog(this, "Tidak dapat mengubah data! Status pada id '"+returnIDText.getText()+"' telah berisi 'Lost/Returned'");
            }else{
                int dialogBtn = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ingin mengubah status pada id '"+returnIDText.getText()+"'?\nIni akan menyebabkan stok pada buku yang memiliki id '"+returnBookIDText.getText()+"' berkurang 1", "PERINGATAN", dialogBtn);
                if(dialogResult==0){
                    DatabaseOperations.updateStatusLost(returnIDText.getText());
//                  Set Stock with current ID_book -1
                    String query = "SELECT * FROM books WHERE id_buku = "+returnBookIDText.getText()+"";
                    int id_buku=0;
                    int stok_buku=0;
                    int stokBukuFinal=0;
                    int booksLength=DatabaseOperations.getBooks(query).size();
                    if(booksLength > 0){
                        id_buku=DatabaseOperations.getBooks(query).get(0).getId_buku();
                        stok_buku=DatabaseOperations.getBooks(query).get(0).getStok();
                        stokBukuFinal = stok_buku - 1;
                    }
                    DatabaseOperations.updateStokLost(id_buku, stokBukuFinal);
                    JOptionPane.showMessageDialog(this, "Successfully changed data!");
                    removePanel();
                    addPanel(returnPanel);
                    buildReturnedTable();
                    resetReturnForm();
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select the data you want to change the status to 'Lost'");
        }
    }//GEN-LAST:event_btnLostReturnActionPerformed

    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        if(checkNullReturnForm()){
            String status = returnStatusText.getText();
            if(status.equals("Borrowed")){
                String id_buku = returnBookIDText.getText();
                String judul_buku =  returnBookTitleText.getText();
                String id_user = returnUserIDText.getText();
                String username = returnUsernameText.getText();
                if(!(id_buku.equals("Data Unavailable") || judul_buku.equals("Data Unavailable") || id_user.equals("Data Unavailable") || username.equals("Data Unavailable"))){
                    int dialogBtn = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to proceed to payment?", "Questions", dialogBtn);
                    if(dialogResult==0){
                        removePanel();
                        addPanel(paymentPanel);
                        resetPaymentPanel();
                        //Calculate fine
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String dateNow = currentDateTime.format(formatter);
                        
                        String dateobj1= returnReturnDateText.getText();
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
                        idPeminjamanText.setText(returnIDText.getText());
                        bookIDText.setText(returnBookIDText.getText());
                        bookTitleText.setText(returnBookTitleText.getText());
                        userIDText.setText(returnUserIDText.getText());
                        usernamePayText.setText(returnUsernameText.getText());
                        exipiryDateText.setText(returnReturnDateText.getText());
                        returnDateText.setText(changeDateFormat2(dateNow));
                        fineText.setText(String.valueOf(fine));
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "Data must be complete!");
                }
            }else{
                JOptionPane.showMessageDialog(this, "Data must be in 'Borrowed' status");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select the data to be changed to 'Returned' status");
        }
    }//GEN-LAST:event_btnReturnActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        removePanel();
        addPanel(returnPanel);
        buildReturnedTable();
        resetReturnForm();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        int dialogBtn = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to complete this payment?", "Payment", dialogBtn);
        if(dialogResult==0){
            Invoice invoice2 = new Invoice(Integer.parseInt(idPeminjamanText.getText()), Integer.parseInt(bookIDText.getText()), Integer.parseInt(userIDText.getText()), exipiryDateText.getText(), returnDateText.getText(), Integer.parseInt(fineText.getText()));
            int id_buku = invoice2.getId_buku();
            int id_anggota = invoice2.getId_user();
            int denda = Integer.parseInt(fineText.getText());
            String tanggal_transaksi=invoice2.getTanggal_transaksi();
            String query = "SELECT * FROM users WHERE username = '"+UsernameLabel.getText()+"'";
            int id_librarian = DatabaseOperations.getUsers(query).get(0).getId_user();
            DatabaseOperations.addInvoice(id_buku, id_anggota, id_librarian, denda, tanggal_transaksi);
            DatabaseOperations.updateStatusReturned(invoice2.getId_peminjaman());
            JOptionPane.showMessageDialog(this, "Your payment is successful!");
            removePanel();
            addPanel(returnPanel);
            buildReturnedTable();
            resetReturnForm();
        }
    }//GEN-LAST:event_btnPayActionPerformed

    private void addStockBukuTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addStockBukuTextKeyPressed
        inputNumberOnly(evt, addStockBukuText);
    }//GEN-LAST:event_addStockBukuTextKeyPressed

    private void stokBukuTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stokBukuTextKeyPressed
        inputNumberOnly(evt, stokBukuText);
    }//GEN-LAST:event_stokBukuTextKeyPressed

    private void tahunTerbitBukuTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tahunTerbitBukuTextKeyPressed
        inputNumberOnly(evt, tahunTerbitBukuText);
    }//GEN-LAST:event_tahunTerbitBukuTextKeyPressed

    private void addYearBukuTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addYearBukuTextKeyPressed
        inputNumberOnly(evt, addYearBukuText);
    }//GEN-LAST:event_addYearBukuTextKeyPressed

    private void addCodeBukuTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addCodeBukuTextKeyPressed
        inputNumberOnly(evt, addCodeBukuText);
    }//GEN-LAST:event_addCodeBukuTextKeyPressed

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
            java.util.logging.Logger.getLogger(Librarian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Librarian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Librarian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Librarian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Librarian("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BrandLibrary;
    private javax.swing.JLabel BrandManagement;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JLabel account;
    private javax.swing.JLabel addAuthorBukuLabel;
    private javax.swing.JTextField addAuthorBukuText;
    private javax.swing.JPanel addBookPanel;
    private javax.swing.JLabel addCodeBukuLabel;
    private javax.swing.JTextField addCodeBukuText;
    private javax.swing.JLabel addGenreBukuLabel;
    private javax.swing.JTextField addGenreBukuText;
    private javax.swing.JLabel addPasswordLabel;
    private javax.swing.JPasswordField addPasswordText;
    private javax.swing.JLabel addPublisherBukuLabel;
    private javax.swing.JTextField addPublisherBukuText;
    private javax.swing.JLabel addStockBukuLabel;
    private javax.swing.JTextField addStockBukuText;
    private javax.swing.JLabel addTitleBukuLabel;
    private javax.swing.JTextField addTitleBukuText;
    private javax.swing.JPanel addUserPanel;
    private javax.swing.JLabel addUsernameLabel;
    private javax.swing.JTextField addUsernameText;
    private javax.swing.JLabel addYearBukuLabel;
    private javax.swing.JTextField addYearBukuText;
    private javax.swing.JTextField authorBookInfo;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JLabel bookIDLabel;
    private javax.swing.JTextField bookIDText;
    private javax.swing.JLabel bookTitleLabel;
    private javax.swing.JTextField bookTitleText;
    private javax.swing.JTextField booksKeyword;
    private javax.swing.JPanel booksPanel;
    private javax.swing.JTable booksTable;
    private javax.swing.JPanel borrowPanel;
    private javax.swing.JButton btnAddBook;
    private javax.swing.JButton btnAddUser;
    private javax.swing.JLabel btnBooks;
    private javax.swing.JLabel btnBorrow;
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel btnDashboard;
    private javax.swing.JButton btnDeleteBuku;
    private javax.swing.JButton btnDeleteReturn;
    private javax.swing.JButton btnHapus;
    private javax.swing.JLabel btnLogOut;
    private javax.swing.JButton btnLostReturn;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetBuku;
    private javax.swing.JButton btnReturn;
    private javax.swing.JLabel btnReturned;
    private javax.swing.JButton btnSaveBuku;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSubmitAddBook;
    private javax.swing.JButton btnSubmitAddUser;
    private javax.swing.JLabel btnUsers;
    private javax.swing.JTextField codeBookInfo;
    private javax.swing.JLabel core;
    private javax.swing.JLabel dashBooksBorrowedLabel;
    private javax.swing.JLabel dashBooksBorrowedText;
    private javax.swing.JLabel dashBooksReturnedText;
    private javax.swing.JLabel dashTotalAccountLabel;
    private javax.swing.JLabel dashTotalAccountText;
    private javax.swing.JLabel dashTotalBooksText;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JTextField enterBookIDText;
    private com.toedter.calendar.JDateChooser enterBorrowingDate;
    private com.toedter.calendar.JDateChooser enterReturnDate;
    private javax.swing.JTextField enterUsernameText;
    private javax.swing.JLabel exipiryDateLabel;
    private javax.swing.JTextField exipiryDateText;
    private javax.swing.JLabel fineLabel;
    private javax.swing.JTextField fineText;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameText;
    private javax.swing.JTextField genreBookInfo;
    private javax.swing.JLabel genreBukuLabel;
    private javax.swing.JTextField genreBukuText;
    private javax.swing.JLabel headerAddBookLabel;
    private javax.swing.JLabel headerBookInfo;
    private javax.swing.JLabel headerBookInfo1;
    private javax.swing.JLabel headerBookInfo10;
    private javax.swing.JLabel headerBookInfo11;
    private javax.swing.JLabel headerBookInfo12;
    private javax.swing.JLabel headerBookInfo13;
    private javax.swing.JLabel headerBookInfo14;
    private javax.swing.JLabel headerBookInfo15;
    private javax.swing.JLabel headerBookInfo16;
    private javax.swing.JLabel headerBookInfo17;
    private javax.swing.JLabel headerBookInfo2;
    private javax.swing.JLabel headerBookInfo3;
    private javax.swing.JLabel headerBookInfo4;
    private javax.swing.JLabel headerBookInfo5;
    private javax.swing.JLabel headerBookInfo6;
    private javax.swing.JLabel headerBookInfo7;
    private javax.swing.JLabel headerBookInfo8;
    private javax.swing.JLabel headerBookInfo9;
    private javax.swing.JLabel headerLabelAddUser;
    private javax.swing.JLabel headerLabelBooks;
    private javax.swing.JLabel headerLabelBorrow;
    private javax.swing.JLabel headerLabelDashboard;
    private javax.swing.JLabel headerLabelPayment;
    private javax.swing.JLabel headerLabelReturned;
    private javax.swing.JLabel headerLabelUsers;
    private javax.swing.JLabel headerUserInfo;
    private javax.swing.JTextField idBookInfo;
    private javax.swing.JTextField idBukuText;
    private javax.swing.JLabel idBukulLabel;
    private javax.swing.JLabel idPeminjamanLabel;
    private javax.swing.JTextField idPeminjamanText;
    private javax.swing.JTextField idText;
    private javax.swing.JTextField idUserInfo;
    private javax.swing.JLabel idUserLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel judulBukuLabel;
    private javax.swing.JTextField judulBukuText;
    private javax.swing.JTextField keywordUser;
    private javax.swing.JTextField kodeBukuText;
    private javax.swing.JLabel kodeBukulLabel;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameText;
    private javax.swing.JPanel mainCardPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField namaText;
    private javax.swing.JLabel namaUserLabel;
    private javax.swing.JTextField nameidUserInfo;
    private javax.swing.JPanel navigasi;
    private javax.swing.JTextArea noteTextArea;
    private javax.swing.JPanel panelTotalBooks;
    private javax.swing.JPanel panelTotalBooks2;
    private javax.swing.JPanel panelTotalBooks3;
    private javax.swing.JPanel panelTotalBooks4;
    private javax.swing.JPanel paymentPanel;
    private javax.swing.JLabel penerbitBukuLabel;
    private javax.swing.JTextField penerbitBukuText;
    private javax.swing.JLabel penulisBukuLabel;
    private javax.swing.JTextField penulisBukuText;
    private javax.swing.JTextField publisherBookInfo;
    private javax.swing.JButton resetBorrow;
    private javax.swing.JTextField returnBookIDText;
    private javax.swing.JTextField returnBookTitleText;
    private javax.swing.JTextField returnBorrowDateText;
    private javax.swing.JLabel returnDateLabel;
    private javax.swing.JTextField returnDateText;
    private javax.swing.JTextField returnIDText;
    private javax.swing.JTextField returnKeyword;
    private javax.swing.JTextArea returnNoteTextArea;
    private javax.swing.JPanel returnPanel;
    private javax.swing.JTextField returnReturnDateText;
    private javax.swing.JTextField returnStatusText;
    private javax.swing.JScrollPane returnTable;
    private javax.swing.JTextField returnUserIDText;
    private javax.swing.JTextField returnUsernameText;
    private javax.swing.JTable returnedTable;
    private javax.swing.JLabel settings;
    private javax.swing.JPanel sideNav;
    private javax.swing.JLabel statusBorrowLabel;
    private javax.swing.JLabel stokBukuLabel;
    private javax.swing.JTextField stokBukuText;
    private javax.swing.JButton submitBorrow;
    private javax.swing.JLabel tahunTerbitBukuLabel;
    private javax.swing.JTextField tahunTerbitBukuText;
    private javax.swing.JLabel tipeUserLabel;
    private javax.swing.JTextField titleBookInfo;
    private javax.swing.JLabel transaction;
    private javax.swing.JLabel userIDLabel;
    private javax.swing.JTextField userIDText;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JTable userTable;
    private javax.swing.JComboBox<String> userTypeCB;
    private javax.swing.JLabel userTypeLabel;
    private javax.swing.JLabel usernamePayLabel;
    private javax.swing.JTextField usernamePayText;
    private javax.swing.JTextField usernameText;
    private javax.swing.JTextField usernameidUserInfo;
    private javax.swing.JPanel usersPanel;
    private javax.swing.JComboBox<String> usertypeCB;
    private javax.swing.JLabel usertypeLabel;
    private javax.swing.JTextField usertypeidUserInfo;
    private javax.swing.JTextField yearBookInfo;
    // End of variables declaration//GEN-END:variables
}
