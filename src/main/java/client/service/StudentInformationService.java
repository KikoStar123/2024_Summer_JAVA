package client.service;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

//模拟前端发送请求
public class StudentInformationService {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 创建请求JSON
            JSONObject request = new JSONObject();
            request.put("requestType", "getAllStudentInformation");
            JSONObject parameters = new JSONObject();
            request.put("parameters", parameters);

            // 发送请求
            out.println(request.toString());

            // 接收响应
            String response = in.readLine();
            System.out.println("Server response: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
