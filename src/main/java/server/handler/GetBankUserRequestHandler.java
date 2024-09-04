package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class GetBankUserRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        String bankpwd = parameters.getString("bankpwd");
        JSONObject result = bankService.getBankUser(username, bankpwd);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("data", result.getJSONObject("data"));
        return jsonResponse.toString();
    }
}