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
        System.out.println(result.toString());
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        if(jsonResponse.getString("status").equals("success")){
            jsonResponse.put("data", result.getJSONObject("data"));
        }
        return jsonResponse.toString();
    }
}
