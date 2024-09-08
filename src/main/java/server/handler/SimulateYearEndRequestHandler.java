package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class SimulateYearEndRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        JSONObject result = bankService.simulateYearEnd();

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
