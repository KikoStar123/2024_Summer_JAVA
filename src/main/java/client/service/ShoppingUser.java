package client.service;

import java.io.*;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShoppingUser {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    public static class oneUser {
        String[] addresses;
        String[] telephones;

        public String[] getAddresses() {
            return addresses;
        }

        public String[] getTelephones() {
            return telephones;
        }
    }

    // 获取用户的所有地址和电话
    public oneUser getUserDetails(String username) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "user");
            request.put("parameters", new JSONObject()
                    .put("action", "viewUser")
                    .put("username", username));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 解析地址和电话
            JSONArray addressesArray = jsonResponse.getJSONArray("addresses");
            JSONArray telephonesArray = jsonResponse.getJSONArray("telephones");

            oneUser user = new oneUser();
            user.addresses = new String[addressesArray.length()];
            user.telephones = new String[telephonesArray.length()];

            for (int i = 0; i < addressesArray.length(); i++) {
                user.addresses[i] = addressesArray.getString(i);
            }

            for (int i = 0; i < telephonesArray.length(); i++) {
                user.telephones[i] = telephonesArray.getString(i);
            }

            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 更新用户的某个地址和电话（指定索引）
    public boolean updateUserContactAtIndex(String username, int index, String newAddress, String newTelephone) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "user");
            request.put("parameters", new JSONObject()
                    .put("action", "updateUserContactAtIndex")
                    .put("username", username)
                    .put("index", index)
                    .put("newAddress", newAddress)
                    .put("newTelephone", newTelephone));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新用户的地址和电话（更新所有）
    public boolean updateUserContacts(String username, String[] addresses, String[] telephones) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject request = new JSONObject();
            request.put("requestType", "user");
            request.put("parameters", new JSONObject()
                    .put("action", "updateUserContacts")
                    .put("username", username)
                    .put("addresses", new JSONArray(addresses))
                    .put("telephones", new JSONArray(telephones)));

            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 添加用户新的地址和电话
    public boolean addUserContact(String username, String newAddress, String newTelephone) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject request = new JSONObject();
            request.put("requestType", "user");
            request.put("parameters", new JSONObject()
                    .put("action", "addUserContact")
                    .put("username", username)
                    .put("address", newAddress)
                    .put("telephone", newTelephone));

            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除用户指定的地址和电话
    public boolean deleteUserContact(String username, int index) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "user");
            request.put("parameters", new JSONObject()
                    .put("action", "deleteUserContact")
                    .put("username", username)
                    .put("index", index));  // 索引位置

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
