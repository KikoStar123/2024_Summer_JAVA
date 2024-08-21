package server.handler;

import server.service.UserService;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request = in.readLine();
            JSONObject jsonRequest = new JSONObject(request);

            String requestType = jsonRequest.getString("requestType");
            JSONObject parameters = jsonRequest.getJSONObject("parameters");

            String response;
            if ("login".equals(requestType)) {
                UserService userService = new UserService();
                boolean success = userService.login(parameters.getString("username"), parameters.getString("password"));

                JSONObject jsonResponse = new JSONObject();
                if (success) {
                    jsonResponse.put("status", "success").put("message", "Login successful");
                } else {
                    jsonResponse.put("status", "fail").put("message", "Invalid credentials");
                }
                response = jsonResponse.toString();
            } else {
                response = new JSONObject().put("status", "error").put("message", "Unknown request").toString();
            }

            out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
