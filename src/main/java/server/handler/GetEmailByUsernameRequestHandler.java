package server.handler;

import server.service.UserService;
import org.json.JSONObject;

public class GetEmailByUsernameRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        UserService userService = new UserService();
        String username = parameters.getString("username");
        JSONObject result = userService.getEmailByUsername(username);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));

        if (result.has("email")) {
            jsonResponse.put("email", result.getString("email"));
        } else {
            jsonResponse.put("email", JSONObject.NULL);
        }

        if (result.has("message")) {
            jsonResponse.put("message", result.getString("message"));
        } else {
            jsonResponse.put("message", JSONObject.NULL);
        }

        return jsonResponse.toString();
    }
}
