package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class GetInterestRateRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String type = parameters.getString("type");
        JSONObject result = bankService.getInterestRate(type);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        jsonResponse.put("rate", result.getDouble("rate"));
        return jsonResponse.toString();
    }
}
