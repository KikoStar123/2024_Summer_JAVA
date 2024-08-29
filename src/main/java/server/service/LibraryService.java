package server.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LibraryService {

    private final Lock lock = new ReentrantLock();

    public JSONObject searchBooksByName(String bookName) {
        JSONObject response = new JSONObject();
        JSONArray booksArray = new JSONArray();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();

        try {
            String query = "SELECT * FROM tblBook WHERE name LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + bookName + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                JSONObject book = new JSONObject();
                book.put("bookID", rs.getString("bookId"));
                book.put("name", rs.getString("name"));
                book.put("author", rs.getString("author"));
                book.put("publishHouse", rs.getString("publishHouse"));
                book.put("publicationYear", rs.getString("publishingYear"));
                book.put("classification", rs.getString("classification"));
                book.put("curNumber", rs.getInt("curNumber"));
                book.put("libNumber", rs.getInt("libNumber"));
                book.put("location", rs.getString("location"));

                booksArray.put(book);
            }

            response.put("status", "success");
            response.put("books", booksArray);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
            lock.unlock();
        }

        return response;
    }

    public JSONObject getBookDetailsById(String bookId) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();

        try {
            String query = "SELECT * FROM tblBook WHERE bookId = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JSONObject book = new JSONObject();
                book.put("bookID", rs.getString("bookId"));
                book.put("name", rs.getString("name"));
                book.put("author", rs.getString("author"));
                book.put("publishHouse", rs.getString("publishHouse"));
                book.put("publicationYear", rs.getString("publishingYear"));
                book.put("classification", rs.getString("classification"));
                book.put("curNumber", rs.getInt("curNumber"));
                book.put("libNumber", rs.getInt("libNumber"));
                book.put("location", rs.getString("location"));

                response.put("status", "success");
                response.put("book", book);
            } else {
                response.put("status", "error");
                response.put("message", "Book not found.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
            lock.unlock();
        }

        return response;
    }
}
