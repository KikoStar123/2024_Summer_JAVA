package server.handler;

import org.json.JSONObject;
import server.service.ShoppingOrderService;

public class OrderRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        ShoppingOrderService orderService = new ShoppingOrderService();

        String action = parameters.getString("action");

        if (action.equals("create")) {
            String username = parameters.getString("username");
            String productID = parameters.getString("productID");
            int productNumber = parameters.getInt("productNumber");
            float paidMoney = (float) parameters.getDouble("paidMoney");

            boolean success = orderService.createOrder(username, productID, productNumber, paidMoney);
            return new JSONObject().put("status", success ? "success" : "fail").toString();

        } else if (action.equals("getDetails")) {
            String orderID = parameters.getString("orderID");
            return orderService.getOrderDetails(orderID).toString();

        } else if (action.equals("updateCommentStatus")) {
            String orderID = parameters.getString("orderID");
            boolean whetherComment = parameters.getBoolean("whetherComment");

            boolean success = orderService.updateOrderCommentStatus(orderID, whetherComment);
            return new JSONObject().put("status", success ? "success" : "fail").toString();
        }

        return new JSONObject().put("status", "fail").toString();
    }
}
