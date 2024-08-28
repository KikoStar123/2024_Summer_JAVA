package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class EnrollInCourseRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String username = parameters.getString("username");
        String courseID = parameters.getString("courseID");

        boolean success = courseService.enrollInCourse(username, courseID);

        JSONObject jsonResponse = new JSONObject();
        if (success) {
            jsonResponse.put("status", "success").put("message", "Student enrolled in course successfully");
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to enroll in course");
        }
        return jsonResponse.toString();
    }
}
