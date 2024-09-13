package server.handler;

import org.json.JSONObject;
import server.service.StoreService;
/**
 * 处理商店相关的请求，如添加、更新、删除商店或查询商店信息。
 */
public class StoreRequestHandler implements RequestHandler {
    /**
     * 处理商店相关的请求。
     *
     * @param parameters 请求参数的 JSON 对象，包含商店的相关操作和信息
     * @return 包含操作结果的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        StoreService storeService = new StoreService();
        JSONObject response = new JSONObject();

        // 获取操作类型
        String action = parameters.optString("action");

        switch (action) {
            case "add": // 添加商店
                response = handleAddStore(parameters, storeService);
                break;

            case "update": // 更新商店
                response = handleUpdateStore(parameters, storeService);
                break;

            case "delete": // 删除商店
                String storeID = parameters.optString("storeID");
                if (!storeID.isEmpty()) {
                    boolean success = storeService.deleteStore(storeID);
                    response.put("status", success ? "success" : "fail");
                } else {
                    response.put("status", "fail").put("message", "Missing or empty storeID");
                }
                break;

            case "getStore": // 获取商店详情
                storeID = parameters.optString("storeID");
                if (!storeID.isEmpty()) {
                    response = storeService.getStore(storeID);
                } else {
                    response.put("status", "fail").put("message", "Missing or empty storeID");
                }
                break;

            case "getAll": // 获取所有商店信息
                response = storeService.getAllStores();
                break;

            case "getStoreIDByUsername": // 根据用户名获取商店ID
                String username = parameters.optString("username");
                if (!username.isEmpty()) {
                    String storeIDByUsername = storeService.getStoreIDByUsername(username);
                    if (storeIDByUsername != null) {
                        response.put("status", "success").put("storeID", storeIDByUsername);
                    } else {
                        response.put("status", "fail").put("message", "Store not found for the given username");
                    }
                } else {
                    response.put("status", "fail").put("message", "Missing or empty username");
                }
                break;

            default:
                response.put("status", "fail").put("message", "Unknown action");
                break;
        }

        return response.toString();
    }

    /**
     * 辅助方法，用于添加商店。
     *
     * @param parameters 请求参数的 JSON 对象
     * @param storeService 商店服务实例
     * @return 包含添加结果的 JSON 对象
     */
    private JSONObject handleAddStore(JSONObject parameters, StoreService storeService) {
        JSONObject response = new JSONObject();
        try {
            String storeID = parameters.getString("storeID");
            String storeName = parameters.getString("storeName");
            String storePhone = parameters.getString("storePhone");
            float storeRate = (float) parameters.getDouble("storeRate");
            boolean storeStatus = parameters.getBoolean("storeStatus");

            boolean success = storeService.addStore(storeID, storeName, storePhone, storeRate, storeStatus);
            response.put("status", success ? "success" : "fail");
        } catch (Exception e) {
            response.put("status", "fail").put("message", "Error adding store: " + e.getMessage());
        }
        return response;
    }

    /**
     * 辅助方法，用于更新商店信息。
     *
     * @param parameters 请求参数的 JSON 对象
     * @param storeService 商店服务实例
     * @return 包含更新结果的 JSON 对象
     */
    private JSONObject handleUpdateStore(JSONObject parameters, StoreService storeService) {
        JSONObject response = new JSONObject();
        try {
            String storeID = parameters.getString("storeID");
            String storeName = parameters.getString("storeName");
            String storePhone = parameters.getString("storePhone");
            float storeRate = (float) parameters.getDouble("storeRate");
            boolean storeStatus = parameters.getBoolean("storeStatus");

            boolean success = storeService.updateStore(storeID, storeName, storePhone, storeRate, storeStatus);
            response.put("status", success ? "success" : "fail");
        } catch (Exception e) {
            response.put("status", "fail").put("message", "Error updating store: " + e.getMessage());
        }
        return response;
    }
}
