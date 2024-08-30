package client.service;

import org.json.JSONObject;
import org.json.JSONArray;
import client.service.Book;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080; // 替换为服务器端口号

    // 假设有一个方法用于查询书籍
    public List<Book> searchBooksByName(String bookName) {
        List<Book> foundBooks = new ArrayList<>();
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "search_books_by_name");
            request.put("parameters", new JSONObject()
                    .put("bookName", bookName));//传递请求类型和书名；

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
                JSONArray booksArray = jsonResponse.getJSONArray("books");
                for (int i = 0; i < booksArray.length(); i++) {
                    JSONObject bookJson = booksArray.getJSONObject(i);
                    Book book = new Book(
                            bookJson.getString("bookID"),
                            bookJson.getString("name"),
                            bookJson.getString("author"),
                            bookJson.getString("publishHouse"),
                            bookJson.getString("publicationYear"),
                            bookJson.getString("classification"),
                            bookJson.getInt("curNumber"),
                            bookJson.getInt("libNumber"),
                            bookJson.getString("location")
                    );
                    foundBooks.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundBooks; // 返回找到的书籍列表
    }
    // 根据书的ID获取书籍详细信息
    public Book getBookDetailsById(String name) {
        Book foundBook = null;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "get_book_by_id");
            request.put("parameters", new JSONObject()
                    .put("name", name)); // 传递请求类型和书的ID

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
                // 假设响应中只包含一本书的信息
                JSONObject bookJson = jsonResponse.getJSONObject("book");
                foundBook = new Book(
                        bookJson.getString("bookID"),
                        bookJson.getString("name"),
                        bookJson.getString("author"),
                        bookJson.getString("publishHouse"),
                        bookJson.getString("publicationYear"),
                        bookJson.getString("classification"),
                        bookJson.getInt("curNumber"),
                        bookJson.getInt("libNumber"),
                        bookJson.getString("location")
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundBook; // 返回找到的书籍对象
    }
    //借阅书籍的请求
    public boolean bookBorrow(String username, String bookID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "bookBorrow");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bookName", bookID)); // 传递请求类型和用户名以及书名

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查服务器响应的状态
            if (jsonResponse.getString("status").equals("success")) {
                // 假设服务器响应中包含借阅操作的结果
                return jsonResponse.getBoolean("borrowed");
            } else {
                // 如果服务器返回失败，打印错误信息
                System.out.println("Failed to borrow book: " + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 如果发生异常或服务器返回失败，返回false
    }
}
