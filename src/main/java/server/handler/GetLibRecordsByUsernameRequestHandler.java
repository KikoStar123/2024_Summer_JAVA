package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * 处理根据用户名获取图书借阅记录的请求，调用 LibraryService 获取借阅记录。
 */
public class GetLibRecordsByUsernameRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        JSONObject result = libraryService.getLibRecordsByUsername(parameters.getString("username"));

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success").put("data", result);
        return jsonResponse.toString();
    }
}
