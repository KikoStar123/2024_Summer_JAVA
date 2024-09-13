package server.handler;

import org.json.JSONObject;
import server.service.ShoppingMapService;
/**
 * 处理商店地图相关请求。
 * 根据不同的操作类型处理添加、删除、更新或查询地图记录的操作。
 */
public class ShoppingMapRequestHandler implements RequestHandler {
    /**
     * 处理地图记录相关请求。
     *
     * @param parameters 包含地图记录相关操作的 JSON 对象
     * @return 返回处理结果的 JSON 字符串
     */
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

            case "getRecordByProductID":  // 根据productID获取记录
                String queryProductID = parameters.optString("productID", null);

                if (queryProductID == null) {
                    response.put("status", "fail").put("message", "缺少产品ID");
                } else {
                    response = mapService.getMapRecordByProductID(queryProductID);
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
