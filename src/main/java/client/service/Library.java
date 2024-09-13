package client.service;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 图书馆服务类，提供图书的查询、借阅、归还、续借、上传图书封面和PDF等功能。
 */
public class Library {

    private final FileService fileService = new FileService();
    private final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private final int SERVER_PORT = IpConfig.SERVER_PORT;

    /**
     * 通过书名搜索图书。
     *
     * @param bookName 书名
     * @return 查找到的图书列表
     */
    public List<Book> searchBooksByName(String bookName) {
        List<Book> foundBooks = new ArrayList<>();
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "searchBooksByName");
            request.put("parameters", new JSONObject().put("bookName", bookName));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
                JSONArray booksArray = jsonResponse.getJSONObject("data").getJSONArray("books");
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
                            bookJson.getString("location"),
                            bookJson.optString("imagePath", "uploads/defaultbook.jpg"),
                            bookJson.optString("pdfPath", "null")
                    );
                    foundBooks.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundBooks;
    }

    /**
     * 根据图书ID获取图书详细信息。
     *
     * @param bookID 图书ID
     * @return 图书对象，包含详细信息
     */
    public Book getBookDetailsById(String bookID) {
        Book foundBook = null;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getBookDetailsById");
            request.put("parameters", new JSONObject().put("bookID", bookID));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
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
                        bookJson.getString("location"),
                        bookJson.optString("imagePath", "uploads/defaultbook.jpg"),
                        bookJson.optString("pdfPath", "null")
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundBook;
    }

    /**
     * 获取指定用户的借阅记录。
     *
     * @param username 用户名
     * @return 借阅记录列表
     */
    public List<LibRecord> getLibRecordsByUsername(String username) {
        List<LibRecord> libRecords = new ArrayList<>();
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
                JSONArray recordsArray = jsonResponse.getJSONObject("data").getJSONArray("recordArray");
                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject recordJson = recordsArray.getJSONObject(i);
                    boolean isReturn = recordJson.getBoolean("isReturn");
                    boolean renewable = recordJson.getBoolean("renewable");

                    LibRecord libRecord = new LibRecord(
                            recordJson.getInt("borrowId"),
                            recordJson.getString("username"),
                            recordJson.getString("bookID"),
                            recordJson.getString("truename"),
                            recordJson.getString("bookName"),
                            recordJson.getString("borrowDate"),
                            recordJson.getString("returnDate"),
                            renewable,
                            isReturn,
                            recordJson.getString("recordStatus")
                    );
                    libRecords.add(libRecord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libRecords;
    }

    /**
     * 添加书籍条目（管理员操作）。
     *
     * @param book 书籍对象
     * @return 如果添加成功返回 true，否则返回 false
     */
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

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 续借图书。
     *
     * @param borrowID 借阅ID
     * @return 如果续借成功返回 true，否则返回 false
     */
    public boolean renewBook(int borrowID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "renewBook");
            request.put("parameters", new JSONObject().put("borrowID", borrowID));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 借阅书籍。
     *
     * @param username 用户名
     * @param bookID   书籍ID
     * @return 如果借阅成功返回 true，否则返回 false
     */
    public boolean bookBorrow(String username, String bookID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "bookBorrow");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bookID", bookID));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 归还书籍。
     *
     * @param username 用户名
     * @param bookID   书籍ID
     * @return 如果归还成功返回 true，否则返回 false
     */
    public boolean bookReturn(String username, String bookID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "bookReturn");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bookID", bookID));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新书籍信息（管理员操作）。
     *
     * @param bookID        书籍ID
     * @param finalLibNumber 最终馆藏数量
     * @return 如果更新成功返回 true，否则返回 false
     */
    public boolean updateBook(String bookID, int finalLibNumber) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "updateBook");
            request.put("parameters", new JSONObject()
                    .put("bookID", bookID)
                    .put("finallibNumber", finalLibNumber));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取所有借阅记录。
     *
     * @return 借阅记录列表
     */
    public List<LibRecord> getAllLibRecords() {
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
                            recordJson.getString("truename"),
                            recordJson.getString("bookName"),
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
        return libRecords;
    }

    /**
     * 上传书籍封面图片。
     *
     * @param imageFile 图片文件
     * @param bookID    书籍ID
     * @return 如果上传成功返回 true，否则返回 false
     */
    public boolean uploadBookImage(File imageFile, String bookID) {
        String fileName = bookID + ".jpg";
        if (fileService.fileExists(fileName)) {
            fileName = bookID + "_" + UUID.randomUUID().toString() + ".jpg";
        }

        if (fileService.uploadFile(imageFile, fileName)) {
            return updateBookImagePath(bookID, "uploads/" + fileName);
        }
        return false;
    }

    /**
     * 上传书籍PDF文件。
     *
     * @param pdfFile PDF文件
     * @param bookID  书籍ID
     * @return 如果上传成功返回 true，否则返回 false
     */
    public boolean uploadBookPDF(File pdfFile, String bookID) {
        String fileName = bookID + ".pdf";
        if (fileService.fileExists(fileName)) {
            fileName = bookID + "_" + UUID.randomUUID().toString() + ".pdf";
        }

        if (fileService.uploadFile(pdfFile, fileName)) {
            return updateBookPDFPath(bookID, "uploads/" + fileName);
        }
        return false;
    }

    /**
     * 更新书籍的图片路径。
     *
     * @param bookID    书籍ID
     * @param imagePath 图片路径
     * @return 如果更新成功返回 true，否则返回 false
     */
    public boolean updateBookImagePath(String bookID, String imagePath) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "updateBookImagePath");
            request.put("parameters", new JSONObject()
                    .put("bookID", bookID)
                    .put("imagePath", imagePath));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新书籍的PDF路径。
     *
     * @param bookID  书籍ID
     * @param pdfPath PDF路径
     * @return 如果更新成功返回 true，否则返回 false
     */
    public boolean updateBookPDFPath(String bookID, String pdfPath) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "updateBookPDFPath");
            request.put("parameters", new JSONObject()
                    .put("bookID", bookID)
                    .put("pdfPath", pdfPath));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
