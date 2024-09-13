package client.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 选课服务类，负责处理选课相关的操作，包括查看所有课程、选课、退课、查询课程等功能。
 */
public class CourseSelection {
    private final String SERVER_ADDRESS = "localhost";// 服务器的地址，即本地服务器
    private final int SERVER_PORT = 8080;// 定义服务器的端口号

    /**
     * 课程时间类，包含起始周、结束周、星期几、起始节次和结束节次等信息。
     */
    public class onePeriod {
        int staWeek;// 起始周
        int endWeek;// 结束周
        int Day;// 星期几
        int stasection;// 起始节次
        int endsection;// 结束节次

        public int getStaWeek() {
            return staWeek;
        }

        public int getEndWeek() {
            return endWeek;
        }

        public int getDay() {
            return Day;
        }

        public int getStasection() {
            return stasection;
        }

        public int getEndsection() {
            return endsection;
        }
    }

    /**
     * 课程信息类，包含课程的ID、名称、教师、学分、课程时间、容量等信息。
     */
    public class oneCourseinfo {
        String courseID;// 课程ID
        String courseName;// 课程名称
        String courseTeacher;// 课程教师
        int courseCredits;// 课程学分
        onePeriod[] courseTime = new onePeriod[0];// 课程时间（周次、星期几、节次）
        int courseCapacity;// 课程最大容量
        int selectedCount;// 已选人数
        String courseRoom;// 课程教室
        String courseType; // 课程类型

        public String getCourseID() {
            return courseID;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getCourseTeacher() {
            return courseTeacher;
        }

        public int getCourseCredits() {
            return courseCredits;
        }

        public onePeriod[] getCourseTime() {
            return courseTime;
        }

        public int getCourseCapacity() {
            return courseCapacity;
        }

        public int getSelectedCount() {
            return selectedCount;
        }

        public String getCourseRoom() {
            return courseRoom;
        }

        public String getCourseType() {
            return courseType;
        }
    }

    /**
     * 解析课程时间字符串，使用链表动态添加元素并返回数组。
     *
     * @param timeString 课程时间字符串
     * @return 解析后的课程时间数组
     */
    public onePeriod[] parseCourseTime(String timeString) {
        List<onePeriod> thePeriods = new ArrayList<>();
        String[] timeSegments = timeString.split(";");
        for (String segment : timeSegments) {
            String[] parts = segment.split("\\|");
            String weeks = parts[0];
            String day = parts[1];
            String periods = parts[2];

            onePeriod thePeriod = new onePeriod();
            thePeriod.staWeek = Integer.parseInt(weeks.split("-")[0]);
            thePeriod.endWeek = Integer.parseInt(weeks.split("-")[1]);
            thePeriod.Day = Integer.parseInt(day);
            thePeriod.stasection = Integer.parseInt(periods.split("-")[0]);
            thePeriod.endsection = Integer.parseInt(periods.split("-")[1]);

            thePeriods.add(thePeriod);
        }
        return thePeriods.toArray(new onePeriod[0]);
    }

    /**
     * 获取所有课程信息。
     *
     * @return 包含所有课程信息的数组
     */
    public oneCourseinfo[] GetAllCourses() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject request = new JSONObject();
            request.put("requestType", "getAllCourses");
            request.put("parameters", new JSONObject());

            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("courses");

            int numCourses = data.length();
            oneCourseinfo[] coursesArray = new oneCourseinfo[numCourses];

            for (int i = 0; i < numCourses; i++) {
                JSONObject course = data.getJSONObject(i);
                coursesArray[i] = new oneCourseinfo();
                coursesArray[i].courseID = course.getString("courseID");
                coursesArray[i].courseName = course.getString("courseName");
                coursesArray[i].courseTeacher = course.getString("courseTeacher");
                coursesArray[i].courseCredits = course.getInt("courseCredits");
                String pre_courseTime = course.getString("courseTime");
                coursesArray[i].courseTime = parseCourseTime(pre_courseTime);
                coursesArray[i].courseCapacity = course.getInt("courseCapacity");
                coursesArray[i].selectedCount = course.getInt("selectedCount");
                coursesArray[i].courseRoom = course.getString("courseRoom");
                coursesArray[i].courseType = course.getString("courseType");
            }

