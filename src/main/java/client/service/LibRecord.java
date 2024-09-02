package client.service;

    public class LibRecord {
        private int borrowId;       // 借阅号，主键，自增
        private String username;     // 用户账号，外键关联 tblUser 表
        private String bookID;       // 书籍号，外键关联 tblBook 表
        private String bookName;//书名，外键关联tblBook表
        private String truename;//真实姓名，外键关联tblUser 表
        private String borrowDate;   // 借阅日期
        private String returnDate;   // 归还日期
        private boolean renewable;    // 可否续借，字符串表示 true 或 false
        private boolean isReturn;    //是否已经归还
        private String recordStatus;  //借阅状态  “已归还，超期未还，借阅中”
        // 构造函数
    public LibRecord(int borrowId, String username, String bookID,String truename,String bookName,
                     String borrowDate, String returnDate, boolean renewable
    , boolean isReturn, String recordStatus) {
        this.borrowId = borrowId;
        this.username = username;
        this.bookID = bookID;
        this.truename = truename;
        this.bookName = bookName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.renewable = renewable;
        this.isReturn = isReturn;
        this.recordStatus = recordStatus;
    }

    // Getter 和 Setter 方法
    public String getName(){
        return bookName;
        }
        public void setName(String name){
        this.bookName = name;
            //return name;
        }
    public String getTruename(){
        return truename;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public boolean getisReturn() {
        return isReturn;
    }
    public void setisReturn(boolean isReturn) {
        this.isReturn = isReturn;
    }
    public String getRecordStatus() {
        return recordStatus;
    }
    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
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

    public boolean getRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
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
