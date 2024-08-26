package client.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class StudentInformation {
    private final String SERVER_ADDRESS = "localhost";//服务器的地址 即本地服务器
    private final int SERVER_PORT = 8080;//定义服务器的端口号
    private final int TIMEOUT = 5000; // 连接的超时时间：5秒超时

    /**
     * 查看学生信息
     *
     * @param id 学生的ID
     * @return 学生信息的JSONObject，如果出错则返回null
     */
    public boolean viewStudentInfo(Role role, String id) {
        //确认是学生身份
        try {
            if (role != Role.student) {
                throw new Exception("The role should be a student.");
            }
        } catch (Exception e) {
            System.out.println("Caught an exception: " + e.getMessage());
            return false;
        }

        //进行正常操作
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT))//创建一个Socket对象，并连接到指定的服务器地址和端口号
        {
            socket.setSoTimeout(TIMEOUT);//设置socket的超时时间

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "viewStudentInfo");//在请求对象中添加一个键值对，键为 "requestType"，值为 "viewStudentInfo"，表示一个查看学生信息的请求
            //添加另一个键值对，键为 "parameters"，值是一个JSON对象，包含一个 "id" 键值对，其值为传入的学生对应id
            request.put("parameters", new JSONObject().put("id", id));

            // 发送请求
            // 输出流，发送请求到服务器端
            // PrintWriter是一个输出流类，将各种数据类型转换为字符串并输出到指定的输出流
            // socket.getOutputStream() 返回与该套接字关联的输出流，可以用于向远程主机发送数据
            // 传入true作为PrintWriter构造函数的第二个参数，表示自动刷新输出缓冲区，意味着数据将被立即写入到输出流中，而不会等待缓冲区填满
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//创建一个PrintWriter对象，用于向网络连接的输出流写入数据
            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            // 输入流，从服务器读取数据
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//创建了一个BufferedReader对象in，用于从网络连接的输入流读取数据
            String response = in.readLine();//从输入流中读取一行数据，并将其存储在字符串response中（假设按行发送）
            JSONObject jsonResponse = new JSONObject(response);//将字符串response解析为一个JSON对象jsonResponse

            // 读取响应
            // 从 JSON 对象中获取特定属性的值
            JSONObject studentData = jsonResponse.getJSONObject("data");
            String name = jsonResponse.getString("name");
            //int id = jsonResponse.getInt("id");
            String gender = jsonResponse.getString("gender");
            String origin = jsonResponse.getString("origin");
            String birthday = jsonResponse.getString("birthday");
            String academy = jsonResponse.getString("academy");

            // 对获取的数据进行处理
            // 展示... 不需要请求
            //System.out.println("Name: " + name);
            //System.out.println("Age: " + age);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //内部类，表示单个学生学籍
    class oneStudentInformation {
        String name;
        String id;
        String gender;
        String origin;
        String birthday;
        String academy;
    }

    /**
     * 修改学生信息
     * //@param studentId 学生的ID
     * //@param newInfo 要更新的新信息的JSONObject
     *
     * @return 如果更新成功返回true，否则返回false
     */
    public boolean modifyStudentInfo(Role role, String id) {
        //确认是教师或管理员身份
        try {
            if (role != Role.teacher || role != Role.manager) {
                throw new Exception("The role should be a teacher or a manager.");
            }
        } catch (Exception e) {
            System.out.println("Caught an exception: " + e.getMessage());
            return false;
        }

        //进行正常操作
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT))//创建一个Socket对象，并连接到指定的服务器地址和端口号
        {
            socket.setSoTimeout(TIMEOUT);//设置socket的超时时间

            //构建请求JSON
            JSONObject request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "checkStudentInfo");//查看所有学生信息

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//创建一个PrintWriter对象，用于向网络连接的输出流写入数据
            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            // 输入流，从服务器读取数据
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//创建了一个BufferedReader对象in，用于从网络连接的输入流读取数据
            String response = in.readLine();//从输入流中读取一行数据，并将其存储在字符串response中（假设按行发送）
            JSONObject jsonResponse = new JSONObject(response);//将字符串response解析为一个JSON对象jsonResponse

            JSONObject data = jsonResponse.getJSONObject("data");//取名为 "data" 的子对象

            // 获取学生数量
            int numStudents = data.length();

            // 创建一个数组来存储学生信息
            oneStudentInformation[] studentArray = new oneStudentInformation[numStudents];

            // 遍历学生信息
            int index = 0;
            for (String key : data.keySet()) {
                JSONObject student = data.getJSONObject(key);

                studentArray[index].name = student.name;
                studentArray[index].id = student.getString("id");
                studentArray[index].gender = student.getString("gender");
                studentArray[index].origin = student.getString("origin");
                studentArray[index].birthday = student.getString("birthday");
                studentArray[index].academy = student.getString("academy");

                // 对每个学生信息进行处理
                // 打印学生信息
                System.out.println("Name: " + studentArray[index].name);
                System.out.println("ID: " + studentArray[index].id);
                System.out.println("Gender: " + studentArray[index].gender);
                System.out.println("Origin: " + studentArray[index].origin);
                System.out.println("Birthday: " + studentArray[index].birthday);
                System.out.println("Academy: " + studentArray[index].academy);

                index++;
            }


            // 对每个学生信息进行处理

            // 点击修改

            // 将修改传回数据库
            index = 0;//发生修改的
            // 创建一个空的 JSON 对象
            JSONObject studentObject = new JSONObject();
            studentObject.put("name", studentArray[index].name);
            studentObject.put("id", studentArray[index].id);
            studentObject.put("gender", studentArray[index].gender);
            studentObject.put("origin", studentArray[index].origin);
            studentObject.put("birthday", studentArray[index].birthday);
            studentObject.put("academy", studentArray[index].academy);

            JSONObject modifiyData = new JSONObject();
            modifiyData.put("data", studentObject);

            //构建请求JSON
            request = new JSONObject();//创建了一个新的 JSON对象request，用于存储整个请求的内容
            request.put("requestType", "modifyStudentInfo");//修改所有学生信息
            request.put("parameters", modifiyData);//查看所有学生信息

            // 发送请求
            out = new PrintWriter(socket.getOutputStream(), true);//创建一个PrintWriter对象，用于向网络连接的输出流写入数据
            out.println(request.toString());//将请求request转换为字符串形式，并使用println方法将其写入输出流中，发送到服务器端

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
