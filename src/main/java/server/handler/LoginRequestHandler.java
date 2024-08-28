package server.handler;

import server.service.UserService;
import org.json.JSONObject;
import client.service.User;
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
    public User login_return(JSONObject parameters) {
        UserService userService = new UserService();
        User user = userService.login_return(parameters.getString("username"), parameters.getString("password"));

        if (user != null) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "success")
                    .put("message", "Login successful");
                  //  .put("user", user.toJson()); // 假设User类有一个toJson方法来转换为JSON对象
            return user;
        } else {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "fail")
                    .put("message", "Invalid credentials");
            // 这里返回null，因为登录失败，没有用户信息可以返回
            return null;
        }
    }
}
