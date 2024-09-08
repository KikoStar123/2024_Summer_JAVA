package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class UpdateInterestRateRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String type = parameters.getString("type");
        double newRate = parameters.getDouble("newRate");
        JSONObject result = bankService.updateInterestRate(type, newRate);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
