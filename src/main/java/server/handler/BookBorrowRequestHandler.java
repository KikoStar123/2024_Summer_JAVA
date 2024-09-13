package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * BookBorrowRequestHandler 处理借阅图书的请求。
 * 调用 LibraryService 进行图书借阅，并返回处理结果。
 */
public class BookBorrowRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        String username = parameters.getString("username");
        String bookId = parameters.getString("bookID");
        JSONObject result = libraryService.borrowBook(username, bookId);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
