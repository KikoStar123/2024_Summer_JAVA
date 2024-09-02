package client.service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ShoppingCart {
    private final String SERVER_ADDRESS = "localhost";//服务器的地址 即本地服务器
    private final int SERVER_PORT = 8080;//定义服务器的端口号

    public class oneCartElement
    {
        String productID;//商品id
        int productNumber;//商品数量
    }

    // 添加商品到购物车
    // 输入 用户账号 username；商品id productID；商品数量 productNumber
    // 返回 状态
    private boolean AddToCart(String username, String productID, int quantity) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据



            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "cart");
            request.put("parameters", new JSONObject()
                    .put("action", "add")
                    .put("username", username)
                    .put("productID", productID)
                    .put("quantity", quantity));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除购物车的商品
    // 输入 用户账号 username；商品id productID
    // 返回 状态
    private boolean removeFromCart(String username, String productID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "cart");
            request.put("parameters", new JSONObject()
                    .put("action", "remove")
                    .put("username", username)
                    .put("productID", productID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新购物车商品信息
    // 输入 用户账号 username；商品id productID；商品数量 productNumber
    // 返回 状态
    private boolean updateCart(String username, String productID, int quantity) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "cart");
            request.put("parameters", new JSONObject()
                    .put("action", "update")
                    .put("username", username)
                    .put("productID", productID)
                    .put("quantity", quantity));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



}
