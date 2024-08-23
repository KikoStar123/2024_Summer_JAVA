package client.service;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class ClientService {
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8080;

    public boolean login(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "login");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean logout(String username, String password) {//登出请求
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "logout");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean register(String username, String password) {//定义注册功能
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // 创建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "register");//main difference
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            // 接收响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 返回注册结果
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean register_out(String username, String password) {//定义注销功能
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // 创建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "register_out");//main difference
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            // 接收响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 返回注册结果
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean hasPermission(String username, String permission) {//定义授权功能
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "checkPermission");//check user permission
            JSONObject parameters = new JSONObject();
            parameters.put("username", username);
            parameters.put("permission", permission);
            request.put("parameters", parameters);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getBoolean("hasPermission");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean update(String username, String password) {//定义注销功能
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // 创建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "update");//main difference
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));
            //应该还要更新学籍信息，图书馆借阅，用户账号信息等等。
            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            // 接收响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 返回注册结果
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
