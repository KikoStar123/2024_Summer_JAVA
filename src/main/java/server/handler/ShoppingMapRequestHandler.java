package server.handler;

import org.json.JSONObject;
import server.service.ShoppingMapService;

public class ShoppingMapRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        ShoppingMapService mapService = new ShoppingMapService();
        JSONObject response = new JSONObject();

        // 获取操作类型
        String action = parameters.optString("action", "getAllRecords");

        switch (action) {
            case "addRecord":  // 添加记录
                String productID = parameters.optString("productID", null);
                String mapStart = parameters.optString("mapStart", null);
                String mapEnd = parameters.optString("mapEnd", null);

                if (productID == null || mapStart == null || mapEnd == null) {
                    response.put("status", "fail").put("message", "缺少必要参数");
                } else {
                    response = mapService.addMapRecord(productID, mapStart, mapEnd);
                }
                break;

            case "deleteRecord":  // 删除记录
                String deleteProductID = parameters.optString("productID", null);

                if (deleteProductID == null) {
                    response.put("status", "fail").put("message", "缺少产品ID");
                } else {
                    response = mapService.deleteMapRecord(deleteProductID);
                }
                break;

            case "updateRecord":  // 更新记录
                String updateProductID = parameters.optString("productID", null);
                String newMapStart = parameters.optString("mapStart", null);
                String newMapEnd = parameters.optString("mapEnd", null);

                if (updateProductID == null || newMapStart == null || newMapEnd == null) {
                    response.put("status", "fail").put("message", "缺少必要参数");
                } else {
                    response = mapService.updateMapRecord(updateProductID, newMapStart, newMapEnd);
                }
                break;

            case "getAllRecords":  // 获取所有记录
            default:
                response = mapService.getAllMapRecords();
                break;
        }

        return response.toString();
    }
}
