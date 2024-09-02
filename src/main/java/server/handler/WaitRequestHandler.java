package server.handler;

import org.json.JSONObject;
import server.service.BankService;

public class WaitRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String orderID = parameters.getString("orderID");
        double amount = parameters.getDouble("amount");
        JSONObject result = bankService.waitForPayment(orderID, amount);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
