package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * 处理检查学生信息的请求。
 * 调用 StudentInformationService 来获取学生信息。
 */
public class DepositRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        double amount = parameters.getDouble("amount");
        String depositType = parameters.getString("depositType");
        int term = parameters.getInt("term");
        JSONObject result = bankService.deposit(username, amount, depositType, term);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
