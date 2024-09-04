package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class DepositRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        double amount = parameters.getDouble("amount");
        JSONObject result = bankService.deposit(username, amount);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}

