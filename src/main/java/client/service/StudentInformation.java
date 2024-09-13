package client.service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 该类用于与服务器通信，以便查看、修改和管理学生信息。
 */
public class StudentInformation {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS; // 服务器的地址，即本地服务器
    private static final int SERVER_PORT = IpConfig.SERVER_PORT; // 定义服务器的端口号
    private static final int TIMEOUT = 5000; // 连接的超时时间：5秒超时

    /**
     * 内部类，表示单个学生的学籍信息。
     */
    public static class oneStudentInformation {
        String name;
        String id; // 学号
        String gender;
        String origin;
        String birthday;
        String academy;

        // Getters and Setters

        /**
         * 获取学生姓名。
         *
         * @return 学生姓名
         */
        public String getName() {
            return name;
        }

        /**
         * 设置学生姓名。
         *
         * @param name 学生姓名
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 获取学生学号。
         *
         * @return 学生学号
         */
        public String getId() {
            return id;
        }

        /**
         * 设置学生学号。
         *
         * @param id 学生学号
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * 获取学生性别。
         *
         * @return 学生性别
         */
        public String getGender() {
            return gender;
        }

        /**
         * 设置学生性别。
         *
         * @param gender 学生性别
         */
        public void setGender(String gender) {
            this.gender = gender;
        }

        /**
         * 获取学生籍贯。
         *
         * @return 学生籍贯
         */
        public String getOrigin() {
            return origin;
        }

        /**
         * 设置学生籍贯。
         *
         * @param origin 学生籍贯
         */
        public void setOrigin(String origin) {
            this.origin = origin;
        }

        /**
         * 获取学生生日。
         *
         * @return 学生生日
         */
        public String getBirthday() {
            return birthday;
        }

        /**
         * 设置学生生日。
         *
         * @param birthday 学生生日
         */
        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        /**
         * 获取学生学院。
         *
         * @return 学生学院
         */
        public String getAcademy() {
            return academy;
        }

        /**
         * 设置学生学院。
         *
         * @param academy 学生学院
         */
        public void setAcademy(String academy) {
            this.academy = academy;
        }
    }

    /**
     * 查看单个学生信息。
     *
     * @param id 学生的学号
     * @return oneStudentInformation 学生信息对象，如果出错则返回null
     */
    public static oneStudentInformation viewOneStudentInfo(String id) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) { // 创建一个Socket对象，并连接到指定的服务器地址和端口号
            socket.setSoTimeout(TIMEOUT); // 设置socket的超时时间

            // 构建请求JSON
            JSONObject request = new JSONObject(); // 创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "viewStudentInfo"); // 请求类型为 "viewStudentInfo"
            request.put("parameters", new JSONObject().put("id", id)); // 添加参数 "id"

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // 创建一个PrintWriter对象，用于向网络连接的输出流写入数据
            out.println(request.toString()); // 将请求转换为字符串形式，发送到服务器端

            // 输入流，从服务器读取数据
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine(); // 从输入流中读取服务器响应
            JSONObject jsonResponse = new JSONObject(response); // 将响应解析为JSON对象

            // 解析学生信息
            JSONObject studentData = jsonResponse.getJSONObject("data");
            oneStudentInformation thestudent = new oneStudentInformation();
            thestudent.name = studentData.getString("name");
            thestudent.id = studentData.getString("id");
            thestudent.gender = studentData.getString("gender");
            thestudent.origin = studentData.getString("origin");
            thestudent.birthday = studentData.getString("birthday");
            thestudent.academy = studentData.getString("academy");

            return thestudent; // 返回学生信息对象
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 出现异常时返回null
        }
    }

    /**
     * 查看所有学生信息。
     *
     * @return oneStudentInformation[] 学生信息数组，如果出错则返回null
     */
    public static oneStudentInformation[] viewAllStudentInfo() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) { // 创建一个Socket对象，并连接到指定的服务器地址和端口号
            socket.setSoTimeout(TIMEOUT); // 设置socket的超时时间

            // 构建请求JSON
            JSONObject request = new JSONObject(); // 创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "checkStudentInfo"); // 请求类型为 "checkStudentInfo"
            request.put("parameters", new JSONObject()); // 空的参数对象

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // 创建一个PrintWriter对象，用于向网络连接的输出流写入数据
            out.println(request.toString()); // 将请求转换为字符串形式，发送到服务器端

            // 输入流，从服务器读取数据
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine(); // 从输入流中读取服务器响应
            JSONObject jsonResponse = new JSONObject(response); // 将响应解析为JSON对象
            JSONObject data = jsonResponse.getJSONObject("data"); // 获取数据字段

            // 获取学生数量
            int numStudents = data.length();

            // 创建一个数组来存储学生信息
            oneStudentInformation[] studentArray = new oneStudentInformation[numStudents];

            // 遍历学生信息
            int index = 0;
            for (String key : data.keySet()) {
                JSONObject student = data.getJSONObject(key);

                studentArray[index] = new oneStudentInformation();
                studentArray[index].name = student.getString("name");
                studentArray[index].id = student.getString("id");
                studentArray[index].gender = student.getString("gender");
                studentArray[index].origin = student.getString("origin");
                studentArray[index].birthday = student.getString("birthday");
                studentArray[index].academy = student.getString("academy");

                // 打印学生信息
                System.out.println("Name: " + studentArray[index].name);
                System.out.println("ID: " + studentArray[index].id);
                System.out.println("Gender: " + studentArray[index].gender);
                System.out.println("Origin: " + studentArray[index].origin);
                System.out.println("Birthday: " + studentArray[index].birthday);
                System.out.println("Academy: " + studentArray[index].academy);

                index++;
            }

            return studentArray; // 返回学生信息数组
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 出现异常时返回null
        }
    }

    /**
     * 修改一个学生信息。
     *
     * @param id      学生的学号
     * @param theinfo 要更新的新信息的oneStudentInformation对象
     * @return 是否更新成功，成功返回true，失败返回false
     */
    public static boolean modifyOneStudentInfo(String id, oneStudentInformation theinfo) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) { // 创建一个Socket对象，并连接到指定的服务器地址和端口号
            socket.setSoTimeout(TIMEOUT); // 设置socket的超时时间

            // 创建一个 JSON 对象来存储学生信息
            JSONObject studentObject = new JSONObject();
            studentObject.put("name", theinfo.name);
            studentObject.put("id", theinfo.id);
            studentObject.put("gender", theinfo.gender);
            studentObject.put("origin", theinfo.origin);
            studentObject.put("birthday", theinfo.birthday);
            studentObject.put("academy", theinfo.academy);

            // 构建请求JSON
            JSONObject request = new JSONObject();
            request.put("requestType", "modifyStudentInfo"); // 请求类型为 "modifyStudentInfo"
            request.put("parameters", new JSONObject()
                    .put("data", studentObject) // 更新的学生信息
                    .put("id", id)); // 学生ID

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // 创建一个PrintWriter对象，用于向网络连接的输出流写入数据
            out.println(request.toString()); // 将请求转换为字符串形式，发送到服务器端

            // 输入流，从服务器读取数据
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            if (response == null || response.isEmpty()) {
                System.err.println("Received null or empty response from server.");
                return false;
            }

            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getString("status").equals("success"); // 判断操作是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false; // 出现异常时返回false
        }
    }
}
