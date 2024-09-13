package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * 处理图书续借请求的处理器类。
 * 该类负责处理客户端发来的续借请求，并调用 LibraryService 进行操作。
 */
public class RenewBookRequestHandler implements RequestHandler {
    /**
     * 处理图书续借请求。
     *
     * @param parameters 包含续借信息（借阅 ID）的 JSON 对象
     * @return 返回续借结果的 JSON 字符串，包含成功或失败的状态和相应的消息
     */
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        int borrowID = parameters.getInt("borrowID");
        JSONObject result = libraryService.renewBook(borrowID);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
