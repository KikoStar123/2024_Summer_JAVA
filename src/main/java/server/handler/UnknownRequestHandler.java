package server.handler;

import org.json.JSONObject;
/**
 * 处理未知请求类型的处理器。
 * 当请求类型不明时返回错误消息。
 */
public class UnknownRequestHandler implements RequestHandler {
    /**
     * 处理未知请求类型。
     *
     * @param parameters 请求参数的 JSON 对象
     * @return 包含错误信息的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        JSONObject response = new JSONObject();
        try {
            response.put("status", "error");
            response.put("message", "Unknown request type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}