package client.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 购物订单服务类，提供与订单相关的操作，包括创建订单、查看订单详情、查询用户订单、支付等功能。
 */
public class ShoppingOrder {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    /**
     * 内部类，用于表示订单信息
     */
    public static class oneOrder {
        String orderID;        // 订单号
        String username;       // 用户账号
        String productID;      // 商品ID
        String productName;    // 商品名称
        int productNumber;     // 商品数量
        boolean whetherComment;// 是否评价
        float paidMoney;       // 支付金额
        boolean paidStatus;    // 是否支付
        String storeID;        // 店铺ID

        // Getters
        public String getOrderID() {
            return orderID;
        }

        public String getUsername() {
            return username;
        }

        public String getProductID() {
            return productID;
        }

        public String productName() {
            return productName;
        }

        public int getProductNumber() {
            return productNumber;
        }

        public boolean isWhetherComment() {
            return whetherComment;
        }

        public float getPaidMoney() {
            return paidMoney;
        }

        public boolean getpaidStatus() {
            return paidStatus;
        }

        public String getStoreID() {
            return storeID;
        }
    }

    /**
     * 创建订单
     *
     * @param username      用户账号
     * @param productID     商品ID
     * @param productName   商品名称
     * @param productNumber 商品数量
     * @param paidMoney     支付金额
     * @return 订单ID
     * @throws IOException 如果发生IO异常
     */
    public static String createOrder(String username, String productID, String productName, int productNumber, float paidMoney) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "create")
                    .put("username", username)
                    .put("productID", productID)
                    .put("productName", productName)
                    .put("productNumber", productNumber)
                    .put("paidMoney", paidMoney));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("orderID");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查看订单详情
     *
     * @param orderID 订单ID
     * @return oneOrder 订单对象
     * @throws IOException 如果发生IO异常
     */
    public oneOrder getOrderDetails(String orderID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "getDetails")
                    .put("orderID", orderID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            oneOrder theOrder = new oneOrder();

            theOrder.orderID = jsonResponse.getString("orderID");
            theOrder.username = jsonResponse.getString("username");
            theOrder.productID = jsonResponse.getString("productID");
            theOrder.productName = jsonResponse.getString("productName");
            theOrder.productNumber = jsonResponse.getInt("productNumber");
            theOrder.whetherComment = jsonResponse.getBoolean("whetherComment");
            theOrder.paidMoney = jsonResponse.getFloat("paidMoney");
            theOrder.paidStatus = jsonResponse.getBoolean("paidStatus");
            theOrder.storeID = jsonResponse.getString("storeID");

            return theOrder;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询用户的所有订单
     *
     * @param username 用户账号
     * @return 订单数组
     * @throws IOException 如果发生IO异常
     */
    public oneOrder[] getAllOrdersByUser(String username) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "getAllOrdersByUser")
                    .put("username", username));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("orders");

            int numOrders = data.length();
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);
                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID = theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID = theOrder.getString("productID");
                ordersArray[i].productName = theOrder.getString("productName");
                ordersArray[i].productNumber = theOrder.getInt("productNumber");
                ordersArray[i].whetherComment = theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney = theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus = theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID = theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按关键词搜索用户订单
     *
     * @param username   用户账号
     * @param searchTerm 搜索关键词
     * @return 订单数组
     * @throws IOException 如果发生IO异常
     */
    public oneOrder[] searchOrdersByUser(String username, String searchTerm) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "searchOrdersByUser")
                    .put("username", username)
                    .put("searchTerm", searchTerm));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("orders");

            int numOrders = data.length();
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);
                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID = theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID = theOrder.getString("productID");
                ordersArray[i].productName = theOrder.getString("productName");
                ordersArray[i].productNumber = theOrder.getInt("productNumber");
                ordersArray[i].whetherComment = theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney = theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus = theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID = theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据搜索关键词查找所有订单
     *
     * @param searchTerm 搜索关键词
     * @return 订单数组
     * @throws IOException 如果发生IO异常
     */
    public oneOrder[] searchOrdersByKeyword(String searchTerm) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "searchOrdersByKeyword")
                    .put("searchTerm", searchTerm));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("orders");

            int numOrders = data.length();
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);
                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID = theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID = theOrder.getString("productID");
                ordersArray[i].productName = theOrder.getString("productName");
                ordersArray[i].productNumber = theOrder.getInt("productNumber");
                ordersArray[i].whetherComment = theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney = theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus = theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID = theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有用户的所有订单
     *
     * @return 订单数组
     * @throws IOException 如果发生IO异常
     */
    public oneOrder[] getAllOrders() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "getAllOrders"));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("orders");

            int numOrders = data.length();
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);
                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID = theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID = theOrder.getString("productID");
                ordersArray[i].productName = theOrder.getString("productName");
                ordersArray[i].productNumber = theOrder.getInt("productNumber");
                ordersArray[i].whetherComment = theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney = theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus = theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID = theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取订单的评论状态
     *
     * @param orderID 订单ID
     * @return 是否已评论
     * @throws IOException 如果发生IO异常
     */
    public static boolean getOrderCommentStatus(String orderID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "getOrderCommentStatus")
                    .put("orderID", orderID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getBoolean("whetherComment");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新订单评论状态
     *
     * @param orderID       订单ID
     * @param whetherComment 是否已评论
     * @return 更新是否成功
     * @throws IOException 如果发生IO异常
     */
    public static boolean updateCommentStatus(String orderID, boolean whetherComment) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "updateCommentStatus")
                    .put("orderID", orderID)
                    .put("whetherComment", whetherComment));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 支付订单
     *
     * @param orderIDs 订单ID数组
     * @param amount   支付金额
     * @return 支付是否成功
     * @throws IOException 如果发生IO异常
     */
    public static boolean payOrder(String[] orderIDs, float amount) throws IOException {
        System.out.println("payOrder: " + orderIDs[0]);
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "pay")
                    .put("orderIDs", orderIDs)
                    .put("amount", amount));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取商店的所有订单
     *
     * @param storeID 商店ID
     * @return 订单数组
     * @throws IOException 如果发生IO异常
     */
    public static oneOrder[] getAllOrdersByStore(String storeID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "getAllOrdersByStore")
                    .put("storeID", storeID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("orders");

            int numOrders = data.length();
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);
                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID = theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID = theOrder.getString("productID");
                ordersArray[i].productName = theOrder.getString("productName");
                ordersArray[i].productNumber = theOrder.getInt("productNumber");
                ordersArray[i].whetherComment = theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney = theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus = theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID = theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
