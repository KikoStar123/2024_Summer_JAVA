package client.service;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class Shop {
    private String serverAddress;
    private int serverPort;

    public Shop(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    // 发送请求并接收响应的通用方法
    private String sendRequestReceiveResponse(String requestType, JSONObject parameters) {
        Socket socket = null;
        try {
            socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", requestType);
            request.put("parameters", parameters);

            // 发送请求
            out.println(request.toString());

            // 接收响应
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 商品浏览与搜索
    public String browseProducts(String searchQuery) {
        JSONObject parameters = new JSONObject();
        parameters.put("searchQuery", searchQuery);
        return sendRequestReceiveResponse("browseProducts", parameters);
    }

    // 购物车管理
    public String addToCart(String productId, int quantity) {
        JSONObject parameters = new JSONObject();
        parameters.put("productId", productId);
        parameters.put("quantity", quantity);
        return sendRequestReceiveResponse("addToCart", parameters);
    }
//update the cart
    public String updateCart(String cartDetails) {
        JSONObject parameters = new JSONObject();
        parameters.put("cartDetails", cartDetails);
        return sendRequestReceiveResponse("updateCart", parameters);
    }

    public String removeFromCart(String productId) {
        JSONObject parameters = new JSONObject();
        parameters.put("productId", productId);
        return sendRequestReceiveResponse("removeFromCart", parameters);
    }

    // 订单处理
    public String createOrder(String cartId) {
        JSONObject parameters = new JSONObject();
        parameters.put("cartId", cartId);
        return sendRequestReceiveResponse("createOrder", parameters);
    }
    //获取用户订单
    public String getOrderHistory() {
        return sendRequestReceiveResponse("getOrderHistory", new JSONObject());
    }

    // 商品评论
    public String submitReview(String productId, String reviewContent, int rating) {
        JSONObject parameters = new JSONObject();
        parameters.put("productId", productId);
        parameters.put("reviewContent", reviewContent);
        parameters.put("rating", rating);
        return sendRequestReceiveResponse("submitReview", parameters);
    }

    public String viewReviews(String productId) {
        JSONObject parameters = new JSONObject();
        parameters.put("productId", productId);
        return sendRequestReceiveResponse("viewReviews", parameters);
    }

    // 其他学生相关方法...
}
