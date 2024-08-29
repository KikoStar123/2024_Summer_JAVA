package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class ViewCourseInfoRequestHandler implements RequestHandler {

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
