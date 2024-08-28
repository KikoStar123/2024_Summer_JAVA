package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class ViewEnrolledCoursesRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String studentId = parameters.getString("studentId");

        JSONObject coursesJson = courseService.viewEnrolledCourses(studentId);

        JSONObject jsonResponse = new JSONObject();
        if (coursesJson != null) {
            jsonResponse.put("status", "success").put("courses", coursesJson.getJSONArray("courses"));
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to retrieve enrolled courses");
        }
        return jsonResponse.toString();
    }
}
