package client.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 购物车服务类，提供添加、删除、更新购物车商品以及获取购物车信息等功能。
 */
public class ShoppingCart {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    /**
     * 购物车元素类，表示购物车中的一个商品项。
     */
    public static class oneCartElement {
        String productID;    // 商品ID
        int productNumber;   // 商品数量
        String storeID;      // 商店ID

        // Getter方法
        public String getProductID() {
            return productID;
        }

        public int getProductNumber() {
            return productNumber;
        }

        public String getStoreID() {
            return storeID;
        }
    }

    /**
     * 添加商品到购物车。
     *
     * @param username    用户账号
     * @param productID   商品ID
     * @param quantity    商品数量
     * @return 如果添加成功返回 true，否则返回 false
     * @throws IOException 如果出现IO异常
     */
    public static boolean AddToCart(String username, String productID, int quantity) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            oneCartElement[] theCartElements = getShoppingCart(username);
            boolean isExist = false;
            int existingQuantity = 0;

            // 检查购物车中是否已有该商品
            for (oneCartElement element : theCartElements) {
                if (element.productID.equals(productID)) {
                    isExist = true;
                    existingQuantity = element.productNumber;
                    break;
                }
            }

            if (isExist) {
                updateCart(username, productID, quantity + existingQuantity);
                return true;
            } else {
                // 构建请求
                JSONObject request = new JSONObject();
                request.put("requestType", "cart");
                request.put("parameters", new JSONObject()
                        .put("action", "add")
                        .put("username", username)
                        .put("productID", productID)
                        .put("quantity", quantity));

                // 发送请求
                out.println(request.toString());

                String response = in.readLine();
                JSONObject jsonResponse = new JSONObject(response);

                return jsonResponse.getString("status").equals("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从购物车中删除商品。
     *
     * @param username  用户账号
     * @param productID 商品ID
     * @return 如果删除成功返回 true，否则返回 false
     * @throws IOException 如果出现IO异常
     */
    public static boolean removeFromCart(String username, String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "cart");
            request.put("parameters", new JSONObject()
                    .put("action", "remove")
                    .put("username", username)
                    .put("productID", productID));

            // 发送请求
            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新购物车中的商品数量。
     *
     * @param username    用户账号
     * @param productID   商品ID
     * @param quantity    更新后的商品数量
     * @return 如果更新成功返回 true，否则返回 false
     * @throws IOException 如果出现IO异常
     */
    public static boolean updateCart(String username, String productID, int quantity) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "cart");
            request.put("parameters", new JSONObject()
                    .put("action", "update")
                    .put("username", username)
                    .put("productID", productID)
                    .put("quantity", quantity));

            // 发送请求
            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取用户的购物车信息。
     *
     * @param username 用户账号
     * @return 包含购物车商品的数组
     * @throws IOException 如果出现IO异常
     */
    public static oneCartElement[] getShoppingCart(String username) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "cart");
            request.put("parameters", new JSONObject()
                    .put("action", "")
                    .put("username", username));

            // 发送请求
            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("cartItems");

            // 获取购物车中的商品数量
            int numElements = data.length();

            // 创建一个数组来存储所有商品信息
            oneCartElement[] theCartElements = new oneCartElement[numElements];

            for (int i = 0; i < numElements; i++) {
                JSONObject course = data.getJSONObject(i);

                theCartElements[i] = new oneCartElement();
                theCartElements[i].productID = course.getString("productID");
                theCartElements[i].productNumber = course.getInt("productNumber");
                theCartElements[i].storeID = course.getString("storeID");
            }

            return theCartElements;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
