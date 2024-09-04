package server.handler;

import server.service.BankService;
import org.json.JSONObject;

public class UpdatePwdRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        String oldPwd = parameters.getString("oldPwd");
        String newPwd = parameters.getString("newPwd");
        JSONObject result = bankService.updatePwd(username, oldPwd, newPwd);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}