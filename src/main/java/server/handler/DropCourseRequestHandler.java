package server.handler;

import org.json.JSONObject;
import server.service.CourseService;
/**
 * 处理银行服务的存款请求。
 * 调用 BankService 处理存款操作。
 */
public class DropCourseRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String username = parameters.getString("username");
        String courseID = parameters.getString("courseID");

        JSONObject jsonResponse = new JSONObject();
        try {
            System.out.println("Received drop course request for user: " + username + " and course: " + courseID);

            JSONObject result = courseService.dropCourse(username, courseID);
            if (result.has("status") && "success".equals(result.getString("status"))) {
                jsonResponse.put("status", "success").put("message", "Course dropped successfully");
            } else {
                jsonResponse.put("status", "fail").put("message", result.optString("message", "Failed to drop course"));
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error").put("message", "An error occurred while dropping the course.");
            e.printStackTrace(); // Log the exception for debugging purposes
        }

        return jsonResponse.toString();
    }
}
