package client.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 购物地图服务类，用于管理商品的地图记录，包括添加、删除、更新和查询地图记录。
 */
public class ShoppingMap {

    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    /**
     * 添加商品的地图记录。
     *
     * @param productID 商品ID
     * @param mapStart  地图的起点
     * @param mapEnd    地图的终点
     * @return 如果添加成功返回 true，否则返回 false
     * @throws IOException 如果出现IO异常
     */
    public static boolean addMapRecord(String productID, String mapStart, String mapEnd) throws IOException {
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

    /**
     * 删除商品的地图记录。
     *
     * @param productID 商品ID
     * @return 如果删除成功返回 true，否则返回 false
     * @throws IOException 如果出现IO异常
     */
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

    /**
     * 更新商品的地图记录。
     *
     * @param productID 商品ID
     * @param mapStart  地图的起点
     * @param mapEnd    地图的终点
     * @return 如果更新成功返回 true，否则返回 false
     * @throws IOException 如果出现IO异常
     */
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

    /**
     * 获取所有商品的地图记录。
     *
     * @return 包含所有地图记录的JSONArray对象
     * @throws IOException 如果出现IO异常
     */
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

    /**
     * 根据商品ID查询对应的地图记录。
     *
     * @param productID 商品ID
     * @return 包含查询结果的JSONObject对象
     * @throws IOException 如果出现IO异常
     */
    public static JSONObject getMapRecordByProductID(String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "map");
            request.put("parameters", new JSONObject()
                    .put("action", "getRecordByProductID")
                    .put("productID", productID));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 输出响应，便于调试
            System.out.println("响应: " + jsonResponse.toString());

            // 返回查询的结果
            return jsonResponse;
        }
    }
}
