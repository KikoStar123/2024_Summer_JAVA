package testhandler.shop;

import java.io.*;
import java.net.Socket;

public class ShopTest {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 8080; // 服务器端口

    public static void main(String[] args) {
        try {
            // 添加商品
            sendAddProductRequest("P12345", "Laptop", "High performance laptop", "base64encodedImageString", 1200.99f, 999.99f, 100, "Warehouse A", 4.7f, true);

            System.out.println('\n');

            // 获取商品详情
            sendGetProductDetailsRequest("P12345");

            System.out.println('\n');

            // 获取所有商品
            sendGetAllProductsRequest();

            System.out.println('\n');

            // 添加商品到购物车
            sendAddToCartRequest("200000001", "P12345", 2);

            System.out.println('\n');

            // 创建订单
            sendCreateOrderRequest("200000001", "P12345", 2, 1999.98f);

            System.out.println('\n');

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 添加商品
    private static void sendAddProductRequest(String productID, String productName, String productDetail, String productImage, float productOriginalPrice, float productCurrentPrice, int productInventory, String productAddress, float productCommentRate, boolean productStatus) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String addProductRequest = createAddProductRequest(productID, productName, productDetail, productImage, productOriginalPrice, productCurrentPrice, productInventory, productAddress, productCommentRate, productStatus);
            System.out.println("Sending add product request: " + addProductRequest);
            out.println(addProductRequest);
            String addProductResponse = in.readLine();
            System.out.println("Server response: " + addProductResponse);
        }
    }

    // 获取商品详情
    private static void sendGetProductDetailsRequest(String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String getProductDetailsRequest = createGetProductDetailsRequest(productID);
            System.out.println("Sending get product details request: " + getProductDetailsRequest);
            out.println(getProductDetailsRequest);
            String getProductDetailsResponse = in.readLine();
            System.out.println("Server response: " + getProductDetailsResponse);
        }
    }

    // 获取所有商品
    private static void sendGetAllProductsRequest() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String getAllProductsRequest = createGetAllProductsRequest();
            System.out.println("Sending get all products request: " + getAllProductsRequest);
            out.println(getAllProductsRequest);
            String getAllProductsResponse = in.readLine();
            System.out.println("Server response: " + getAllProductsResponse);
        }
    }

    // 添加商品到购物车
    private static void sendAddToCartRequest(String username, String productID, int quantity) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String addToCartRequest = createAddToCartRequest(username, productID, quantity);
            System.out.println("Sending add to cart request: " + addToCartRequest);
            out.println(addToCartRequest);
            String addToCartResponse = in.readLine();
            System.out.println("Server response: " + addToCartResponse);
        }
    }

    // 创建订单
    private static void sendCreateOrderRequest(String username, String productID, int productNumber, float paidMoney) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String createOrderRequest = createCreateOrderRequest(username, productID, productNumber, paidMoney);
            System.out.println("Sending create order request: " + createOrderRequest);
            out.println(createOrderRequest);
            String createOrderResponse = in.readLine();
            System.out.println("Server response: " + createOrderResponse);
        }
    }

    // 构造添加商品请求
    private static String createAddProductRequest(String productID, String productName, String productDetail, String productImage, float productOriginalPrice, float productCurrentPrice, int productInventory, String productAddress, float productCommentRate, boolean productStatus) {
        return String.format("{\"requestType\": \"product\", \"parameters\": {\"action\": \"add\", \"productID\": \"%s\", \"productName\": \"%s\", \"productDetail\": \"%s\", \"productImage\": \"%s\", \"productOriginalPrice\": %.2f, \"productCurrentPrice\": %.2f, \"productInventory\": %d, \"productAddress\": \"%s\", \"productCommentRate\": %.2f, \"productStatus\": %b}}",
                productID, productName, productDetail, productImage, productOriginalPrice, productCurrentPrice, productInventory, productAddress, productCommentRate, productStatus);
    }

    // 构造获取商品详情请求
    private static String createGetProductDetailsRequest(String productID) {
        return String.format("{\"requestType\": \"product\", \"parameters\": {\"action\": \"getDetails\", \"productID\": \"%s\"}}", productID);
    }

    // 构造获取所有商品请求
    private static String createGetAllProductsRequest() {
        return "{\"requestType\": \"product\", \"parameters\": {\"action\": \"getAll\"}}";
    }

    // 构造添加商品到购物车请求
    private static String createAddToCartRequest(String username, String productID, int quantity) {
        return String.format("{\"requestType\": \"cart\", \"parameters\": {\"action\": \"add\", \"username\": \"%s\", \"productID\": \"%s\", \"quantity\": %d}}", username, productID, quantity);
    }

    // 构造创建订单请求
    private static String createCreateOrderRequest(String username, String productID, int productNumber, float paidMoney) {
        return String.format("{\"requestType\": \"order\", \"parameters\": {\"action\": \"create\", \"username\": \"%s\", \"productID\": \"%s\", \"productNumber\": %d, \"paidMoney\": %.2f}}", username, productID, productNumber, paidMoney);
    }
}
