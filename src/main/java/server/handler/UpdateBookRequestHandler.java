package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * 处理更新图书库存数量的请求。
 */
public class UpdateBookRequestHandler implements RequestHandler {
    /**
     * 处理更新图书库存数量的请求。
     *
     * @param parameters 请求参数的 JSON 对象，包含书籍 ID 和最终库存数量
     * @return 返回包含操作结果的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        String bookID = parameters.getString("bookID");
        int finalLibNumber = parameters.getInt("finallibNumber");
        JSONObject result = libraryService.updateBook(bookID, finalLibNumber);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
