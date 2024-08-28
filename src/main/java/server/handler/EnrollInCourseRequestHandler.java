package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class EnrollInCourseRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String username = parameters.getString("username");
        String courseID = parameters.getString("courseID");

        // 调用 enrollInCourse 方法，获取返回的结果
        JSONObject result = courseService.enrollInCourse(username, courseID);
        boolean success = "success".equals(result.getString("status"));

        // 构建响应 JSON 对象
        JSONObject jsonResponse = new JSONObject();
        if (success) {
            jsonResponse.put("status", "success")
                    .put("message", "Student enrolled in course successfully");
        } else {
            jsonResponse.put("status", "fail")
                    .put("message", result.optString("message", "Failed to enroll in course"));
        }
        return jsonResponse.toString();
    }
}
