package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class ViewEnrolledCoursesRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String username = parameters.getString("username");

        JSONObject jsonResponse = new JSONObject();
        try {
            JSONObject coursesJson = courseService.getEnrolledCourses(username);
            if (coursesJson != null && coursesJson.length() > 0) {
                jsonResponse.put("status", "success").put("courses", coursesJson.getJSONArray("courses"));
            } else {
                jsonResponse.put("status", "fail").put("message", "No courses found for the student.");
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error").put("message", "An error occurred while retrieving the courses.");
            e.printStackTrace(); // Log the exception for debugging purposes
        }

        return jsonResponse.toString();
    }
}
