package server.handler;

import org.json.JSONObject;
import server.service.ShoppingCartService;

public class CartRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        ShoppingCartService cartService = new ShoppingCartService();
        String username = parameters.getString("username");
        JSONObject response = new JSONObject();

        if (parameters.has("action")) {
            String action = parameters.getString("action");
            String productID = parameters.optString("productID", null);
            int quantity = parameters.optInt("quantity", 0);

            switch (action) {
                case "add":
                    boolean addSuccess = cartService.addToCart(username, productID, quantity);
                    response.put("status", addSuccess ? "success" : "fail");
                    break;
                case "update":
                    boolean updateSuccess = cartService.updateCart(username, productID, quantity);
                    response.put("status", updateSuccess ? "success" : "fail");
                    break;
                case "remove":
                    boolean removeSuccess = cartService.removeFromCart(username, productID);
                    response.put("status", removeSuccess ? "success" : "fail");
                    break;
                default:
                    response.put("status", "fail").put("message", "Unknown action");
            }
        } else {
            response = cartService.getShoppingCart(username);
        }

        return response.toString();
    }
}
