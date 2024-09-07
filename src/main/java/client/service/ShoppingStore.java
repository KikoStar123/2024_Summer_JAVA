package client.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ShoppingStore {
    private static final String SERVER_ADDRESS = "localhost";//服务器的地址 即本地服务器
    private static final int SERVER_PORT = 8080;//定义服务器的端口号

    public static class oneStore{
        String storeID;//商店id
        String storeName;//商店名称
        String storePhone;//联系电话
        float storeRate;//商店好评率
        boolean storeStatus;//商店状态

        public String getStoreID() {
            return storeID;
        }

        public String getStoreName() {
            return storeName;
        }

        public String getStorePhone() {
            return storePhone;
        }

        public float getStoreRate() {
            return storeRate;
        }

        public boolean getStoreStatus() {
            return storeStatus;
        }
    }

    // 添加商店
    // 输入 商店id storeID；商店名称 storeName；联系电话 storePhone；商店好评率 storeRate；商店状态 storeStatus
    // 返回 状态
    public boolean addStore(String storeID, String storeName, String storePhone, float storeRate, boolean storeStatus) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");//是这个type吗？
            request.put("parameters", new JSONObject()
                    .put("action", "add")
                    .put("storeID", storeID)
                    .put("storeName", storeName)
                    .put("storePhone", storePhone)
                    .put("storeRate", storeRate)
                    .put("storeStatus", storeStatus));

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

    // 更新商店信息
    // 输入 商店id storeID；商店名称 storeName；联系电话 storePhone；商店好评率 storeRate；商店状态 storeStatus
    // 返回 状态
    public boolean updateStore(String storeID, String storeName, String storePhone, float storeRate, boolean storeStatus) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");//是这个type吗？
            request.put("parameters", new JSONObject()
                    .put("action", "update")
                    .put("storeID", storeID)
                    .put("storeName", storeName)
                    .put("storePhone", storePhone)
                    .put("storeRate", storeRate)
                    .put("storeStatus", storeStatus));

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

    // 删除商店
    // 输入 商店id storeID
    // 返回 状态
    public boolean deleteStore(String storeID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");//是这个type吗？
            request.put("parameters", new JSONObject()
                    .put("action", "delete")
                    .put("storeID", storeID));

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

    // 获取商店详情
    // 输入 商店id storeID
    // 返回 商店对象
    static oneStore theoStore=new oneStore() ;
    public static oneStore oneStore(String storeID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");//是这个type吗？
            request.put("parameters", new JSONObject()
                    .put("action", "getStore")
                    .put("storeID", storeID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject data = new JSONObject(response);

            theoStore.storeID = storeID;
            theoStore.storeName = data.getString("storeName");
            theoStore.storePhone = data.getString("storePhone");
            theoStore.storeRate = data.getFloat("storeRate");
            theoStore.storeStatus = data.getBoolean("storeStatus");

            return theoStore;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取所有商店信息
    // 输入 无
    // 返回 商店数组
    public oneStore[] getAllStores() throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");//是这个type吗？
            request.put("parameters", new JSONObject()
                    .put("action", "getAll"));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("stores");//获取JSON数组

            // 获取商品数量
            int numStores = data.length();

            // 创建一个数组来存储所有商品信息
            oneStore[] storesArray = new oneStore[numStores];

            for (int i = 0; i < numStores; i++) {
                JSONObject theStore = data.getJSONObject(i);

                storesArray[i] = new oneStore();
                storesArray[i].storeID = theStore.getString("storeID");
                storesArray[i].storeName = theStore.getString("storeName");
                storesArray[i].storePhone = theStore.getString("storePhone");
                storesArray[i].storeRate = theStore.getFloat("storeRate");
                storesArray[i].storeStatus = theStore.getBoolean("storeStatus");
            }

            return storesArray;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
