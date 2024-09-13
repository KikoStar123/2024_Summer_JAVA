package server.handler;

import org.json.JSONObject;
/**
 * 请求处理器接口。
 * 所有的请求处理器类都需要实现此接口，以处理特定的客户端请求。
 */
public interface RequestHandler {
    /**
     * 处理客户端请求。
     *
     * @param parameters 包含请求信息的 JSON 对象
     * @return 返回处理结果的 JSON 字符串
     */
    String handle(JSONObject parameters);
}
