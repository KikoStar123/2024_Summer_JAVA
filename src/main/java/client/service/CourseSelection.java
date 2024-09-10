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
import java.util.function.Function;

public class CourseSelection {
    private final String SERVER_ADDRESS = "localhost";//服务器的地址 即本地服务器
    private final int SERVER_PORT = 8080;//定义服务器的端口号

    public class onePeriod//课程时间:周次、周几、节次
    {
        int staWeek;//起始周
        int endWeek;//结束周
        int Day;//周几
        int stasection;//起始节次
        int endsection;//结束节次

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
        @Override
        public String toString() {
            return  getStaWeek() + "-" + getEndWeek()+ " 周"   + "，" + convertDayToInt(getDay()) +" , " + getStasection() + "-" + getEndsection()+ " 节 ";
        }

        // 将数字转换为星期几的名称
        private String convertDayToInt(int day) {
            String[] days = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
            return days[day - 1];
        }
    }

    public class oneCourseinfo
    {
        String courseID;//课程id
        String courseName;//课程名称
        String courseTeacher;//课程教师
        int courseCredits;//课程学分
        onePeriod[] courseTime=new onePeriod[0];//课程时间（周次、周几、节次）
        int courseCapacity;//课程最大容量
        int selectedCount;//已选人数

        String courseRoom;// 添加课程教室
        String courseType; // 添加课程类型

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
     * 解析课程时间字符串
     * 使用Lambda表达式
     * @
     * @return onePeriod
     */
    public onePeriod[] parseCourseTime(String timeString) {
        //System.out.println("Course time string: " + timeString);
        List<onePeriod> thePeriods = new ArrayList<>();

        // 按";"分割时间段
        String[] timeSegments = timeString.split(";");
        for (String segment : timeSegments) {
            String[] parts = segment.split("\\|");
            if (parts.length != 3) {
                // 如果部分数量不正确，跳过当前段落
                System.err.println("Invalid segment format: " + segment);
                continue;
            }

            onePeriod thePeriod = new onePeriod();
            try {
                // 解析周次
                String[] weeks = parts[0].split("-");
                if (weeks.length != 2) throw new IllegalArgumentException("Invalid week range: " + parts[0]);
                thePeriod.staWeek = Integer.parseInt(weeks[0]);
                thePeriod.endWeek = Integer.parseInt(weeks[1]);

                // 解析星期几
                thePeriod.Day = Integer.parseInt(parts[1]);

                // 解析节次
                String[] periods = parts[2].split("-");
                if (periods.length != 2) throw new IllegalArgumentException("Invalid period range: " + parts[2]);
                thePeriod.stasection = Integer.parseInt(periods[0]);
                thePeriod.endsection = Integer.parseInt(periods[1]);

                thePeriods.add(thePeriod);
            } catch (NumberFormatException e) {
                System.err.println("Number format error in segment: " + segment);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage() + " in segment: " + segment);
            }
        }
        return thePeriods.toArray(new onePeriod[0]);
    }

