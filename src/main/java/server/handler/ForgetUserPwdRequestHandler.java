package server.handler;

import server.service.UserService;
import org.json.JSONObject;

public class ForgetUserPwdRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        UserService userService = new UserService();
        String username = parameters.getString("username");
        String newPwd = parameters.getString("newPwd");
        JSONObject result = userService.forgetPwd(username, newPwd);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
