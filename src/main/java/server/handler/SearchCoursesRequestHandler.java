package server.handler;

import org.json.JSONObject;
import server.service.CourseService;
/**
 * 处理课程搜索请求。
 * 该类根据课程名或教师名搜索课程。
 */
public class SearchCoursesRequestHandler implements RequestHandler {
    /**
     * 处理课程搜索请求。
     *
     * @param parameters 包含搜索条件的 JSON 对象，课程名和教师名可选
     * @return 返回包含课程搜索结果的 JSON 字符串
     */
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
