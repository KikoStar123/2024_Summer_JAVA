package testhandler.course;

import java.io.*;
import java.net.Socket;

public class CourseTest {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 8080; // 服务器端口

    public static void main(String[] args) {
        try {
            sendAddCourseRequest("CS103", "计算机组成原理", "任国林", 4, "1-10|2|2-4", 50, "Room 101", "required"); // 添加一门课程
            System.out.println('\n');

            sendAddCourseRequest("CS104", "数据结构", "Prof. Brown", 4, "1-16|2|2-4", 50, "Room 103", "required"); // 添加一门课程

            sendAddCourseRequest("CS105", "计算机网络", "Prof. Bob", 4, "1-16|2|2-4", 50, "Room 103", "required"); // 时间冲突&教室冲突

            sendAddCourseRequest("CS106", "数据结构2", "Prof. Brown", 4, "1-16|2|2-4", 50, "Room 106", "required"); // 时间冲突&老师冲突
            System.out.println('\n');

            sendEnrollRequest("200000001", "CS103"); // 选课 学生-选课编号
            System.out.println('\n');

            sendViewEnrolledCoursesRequest("200000001"); // 查看某个学生的已选课程信息
            System.out.println('\n');

            sendDropRequest("200000001", "CS103"); // 退课
            System.out.println('\n');

            System.out.println("课程名单参数：");
            sendSearchCoursesRequest("计算机组成原理", null); // 根据课程名称或者教师名称检索
            System.out.println('\n');

            System.out.println("教师名单参数：");
            sendSearchCoursesRequest(null, "任国林"); // 根据教师名称检索
            System.out.println('\n');

            System.out.println("双参数：");
            sendSearchCoursesRequest("计算机组成原理", "任国林"); // 根据课程名称和教师名称同时检索
            System.out.println('\n');

            sendViewCourseInfoRequest("CS101"); // 查看课程信息功能
            System.out.println('\n');

            sendGetAllCoursesRequest(); // 获取所有课程信息功能
            Thread.sleep(500); // 再次延时
            System.out.println('\n');

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 选课 学生-选课编号
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

    // 退课
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

    // 查看某个学生的已选课程信息
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

    // 根据课程名称或者教师名称检索（两个一起检索都可以）
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

    // 查看课程信息功能
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

    // 获取所有课程信息功能
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

    // 添加一门课程
    private static void sendAddCourseRequest(String courseID, String courseName, String courseTeacher, int courseCredits, String courseTime, int courseCapacity, String courseRoom, String courseType) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 将课程信息整合为String
            String addCourseRequest = createAddCourseRequest(courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity, courseRoom, courseType);
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

    private static String createAddCourseRequest(String courseID, String courseName, String courseTeacher, int courseCredits, String courseTime, int courseCapacity, String courseRoom, String courseType) {
        return String.format("{\"requestType\": \"addCourse\", \"parameters\": {\"courseID\": \"%s\", \"courseName\": \"%s\", \"courseTeacher\": \"%s\", \"courseCredits\": %d, \"courseTime\": \"%s\", \"courseCapacity\": %d, \"courseRoom\": \"%s\", \"courseType\": \"%s\"}}",
                courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity, courseRoom, courseType);
    }

}
