package server.handler;

import org.json.JSONObject;
import server.service.ShoppingProductService;

public class ProductRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        ShoppingProductService productService = new ShoppingProductService();
        JSONObject response = new JSONObject();

        // 获取操作类型
        String action = parameters.optString("action");

        switch (action) {
            case "getDetails":
                String productID = parameters.getString("productID");
                response = productService.getProductDetails(productID);
                break;

            case "getAll":
                response = productService.getAllProducts();
                break;

            case "add":
                response.put("status", addProduct(parameters, productService) ? "success" : "fail");
                break;

            case "delete":
                productID = parameters.getString("productID");
                response.put("status", productService.deleteProduct(productID) ? "success" : "fail");
                break;

            case "updateStatus":
                productID = parameters.getString("productID");
                boolean status = parameters.getBoolean("status");
                response.put("status", productService.updateProductStatus(productID, status) ? "success" : "fail");
                break;

            case "getSameCategory":
                productID = parameters.getString("productID");
                response = productService.getSameCategoryProducts(productID);
                break;

            case "search":
                String searchTerm = parameters.optString("searchTerm");
                response = productService.searchProducts(searchTerm);
                break;

            default:
                response.put("status", "fail").put("message", "Unknown action");
                break;
        }

        return response.toString();
    }

    // Helper method to add a product
    private boolean addProduct(JSONObject parameters, ShoppingProductService productService) {
        String productID = parameters.getString("productID");
        String productName = parameters.getString("productName");
        String productDetail = parameters.getString("productDetail");
        byte[] productImage = parameters.getString("productImage").getBytes();  // Assumes image is sent as base64 string
        float productOriginalPrice = (float) parameters.getDouble("productOriginalPrice");
        float productCurrentPrice = (float) parameters.getDouble("productCurrentPrice");
        int productInventory = parameters.getInt("productInventory");
        String productAddress = parameters.getString("productAddress");
        float productCommentRate = (float) parameters.getDouble("productCommentRate");
        boolean productStatus = parameters.getBoolean("productStatus");

        return productService.addProduct(productID, productName, productDetail, productImage, productOriginalPrice,
                productCurrentPrice, productInventory, productAddress, productCommentRate,
                productStatus);
    }
}
