package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * BankRegisterRequestHandler 处理银行用户注册的请求。
 * 调用 BankService 进行用户注册，并返回处理结果。
 */
public class BankRegisterRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        String bankpwd = parameters.getString("bankpwd");
        JSONObject result = bankService.bankRegister(username, bankpwd);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
