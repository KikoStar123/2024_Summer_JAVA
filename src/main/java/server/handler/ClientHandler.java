package server.handler;

import server.service.UserService;
import server.service.StudentInformationService;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

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
            if (request == null || request.trim().isEmpty()) {
                System.out.println("Received empty request");
                return;
            }

            System.out.println("Received request: " + request);
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
            } else if ("checkStudentInfo".equals(requestType)) {
                StudentInformationService studentService = new StudentInformationService();
                JSONObject studentInfo = studentService.checkStudentInfo();

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "success").put("data", studentInfo);
                response = jsonResponse.toString();
            } else if ("viewStudentInfo".equals(requestType)) {
                StudentInformationService studentService = new StudentInformationService();
                JSONObject studentInfo = studentService.viewStudentInfo(parameters.getString("id"));

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "success").put("data", studentInfo);
                response = jsonResponse.toString();
            } else if ("modifyStudentInfo".equals(requestType)) {
                StudentInformationService studentService = new StudentInformationService();
                boolean success = studentService.modifyStudentInfo(parameters);

                JSONObject jsonResponse = new JSONObject();
                if (success) {
                    jsonResponse.put("status", "success").put("message", "Student information updated successfully");
                } else {
                    jsonResponse.put("status", "fail").put("message", "Failed to update student information");
                }
                response = jsonResponse.toString();
            } else {
                response = new JSONObject().put("status", "error").put("message", "Unknown request").toString();
            }

            out.println(response);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
