package client.service;

public class Book {
    // 定义书籍的属性
    private String bookID;
    private String name;
    private String author;
    private String publishHouse;
    private String publicationYear; // 修改字段名
    private String classification;
    private int curNumber;//可借数量
    private int libNumber; // 新增馆藏数量
    private String location;

    // 构造函数
    public Book(String bookID, String name, String author, String publishHouse,
                String publicationYear, String classification, int curNumber, int libNumber, String location) {
        this.bookID = bookID;
        this.name = name;
        this.author = author;
        this.publishHouse = publishHouse;
        this.publicationYear = publicationYear; // 修改参数名
        this.classification = classification;
        this.curNumber = curNumber;
        this.libNumber = libNumber; // 初始化馆藏数量
        this.location = location;
    }

//    public void Book_4(String name, String author, int curNumber, int libNumber) {
//        this.name = name;
//        this.author = author;
//        this.curNumber = curNumber;
//        this.libNumber = libNumber;
//
//        // 其他属性可以设置为 null 或者默认值，如果适用
//        this.bookID = null; // 或者一个默认的 ID
//        this.publishHouse = null; // 或者一个默认的出版社
//        this.publicationYear = "Unknown"; // 如果出版年份未知
//        this.classification = "Unknown"; // 如果分类未知
//        this.location = "Unknown"; // 如果位置未知
//    }
    // Getter和Setter方法
    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishHouse() {
        return publishHouse;
    }

    public void setPublishHouse(String publishHouse) {
        this.publishHouse = publishHouse;
    }

    public String getPublicationYear() { // 修改方法名
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) { // 修改方法名
        this.publicationYear = publicationYear;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getCurNumber() {
        return curNumber;
    }

    public void setCurNumber(int curNumber) {
        this.curNumber = curNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    // 新增馆藏数量的getter和setter方法
    public int getLibNumber() {
        return libNumber;
    }

    public void setLibNumber(int libNumber) {
        this.libNumber = libNumber;
    }

    // 重写toString方法，方便打印书籍信息
    @Override
    public String toString() {
        return "Book{" +
                "bookID='" + bookID + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publishHouse='" + publishHouse + '\'' +
                ", publicationYear='" + publicationYear + '\'' + // 修改字段引用
                ", classification='" + classification + '\'' +
                ", curNumber=" + curNumber +
                ", libNumber=" + libNumber + // 添加馆藏数量的打印
                ", location='" + location + '\'' +
                '}';
    }
}