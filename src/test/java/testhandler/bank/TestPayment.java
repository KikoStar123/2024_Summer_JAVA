package testhandler.bank;

import org.json.JSONObject;
import java.io.*;
import java.net.Socket;

public class TestPayment {

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8080;
        String orderID = "order12345"; // 示例订单ID
        String username = "200000001"; // 示例用户名
        String bankpwd = "password12"; // 示例密码
        double amount = 100.0; // 示例金额

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "payment");
            request.put("parameters", new JSONObject()
                    .put("orderID", orderID)
                    .put("username", username)
                    .put("bankpwd", bankpwd)
                    .put("amount", amount));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            System.out.println("Server response: " + jsonResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
