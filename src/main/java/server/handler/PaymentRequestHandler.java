package server.handler;

import org.json.JSONObject;
import server.service.BankService;
/**
 * 处理支付请求的类。负责处理客户端发来的支付请求并调用对应的服务进行处理。
 */
public class PaymentRequestHandler implements RequestHandler {
    /**
     * 处理支付请求。
     *
     * @param parameters 包含支付相关参数的 JSON 对象。包括 orderID, username, bankpwd, amount。
     * @return 包含支付结果的 JSON 响应字符串，表示成功或失败状态。
     */
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String orderID = parameters.getString("orderID");
        String username = parameters.getString("username");
        String bankpwd = parameters.getString("bankpwd");
        double amount = parameters.getDouble("amount");
        JSONObject result = bankService.processPayment(orderID, username, bankpwd, amount);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
