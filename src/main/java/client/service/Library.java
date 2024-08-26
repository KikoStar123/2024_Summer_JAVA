package client.service;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
public class Library {
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 8080; // 服务器端口号
    public boolean borrowBook(String userId, String bookId) {
        // 查找用户和书籍
        User user = findUserById(userId);
        Book book = findBookById(bookId);

        if (user != null && book != null && book.isAvailable()) {
            book.setAvailable(false); // 标记为已借出
            return true; // 借阅成功
        }
        return false; // 借阅失败
    }

    public boolean returnBook(String userId, String bookId) {
        Book book = findBookById(bookId);
        if (book != null) {
            book.setAvailable(true); // 标记为可借
            return true; // 还书成功
        }
        return false; // 还书失败
    }
//按照查询字符串来执行
        public List<Book> searchBooks(String query) {
            List<Book> result = new ArrayList<>();
            for (Book book : books) {
                if (book.getTitle().contains(query) || book.getAuthor().contains(query)||book.getId().equals(query)) {
                    result.add(book);//前两种是包含，后两种是判断相等。
                }
            }
            return result; // 返回符合条件的书籍列表
        }

    private User findUserById(String userId) {
        // 查找用户的逻辑
        // 使用Java 8的流式编程查找用户
        return users.stream()
                .filter(user -> userId.equals(user.getId()))
                .findFirst()
                .orElse(null); // 如果没有找到，返回null
    }
//按照书的id来执行，适合查找一本书的情况
//辅助完成上述方法
    private Book findBookById(String bookId) {
        // 查找书籍的逻辑
        // 使用Java 8的流式编程查找书籍
        return books.stream()
                .filter(book -> bookId.equals(book.getId()))
                .findFirst()
                .orElse(null); // 如果没有找到，返回null
    }

}
