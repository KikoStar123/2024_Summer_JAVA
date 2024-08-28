package client.service;

import java.io.*;
import java.net.Socket;

public class CourseTest {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 8080; // 服务器端口



    public static void main(String[] args) {
        try {
            sendEnrollRequest("student4", "CS101");
            Thread.sleep(500); // 延时，确保服务器有时间处理

            sendViewEnrolledCoursesRequest("student4");
            Thread.sleep(500); // 再次延时

            sendDropRequest("student4", "CS101");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //每个请求独立发送避免数据库死锁

    private static void sendEnrollRequest(String username, String courseID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String enrollRequest = createEnrollRequest(username, courseID);
            System.out.println("Sending enroll request: " + enrollRequest);
            out.println(enrollRequest);
            String enrollResponse = in.readLine();
            System.out.println("Server response: " + enrollResponse);
        }
    }

    private static void sendDropRequest(String username, String courseID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String dropRequest = createDropRequest(username, courseID);
            System.out.println("Sending drop request: " + dropRequest);
            out.println(dropRequest);
            String dropResponse = in.readLine();
            System.out.println("Server response: " + dropResponse);
        }
    }

    private static void sendViewEnrolledCoursesRequest(String username) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String viewRequest = createViewEnrolledCoursesRequest(username);
            System.out.println("Sending view request: " + viewRequest);
            out.println(viewRequest);
            String viewResponse = in.readLine();
            System.out.println("Server response: " + viewResponse);
        }
    }

    private static String createEnrollRequest(String username, String courseID) {
        return String.format("{\"requestType\": \"enrollInCourse\", \"parameters\": {\"username\": \"%s\", \"courseID\": \"%s\"}}", username, courseID);
    }

    private static String createDropRequest(String username, String courseID) {
        return String.format("{\"requestType\": \"dropCourse\", \"parameters\": {\"username\": \"%s\", \"courseID\": \"%s\"}}", username, courseID);
    }

    private static String createViewEnrolledCoursesRequest(String username) {
        return String.format("{\"requestType\": \"viewEnrolledCourses\", \"parameters\": {\"username\": \"%s\"}}", username);
    }
}
