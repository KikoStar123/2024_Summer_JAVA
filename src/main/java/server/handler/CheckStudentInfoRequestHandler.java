package server.handler;

import server.service.StudentInformationService;
import org.json.JSONObject;

public class CheckStudentInfoRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        StudentInformationService studentService = new StudentInformationService();
        JSONObject studentInfo = studentService.checkStudentInfo();

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success").put("data", studentInfo);
        return jsonResponse.toString();
    }
}
