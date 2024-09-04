package server.handler;

import org.json.JSONObject;
import server.service.ShoppingCartService;

public class CartRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        ShoppingCartService cartService = new ShoppingCartService();
        String username = parameters.optString("username", null);
        JSONObject response = new JSONObject();

        // 检查是否提供了用户名
        if (username == null || username.isEmpty()) {
            return response.put("status", "fail").put("message", "Missing or empty username").toString();
        }

        // 获取操作类型
        String action = parameters.optString("action", null);

        if (action != null) {
            String productID = parameters.optString("productID", null);
            int quantity = parameters.optInt("quantity", 0);

            switch (action) {
                case "add":
                    if (productID != null && quantity > 0) {
                        boolean addSuccess = cartService.addToCart(username, productID, quantity);
                        response.put("status", addSuccess ? "success" : "fail");
                    } else {
                        response.put("status", "fail").put("message", "Invalid productID or quantity");
                    }
                    break;
                case "update":
                    if (productID != null && quantity > 0) {
                        boolean updateSuccess = cartService.updateCart(username, productID, quantity);
                        response.put("status", updateSuccess ? "success" : "fail");
                    } else {
                        response.put("status", "fail").put("message", "Invalid productID or quantity");
                    }
                    break;
                case "remove":
                    if (productID != null) {
                        boolean removeSuccess = cartService.removeFromCart(username, productID);
                        response.put("status", removeSuccess ? "success" : "fail");
                    } else {
                        response.put("status", "fail").put("message", "Invalid productID");
                    }
                    break;
                default:
                    response.put("status", "fail").put("message", "Unknown action");
                    break;
            }
        } else {
            response = cartService.getShoppingCart(username);
        }

        return response.toString();
    }
}
