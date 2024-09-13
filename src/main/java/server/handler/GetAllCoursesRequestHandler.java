package server.handler;

import org.json.JSONObject;
import server.service.CourseService;
/**
 * 处理获取用户所有银行记录的请求。
 * 调用 BankService 来获取用户的银行记录。
 */
public class GetAllCoursesRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        JSONObject result = courseService.getAllCourses();

        JSONObject jsonResponse = new JSONObject();
        if (result != null) {
            jsonResponse.put("status", "success");
            jsonResponse.put("courses", result.getJSONArray("courses"));
        } else {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Failed to retrieve courses.");
        }

        return jsonResponse.toString();
    }
}
