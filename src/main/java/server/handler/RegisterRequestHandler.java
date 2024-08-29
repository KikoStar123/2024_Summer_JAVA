package server.handler;

import org.json.JSONObject;
import server.service.UserService;

public class RegisterRequestHandler implements RequestHandler{
    @Override
    public String handle(JSONObject parameters) {
        UserService userService = new UserService();

        JSONObject jsonResponse = userService.register(parameters);

        if (jsonResponse != null) {
            jsonResponse.put("status", "success").put("message", "Register successfully");
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to register");
        }

        return jsonResponse.toString();
    }
}
