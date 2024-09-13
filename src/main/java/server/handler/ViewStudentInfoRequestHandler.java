package server.handler;

import server.service.StudentInformationService;
import org.json.JSONObject;
/**
 * 处理查看学生信息的请求。
 */
public class ViewStudentInfoRequestHandler implements RequestHandler {
    /**
     * 处理查看学生信息的请求。
     *
     * @param parameters 请求参数，包括学生 ID
     * @return JSON 格式的响应，包含学生信息
     */
    @Override
    public String handle(JSONObject parameters) {
        StudentInformationService studentService = new StudentInformationService();
        JSONObject studentInfo = studentService.viewStudentInfo(parameters.getString("id"));

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success").put("data", studentInfo);
        return jsonResponse.toString();
    }
}
