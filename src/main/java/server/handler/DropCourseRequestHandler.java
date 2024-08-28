package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class DropCourseRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String studentId = parameters.getString("studentId");
        String courseId = parameters.getString("courseId");

        boolean success = courseService.dropCourse(studentId, courseId);

        JSONObject jsonResponse = new JSONObject();
        if (success) {
            jsonResponse.put("status", "success").put("message", "Course dropped successfully");
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to drop course");
        }
        return jsonResponse.toString();
    }
}
