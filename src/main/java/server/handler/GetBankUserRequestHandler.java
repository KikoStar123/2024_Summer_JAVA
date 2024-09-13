package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * 处理获取所有图书馆借阅记录的请求。
 * 调用 LibraryService 获取所有借阅记录。
 */
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
