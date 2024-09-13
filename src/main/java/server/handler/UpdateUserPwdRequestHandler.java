package server.handler;

import server.service.UserService;
import org.json.JSONObject;
/**
 * 处理用户更新密码的请求。
 */
public class UpdateUserPwdRequestHandler implements RequestHandler {
    /**
     * 处理更新用户系统密码的请求。
     *
     * @param parameters 请求参数，包括用户名、旧密码和新密码
     * @return JSON 格式的响应，包含操作的状态和消息
     */
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
