package client.service;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;
import java.time.LocalDate;
import java.time.Period;

public class ClientService {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

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

            //System.out.println("client receive: " + jsonResponse.toString());

            if (jsonResponse.getString("status").equals("success")) {
                // 假设服务器返回一个User对象的JSON表示
                JSONObject userJson = jsonResponse.getJSONObject("user"); // 获取用户信息

                String userName = userJson.getString("username");
             //   String userEmail = userJson.getString("email"); // 示例属性
               // String userName = userJson.getString("username");
                Gender gender = Gender.valueOf(userJson.getString("gender").toLowerCase()); // 性别
                Role role = Role.valueOf(userJson.getString("role")); // 角色
                int age = userJson.getInt("age"); // 年龄
                // 假设User类有一个构造函数接受用户名和电子邮件
                return new User(userName,role, age, gender,password); // 返回User对象
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 登录失败，返回null
    }

    public boolean logout(String username, String password) {//登出请求
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "logout");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //第一个返回学籍信息
    //第二个要返回用户信息
    public static int calculateAge(String birthday) {
        try {
            LocalDate birthDate = LocalDate.parse(birthday);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use 'yyyy-mm-dd'.");
            return -1; // Return -1 or throw an exception to indicate an error
        }
    }
    public static JSONObject register(String truename, Gender gender, String stuid, String origin, String birthday, String academy, String password, String email) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // 创建请求
            int age =calculateAge(birthday);
            JSONObject request = new JSONObject();
            request.put("requestType", "register");
            request.put("parameters", new JSONObject()
                    .put("truename", truename) // 用户的真实姓名
                    .put("gender", gender.toString()) // 用户的性别
                    .put("stuid", stuid) // 学生的ID
                    .put("origin", origin) // 用户的籍贯
                    .put("birthday", birthday) // 用户的生日
                    .put("academy", academy) // 用户所在的学院
                    .put("password", password) // 用户的密码
                    .put("age", age)
                    .put("email", email)
            );

            // 发送请求
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            // 接收响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public User register_user(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "register_user");
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
                String userName = userJson.getString("username");
                //   String userEmail = userJson.getString("email"); // 示例属性
                // String userName = userJson.getString("username");
                Gender gender = Gender.valueOf(userJson.getString("gender").toLowerCase()); // 性别
                Role role = Role.valueOf(userJson.getString("role").toLowerCase()); // 角色
                int age = userJson.getInt("age"); // 年龄
                // 假设User类有一个构造函数接受用户名和电子邮件
                return new User(userName,role, age, gender,password); // 返回User对象
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 登录失败，返回null
    }
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
    public boolean hasPermission(String username, String permission) {//定义授权功能
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "checkPermission");//check user permission
            JSONObject parameters = new JSONObject();
            parameters.put("username", username);
            parameters.put("permission", permission);
            request.put("parameters", parameters);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getBoolean("hasPermission");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean update(String username, String password) {//定义注销功能
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            // 创建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "update");//main difference
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("password", password));
            //应该还要更新学籍信息，图书馆借阅，用户账号信息等等。
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
    //修改密码调用的函数
    public boolean updateUserPwd(String username, String oldPwd, String newPwd) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "updateUserPwd");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("oldPwd", oldPwd)
                    .put("newPwd", newPwd));

            System.out.println(request.toString());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            System.out.println(jsonResponse.toString());

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("密码修改成功！");
            } else {
                System.out.println("密码修改失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
    //通过用户名来获取邮箱函数----方便重置密码
    public String getEmailByUsername(String username) {
        //lzy,here;
        String email = null;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getEmailByUsername");
            request.put("parameters", new JSONObject()
                    .put("username", username));

            System.out.println(request.toString());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            System.out.println(jsonResponse.toString());

            email=jsonResponse.getString("email");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return email;
    }

    public boolean forgetUserPwd(String username, String newPwd) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "forgetUserPwd");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("newPwd", newPwd));

            System.out.println(request.toString());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            System.out.println(jsonResponse.toString());

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("密码重置成功！");
            } else {
                System.out.println("密码重置失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

}
