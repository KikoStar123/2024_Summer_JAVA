package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * 处理根据用户名搜索用户的请求。
 * 该类负责接收用户名并调用 BankService 搜索用户。
 */
public class SearchUserRequestHandler implements RequestHandler {
    /**
     * 处理根据用户名搜索用户的请求。
     *
     * @param parameters 包含用户名的 JSON 对象
     * @return 返回包含搜索结果的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        JSONObject result = bankService.searchByUsername(username);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("data", result.getJSONObject("data"));
        return jsonResponse.toString();
    }
}
