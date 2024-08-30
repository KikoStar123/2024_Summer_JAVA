package client.service;

public class LibRecord {
    private int borrowId;       // 借阅号，主键，自增
    private String username;     // 用户账号，外键关联 tblUser 表
    private String bookID;       // 书籍号，外键关联 tblBook 表
    private String borrowDate;   // 借阅日期
    private String returnDate;   // 归还日期
    private String renewable;    // 可否续借，字符串表示 true 或 false

    // 构造函数
    public LibRecord(int borrowId, String username, String bookID, String borrowDate, String returnDate, String renewable) {
        this.borrowId = borrowId;
        this.username = username;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.renewable = renewable;
    }

    // Getter 和 Setter 方法
    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getRenewable() {
        return renewable;
    }

    public void setRenewable(String renewable) {
        this.renewable = renewable;
    }

    // toString 方法，用于打印借阅记录的详细信息
    @Override
    public String toString() {
        return "LibRecord{" +
                "borrowId=" + borrowId +
                ", username='" + username + '\'' +
                ", bookID='" + bookID + '\'' +
                ", borrowDate='" + borrowDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", renewable='" + renewable + '\'' +
                '}';
    }
}