            return coursesArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 选课。
     *
     * @param username 学生用户名
     * @param courseID 课程ID
     * @return 如果选课成功返回 true，否则返回 false
     */
    public boolean enrollInCourse(String username, String courseID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject request = new JSONObject();
            request.put("requestType", "enrollInCourse");
            request.put("parameters", new JSONObject().put("username", username).put("courseID", courseID));

            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 退课。
     *
     * @param username 学生用户名
     * @param courseID 课程ID
     * @return 如果退课成功返回 true，否则返回 false
     */
    public boolean dropCourse(String username, String courseID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject request = new JSONObject();
            request.put("requestType", "dropCourse");
            request.put("parameters", new JSONObject().put("username", username).put("courseID", courseID));

            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查看某个学生的已选课程信息。
     *
     * @param username 学生用户名
     * @return 包含已选课程信息的数组
     */
    public oneCourseinfo[] viewEnrolledCourses(String username) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject request = new JSONObject();
            request.put("requestType", "viewEnrolledCourses");
            request.put("parameters", new JSONObject().put("username", username));

            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("fail")) {
                return null;
            }

            JSONArray data = jsonResponse.getJSONArray("courses");
            int numCourses = data.length();
            oneCourseinfo[] coursesArray = new oneCourseinfo[numCourses];

            for (int i = 0; i < numCourses; i++) {
                JSONObject course = data.getJSONObject(i);
                coursesArray[i] = new oneCourseinfo();
                coursesArray[i].courseID = course.getString("courseID");
                coursesArray[i].courseName = course.getString("courseName");
                coursesArray[i].courseTeacher = course.getString("courseTeacher");
                coursesArray[i].courseCredits = course.getInt("courseCredits");
                String pre_courseTime = course.getString("courseTime");
                coursesArray[i].courseTime = parseCourseTime(pre_courseTime);
                coursesArray[i].courseCapacity = course.getInt("courseCapacity");
                coursesArray[i].selectedCount = course.getInt("selectedCount");
                coursesArray[i].courseRoom = course.getString("courseRoom");
                coursesArray[i].courseType = course.getString("courseType");
            }

            return coursesArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查看单个课程的详细信息。
     *
     * @param courseID 课程ID
     * @return 包含课程详细信息的对象
     */
    public oneCourseinfo viewCourseInfo(String courseID) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject request = new JSONObject();
            request.put("requestType", "viewCourseInfo");
            request.put("parameters", new JSONObject().put("courseID", courseID));

            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject data = jsonResponse.getJSONObject("courseInfo");

            oneCourseinfo thecourse = new oneCourseinfo();
            thecourse.courseID = data.getString("courseID");
            thecourse.courseName = data.getString("courseName");
            thecourse.courseTeacher = data.getString("courseTeacher");
            thecourse.courseCredits = data.getInt("courseCredits");
            String pre_courseTime = data.getString("courseTime");
            thecourse.courseTime = parseCourseTime(pre_courseTime);
            thecourse.courseCapacity = data.getInt("courseCapacity");
            thecourse.selectedCount = data.getInt("selectedCount");
            thecourse.courseRoom = data.getString("courseRoom");
            thecourse.courseType = data.getString("courseType");

            return thecourse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加一门课程。
     *
     * @param courseID      课程ID
     * @param courseName    课程名称
     * @param courseTeacher 课程教师
     * @param courseCredits 课程学分
     * @param courseTime    课程时间
     * @param courseCapacity 课程容量
     * @param courseRoom    课程教室
     * @param courseType    课程类型
     * @return 如果添加成功返回 true，否则返回 false
     */
    public boolean addCourse(String courseID, String courseName, String courseTeacher, int courseCredits, String courseTime, int courseCapacity, String courseRoom, String courseType) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject courseObject = new JSONObject();
            courseObject.put("courseID", courseID);
            courseObject.put("courseName", courseName);
            courseObject.put("courseTeacher", courseTeacher);
            courseObject.put("courseCredits", Integer.toString(courseCredits));
            courseObject.put("courseTime", courseTime);
            courseObject.put("courseCapacity", Integer.toString(courseCapacity));
            courseObject.put("courseRoom", courseRoom);
            courseObject.put("courseType", courseType);

            JSONObject request = new JSONObject();
            request.put("requestType", "addCourse");
            request.put("parameters", courseObject);

            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询课程信息。
     *
     * @param courseName    课程名称
     * @param courseTeacher 课程教师
     * @return 包含查询结果的课程信息数组
     */
    public oneCourseinfo[] searchCourses(String courseName, String courseTeacher) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject request = new JSONObject();
            request.put("requestType", "searchCourses");

            if ((courseName != null && !courseName.isEmpty()) && !(courseTeacher != null && !courseTeacher.isEmpty())) {
                request.put("parameters", new JSONObject().put("courseName", courseName));
            } else if (!(courseName != null && !courseName.isEmpty()) && (courseTeacher != null && !courseTeacher.isEmpty())) {
                request.put("parameters", new JSONObject().put("courseTeacher", courseTeacher));
            } else {
                request.put("parameters", new JSONObject().put("courseName", courseName).put("courseTeacher", courseTeacher));
            }

            out.println(request.toString());

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("courses");

            int numCourses = data.length();
            oneCourseinfo[] coursesArray = new oneCourseinfo[numCourses];

            for (int i = 0; i < numCourses; i++) {
                JSONObject course = data.getJSONObject(i);
                coursesArray[i] = new oneCourseinfo();
                coursesArray[i].courseID = course.getString("courseID");
                coursesArray[i].courseName = course.getString("courseName");
                coursesArray[i].courseTeacher = course.getString("courseTeacher");
                coursesArray[i].courseCredits = course.getInt("courseCredits");
                String pre_courseTime = course.getString("courseTime");
                coursesArray[i].courseTime = parseCourseTime(pre_courseTime);
                coursesArray[i].courseCapacity = course.getInt("courseCapacity");
                coursesArray[i].selectedCount = course.getInt("selectedCount");
                coursesArray[i].courseRoom = course.getString("courseRoom");
                coursesArray[i].courseType = course.getString("courseType");
            }

            return coursesArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
