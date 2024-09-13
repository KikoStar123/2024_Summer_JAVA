package client.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 购物评论服务类，提供查看商品评论、添加评论等功能。
 */
public class ShoppingComment {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    /**
     * 内部类表示商品评论的详细信息。
     */
    public static class oneComment {
        String username;         // 用户账号
        String productID;        // 商品ID
        String commentID;        // 评论ID
        int commentAttitude;     // 评论态度（差评=1、中评=2、好评=3）
        String commentContent;   // 评论内容

        // Getter方法
        public String getUsername() {
            return username;
        }

        public String getProductID() {
            return productID;
        }

        public String getCommentID() {
            return commentID;
        }

        public int getCommentAttitude() {
            return commentAttitude;
        }

        public String getCommentContent() {
            return commentContent;
        }
    }

    /**
     * 管理员查看所有商品的评论。
     *
     * @return 包含所有商品评论的数组
     * @throws IOException 如果出现IO异常
     */
    public oneComment[] getAllProductComments() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject().put("action", "getAllProductComments"));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("comments");

            // 获取评论数量
            int numComments = data.length();

            // 创建一个数组来存储所有评论信息
            oneComment[] commentsArray = new oneComment[numComments];

            for (int i = 0; i < numComments; i++) {
                JSONObject theComment = data.getJSONObject(i);
                commentsArray[i] = new oneComment();
                commentsArray[i].username = theComment.getString("username");
                commentsArray[i].productID = theComment.getString("productID");
                commentsArray[i].commentID = theComment.getString("commentID");
                commentsArray[i].commentAttitude = theComment.getInt("commentAttitude");
                commentsArray[i].commentContent = theComment.getString("commentContent");
            }

            return commentsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据商品ID和评论态度查询商品的评论。
     *
     * @param productID       商品ID
     * @param commentAttitude 评论态度（差评=1、中评=2、好评=3，查看所有评论=0）
     * @return 包含筛选后评论的数组
     * @throws IOException 如果出现IO异常
     */
    public oneComment[] getProductComments(String productID, int commentAttitude) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            if (commentAttitude == 0) {
                // 查看所有评论
                request.put("parameters", new JSONObject()
                        .put("action", "getProductComments")
                        .put("productID", productID));
            } else {
                // 根据态度筛选评论
                request.put("parameters", new JSONObject()
                        .put("action", "getProductComments")
                        .put("productID", productID)
                        .put("commentAttitude", commentAttitude));
            }

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("comments");

            // 获取评论数量
            int numComments = data.length();

            // 创建一个数组来存储评论信息
            oneComment[] commentsArray = new oneComment[numComments];

            for (int i = 0; i < numComments; i++) {
                JSONObject theComment = data.getJSONObject(i);
                commentsArray[i] = new oneComment();
                commentsArray[i].username = theComment.getString("username");
                commentsArray[i].productID = theComment.getString("productID");
                commentsArray[i].commentID = theComment.getString("commentID");
                commentsArray[i].commentAttitude = theComment.getInt("commentAttitude");
                commentsArray[i].commentContent = theComment.getString("commentContent");
            }

            return commentsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询某个用户对某个商品的评论。
     *
     * @param username 用户账号
     * @param productID 商品ID
     * @return 包含该用户对指定商品的评论的数组
     * @throws IOException 如果出现IO异常
     */
    public oneComment[] searchProductComments(String username, String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "searchProductComments")
                    .put("username", username)
                    .put("productID", productID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("comments");

            // 获取评论数量
            int numComments = data.length();

            // 创建一个数组来存储评论信息
            oneComment[] commentsArray = new oneComment[numComments];

            for (int i = 0; i < numComments; i++) {
                JSONObject theComment = data.getJSONObject(i);
                commentsArray[i] = new oneComment();
                commentsArray[i].username = theComment.getString("username");
                commentsArray[i].productID = theComment.getString("productID");
                commentsArray[i].commentID = theComment.getString("commentID");
                commentsArray[i].commentAttitude = theComment.getInt("commentAttitude");
                commentsArray[i].commentContent = theComment.getString("commentContent");
            }

            return commentsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 顾客添加评论。
     *
     * @param username        用户账号
     * @param productID       商品ID
     * @param commentAttitude 评论态度（差评=1、中评=2、好评=3）
     * @param commentContent  评论内容
     * @param orderID         订单ID
     * @return 如果添加成功返回 true，否则返回 false
     * @throws IOException 如果出现IO异常
     */
    public static boolean addComment(String username, String productID, int commentAttitude, String commentContent, String orderID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 检查订单是否已评价
            ShoppingOrder tempOrder = new ShoppingOrder();
            if (tempOrder.getOrderCommentStatus(orderID)) { // 如果订单已评价，返回 false
                return false;
            }

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "addComment")
                    .put("username", username)
                    .put("productID", productID)
                    .put("commentAttitude", commentAttitude)
                    .put("commentContent", commentContent));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 修改订单评价状态
            tempOrder.updateCommentStatus(orderID, true);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
