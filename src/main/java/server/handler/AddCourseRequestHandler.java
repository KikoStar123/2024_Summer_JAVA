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
        String courseRoom = parameters.getString("courseRoom");
        String courseType = parameters.getString("courseType");

        // 调用 addCourse 方法，并接收返回的 JSONObject
        JSONObject response = courseService.addCourse(courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity, courseRoom, courseType);

        // 返回 response 对象
        return response.toString();
    }
}
