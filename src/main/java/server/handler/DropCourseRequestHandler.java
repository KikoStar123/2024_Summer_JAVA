package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class DropCourseRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String username = parameters.getString("username");
        String courseID = parameters.getString("courseID");

        //boolean success = courseService.dropCourse(username, courseID);
        JSONObject result = courseService.dropCourse(username, courseID);
        boolean success = "success".equals(result.getString("status"));


        JSONObject jsonResponse = new JSONObject();
        if (success) {
            jsonResponse.put("status", "success").put("message", "Course dropped successfully");
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to drop course");
        }
        return jsonResponse.toString();
    }
}
