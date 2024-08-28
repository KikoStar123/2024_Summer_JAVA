package client.service;
import org.json.JSONObject;
import java.io.*;
import java.net.Socket;


public class UserTest {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request_1 = new JSONObject();
            request_1.put("requestType", "login_return");
            JSONObject parameters_1 = new JSONObject();
            parameters_1.put("username", "000002");
            parameters_1.put("password", "password123");
            request_1.put("parameters", parameters_1);


            out.println(request_1.toString());


            String response_1 = in.readLine();
            System.out.println("Server response: " + response_1);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}