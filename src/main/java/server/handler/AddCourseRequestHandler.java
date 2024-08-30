package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class AddCourseRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String courseID = parameters.getString("courseID");
        String courseName = parameters.getString("courseName");
        String courseTeacher = parameters.getString("courseTeacher");
        int courseCredits = parameters.getInt("courseCredits");
        String courseTime = parameters.getString("courseTime");
        int courseCapacity = parameters.getInt("courseCapacity");

        boolean success = courseService.addCourse(courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity);

        JSONObject jsonResponse = new JSONObject();
        if (success) {
            jsonResponse.put("status", "success").put("message", "Course added successfully");
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to add course");
        }
        return jsonResponse.toString();
    }
}
