package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * 处理更新银行利率的请求。
 */
public class UpdateInterestRateRequestHandler implements RequestHandler {
    /**
     * 处理更新银行利率的请求。
     *
     * @param parameters 请求参数的 JSON 对象，包含利率类型和新的利率值
     * @return 返回包含操作结果的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String type = parameters.getString("type");
        double newRate = parameters.getDouble("newRate");
        JSONObject result = bankService.updateInterestRate(type, newRate);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
