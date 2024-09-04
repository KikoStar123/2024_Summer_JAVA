package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class SearchUserRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        JSONObject result = bankService.searchByUsername(username);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("data", result.getJSONObject("data"));
        return jsonResponse.toString();
    }
}