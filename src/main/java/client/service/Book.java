package client.service;

/**
 * 该类表示书籍的属性和操作，包含书籍的ID、名称、作者、出版社、出版年份、分类、馆藏数量、位置等信息。
 */
public class Book {
    // 书籍的属性
    private String bookID;
    private String name;
    private String author;
    private String publishHouse;
    private String publicationYear;
    private String classification;
    private int curNumber;
    private int libNumber;
    private String location;
    private String imagePath;
    private String pdfPath;

    /**
     * Book 构造函数，用于初始化包含所有信息的书籍对象。
     *
     * @param bookID           书籍ID
     * @param name             书籍名称
     * @param author           作者
     * @param publishHouse     出版社
     * @param publicationYear  出版年份
     * @param classification   分类
     * @param curNumber        当前馆藏数量
     * @param libNumber        馆藏总量
     * @param location         书籍存放位置
     * @param imagePath        书籍封面图片路径
     * @param pdfPath          书籍PDF文件路径
     */
    public Book(String bookID, String name, String author, String publishHouse,
                String publicationYear, String classification, int curNumber, int libNumber, String location,
                String imagePath, String pdfPath) {
        this.bookID = bookID;
        this.name = name;
        this.author = author;
        this.publishHouse = publishHouse;
        this.publicationYear = publicationYear;
        this.classification = classification;
        this.curNumber = curNumber;
        this.libNumber = libNumber;
        this.location = location;
        this.imagePath = imagePath;
        this.pdfPath = pdfPath;
    }

    /**
     * 重载的构造函数，不包含 imagePath 和 pdfPath。
     *
     * @param bookID           书籍ID
     * @param name             书籍名称
     * @param author           作者
     * @param publishHouse     出版社
     * @param publicationYear  出版年份
     * @param classification   分类
     * @param curNumber        当前馆藏数量
     * @param libNumber        馆藏总量
     * @param location         书籍存放位置
     */
    public Book(String bookID, String name, String author, String publishHouse,
                String publicationYear, String classification, int curNumber, int libNumber, String location) {
        this(bookID, name, author, publishHouse, publicationYear, classification, curNumber, libNumber, location, null, null);
    }

    // Getter和Setter方法

    /**
     * 获取书籍ID。
     *
     * @return 书籍ID
     */
    public String getBookID() {
        return bookID;
    }

    /**
     * 设置书籍ID。
     *
     * @param bookID 书籍ID
     */
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    /**
     * 获取书籍名称。
     *
     * @return 书籍名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置书籍名称。
     *
     * @param name 书籍名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取作者名称。
     *
     * @return 作者名称
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者名称。
     *
     * @param author 作者名称
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取出版社名称。
     *
     * @return 出版社名称
     */
    public String getPublishHouse() {
        return publishHouse;
    }

    /**
     * 设置出版社名称。
     *
     * @param publishHouse 出版社名称
     */
    public void setPublishHouse(String publishHouse) {
        this.publishHouse = publishHouse;
    }

    /**
     * 获取出版年份。
     *
     * @return 出版年份
     */
    public String getPublicationYear() {
        return publicationYear;
    }

    /**
     * 设置出版年份。
     *
     * @param publicationYear 出版年份
     */
    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * 获取书籍分类。
     *
     * @return 书籍分类
     */
    public String getClassification() {
        return classification;
    }

    /**
     * 设置书籍分类。
     *
     * @param classification 书籍分类
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * 获取当前馆藏数量。
     *
     * @return 当前馆藏数量
     */
    public int getCurNumber() {
        return curNumber;
    }

    /**
     * 设置当前馆藏数量。
     *
     * @param curNumber 当前馆藏数量
     */
    public void setCurNumber(int curNumber) {
        this.curNumber = curNumber;
    }

    /**
     * 获取书籍存放位置。
     *
     * @return 书籍存放位置
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置书籍存放位置。
     *
     * @param location 书籍存放位置
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取馆藏总量。
     *
     * @return 馆藏总量
     */
    public int getLibNumber() {
        return libNumber;
    }

    /**
     * 设置馆藏总量。
     *
     * @param libNumber 馆藏总量
     */
    public void setLibNumber(int libNumber) {
        this.libNumber = libNumber;
    }

    /**
     * 获取书籍封面图片路径。
     *
     * @return 书籍封面图片路径
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 设置书籍封面图片路径。
     *
     * @param imagePath 书籍封面图片路径
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 获取书籍PDF文件路径。
     *
     * @return 书籍PDF文件路径
     */
    public String getPdfPath() {
        return pdfPath;
    }

    /**
     * 设置书籍PDF文件路径。
     *
     * @param pdfPath 书籍PDF文件路径
     */
    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    /**
     * 重写toString方法，便于打印书籍信息。
     *
     * @return 包含书籍信息的字符串
     */
    @Override
    public String toString() {
        return "Book{" +
                "bookID='" + bookID + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publishHouse='" + publishHouse + '\'' +
                ", publicationYear='" + publicationYear + '\'' +
                ", classification='" + classification + '\'' +
                ", curNumber=" + curNumber +
                ", libNumber=" + libNumber +
                ", location='" + location + '\'' +
                ", imagePath=" + imagePath + '\'' +
                ", pdfPath=" + pdfPath + '\'' +
                '}';
    }
}
