package client.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ShoppingStore 类用于处理与商店相关的操作，如添加、更新、删除和获取商店信息。
 */
public class ShoppingStore {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器的地址，即本地服务器
    private static final int SERVER_PORT = 8080; // 定义服务器的端口号

    /**
     * oneStore 类表示一个商店的基本信息。
     */
    public static class oneStore {
        String storeID; // 商店ID
        String storeName; // 商店名称
        String storePhone; // 联系电话
        float storeRate; // 商店好评率
        boolean storeStatus; // 商店状态

        // 获取商店ID
        public String getStoreID() {
            return storeID;
        }

        // 获取商店名称
        public String getStoreName() {
            return storeName;
        }

        // 获取商店联系电话
        public String getStorePhone() {
            return storePhone;
        }

        // 获取商店好评率
        public float getStoreRate() {
            return storeRate;
        }

        // 获取商店状态
        public boolean getStoreStatus() {
            return storeStatus;
        }
    }

    /**
     * 添加商店
     *
     * @param storeID    商店ID
     * @param storeName  商店名称
     * @param storePhone 联系电话
     * @param storeRate  商店好评率
     * @param storeStatus 商店状态
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean addStore(String storeID, String storeName, String storePhone, float storeRate, boolean storeStatus) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");
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

            return jsonResponse.getString("status").equals("success"); // 判断返回值，是否成功

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新商店信息
     *
     * @param storeID    商店ID
     * @param storeName  商店名称
     * @param storePhone 联系电话
     * @param storeRate  商店好评率
     * @param storeStatus 商店状态
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public static boolean updateStore(String storeID, String storeName, String storePhone, float storeRate, boolean storeStatus) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");
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

            return jsonResponse.getString("status").equals("success"); // 判断返回值，是否成功

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除商店
     *
     * @param storeID 商店ID
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean deleteStore(String storeID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");
            request.put("parameters", new JSONObject()
                    .put("action", "delete")
                    .put("storeID", storeID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success"); // 判断返回值，是否成功

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取商店详情
     *
     * @param storeID 商店ID
     * @return 商店对象
     * @throws IOException 如果发生IO异常
     */
    public static oneStore oneStore(String storeID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");
            request.put("parameters", new JSONObject()
                    .put("action", "getStore")
                    .put("storeID", storeID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject data = new JSONObject(response);

            oneStore theoStore = new oneStore();

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

    /**
     * 获取所有商店信息
     *
     * @return 商店数组
     * @throws IOException 如果发生IO异常
     */
    public oneStore[] getAllStores() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");
            request.put("parameters", new JSONObject()
                    .put("action", "getAll"));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("stores");

            int numStores = data.length();
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

    /**
     * 根据用户名获取商店ID
     *
     * @param username 用户名
     * @return 商店ID
     * @throws IOException 如果发生IO异常
     */
    public String getStoreIDByUsername(String username) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "store");
            request.put("parameters", new JSONObject()
                    .put("action", "getStoreIDByUsername")
                    .put("username", username));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("storeID"); // 返回商店ID

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
