package server.handler;

import server.service.UserService;
import org.json.JSONObject;

public class LoginRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        UserService userService = new UserService();
        boolean success = userService.login(parameters.getString("username"), parameters.getString("password"));

        JSONObject jsonResponse = new JSONObject();
        if (success) {
            jsonResponse.put("status", "success").put("message", "Login successful");
        } else {
            jsonResponse.put("status", "fail").put("message", "Invalid credentials");
        }
        return jsonResponse.toString();
    }
}
