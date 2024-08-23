package client.service;

public class Book {
    private String id;
    private String title;
    private String author;
    private boolean isAvailable;
    private int number;//数量
    // 构造函数、getter 和 setter 方法

    // 构造函数（可选，根据需要实现）
    public Book(String id, String title, String author, boolean isAvailable, int number) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
        this.number = number;
    }

    // 以下是getter和setter方法的实现

    // 检查书籍是否可借
    public boolean isAvailable() {
        return isAvailable;
    }

    // 设置书籍的可借状态
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // 获取书籍的标题
    public String getTitle() {
        return title;
    }

    // 设置书籍的标题
    public void setTitle(String title) {
        this.title = title;
    }

    // 获取书籍的作者
    public String getAuthor() {
        return author;
    }

    // 获取书籍的id
    public String getId() {
        return id;
    }

    // 设置书籍的作者
    public void setAuthor(String author) {
        this.author = author;
    }

    // 其他getter和setter方法可以根据需要添加

}