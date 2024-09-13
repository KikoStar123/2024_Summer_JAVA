package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * 处理获取所有课程信息的请求。
 * 调用 CourseService 来获取所有课程信息。
 */
public class GetAllLibRecordsRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        JSONObject result = libraryService.getAllLibRecords();

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("libRecords", result.getJSONArray("libRecords"));
        return jsonResponse.toString();
    }
}
