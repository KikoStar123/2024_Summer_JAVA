package client.service;

import java.io.*;
import java.net.Socket;

public class CourseTest {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 8080; // 服务器端口

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 示例：student4 选课 CS101
            String enrollRequest = createEnrollRequest("student4", "CS101");
            System.out.println("Sending enroll request: " + enrollRequest);
            out.println(enrollRequest);
            System.out.println("Server response: " + in.readLine());

            // 示例：student5 选课 MATH101
            enrollRequest = createEnrollRequest("student5", "MATH101");
            System.out.println("Sending enroll request: " + enrollRequest);
            out.println(enrollRequest);
            System.out.println("Server response: " + in.readLine());

            // 示例：student4 退课 CS101
            String dropRequest = createDropRequest("student4", "CS101");
            System.out.println("Sending drop request: " + dropRequest);
            out.println(dropRequest);
            System.out.println("Server response: " + in.readLine());

            // 示例：student4 查看已选课程
            String viewRequest = createViewEnrolledCoursesRequest("student4");
            System.out.println("Sending view request: " + viewRequest);
            out.println(viewRequest);
            System.out.println("Server response: " + in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
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
