package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * AddBookRequestHandler 处理添加图书的请求。
 * 调用 LibraryService 进行图书添加，并返回处理结果。
 */
public class AddBookRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        JSONObject result = libraryService.addBook(parameters);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
