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
            case "getDetails"://查看单个商品详细信息，已实现
                String productID = parameters.getString("productID");
                response = productService.getProductDetails(productID);
                break;

            case "getAll"://查看所有商品详细信息，已实现
                // 获取排序参数
                String sortBy = parameters.optString("sortBy", "price");  // 默认按价格排序，price-价格排序，rate-好评率排序
                String sortOrder = parameters.optString("sortOrder", "ASC");  // 默认升序排序，ASC-升序，DESC-降序
                response = productService.getAllProducts(sortBy, sortOrder);
                break;

            case "add"://添加商品，已实现
                response.put("status", addProduct(parameters, productService) ? "success" : "fail");
                break;

            case "delete"://删除商品，已实现
                productID = parameters.getString("productID");
                response.put("status", productService.deleteProduct(productID) ? "success" : "fail");
                break;

            case "enableProduct"://上架商品，已实现
                productID = parameters.getString("productID");
                response.put("status", productService.enableProduct(productID) ? "success" : "fail");
                break;

            case "disableProduct"://下架商品，已实现
                productID = parameters.getString("productID");
                response.put("status", productService.disableProduct(productID) ? "success" : "fail");
                break;


            case "getSameCategory"://获取所有同类商品，已实现
                productID = parameters.getString("productID");
                response = productService.getSameCategoryProducts(productID);
                break;

            case "search"://支持带排序的搜索，已实现
                String searchTerm = parameters.optString("searchTerm");
                sortBy = parameters.optString("sortBy", "price");  // 默认按价格排序，price-价格排序，rate-好评率排序
                sortOrder = parameters.optString("sortOrder", "ASC");  // 默认升序排序，ASC-升序，DESC-降序
                response = productService.searchProducts(searchTerm, sortBy, sortOrder);
                break;

            case "updateOriginalPrice":// 更新商品原价，已实现
                productID = parameters.getString("productID");
                float newOriginalPrice = (float) parameters.getDouble("newOriginalPrice");
                response.put("status", productService.updateProductOriginalPrice(productID, newOriginalPrice) ? "success" : "fail");
                break;

            case "updateCurrentPrice":// 更新商品现价，已实现
                productID = parameters.getString("productID");
                float newCurrentPrice = (float) parameters.getDouble("newCurrentPrice");
                response.put("status", productService.updateProductCurrentPrice(productID, newCurrentPrice) ? "success" : "fail");
                break;

            case "increaseInventory"://增加库存，已实现
                productID = parameters.getString("productID");
                int increaseAmount = parameters.getInt("amount");
                response.put("status", productService.increaseProductInventory(productID, increaseAmount) ? "success" : "fail");
                break;

            case "decreaseInventory"://减少库存，已实现
                productID = parameters.getString("productID");
                int decreaseAmount = parameters.getInt("amount");
                response.put("status", productService.decreaseProductInventory(productID, decreaseAmount) ? "success" : "fail");
                break;

            case "getProductComments"://查询商品的评论（商品详情页）---态度参数可以为null，需添加
                productID = parameters.getString("productID");
                Integer commentAttitude = parameters.has("commentAttitude") ? parameters.getInt("commentAttitude") : null;
                response = productService.getProductComments(productID, commentAttitude);//id+评论态度
                break;

            case "getAllProductComments": //查看所有商品的评论（管理员），已实现
                response = productService.getAllProductComments();
                break;

            case "searchProductComments"://查询某个用户对某个商品的评论，需添加
                String username = parameters.optString("username");
                productID = parameters.optString("productID");
                response = productService.searchProductComments(username, productID);
                break;

            case "addComment"://添加评论，需添加
                username = parameters.getString("username");
                productID = parameters.getString("productID");
                commentAttitude = parameters.getInt("commentAttitude");
                String commentContent = parameters.getString("commentContent");
                response.put("status", productService.addComment(username, productID, commentAttitude, commentContent) ? "success" : "fail");
                break;

            default://默认无操作
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
        byte[] productImage = parameters.getString("productImage").getBytes();  // 假设图片以base64字符串形式发送
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
