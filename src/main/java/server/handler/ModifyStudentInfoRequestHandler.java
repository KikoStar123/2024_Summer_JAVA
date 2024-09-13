package server.handler;

import server.service.StudentInformationService;
import org.json.JSONObject;
/**
 * 处理修改学生信息的请求，调用 StudentInformationService 修改学生信息。
 */
public class ModifyStudentInfoRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        StudentInformationService studentService = new StudentInformationService();
        boolean success = studentService.modifyStudentInfo(parameters);

        JSONObject jsonResponse = new JSONObject();
        if (success) {
            jsonResponse.put("status", "success").put("message", "Student information updated successfully");
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to update student information");
        }
        return jsonResponse.toString();
    }
}
