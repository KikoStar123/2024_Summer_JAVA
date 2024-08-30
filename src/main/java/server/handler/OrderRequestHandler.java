package server.handler;

import org.json.JSONObject;
import server.service.ShoppingOrderService;

public class OrderRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        ShoppingOrderService orderService = new ShoppingOrderService();
        JSONObject response = new JSONObject();

        // 获取操作类型
        String action = parameters.optString("action");

        switch (action) {
            case "create":
                response = handleCreateOrder(parameters, orderService);
                break;

            case "getDetails":
                String orderID = parameters.optString("orderID");
                if (orderID != null && !orderID.isEmpty()) {
                    response = orderService.getOrderDetails(orderID);
                } else {
                    response.put("status", "fail").put("message", "Missing or empty orderID");
                }
                break;

            case "updateCommentStatus":
                response = handleUpdateCommentStatus(parameters, orderService);
                break;

            default:
                response.put("status", "fail").put("message", "Unknown action");
                break;
        }

        return response.toString();
    }

    // Helper method to handle order creation
    private JSONObject handleCreateOrder(JSONObject parameters, ShoppingOrderService orderService) {
        JSONObject response = new JSONObject();

        try {
            String username = parameters.getString("username");
            String productID = parameters.getString("productID");
            int productNumber = parameters.getInt("productNumber");
            float paidMoney = (float) parameters.getDouble("paidMoney");

            boolean success = orderService.createOrder(username, productID, productNumber, paidMoney);
            response.put("status", success ? "success" : "fail");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "Error creating order: " + e.getMessage());
        }

        return response;
    }

    // Helper method to handle updating the order comment status
    private JSONObject handleUpdateCommentStatus(JSONObject parameters, ShoppingOrderService orderService) {
        JSONObject response = new JSONObject();

        try {
            String orderID = parameters.getString("orderID");
            boolean whetherComment = parameters.getBoolean("whetherComment");

            boolean success = orderService.updateOrderCommentStatus(orderID, whetherComment);
            response.put("status", success ? "success" : "fail");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "fail").put("message", "Error updating comment status: " + e.getMessage());
        }

        return response;
    }
}
