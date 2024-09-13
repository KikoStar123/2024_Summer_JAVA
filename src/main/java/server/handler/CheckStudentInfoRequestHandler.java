package server.handler;

import server.service.StudentInformationService;
import org.json.JSONObject;
/**
 * 处理检查学生信息的请求。
 */
public class CheckStudentInfoRequestHandler implements RequestHandler {
    /**
     * 处理检查所有学生信息的请求。
     *
     * @param parameters 请求参数，通常为空，因为此操作不需要传入参数
     * @return JSON 格式的响应，包含操作状态和所有学生信息
     */
    @Override
    public String handle(JSONObject parameters) {
        StudentInformationService studentService = new StudentInformationService();
        JSONObject studentInfo = studentService.checkStudentInfo();

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success").put("data", studentInfo);
        return jsonResponse.toString();
    }
}
