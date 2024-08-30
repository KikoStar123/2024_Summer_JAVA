package client.service;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

import client.service.User;
import java.time.LocalDate;
import java.time.Period;//方便计算生日

public class ClientService {
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8080;

    public boolean login(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "login");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            if (response == null || response.isEmpty()) {
                System.err.println("Received null or empty response from server.");
                return false;
            }

            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    //再写一个返回用户信息的方法
    public User login_return(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "login_return");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
                // 假设服务器返回一个User对象的JSON表示
                JSONObject userJson = jsonResponse.getJSONObject("user"); // 获取用户信息
                String userName = userJson.getString("username");//***
             //   String userEmail = userJson.getString("email"); // 示例属性
               // String userName = userJson.getString("username");
                Gender gender = Gender.valueOf(userJson.getString("gender").toLowerCase()); // 性别***
                Role role = Role.valueOf(userJson.getString("role").toLowerCase()); // 角色***
                int age = userJson.getInt("age"); // 年龄***
                // 假设User类有一个构造函数接受用户名和电子邮件
                return new User(userName,role, age, gender,password); // 返回User对象
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 登录失败，返回null
    }

//    public boolean logout(String username, String password) {//登出请求
//        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
//            JSONObject request = new JSONObject();
//            request.put("requestType", "logout");
//            request.put("parameters", new JSONObject()
//                    .put("username", username)
//                    .put("password", password));
//
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            out.println(request.toString());
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String response = in.readLine();
//            JSONObject jsonResponse = new JSONObject(response);
//
//            return jsonResponse.getString("status").equals("success");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    //第一个返回学籍信息******学籍管理模块需要
    //第二个要返回用户信息
//    public boolean register(String username,Gender gender,String origin,String birthday,String academy) {//定义注册功能
//        //六个
//        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
//            // 创建请求
//            JSONObject request = new JSONObject();
//            request.put("requestType", "register");//main difference
//            request.put("parameters", new JSONObject()
//                    .put("username", username)
//                    .put("gender",gender)
//                    .put("birthday", birthday)
//                    .put("academy", academy)
//                    .put("origin", origin)
//            );
//
//            // 发送请求
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            out.println(request.toString());
//
//            // 接收响应
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String response = in.readLine();
//            JSONObject jsonResponse = new JSONObject(response);
//
//            // 返回注册结果
//            return jsonResponse.getString("status").equals("success");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    public User register(String truename, Gender gender, String origin, String birthday, String academy, String pwd, String id) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // 创建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "register");
            request.put("parameters", new JSONObject()
                    .put("truename", truename)
                    .put("gender", gender.toString())
                    .put("birthday", birthday)
                    .put("academy", academy)
                    .put("origin", origin)
                    .put("pwd", pwd) // 密码字段
                    .put("id", id) // 添加学号字段
                    .put("age",calculateAge(birthday))//添加年龄字段
            );

            // 发送请求和接收响应的代码保持不变...
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            // 接收响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 检查响应状态
            if (jsonResponse.getString("status").equals("success")) {
                // 从响应中构造 User 对象并返回
                User user = new User();
                user.setTruename(jsonResponse.getString("truename"));
                user.setUsername(jsonResponse.getString("username")); // 根据实际业务逻辑调整
                user.setGender(Gender.valueOf(jsonResponse.getString("gender")));
                user.setRole(Role.valueOf(jsonResponse.getString("role")));
                user.setPwd(jsonResponse.getString("pwd"));
                int age = calculateAge(birthday);
                user.setAge(age);
                return user;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private int calculateAge(String birthday) {
        // 将生日字符串转换为 LocalDate 对象
        LocalDate birthDate = LocalDate.parse(birthday);

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 计算两个日期之间的时间段
        Period period = Period.between(birthDate, currentDate);

        // 返回年龄
        return period.getYears();
    }
    //主界面需要使用
//    public User register_user(String username, String password) {
//        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
//            JSONObject request = new JSONObject();
//            request.put("requestType", "register_user");
//            request.put("parameters", new JSONObject()
//                    .put("username", username)
//                    .put("password", password));
//
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            out.println(request.toString());
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String response = in.readLine();
//            JSONObject jsonResponse = new JSONObject(response);
////返回string类型的account：
//            if (jsonResponse.getString("status").equals("success")) {
//                // 假设服务器返回一个User对象的JSON表示
//                JSONObject userJson = jsonResponse.getJSONObject("user"); // 获取用户信息
//                String userName = userJson.getString("username");
//                //   String userEmail = userJson.getString("email"); // 示例属性
//                // String userName = userJson.getString("username");
//                Gender gender = Gender.valueOf(userJson.getString("gender").toLowerCase()); // 性别
//                Role role = Role.valueOf(userJson.getString("role").toLowerCase()); // 角色
//                int age = userJson.getInt("age"); // 年龄
//                // 假设User类有一个构造函数接受用户名和电子邮件
//                return new User(userName,role, age, gender,password); // 返回User对象
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null; // 登录失败，返回null
//    }
    public boolean register_out(String username, String password) {//定义注销功能
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // 创建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "register_out");//main difference
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            // 接收响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            // 返回注册结果
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
//    public boolean hasPermission(String username, String permission) {//定义授权功能
//        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
//            JSONObject request = new JSONObject();
//            request.put("requestType", "checkPermission");//check user permission
//            JSONObject parameters = new JSONObject();
//            parameters.put("username", username);
//            parameters.put("permission", permission);
//            request.put("parameters", parameters);
//
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            out.println(request.toString());
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String response = in.readLine();
//            JSONObject jsonResponse = new JSONObject(response);
//
//            return jsonResponse.getBoolean("hasPermission");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    //修改密码的前端请求
public boolean updatePassword(String username, String oldPassword, String newPassword) {
    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
        // 创建请求
        JSONObject request = new JSONObject();
        request.put("requestType", "update_password"); // 明确请求类型为更新密码
        request.put("parameters", new JSONObject()
                .put("username", username)
                .put("oldPassword", oldPassword) // 添加旧密码参数
                .put("newPassword", newPassword)); // 添加新密码参数

        // 发送请求
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(request.toString());

        // 接收响应
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
        JSONObject jsonResponse = new JSONObject(response);

        // 返回密码更新结果
        return jsonResponse.getString("status").equals("success");
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}
}
