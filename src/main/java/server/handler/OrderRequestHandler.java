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
            case "create": // 添加订单，已实现
                response = handleCreateOrder(parameters, orderService);
                break;

            case "getDetails": // 用订单ID获取订单详情，已实现
                String orderID = parameters.optString("orderID");
                if (orderID != null && !orderID.isEmpty()) {
                    response = orderService.getOrderDetails(orderID);
                } else {
                    response.put("status", "fail").put("message", "Missing or empty orderID");
                }
                break;

            case "getAllOrdersByUser": // 查询特定用户的所有订单，已实现
                String username = parameters.getString("username");
                response = orderService.getAllOrdersByUser(username);
                break;

            case "searchOrdersByUser": // 按照关键词搜索特定用户的订单，已实现
                username = parameters.getString("username");
                String searchTerm = parameters.optString("searchTerm");
                response = orderService.searchOrdersByUser(username, searchTerm);
                break;

            case "searchOrdersByKeyword": // 在所有订单中搜索关键词
                searchTerm = parameters.optString("searchTerm");
                response = orderService.searchOrdersByKeyword(searchTerm);
                break;

            case "getAllOrders": // 获取所有用户的所有订单
                response = orderService.getAllOrders();
                break;

            case "updateCommentStatus": // 更新是否评论状态
                response = handleUpdateCommentStatus(parameters, orderService);
                break;

            case "getOrderCommentStatus": // 获取订单是否评论的状态
                orderID = parameters.getString("orderID");
                response = orderService.getOrderCommentStatus(orderID);
                break;

            case "pay": // 支付订单
                orderID = parameters.getString("orderID");
                double amount = parameters.getDouble("amount");
                response = orderService.payOrder(orderID, amount);
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
            String productName = parameters.getString("productName"); // 新增 productName 字段
            int productNumber = parameters.getInt("productNumber");
            float paidMoney = (float) parameters.getDouble("paidMoney");

            // 调用 createOrder 并获取 JSON 响应
            JSONObject createOrderResponse = orderService.createOrder(username, productID, productName, productNumber, paidMoney); // 修改为包含 productName
            // 将订单创建的响应返回给客户端
            response.put("status", createOrderResponse.getString("status"));

            // 如果订单创建成功，返回 orderID
            if (createOrderResponse.has("orderID")) {
                response.put("orderID", createOrderResponse.getString("orderID"));
            }
            if (createOrderResponse.has("message")) {
                response.put("message", createOrderResponse.getString("message"));
            }

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
