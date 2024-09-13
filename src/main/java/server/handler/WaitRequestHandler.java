package server.handler;

import org.json.JSONObject;
import server.service.BankService;
/**
 * 处理支付等待的请求。
 */
public class WaitRequestHandler implements RequestHandler {
    /**
     * 处理等待支付请求。
     *
     * @param parameters 请求参数，包括订单 ID 和金额
     * @return JSON 格式的响应，包含支付状态和消息
     */
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String orderID = parameters.getString("orderID");
        double amount = parameters.getDouble("amount");
        JSONObject result = bankService.waitForPayment(orderID, amount);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
