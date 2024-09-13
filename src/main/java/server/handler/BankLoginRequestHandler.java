package server.handler;

import server.service.BankService;
import org.json.JSONObject;

/**
 * BankLoginRequestHandler 处理银行用户登录的请求。
 * 调用 BankService 进行用户验证，并返回处理结果。
 */
public class BankLoginRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        String username = parameters.getString("username");
        String bankpwd = parameters.getString("bankpwd");
        JSONObject result = bankService.bankLogin(username, bankpwd);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
