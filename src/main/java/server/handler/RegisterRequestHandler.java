package server.handler;

import org.json.JSONObject;
import server.service.UserService;
/**
 * 处理用户注册请求的处理器类。
 * 该类负责调用 UserService 中的注册方法，处理客户端发来的注册请求。
 */
public class RegisterRequestHandler implements RequestHandler{
    /**
     * 处理用户注册请求。
     *
     * @param parameters 包含用户注册信息的 JSON 对象
     * @return 返回注册结果的 JSON 字符串，包含成功或失败的状态和相应的消息
     */
    @Override
    public String handle(JSONObject parameters) {
        UserService userService = new UserService();
        System.out.println("111111");
        JSONObject jsonResponse = userService.register(parameters);
        System.out.println("222222");
        if (jsonResponse != null) {
            jsonResponse.put("status", "success").put("message", "Register successfully");
        } else {
            jsonResponse.put("status", "fail").put("message", "Failed to register");
        }

        return jsonResponse.toString();
    }
}
