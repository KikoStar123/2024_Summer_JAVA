package server.handler;

import org.json.JSONObject;
import server.service.BankService;

public class PaymentRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String orderID = parameters.getString("orderID");
        String username = parameters.getString("username");
        String bankpwd = parameters.getString("bankpwd");
        double amount = parameters.getDouble("amount");
        JSONObject result = bankService.processPayment(orderID, username, bankpwd, amount);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
