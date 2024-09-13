package server.handler;
import org.json.JSONObject;
import server.service.UserService;

/**
 * 处理用户登录请求，调用 UserService 验证用户名和密码。
 */
public class LoginRequestHandler implements RequestHandler {

    private final UserService userService;

    public LoginRequestHandler() {
        this.userService = new UserService(); // 实例化 UserService
    }

    @Override
    public String handle(JSONObject parameters) {
        // 获取传入的用户名和密码
        String username = parameters.getString("username");
        String password = parameters.getString("password");

        // 在终端输出用户名和密码
        System.out.println("Received username: " + username);
        System.out.println("Received password: " + password);

        // 使用 UserService 进行身份验证
        if (userService.login(username, password)) {
            // 如果验证成功，返回成功信息
            System.out.println("Login successful for user: " + username);
            return new JSONObject()
                    .put("status", "success")
                    .put("username", username)
                    .put("message", "Login successful")
                    .toString();
        } else {
            // 登录失败
            System.out.println("Login failed for user: " + username);
            return new JSONObject()
                    .put("status", "failure")
                    .put("message", "Invalid username or password")
                    .toString();
        }
    }
}