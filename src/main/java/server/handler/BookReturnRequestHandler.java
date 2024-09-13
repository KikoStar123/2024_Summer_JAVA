package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * BookReturnRequestHandler 处理归还图书的请求。
 * 调用 LibraryService 进行图书归还，并返回处理结果。
 */
public class BookReturnRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        String bookID = parameters.getString("bookID");
        String username = parameters.getString("username");
        JSONObject result = libraryService.returnBook(username, bookID);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
