package client.service;

import java.io.*;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShoppingMap {

    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    // 添加地图记录
    public boolean addMapRecord(String productID, String mapStart, String mapEnd) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "map");
            request.put("parameters", new JSONObject()
                    .put("action", "addRecord")
                    .put("productID", productID)
                    .put("mapStart", mapStart)
                    .put("mapEnd", mapEnd));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查返回的 status 字段
            return jsonResponse.optString("status").equals("success");
        }
    }

    // 删除地图记录
    public boolean deleteMapRecord(String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "map");
            request.put("parameters", new JSONObject()
                    .put("action", "deleteRecord")
                    .put("productID", productID));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查返回的 status 字段
            return jsonResponse.optString("status").equals("success");
        }
    }

    // 更新地图记录
    public boolean updateMapRecord(String productID, String mapStart, String mapEnd) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "map");
            request.put("parameters", new JSONObject()
                    .put("action", "updateRecord")
                    .put("productID", productID)
                    .put("mapStart", mapStart)
                    .put("mapEnd", mapEnd));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查返回的 status 字段
            return jsonResponse.optString("status").equals("success");
        }
    }

    // 获取所有地图记录
    public JSONArray getAllMapRecords() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "map");
            request.put("parameters", new JSONObject().put("action", "getAllRecords"));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 输出响应，便于调试
            System.out.println("响应: " + jsonResponse.toString());

            // 检查是否有 records 字段
            if (jsonResponse.has("records")) {
                return jsonResponse.getJSONArray("records");
            } else {
                // 如果没有返回 records 字段，输出错误信息并返回空数组
                System.err.println("未找到 records 字段");
                return new JSONArray();
            }
        }
    }
}
