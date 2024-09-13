package server.handler;

import server.service.BankService;
import org.json.JSONObject;
/**
 * 处理模拟年末的请求。
 * 该类负责调用 BankService 模拟年末处理银行业务。
 */
public class SimulateMonthEndRequestHandler implements RequestHandler {
    /**
     * 处理模拟年末的请求。
     *
     * @param parameters 请求参数的 JSON 对象
     * @return 返回处理结果的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        BankService bankService = BankService.getInstance();
        JSONObject result = bankService.simulateMonthEnd();

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
