package server.handler;

import org.json.JSONObject;
import server.service.CourseService;

public class SearchCoursesRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        CourseService courseService = new CourseService();

        String courseName = parameters.optString("courseName", null);
        String courseTeacher = parameters.optString("courseTeacher", null);

        // 调用 searchCourses 方法，并传递课程名和教师名参数
        JSONObject searchResult = courseService.searchCourses(courseName, courseTeacher);

        JSONObject jsonResponse = new JSONObject();
        if (searchResult != null && searchResult.has("courses")) {
            jsonResponse.put("status", "success").put("courses", searchResult.getJSONArray("courses"));
        } else {
            jsonResponse.put("status", "fail").put("message", "No courses found matching the criteria.");
        }

        return jsonResponse.toString();
    }
}
