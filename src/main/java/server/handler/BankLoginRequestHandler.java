package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class BankLoginRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = new BankService();
        String username = parameters.getString("username");
        String bankpwd = parameters.getString("bankpwd");
        JSONObject result = bankService.bankLogin(username, bankpwd);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}