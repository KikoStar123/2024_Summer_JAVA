package server.handler;

import org.json.JSONObject;
import server.service.CourseService;
/**
 * 处理查看课程详细信息的请求。
 */
public class ViewCourseInfoRequestHandler implements RequestHandler {
    /**
     * 处理查看指定课程信息的请求。
     *
     * @param parameters 请求参数，包括课程 ID
     * @return JSON 格式的响应，包含课程信息或失败消息
     */
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String courseID = parameters.getString("courseID");

        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject courseInfo = courseService.getCourseInfo(courseID);
            if (courseInfo != null) {
                jsonResponse.put("status", "success").put("courseInfo", courseInfo);
            } else {
                jsonResponse.put("status", "fail").put("message", "Course not found");
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error").put("message", "An error occurred while retrieving the course information.");
            e.printStackTrace(); // Log the exception for debugging purposes
        }

        return jsonResponse.toString();
    }
}
