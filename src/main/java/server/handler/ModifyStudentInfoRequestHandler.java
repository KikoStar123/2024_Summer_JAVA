package server.handler;

import server.service.StudentInformationService;
import org.json.JSONObject;

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
