package client.service;

import org.json.JSONObject;
import org.json.JSONArray;
import client.service.Book;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
//import java.util.*;

public class Library {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080; // 替换为服务器端口号

    // 假设有一个方法用于查询书籍
    public List<Book> searchBooksByName(String bookName) {
        List<Book> foundBooks = new ArrayList<>();
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "searchBooksByName");
            request.put("parameters", new JSONObject()
                    .put("bookName", bookName));//传递请求类型和书名；

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONObject data = jsonResponse.getJSONObject("data");
            if (jsonResponse.getString("status").equals("success")) {
                JSONArray booksArray = data.getJSONArray("books");
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
    public Book getBookDetailsById(String bookID) {
        Book foundBook = null;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getBookDetailsById");
            request.put("parameters", new JSONObject()
                    .put("bookID", bookID)); // 传递请求类型和书的ID

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            System.out.println(jsonResponse.toString());
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

    //查看借阅记录的函数（返回recordstatus：中文）以及已还和为还数量
    public List<LibRecord> getLibRecordsByUsername(String username) {
        List<LibRecord> libRecords = new ArrayList<>();
        int totalReturnedBooks = 0;    // 总已还书籍数量
        int totalNotReturnedBooks = 0; // 总未还书籍数量

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getLibRecordsByUsername");
            request.put("parameters", new JSONObject().put("username", username));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
                JSONObject data = jsonResponse.getJSONObject("data");
                JSONArray recordsArray = data.getJSONArray("recordArray");

                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject recordJson = recordsArray.getJSONObject(i);//***yjb
                    boolean isReturn = recordJson.getBoolean("isReturn"); // 根据实际情况调整
                    boolean renewable = recordJson.getBoolean("renewable");
                    LibRecord libRecord = new LibRecord(
                            recordJson.getInt("borrowId"),
                            recordJson.getString("username"),
                            recordJson.getString("bookID"),
                            recordJson.getString("borrowDate"),
                            recordJson.getString("returnDate"),
                            renewable,
                            isReturn,
                            recordJson.getString("recordStatus")
                    );
                    libRecords.add(libRecord);
                }

                // 从数据中获取总已还和未还书籍数量
                totalReturnedBooks = data.getInt("retNumber");
                totalNotReturnedBooks = data.getInt("noretNumber");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return libRecords; // 返回借阅记录列表
    }
    //添加书籍条目，由管理员实现（管理员写ISBN）
    public boolean addBook(Book book) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "addBook");

            JSONObject bookDetails = new JSONObject();
            bookDetails.put("bookID", book.getBookID())
                    .put("name", book.getName())
                    .put("author", book.getAuthor())
                    .put("publishHouse", book.getPublishHouse())
                    .put("publicationYear", book.getPublicationYear())
                    .put("classification", book.getClassification())
                    .put("curNumber", book.getCurNumber())
                    .put("libNumber", book.getLibNumber())
                    .put("location", book.getLocation());

            request.put("parameters", bookDetails);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查服务器响应的状态
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean renewBook(int borrowID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "renewBook"); // 请求类型为续借
            JSONObject parameters = new JSONObject();
            parameters.put("borrowID", borrowID); // 传递需要续借的书籍的借阅号
            request.put("parameters", parameters);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查服务器响应的状态
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //借阅书籍的请求
    public boolean bookBorrow(String username, String bookID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "bookBorrow");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bookID", bookID)); // 传递请求类型和用户名以及书名

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查服务器响应的状态
            if (jsonResponse.getString("status").equals("success")) {
                // 假设服务器响应中包含借阅操作的结果
                System.out.println("Success!");
                return true;

            } else {
                // 如果服务器返回失败，打印错误信息
                System.out.println("Failed to borrow book: " + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 如果发生异常或服务器返回失败，返回false
    }
    //归还书籍的请求
    //多传上去一个参数
    public boolean bookReturn(String username, String bookID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "bookReturn");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bookID", bookID)); // 传递请求类型和用户名以及书名
                    //.put("borrowID", borrowID));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查服务器响应的状态
            if (jsonResponse.getString("status").equals("success")) {
                // 假设服务器响应中包含借阅操作的结果
                return true;
            } else {
                // 如果服务器返回失败，打印错误信息
                System.out.println("Failed to return book: " + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 如果发生异常或服务器返回失败，返回false
    }
    //bookID按照ISBN来区分，所以同名不同版本也是不同的id。
    public boolean updateBook(String bookID, int finalLibNumber) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "updateBook"); // 请求类型为更新书籍信息

            JSONObject parameters = new JSONObject();
            parameters.put("bookID", bookID); // 传递书籍ID
            parameters.put("finallibNumber", finalLibNumber); // 传递要更改的数量
// libnumber和curnumber都要改变。
            request.put("parameters", parameters);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查服务器响应的状态
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //更新全部信息，对于管理员****来说都可以用。

    public List<LibRecord> getAllLibRecords() {
        List<LibRecord> libRecords = new ArrayList<>();
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getAllLibRecords"); // 请求类型为获取所有借阅记录

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
                JSONArray recordsArray = jsonResponse.getJSONArray("libRecords");
                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject recordJson = recordsArray.getJSONObject(i);
                    LibRecord libRecord = new LibRecord(
                            recordJson.getInt("borrowId"),
                            recordJson.getString("username"),
                            recordJson.getString("bookID"),
                            recordJson.getString("borrowDate"),
                            recordJson.getString("returnDate"),
                            recordJson.getBoolean("renewable"),
                            recordJson.getBoolean("isReturn"),
                            recordJson.getString("recordStatus")
                    );
                    libRecords.add(libRecord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libRecords; // 返回借阅记录列表，即使为空也会返回
    }
    public List<LibRecord> getAllLibRecords(List<String> bookNameList) {
        List<LibRecord> libRecords = new ArrayList<>();
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getAllLibRecords");
            request.put("parameters", new JSONObject());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
                JSONArray recordsArray = jsonResponse.getJSONArray("libRecords");
                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject recordJson = recordsArray.getJSONObject(i);
                    LibRecord libRecord = new LibRecord(
                            recordJson.getInt("borrowId"),
                            recordJson.getString("username"),
                            recordJson.getString("bookID"),
                            recordJson.getString("borrowDate"),
                            recordJson.getString("returnDate"),
                            recordJson.getBoolean("renewable"),
                            recordJson.getBoolean("isReturn"),
                            recordJson.getString("recordStatus")
                    );
                    libRecords.add(libRecord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 填充书名
        for (LibRecord record : libRecords) {
            Book book = getBookDetailsById(record.getBookID());
            String bookName = book.getName();
            bookNameList.add(bookName);//直接添加一个书名到书名的列表里
        }

        return libRecords; // 返回借阅记录列表
    }

}
