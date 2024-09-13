package client.service;

import java.io.*;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * ShoppingUser 类用于处理与用户相关的操作，如获取、更新、添加和删除用户的地址和电话信息。
 */
public class ShoppingUser {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS; // 服务器地址
    private static final int SERVER_PORT = IpConfig.SERVER_PORT; // 服务器端口号

    /**
     * oneUser 类表示一个用户的地址和电话信息。
     */
    public static class oneUser {
        String[] addresses; // 用户地址数组
        String[] telephones; // 用户电话数组

        // 获取地址数组
        public String[] getAddresses() {
            return addresses;
        }

        // 获取电话数组
        public String[] getTelephones() {
            return telephones;
        }
    }

    /**
     * 获取用户的所有地址和电话
     *
     * @param username 用户名
     * @return 用户的地址和电话信息
     * @throws IOException 如果发生IO异常
     */
    public static oneUser getUserDetails(String username) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // 创建Socket对象连接服务器
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 读取服务器响应
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) { // 发送请求数据到服务器

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "user"); // 请求类型为user
            request.put("parameters", new JSONObject()
                    .put("action", "viewUser") // 请求的操作为查看用户
                    .put("username", username)); // 用户名

            // 发送请求
            out.println(request);

            // 读取服务器响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 解析地址和电话
            JSONArray addressesArray = jsonResponse.getJSONArray("addresses"); // 获取地址数组
            JSONArray telephonesArray = jsonResponse.getJSONArray("telephones"); // 获取电话数组

            // 构建oneUser对象并填充数据
            oneUser user = new oneUser();
            user.addresses = new String[addressesArray.length()];
            user.telephones = new String[telephonesArray.length()];

            // 将JSON数组转换为Java数组
            for (int i = 0; i < addressesArray.length(); i++) {
                user.addresses[i] = addressesArray.getString(i);
            }

            for (int i = 0; i < telephonesArray.length(); i++) {
                user.telephones[i] = telephonesArray.getString(i);
            }

            return user; // 返回用户地址和电话信息
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 如果出现异常，返回null
        }
    }

    /**
     * 更新用户的某个地址和电话（根据索引更新）
     *
     * @param username    用户名
     * @param index       索引位置
     * @param newAddress  新地址
     * @param newTelephone 新电话
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean updateUserContactAtIndex(String username, int index, String newAddress, String newTelephone) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // 创建Socket对象连接服务器
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 读取服务器响应
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) { // 发送请求数据到服务器

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "user"); // 请求类型为user
            request.put("parameters", new JSONObject()
                    .put("action", "updateUserContactAtIndex") // 请求的操作为更新某个索引的地址和电话
                    .put("username", username) // 用户名
                    .put("index", index) // 索引位置
                    .put("newAddress", newAddress) // 新地址
                    .put("newTelephone", newTelephone)); // 新电话

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success"); // 判断是否更新成功
        } catch (IOException e) {
            e.printStackTrace();
            return false; // 如果出现异常，返回false
        }
    }

    /**
     * 更新用户的所有地址和电话
     *
     * @param username   用户名
     * @param addresses  地址数组
     * @param telephones 电话数组
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean updateUserContacts(String username, String[] addresses, String[] telephones) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // 创建Socket对象连接服务器
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 读取服务器响应
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) { // 发送请求数据到服务器

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "user"); // 请求类型为user
            request.put("parameters", new JSONObject()
                    .put("action", "updateUserContacts") // 请求的操作为更新用户的所有地址和电话
                    .put("username", username) // 用户名
                    .put("addresses", new JSONArray(addresses)) // 地址数组
                    .put("telephones", new JSONArray(telephones))); // 电话数组

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success"); // 判断是否更新成功
        } catch (IOException e) {
            e.printStackTrace();
            return false; // 如果出现异常，返回false
        }
    }

    /**
     * 添加用户新的地址和电话
     *
     * @param username    用户名
     * @param newAddress  新地址
     * @param newTelephone 新电话
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public static boolean addUserContact(String username, String newAddress, String newTelephone) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // 创建Socket对象连接服务器
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 读取服务器响应
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) { // 发送请求数据到服务器

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "user"); // 请求类型为user
            request.put("parameters", new JSONObject()
                    .put("action", "addUserContact") // 请求的操作为添加新的地址和电话
                    .put("username", username) // 用户名
                    .put("address", newAddress) // 新地址
                    .put("telephone", newTelephone)); // 新电话

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success"); // 判断是否添加成功
        } catch (IOException e) {
            e.printStackTrace();
            return false; // 如果出现异常，返回false
        }
    }

    /**
     * 删除用户指定的地址和电话
     *
     * @param username 用户名
     * @param index    地址和电话的索引位置
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public static boolean deleteUserContact(String username, int index) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // 创建Socket对象连接服务器
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 读取服务器响应
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) { // 发送请求数据到服务器

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "user"); // 请求类型为user
            request.put("parameters", new JSONObject()
                    .put("action", "deleteUserContact") // 请求的操作为删除用户的地址和电话
                    .put("username", username) // 用户名
                    .put("index", index)); // 索引位置

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success"); // 判断是否删除成功
        } catch (IOException e) {
            e.printStackTrace();
            return false; // 如果出现异常，返回false
        }
    }
}
