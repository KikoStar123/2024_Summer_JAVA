package server.handler;

import org.json.JSONObject;
import server.service.StoreService;

public class StoreRequestHandler implements RequestHandler {

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

            default:
                response.put("status", "fail").put("message", "Unknown action");
                break;
        }

        return response.toString();
    }

    // Helper method to handle adding a store
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

    // Helper method to handle updating a store
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
