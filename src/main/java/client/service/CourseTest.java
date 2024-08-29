package client.service;

import java.io.*;
import java.net.Socket;

public class CourseTest {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 8080; // 服务器端口

    public static void main(String[] args) {
        try {
            sendEnrollRequest("student4", "CS101");//选课 学生-选课编号
            Thread.sleep(500); // 延时，确保服务器有时间处理

            sendViewEnrolledCoursesRequest("student4");//查看某个学生的选课信息（全部课程）
            Thread.sleep(500); // 再次延时

            sendDropRequest("student4", "CS101");//退课
            Thread.sleep(500); // 再次延时

            sendSearchCoursesRequest("Computer Science");//根据课程名称或者教师名称检索（两个一起检索都可以）
            Thread.sleep(500); // 再次延时

            sendViewCourseInfoRequest("CS101");// 查看课程信息功能
            Thread.sleep(500); // 再次延时

            sendGetAllCoursesRequest();// 获取所有课程信息功能

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

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

    private static void sendSearchCoursesRequest(String keyword) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String searchRequest = createSearchCoursesRequest(keyword);
            System.out.println("Sending search request: " + searchRequest);
            out.println(searchRequest);
            String searchResponse = in.readLine();
            System.out.println("Server response: " + searchResponse);
        }
    }

    private static void sendViewCourseInfoRequest(String courseID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String viewCourseInfoRequest = createViewCourseInfoRequest(courseID);
            System.out.println("Sending view course info request: " + viewCourseInfoRequest);
            out.println(viewCourseInfoRequest);
            String viewCourseInfoResponse = in.readLine();
            System.out.println("Server response: " + viewCourseInfoResponse);
        }
    }

    private static void sendGetAllCoursesRequest() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String getAllCoursesRequest = createGetAllCoursesRequest();
            System.out.println("Sending get all courses request: " + getAllCoursesRequest);
            out.println(getAllCoursesRequest);
            String getAllCoursesResponse = in.readLine();
            System.out.println("Server response: " + getAllCoursesResponse);
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

    private static String createSearchCoursesRequest(String keyword) {
        return String.format("{\"requestType\": \"searchCourses\", \"parameters\": {\"keyword\": \"%s\"}}", keyword);
    }

    private static String createViewCourseInfoRequest(String courseID) {
        return String.format("{\"requestType\": \"viewCourseInfo\", \"parameters\": {\"courseID\": \"%s\"}}", courseID);
    }

    private static String createGetAllCoursesRequest() {
        return "{\"requestType\": \"getAllCourses\", \"parameters\": {}}";
    }

}

