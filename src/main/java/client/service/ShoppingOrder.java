package client.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ShoppingOrder {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    public static class oneOrder
    {
        String orderID;//订单号
        String username;//用户账号
        String productID;//商品id
        String productName;//商品名称
        int productNumber;//商品数量
        boolean whetherComment;//是否评价
        float paidMoney;//支付金额

        boolean paidStatus;//是否支付（支付true，未支付false）

        String storeID;//店铺id

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

        public String getStoreID() {return storeID;}
    }

    // 创建订单
    // 输入 用户账号 username；商品id productID；商品名称 productName；商品数量 productNumber；支付金额 paidMoney（店铺id storeID 后端自行搜索，前端无需传递）
    // 返回 订单号 orderID
    public String createOrder(String username, String productID, String productName, int productNumber, float paidMoney) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

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

            String orderID = jsonResponse.getString("orderID");

            return orderID;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查看单个订单详细信息
    // 输入 订单id orderID
    // 返回 一个订单对象
    public oneOrder getOrderDetails(String orderID) throws IOException
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

            oneOrder theOrder = new oneOrder();

            //注意！这里后端传给我的数据有问题，没有放在“orders”里面
            theOrder.orderID=jsonResponse.getString("orderID");
            theOrder.username=jsonResponse.getString("username");
            theOrder.productID=jsonResponse.getString("productID");
            theOrder.productName=jsonResponse.getString("productName");
            theOrder.productNumber=jsonResponse.getInt("productNumber");
            theOrder.whetherComment=jsonResponse.getBoolean("whetherComment");
            theOrder.paidMoney=jsonResponse.getFloat("paidMoney");
            theOrder.paidStatus=jsonResponse.getBoolean("paidStatus");
            theOrder.storeID=jsonResponse.getString("storeID");

            return theOrder;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 用户/管理员：查询特定用户的所有订单
    // 输入 用户账号 username
    // 返回 订单数组
    public oneOrder[] getAllOrdersByUser(String username) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

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

            JSONArray data = jsonResponse.getJSONArray("orders");//获取JSON数组

            // 获取评论数量
            int numOrders = data.length();

            // 创建一个数组来存储所有商品信息
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);

                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID=theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID=theOrder.getString("productID");
                ordersArray[i].productName=theOrder.getString("productName");
                ordersArray[i].productNumber=theOrder.getInt("productNumber");
                ordersArray[i].whetherComment=theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney=theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus=theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID=theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 用户/管理员：按照关键词搜索特定用户的订单
    // 输入 用户账号 username；搜索关键词 searchTerm
    // 返回 订单数组
    public oneOrder[] searchOrdersByUser(String username, String searchTerm) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

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

            JSONArray data = jsonResponse.getJSONArray("orders");//获取JSON数组

            // 获取评论数量
            int numOrders = data.length();

            // 创建一个数组来存储所有商品信息
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);

                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID=theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID=theOrder.getString("productID");
                ordersArray[i].productName=theOrder.getString("productName");
                ordersArray[i].productNumber=theOrder.getInt("productNumber");
                ordersArray[i].whetherComment=theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney=theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus=theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID=theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 管理员：在所有订单中搜索关键词
    // 输入 搜索关键词 searchTerm
    // 返回 订单数组
    public oneOrder[] searchOrdersByKeyword(String searchTerm) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

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

            JSONArray data = jsonResponse.getJSONArray("orders");//获取JSON数组

            // 获取评论数量
            int numOrders = data.length();

            // 创建一个数组来存储所有商品信息
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);

                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID=theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID=theOrder.getString("productID");
                ordersArray[i].productName=theOrder.getString("productName");
                ordersArray[i].productNumber=theOrder.getInt("productNumber");
                ordersArray[i].whetherComment=theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney=theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus=theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID=theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取所有用户的所有订单
    // 输入 无
    // 返回 订单数组
    public oneOrder[] getAllOrders() throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "getAllOrders"));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("orders");//获取JSON数组

            // 获取评论数量
            int numOrders = data.length();

            // 创建一个数组来存储所有商品信息
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);

                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID=theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID=theOrder.getString("productID");
                ordersArray[i].productName=theOrder.getString("productName");
                ordersArray[i].productNumber=theOrder.getInt("productNumber");
                ordersArray[i].whetherComment=theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney=theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus=theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID=theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取订单是否评论的状态
    // 输入 订单id orderID
    // 返回 是否评价 1代表评价过了，0代表没评价过，允许评价
    public boolean getOrderCommentStatus(String orderID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

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

    // 更新是否评论状态
    // 输入 订单id orderID；是否评价 whetherComment 1代表评价过了，0代表没评价过，允许评价
    // 返回 状态
    public boolean updateCommentStatus(String orderID, boolean whetherComment) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

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

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 支付
    // 输入 订单id orderID；支付金额 amount
    // 返回 状态
    public static boolean payOrder(String orderID, float amount) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "pay")
                    .put("orderID", orderID)
                    .put("amount", amount));

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

    // 根据商店ID查询该商店的所有订单
    // 输入 商店id storeID
    // 返回 订单数组
    public oneOrder[] getAllOrdersByStore(String storeID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "order");
            request.put("parameters", new JSONObject()
                    .put("action", "getAllOrdersByStore")
                    .put("username", storeID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("orders");//获取JSON数组

            // 获取评论数量
            int numOrders = data.length();

            // 创建一个数组来存储所有商品信息
            oneOrder[] ordersArray = new oneOrder[numOrders];

            for (int i = 0; i < numOrders; i++) {
                JSONObject theOrder = data.getJSONObject(i);

                ordersArray[i] = new oneOrder();
                ordersArray[i].orderID=theOrder.getString("orderID");
                ordersArray[i].username = theOrder.getString("username");
                ordersArray[i].productID=theOrder.getString("productID");
                ordersArray[i].productName=theOrder.getString("productName");
                ordersArray[i].productNumber=theOrder.getInt("productNumber");
                ordersArray[i].whetherComment=theOrder.getBoolean("whetherComment");
                ordersArray[i].paidMoney=theOrder.getFloat("paidMoney");
                ordersArray[i].paidStatus=theOrder.getBoolean("paidStatus");
                ordersArray[i].storeID=theOrder.getString("storeID");
            }

            return ordersArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
