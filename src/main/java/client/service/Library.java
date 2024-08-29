package client.service;

import org.json.JSONObject;
import org.json.JSONArray;
import client.service.Book;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private static final String SERVER_ADDRESS = "your.server.address";
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
}
