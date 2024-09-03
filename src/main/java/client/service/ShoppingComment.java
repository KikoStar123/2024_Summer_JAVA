package client.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ShoppingComment {
    private final String SERVER_ADDRESS = "localhost";//服务器的地址 即本地服务器
    private final int SERVER_PORT = 8080;//定义服务器的端口号

    public class oneComment {
        int commentAttitude;//评论态度（差评=1、中评=2、好评=3）
        String commentContent;//评论内容
    }



    // 管理员：查看所有商品的评论
    // 输入 无
    // 返回 评论数组
    private oneComment[] getAllProductComments() throws IOException
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

            JSONArray data = jsonResponse.getJSONArray("products");//获取JSON数组

            // 获取评论数量
            int numComments = data.length();

            // 创建一个数组来存储所有商品信息
            oneComment[] commentsArray = new oneComment[numComments];

            for (int i = 0; i < numComments; i++) {

                JSONObject theComment = data.getJSONObject(i);

                commentsArray[i] = new oneComment();
                commentsArray[i].commentAttitude = theComment.getInt("commentAttitude");
                commentsArray[i].commentContent = theComment.getString("commentContent");
            }

            return commentsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
