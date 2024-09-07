package client.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ShoppingComment {
    private final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private final int SERVER_PORT = IpConfig.SERVER_PORT;

    public class oneComment {
        String username;//用户账号
        String productID;//商品id
        String commentID;//评论id
        int commentAttitude;//评论态度（差评=1、中评=2、好评=3）
        String commentContent;//评论内容

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

    // 管理员：查看所有商品的评论
    // 输入 无
    // 返回 评论数组
    public oneComment[] getAllProductComments() throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getAllProductComments"));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("comments");//获取JSON数组

            // 获取评论数量
            int numComments = data.length();

            // 创建一个数组来存储所有商品信息
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

    // 管理员/顾客：查询商品的评论（用于商品详情页）（可选择评论态度也可以不选择）
    // 输入 商品id productID； 评论态度 commentAttitude（查看差评=1、查看中评=2、查看好评=3，查看所有评论=0）
    // 返回 评论数组
    public oneComment[] getProductComments(String productID, int commentAttitude) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            if(commentAttitude == 0)//查看所有评论
            {
                request.put("parameters", new JSONObject()
                        .put("action", "getProductComments")
                        .put("productID", productID));
            }
            else//查看差评/中评/好评
            {
                request.put("parameters", new JSONObject()
                        .put("action", "getProductComments")
                        .put("productID", productID)
                        .put("commentAttitude", commentAttitude));
            }


            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("comments");//获取JSON数组

            // 获取评论数量
            int numComments = data.length();

            // 创建一个数组来存储所有商品信息
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

    // 管理员：查询某个用户对某个商品的评论
    // 输入 用户账号 username；商品id productID
    // 返回 评论数组
    public oneComment[] searchProductComments(String username, String productID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

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

            JSONArray data = jsonResponse.getJSONArray("comments");//获取JSON数组

            // 获取评论数量
            int numComments = data.length();

            // 创建一个数组来存储所有商品信息
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

    // 顾客：添加评论
    // 输入 用户账号 username；商品id productID；评论态度（差评=1、中评=2、好评=3） commentAttitude；评论内容 commentContent；订单号 orderID
    // （注意修改订单的 是否评价 属性）
    // 返回 状态
    public boolean addComment(String username, String productID, int commentAttitude, String commentContent, String orderID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            //检测是否可评论
            ShoppingOrder temporder = new ShoppingOrder();
            if(temporder.getOrderCommentStatus(orderID))//1代表评价过了，0代表没评价过，允许评价
            {
                return false;
            }

            //可评论，继续操作

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "addComment")
                    .put("username", username)
                    .put("productID", productID)
                    .put("commentAttitude", commentAttitude)
                    .put("commentContent", commentContent));

            System.out.println(request.toString());
            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            //修改该订单为已评价
            temporder.updateCommentStatus(orderID,true);//1代表评价过了，以后不允许评价

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
