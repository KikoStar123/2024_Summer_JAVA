package server.service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class UserService {

    private static final int SERVER_PORT = 8080;

    public void startServer() {
        DatabaseConnection.initializeDatabase();

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is listening on port " + SERVER_PORT);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    handleClientRequest(socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientRequest(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request = in.readLine();
            JSONObject jsonRequest = new JSONObject(request);

            String requestType = jsonRequest.getString("requestType");
            JSONObject parameters = jsonRequest.getJSONObject("parameters");

            JSONObject jsonResponse = new JSONObject();

            if ("login".equals(requestType)) {
                String username = parameters.getString("username");
                String password = parameters.getString("password");

                if (DatabaseConnection.validateUser(username, password)) {
                    jsonResponse.put("status", "success");
                } else {
                    jsonResponse.put("status", "failure");
                }
            } else if ("register".equals(requestType)) {
                String username = parameters.getString("username");
                String password = parameters.getString("password");

                if (DatabaseConnection.registerUser(username, password)) {
                    jsonResponse.put("status", "success");
                } else {
                    jsonResponse.put("status", "failure");
                }
            }

            out.println(jsonResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.startServer();
    }
}