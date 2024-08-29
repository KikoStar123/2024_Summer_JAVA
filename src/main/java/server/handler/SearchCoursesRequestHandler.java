package server.handler;


import org.json.JSONObject;
import server.service.CourseService;

public class SearchCoursesRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();
        String courseName = parameters.optString("courseName", null);
        String courseTeacher = parameters.optString("courseTeacher", null);

        JSONObject result = courseService.searchCourses(courseName, courseTeacher);

        return result != null ? result.toString() : "{\"status\":\"fail\",\"message\":\"No courses found.\"}";
    }
}
