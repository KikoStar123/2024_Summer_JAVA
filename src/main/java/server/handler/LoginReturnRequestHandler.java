package server.handler;

import org.json.JSONObject;
import server.service.UserService;

public class LoginReturnRequestHandler implements RequestHandler {

    private final UserService userService;

    public LoginReturnRequestHandler() {
        this.userService = new UserService(); // 实例化 UserService
    }

    @Override
    public String handle(JSONObject parameters) {
        String username = parameters.getString("username");
        String password = parameters.getString("password");

        // 使用 UserService 进行身份验证，并获取用户信息
        JSONObject userJson = userService.loginReturn(username, password);
        if (userJson != null) {
            // 如果验证成功，返回包含用户信息的 JSON 对象
            JSONObject jsonResponse = new JSONObject()
                    .put("status", "success")
                    .put("user", userJson);
            return jsonResponse.toString();
        } else {
            // 登录失败
            return new JSONObject()
                    .put("status", "failure")
                    .put("message", "Invalid username or password")
                    .toString();
        }
    }
}