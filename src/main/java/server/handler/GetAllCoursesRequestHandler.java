package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

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
