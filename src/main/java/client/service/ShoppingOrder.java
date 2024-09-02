package client.service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ShoppingOrder {
    private final String SERVER_ADDRESS = "localhost";//服务器的地址 即本地服务器
    private final int SERVER_PORT = 8080;//定义服务器的端口号

    public class oneOrder
    {
        String orderID;//订单号
        String username;//用户账号
        String productID;//商品id
        int productNumber;//商品数量
        boolean whetherComment;//是否评价
        float paidMoney;//支付金额
    }

    // 添加订单
    // 输入 用户账号 username；商品id productID；商品数量 productNumber；支付金额 paidMoney
    // 返回 状态
    private boolean CreateOrder(String username, String productID, int productNumber, float paidMoney) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "create")
                    .put("productID", productID)
                    .put("productNumber", productNumber)
                    .put("paidMoney", paidMoney)
                    .put("username", username));

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

    // 查看单个订单详细信息
    // 输入 订单id orderID
    // 返回 一个订单对象
    private oneOrder getOrderDetails(String orderID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

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

            JSONObject data = jsonResponse.getJSONObject("product");

            oneOrder theOrder = new oneOrder();

            theOrder.orderID=data.getString("orderID");
            theOrder.username=data.getString("username");
            theOrder.productID=data.getString("productID");
            theOrder.productNumber=data.getInt("productNumber");
            theOrder.whetherComment=data.getBoolean("whetherComment");
            theOrder.paidMoney=data.getFloat("paidMoney");

            return theOrder;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 更新订单评论状态（未完成）
    // 输入 订单id orderID； 是否评论过 whetherComment
    // 返回 一个订单对象
    private boolean updateOrderComment(String orderID, boolean whetherComment) throws IOException
    {
        if
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "updateCommentStatus")
                    .put("productID", productID)
                    .put("productNumber", productNumber)
                    .put("paidMoney", paidMoney)
                    .put("username", username));

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
