package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * 处理获取利率的请求，调用 BankService 获取指定类型的利率。
 */
public class GetInterestRateRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String type = parameters.getString("type");
        JSONObject result = bankService.getInterestRate(type);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        jsonResponse.put("rate", result.getDouble("rate"));
        return jsonResponse.toString();
    }
}
