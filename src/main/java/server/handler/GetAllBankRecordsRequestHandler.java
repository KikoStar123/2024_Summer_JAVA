package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * 处理用户忘记密码请求。
 * 调用 UserService 来修改用户密码。
 */
public class GetAllBankRecordsRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        JSONObject result = bankService.getAllBankRecords(username);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("records", result.getJSONArray("records"));
        return jsonResponse.toString();
    }
}
