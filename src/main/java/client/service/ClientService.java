package client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;

public class ClientService {

    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8080;

    public boolean login(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // 创建请求的JSON对象
            JSONObject request = new JSONObject();
            request.put("requestType", "login");

            // 创建参数的JSON对象
            JSONObject parameters = new JSONObject();
            parameters.put("username", username);
            parameters.put("password", password);
            request.put("parameters", parameters);

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            // 接收响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();

            // 解析响应的JSON对象
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
