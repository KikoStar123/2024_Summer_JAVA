package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class ViewEnrolledCoursesRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String username = parameters.getString("username");

        JSONObject coursesJson = courseService.getCourseInfo(username);

        JSONObject jsonResponse = new JSONObject();
        if (coursesJson != null) {
            jsonResponse.put("status", "success").put("courses", coursesJson.getJSONArray("courses"));
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to retrieve enrolled courses");
        }
        return jsonResponse.toString();
    }
}
