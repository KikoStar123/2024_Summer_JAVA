package server.handler;

import server.service.UserService;
import org.json.JSONObject;

public class UpdateUserPwdRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        UserService userService = new UserService();
        String username = parameters.getString("username");
        String oldPwd = parameters.getString("oldPwd");
        String newPwd = parameters.getString("newPwd");
        System.out.println(parameters.toString());//
        JSONObject result = userService.updateUserPwd(username, oldPwd, newPwd);
        System.out.println(result.toString());//
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
