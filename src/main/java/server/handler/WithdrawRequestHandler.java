package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * 处理用户提现的请求。
 */
public class WithdrawRequestHandler implements RequestHandler {
    /**
     * 处理用户提现请求。
     *
     * @param parameters 请求参数，包括用户名和提现金额
     * @return JSON 格式的响应，包含操作状态和消息
     */
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        double amount = parameters.getDouble("amount");
        JSONObject result = bankService.withdraw(username, amount);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
