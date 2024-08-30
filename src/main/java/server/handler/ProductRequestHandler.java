package server.handler;

import org.json.JSONObject;
import server.service.ShoppingProductService;

public class ProductRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        ShoppingProductService productService = new ShoppingProductService();
        JSONObject response = new JSONObject();

        if (parameters.has("productID")) {
            response = productService.getProductDetails(parameters.getString("productID"));
        } else {
            response = productService.getAllProducts();
        }

        return response.toString();
    }
}
