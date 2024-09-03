package client.service;

import org.json.JSONArray;
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

        public String getProductID() {
            return productID;
        }

        public int getProductNumber() {
            return productNumber;
        }
    }

    // 添加商品到购物车
    // 输入 用户账号 username；商品id productID；商品数量 productNumber
    // 返回 状态
    public boolean AddToCart(String username, String productID, int quantity) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            oneCartElement[] theCartElements = getShoppingCart(username);
            boolean isexist = false;
            int existingQuantity = 0;
            for (oneCartElement element : theCartElements) {
                if (element.productID.equals(productID)) //如果存在添加的是已有商品
                {
                    isexist = true;
                    existingQuantity=element.productNumber;
                    break;
                }
            }
            if (isexist)
            {
                updateCart(username, productID, quantity+existingQuantity);
                return true;
            }
            else
            {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除购物车的商品
    // 输入 用户账号 username；商品id productID
    // 返回 状态
    public boolean removeFromCart(String username, String productID) throws IOException
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
    public boolean updateCart(String username, String productID, int quantity) throws IOException
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

    // 获取购物车信息
    // 输入 用户账号 username
    // 返回 购物车元素数组
    public oneCartElement[] getShoppingCart(String username) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "cart");
            request.put("parameters", new JSONObject()
                    .put("action", "")
                    .put("username", username));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("cartItems");//获取JSON数组

            // 获取商品数量
            int numElements = data.length();

            // 创建一个数组来存储所有商品信息
            oneCartElement[] theCartElements = new oneCartElement[numElements];

            for (int i = 0; i < numElements; i++) {

                JSONObject course = data.getJSONObject(i);

                theCartElements[i]=new oneCartElement();
                theCartElements[i].productID = course.getString("productID");
                theCartElements[i].productNumber = course.getInt("productNumber");
            }

            return theCartElements;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
