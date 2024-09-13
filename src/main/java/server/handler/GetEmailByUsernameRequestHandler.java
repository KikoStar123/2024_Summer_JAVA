package server.handler;

import server.service.UserService;
import org.json.JSONObject;
/**
 * 处理通过书籍ID获取书籍详情的请求。
 * 调用 LibraryService 来获取书籍详细信息。
 */
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