    /**
     * 获取所有课程信息
     *
     * @
     * @
     */
    public oneCourseinfo[] GetAllCourses()
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "getAllCourses");//在请求对象中添加一个键值对，键为 "requestType"，值为 "getAllCourses"，表示获取所有课程信息
            request.put("parameters", new JSONObject());//不传递任何参数

            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            // 读取响应
            //创建了一个BufferedReader对象in，用于从网络连接的输入流读取数据
            String response = in.readLine();//从输入流中读取一行数据，并将其存储在字符串response中（假设按行发送）
            JSONObject jsonResponse = new JSONObject(response);//将字符串解析为一个JSON对象

            JSONArray data = jsonResponse.getJSONArray("courses");//获取JSON数组

            // 获取课程数量
            int numCourses = data.length();

            // 创建一个数组来存储所有课程信息
            oneCourseinfo[] coursesArray = new oneCourseinfo[numCourses];

            for (int i = 0; i < numCourses; i++) {

                JSONObject course = data.getJSONObject(i);
                coursesArray[i] = new CourseSelection.oneCourseinfo();
                coursesArray[i].courseID = course.getString("courseID");
                coursesArray[i].courseName = course.getString("courseName");
                coursesArray[i].courseTeacher = course.getString("courseTeacher");
                coursesArray[i].courseCredits = course.getInt("courseCredits");
                String pre_courseTime=course.getString("courseTime");
                coursesArray[i].courseTime=parseCourseTime(pre_courseTime);
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
     * 选课
     * 输入学生账号、课程id
     * @
     * @
     */
    public boolean enrollInCourse(String username, String courseID)
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "enrollInCourse");//在请求对象中添加一个键值对，键为 "requestType"，值为 "enrollInCourse"，表示选课请求
            request.put("parameters", new JSONObject());//传递学生账号、课程id
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("courseID", courseID));

            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 退课
     * 输入学生账号、课程id
     * @
     * @
     */
    public boolean dropCourse(String username, String courseID)
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "dropCourse");//在请求对象中添加一个键值对，键为 "requestType"，值为 "dropCourse"，表示退课请求
            request.put("parameters", new JSONObject());//传递学生账号、课程id
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("courseID", courseID));

            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查看某个学生的已选课程信息
     *
     * @
     * @
     */
    public oneCourseinfo[] viewEnrolledCourses(String username)
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "viewEnrolledCourses");//在请求对象中添加一个键值对，键为 "requestType"，值为 "viewEnrolledCourses"，表示查看已选课程信息
            request.put("parameters", new JSONObject().put("username", username));//不传递任何参数

            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            // 读取响应
            //创建了一个BufferedReader对象in，用于从网络连接的输入流读取数据
            String response = in.readLine();//从输入流中读取一行数据，并将其存储在字符串response中（假设按行发送）
            JSONObject jsonResponse = new JSONObject(response);//将字符串解析为一个JSON对象

            if(jsonResponse.getString("status").equals("fail")) return null;

            JSONArray data = jsonResponse.getJSONArray("courses");//获取JSON数组

            // 获取课程数量
            int numCourses = data.length();

            // 创建一个数组来存储所有课程信息
            oneCourseinfo[] coursesArray = new oneCourseinfo[numCourses];

            for (int i = 0; i < numCourses; i++) {

                JSONObject course = data.getJSONObject(i);
                coursesArray[i] = new CourseSelection.oneCourseinfo();
                coursesArray[i].courseID = course.getString("courseID");
                coursesArray[i].courseName = course.getString("courseName");
                coursesArray[i].courseTeacher = course.getString("courseTeacher");
                coursesArray[i].courseCredits = course.getInt("courseCredits");
                String pre_courseTime=course.getString("courseTime");
                coursesArray[i].courseTime=parseCourseTime(pre_courseTime);
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
     * 查看单个课程的详细信息
     *
     * @
     * @
     */
    public oneCourseinfo viewCourseInfo(String courseID)
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "viewCourseInfo");//在请求对象中添加一个键值对，键为 "requestType"，值为 "viewCourseInfo"
            request.put("parameters", new JSONObject().put("courseID", courseID));//不传递任何参数

            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            // 读取响应
            //创建了一个BufferedReader对象in，用于从网络连接的输入流读取数据
            String response = in.readLine();//从输入流中读取一行数据，并将其存储在字符串response中（假设按行发送）
            JSONObject jsonResponse = new JSONObject(response);//将字符串解析为一个JSON对象
            JSONObject data = jsonResponse.getJSONObject("courseInfo");


            oneCourseinfo thecourse = new CourseSelection.oneCourseinfo();

            thecourse.courseID = data.getString("courseID");
            thecourse.courseName = data.getString("courseName");
            thecourse.courseTeacher = data.getString("courseTeacher");
            thecourse.courseCredits = data.getInt("courseCredits");
            String pre_courseTime=data.getString("courseTime");
            thecourse.courseTime=parseCourseTime(pre_courseTime);
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
     * 添加一门课程
     *
     * @
     * @
     */
    public boolean addCourse(String courseID, String courseName, String courseTeacher, int courseCredits, String courseTime, int courseCapacity,String courseRoom, String courseType)
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 创建一个空的 JSON 对象
            JSONObject courseObject = new JSONObject();
            courseObject.put("courseID", courseID);
            courseObject.put("courseName", courseName);
            courseObject.put("courseTeacher", courseTeacher);
            courseObject.put("courseCredits", Integer.toString(courseCredits));
            courseObject.put("courseTime", courseTime);
            courseObject.put("courseCapacity", Integer.toString(courseCapacity));
            courseObject.put("courseRoom", courseRoom);
            courseObject.put("courseType", courseType);

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "addCourse");//在请求对象中添加一个键值对，键为 "requestType"，值为 "addCourse"
            request.put("parameters", courseObject);//传递课程信息

            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所需课程信息
     *
     * @
     * @
     */
    public oneCourseinfo[] searchCourses(String courseName, String courseTeacher)
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "searchCourses");//在请求对象中添加一个键值对，键为 "requestType"，值为 "searchCourses"，表示查询所需课程信息
            //仅有课程名
            if ((courseName != null && !courseName.isEmpty())&&!(courseTeacher != null && !courseTeacher.isEmpty())) {
                request.put("parameters", new JSONObject().put("courseName", courseName));
            }
            //仅有教师名
            else if (!(courseName != null && !courseName.isEmpty())&&(courseTeacher != null && !courseTeacher.isEmpty())) {
                request.put("parameters", new JSONObject().put("courseTeacher", courseTeacher));
            }
            //又有课程名又有教师名
            else{
                request.put("parameters", new JSONObject()
                        .put("courseName", courseName)
                        .put("courseTeacher", courseTeacher));
            }

            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            // 读取响应
            //创建了一个BufferedReader对象in，用于从网络连接的输入流读取数据
            String response = in.readLine();//从输入流中读取一行数据，并将其存储在字符串response中（假设按行发送）
            JSONObject jsonResponse = new JSONObject(response);//将字符串解析为一个JSON对象

            JSONArray data = jsonResponse.getJSONArray("courses");//获取JSON数组

            // 获取课程数量
            int numCourses = data.length();

            // 创建一个数组来存储所有课程信息
            oneCourseinfo[] coursesArray = new oneCourseinfo[numCourses];

            for (int i = 0; i < numCourses; i++) {

                JSONObject course = data.getJSONObject(i);
                coursesArray[i] = new CourseSelection.oneCourseinfo();
                coursesArray[i].courseID = course.getString("courseID");
                coursesArray[i].courseName = course.getString("courseName");
                coursesArray[i].courseTeacher = course.getString("courseTeacher");
                coursesArray[i].courseCredits = course.getInt("courseCredits");
                String pre_courseTime=course.getString("courseTime");
                coursesArray[i].courseTime=parseCourseTime(pre_courseTime);
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
