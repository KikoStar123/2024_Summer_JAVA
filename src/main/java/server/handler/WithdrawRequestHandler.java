package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class WithdrawRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = new BankService();
        String username = parameters.getString("username");
        double amount = parameters.getDouble("amount");
        JSONObject result = bankService.withdraw(username, amount);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
