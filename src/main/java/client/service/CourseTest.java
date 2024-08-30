package client.service;

import java.io.*;
import java.net.Socket;

public class CourseTest {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 8080; // 服务器端口

    public static void main(String[] args) {
        try {

            sendAddCourseRequest("CS102", "Data Structures", "Prof. Brown", 4, "1-10|Tue|2-4", 50); //添加一门课程
//            Thread.sleep(500); // 延时，确保服务器有时间处理
            System.out.println('\n');

            sendEnrollRequest("student4", "CS101");//选课 学生-选课编号
//            Thread.sleep(500); // 延时，确保服务器有时间处理
            System.out.println('\n');

            sendViewEnrolledCoursesRequest("student4");//查看某个学生的选课信息（全部课程）
//            Thread.sleep(500); // 再次延时
            System.out.println('\n');

            sendDropRequest("student4", "CS101");//退课
//            Thread.sleep(500); // 再次延时
            System.out.println('\n');

            System.out.println("课程名单参数：");
            sendSearchCoursesRequest("Computer Science",null);//根据课程名称或者教师名称检索（两个一起检索都可以）
//            Thread.sleep(500); // 再次延时
            System.out.println('\n');

            System.out.println("教师名单参数：");
            sendSearchCoursesRequest(null,"Dr. Smith");//根据课程名称或者教师名称检索（两个一起检索都可以）
//            Thread.sleep(500); // 再次延时
            System.out.println('\n');

            System.out.println("双参数：");
            sendSearchCoursesRequest("Computer Science","Dr. Smith");//根据课程名称或者教师名称检索（两个一起检索都可以）
//            Thread.sleep(500); // 再次延时
            System.out.println('\n');

            sendViewCourseInfoRequest("CS101");// 查看课程信息功能
//            Thread.sleep(500); // 再次延时
            System.out.println('\n');

            sendGetAllCoursesRequest();// 获取所有课程信息功能
            Thread.sleep(500); // 再次延时
            System.out.println('\n');

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

    private static void sendSearchCoursesRequest(String courseName, String courseTeacher) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String searchRequest = createSearchCoursesRequest(courseName, courseTeacher);
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

    private static void sendAddCourseRequest(String courseID, String courseName, String courseTeacher, int courseCredits, String courseTime, int courseCapacity) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String addCourseRequest = createAddCourseRequest(courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity);
            System.out.println("Sending add course request: " + addCourseRequest);
            out.println(addCourseRequest);
            String addCourseResponse = in.readLine();
            System.out.println("Server response: " + addCourseResponse);
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

    private static String createSearchCoursesRequest(String courseName, String courseTeacher) {
        // 构建JSON请求，根据是否传入了courseName和courseTeacher进行构建
        StringBuilder request = new StringBuilder("{\"requestType\": \"searchCourses\", \"parameters\": {");

        if (courseName != null && !courseName.isEmpty()) {
            request.append("\"courseName\": \"").append(courseName).append("\", ");
        }
        if (courseTeacher != null && !courseTeacher.isEmpty()) {
            request.append("\"courseTeacher\": \"").append(courseTeacher).append("\", ");
        }

        // 删除最后一个多余的逗号和空格
        if (request.charAt(request.length() - 2) == ',') {
            request.delete(request.length() - 2, request.length());
        }

        request.append("}}");
        return request.toString();
    }

    private static String createViewCourseInfoRequest(String courseID) {
        return String.format("{\"requestType\": \"viewCourseInfo\", \"parameters\": {\"courseID\": \"%s\"}}", courseID);
    }

    private static String createGetAllCoursesRequest() {
        return "{\"requestType\": \"getAllCourses\", \"parameters\": {}}";
    }

    private static String createAddCourseRequest(String courseID, String courseName, String courseTeacher, int courseCredits, String courseTime, int courseCapacity) {
        return String.format("{\"requestType\": \"addCourse\", \"parameters\": {\"courseID\": \"%s\", \"courseName\": \"%s\", \"courseTeacher\": \"%s\", \"courseCredits\": %d, \"courseTime\": \"%s\", \"courseCapacity\": %d}}",
                courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity);
    }

}

