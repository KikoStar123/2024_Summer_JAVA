package server.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
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

    public JSONObject getLibRecordsByUsername(String username) {
        JSONObject response = new JSONObject();
        JSONArray recordArray = new JSONArray();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();

        try {
            String query = "SELECT * FROM tblLibRecord WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            int retNumber = 0;
            int noretNumber = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();

            while (rs.next()) {
                JSONObject record = new JSONObject();
                record.put("borrowId", rs.getInt("borrowId"));
                record.put("username", rs.getString("username"));
                record.put("bookID", rs.getString("bookID"));
                record.put("borrowDate", rs.getString("borrowDate"));
                record.put("returnDate", rs.getString("returnDate"));
                record.put("isReturn", rs.getBoolean("isReturn"));
                record.put("renewable", rs.getBoolean("renewable"));

                String recordStatus;
                if (rs.getBoolean("isReturn")) {
                    recordStatus = "已还";
                    retNumber++;
                } else {
                    Date returnDate = sdf.parse(rs.getString("returnDate"));
                    if (currentDate.before(returnDate) || currentDate.equals(returnDate)) {
                        recordStatus = "未还";
                    } else {
                        recordStatus = "超期";
                    }
                    noretNumber++;
                }
                record.put("recordStatus", recordStatus);

                recordArray.put(record);
            }

            response.put("recordArray", recordArray);
            response.put("retNumber", retNumber);
            response.put("noretNumber", noretNumber);
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

    public JSONObject returnBook(int borrowId) {
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
            // 获取书籍ID
            String getBookIdQuery = "SELECT bookID FROM tblLibRecord WHERE borrowId = ? AND isReturn = ?";
            PreparedStatement getBookIdStmt = conn.prepareStatement(getBookIdQuery);
            getBookIdStmt.setInt(1, borrowId);
            getBookIdStmt.setBoolean(2, false);
            ResultSet rs = getBookIdStmt.executeQuery();

            if (rs.next()) {
                String bookId = rs.getString("bookID");

                // 更新借阅记录
                String updateRecordQuery = "UPDATE tblLibRecord SET isReturn = ?, returnDate = ? WHERE borrowId = ? AND isReturn = ?";
                PreparedStatement updateRecordStmt = conn.prepareStatement(updateRecordQuery);
                updateRecordStmt.setBoolean(1, true);
                updateRecordStmt.setString(2, new java.sql.Date(System.currentTimeMillis()).toString());
                updateRecordStmt.setInt(3, borrowId);
                updateRecordStmt.setBoolean(4, false);

                int rowsUpdated = updateRecordStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    // 更新书籍数量
                    String updateBookQuery = "UPDATE tblBook SET curNumber = curNumber + 1 WHERE bookId = ?";
                    PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery);
                    updateBookStmt.setString(1, bookId);
                    updateBookStmt.executeUpdate();

                    response.put("status", "success");
                    response.put("message", "Book returned successfully.");
                } else {
                    response.put("status", "error");
                    response.put("message", "No matching record found or book already returned.");
                }
            } else {
                response.put("status", "error");
                response.put("message", "No matching record found or book already returned.");
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

    public JSONObject borrowBook(String username, String bookId) {
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
            // 检查书籍是否可借
            String checkQuery = "SELECT curNumber FROM tblBook WHERE bookId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, bookId);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next()) {
                int curNumber = checkRs.getInt("curNumber");
                if (curNumber <= 0) {
                    response.put("status", "error");
                    response.put("message", "No available copies for borrowing.");
                    return response;
                }
            } else {
                response.put("status", "error");
                response.put("message", "Book not found.");
                return response;
            }

            // 更新书籍数量
            String updateBookQuery = "UPDATE tblBook SET curNumber = curNumber - 1 WHERE bookId = ?";
            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery);
            updateBookStmt.setString(1, bookId);
            updateBookStmt.executeUpdate();

            // 计算初始归还日期为当前日期起后一个月
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            java.sql.Date returnDate = new java.sql.Date(calendar.getTimeInMillis());

            // 插入借阅记录
            String insertRecordQuery = "INSERT INTO tblLibRecord (username, bookID, borrowDate, returnDate, isReturn, renewable) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
            insertRecordStmt.setString(1, username);
            insertRecordStmt.setString(2, bookId);
            insertRecordStmt.setString(3, new java.sql.Date(System.currentTimeMillis()).toString());
            insertRecordStmt.setString(4, returnDate.toString());
            insertRecordStmt.setBoolean(5, false); // 初始未归还
            insertRecordStmt.setBoolean(6, true); // 初始可续借
            insertRecordStmt.executeUpdate();

            response.put("status", "success");
            response.put("message", "Book borrowed successfully.");
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

    public JSONObject updateBook(String bookID, int finalLibNumber) {
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
            // 获取当前的馆藏数量和可借数量
            String getBookQuery = "SELECT libNumber, curNumber FROM tblBook WHERE bookId = ?";
            PreparedStatement getBookStmt = conn.prepareStatement(getBookQuery);
            getBookStmt.setString(1, bookID);
            ResultSet rs = getBookStmt.executeQuery();

            if (rs.next()) {
                int currentLibNumber = rs.getInt("libNumber");
                int currentCurNumber = rs.getInt("curNumber");

                // 计算新的可借数量
                int newCurNumber = currentCurNumber + (finalLibNumber - currentLibNumber);

                // 检查数据合法性
                if (finalLibNumber < 0 || newCurNumber < 0) {
                    response.put("status", "error");
                    response.put("message", "Invalid data: finalLibNumber and resulting curNumber must be non-negative.");
                } else {
                    // 更新书籍信息
                    String updateBookQuery = "UPDATE tblBook SET libNumber = ?, curNumber = ? WHERE bookId = ?";
                    PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery);
                    updateBookStmt.setInt(1, finalLibNumber);
                    updateBookStmt.setInt(2, newCurNumber);
                    updateBookStmt.setString(3, bookID);
                    updateBookStmt.executeUpdate();

                    response.put("status", "success");
                    response.put("message", "Book information updated successfully.");
                }
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
    public JSONObject addBook(JSONObject bookDetails) {
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
            String bookID = bookDetails.getString("bookID");
            String name = bookDetails.getString("name");
            String author = bookDetails.getString("author");
            String publishHouse = bookDetails.getString("publishHouse");
            String publicationYear = bookDetails.getString("publicationYear");
            String classification = bookDetails.getString("classification");
            int curNumber = bookDetails.getInt("curNumber");
            int libNumber = bookDetails.getInt("libNumber");
            String location = bookDetails.getString("location");

            // 插入新书籍信息
            String insertBookQuery = "INSERT INTO tblBook (bookId, name, author, publishHouse, publishingYear, classification, curNumber, libNumber, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertBookStmt = conn.prepareStatement(insertBookQuery);
            insertBookStmt.setString(1, bookID);
            insertBookStmt.setString(2, name);
            insertBookStmt.setString(3, author);
            insertBookStmt.setString(4, publishHouse);
            insertBookStmt.setString(5, publicationYear);
            insertBookStmt.setString(6, classification);
            insertBookStmt.setInt(7, curNumber);
            insertBookStmt.setInt(8, libNumber);
            insertBookStmt.setString(9, location);
            insertBookStmt.executeUpdate();

            response.put("status", "success");
            response.put("message", "Book added successfully.");
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

    public JSONObject renewBook(int borrowID) {
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
            // 检查借阅记录是否存在且可续借
            String checkQuery = "SELECT returnDate, renewable FROM tblLibRecord WHERE borrowId = ? AND isReturn = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, borrowID);
            checkStmt.setBoolean(2, false);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                boolean renewable = rs.getBoolean("renewable");
                if (!renewable) {
                    response.put("status", "error");
                    response.put("message", "This book cannot be renewed.");
                    return response;
                }

                // 计算新的归还日期为当前归还日期起后一个月
                String currentReturnDate = rs.getString("returnDate");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(java.sql.Date.valueOf(currentReturnDate));
                calendar.add(Calendar.MONTH, 1);
                java.sql.Date newReturnDate = new java.sql.Date(calendar.getTimeInMillis());

                // 更新借阅记录
                String updateRecordQuery = "UPDATE tblLibRecord SET returnDate = ?, renewable = ? WHERE borrowId = ?";
                PreparedStatement updateRecordStmt = conn.prepareStatement(updateRecordQuery);
                updateRecordStmt.setString(1, newReturnDate.toString());
                updateRecordStmt.setBoolean(2, false); // 续借后不可再续借
                updateRecordStmt.setInt(3, borrowID);
                updateRecordStmt.executeUpdate();

                response.put("status", "success");
                response.put("message", "Book renewed successfully.");
            } else {
                response.put("status", "error");
                response.put("message", "No matching record found or book already returned.");
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
