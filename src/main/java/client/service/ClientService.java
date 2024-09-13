package client.service;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;
import java.time.LocalDate;
import java.time.Period;

/**
 * 客户端服务类，用于与服务器进行通信并处理用户登录、注册、权限检查等操作。
 */
public class ClientService {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    /**
     * 用户登录方法。
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果登录成功返回 true，否则返回 false
     */
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
                System.err.println("接收到的服务器响应为空。");
                return false;
            }

            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 用户登录并返回用户信息的方法。
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果登录成功返回 User 对象，否则返回 null
     */
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
                JSONObject userJson = jsonResponse.getJSONObject("user");
                String userName = userJson.getString("username");
                Gender gender = Gender.valueOf(userJson.getString("gender").toLowerCase());
                Role role = Role.valueOf(userJson.getString("role"));
                int age = userJson.getInt("age");
                return new User(userName, role, age, gender, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用户登出方法。
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果登出成功返回 true，否则返回 false
     */
    public boolean logout(String username, String password) {
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

    /**
     * 根据出生日期计算用户年龄。
     *
     * @param birthday 出生日期，格式为 'yyyy-mm-dd'
     * @return 年龄，如果日期格式无效，返回 -1
     */
    public static int calculateAge(String birthday) {
        try {
            LocalDate birthDate = LocalDate.parse(birthday);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (Exception e) {
            System.out.println("无效的日期格式，请使用 'yyyy-mm-dd' 格式。");
            return -1;
        }
    }

    /**
     * 用户注册方法。
     *
     * @param truename 用户真实姓名
     * @param gender   性别
     * @param stuid    学生ID
     * @param origin   籍贯
     * @param birthday 生日
     * @param academy  学院
     * @param password 密码
     * @param email    邮箱
     * @return 服务器返回的 JSON 响应对象
     */
    public static JSONObject register(String truename, Gender gender, String stuid, String origin, String birthday, String academy, String password, String email) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            int age = calculateAge(birthday);
            JSONObject request = new JSONObject();
            request.put("requestType", "register");
            request.put("parameters", new JSONObject()
                    .put("truename", truename)
                    .put("gender", gender.toString())
                    .put("stuid", stuid)
                    .put("origin", origin)
                    .put("birthday", birthday)
                    .put("academy", academy)
                    .put("password", password)
                    .put("age", age)
                    .put("email", email));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            return new JSONObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户名和密码注册用户。
     *
     * @param username 用户名
     * @param password 密码
     * @return 注册成功后返回 User 对象，失败则返回 null
     */
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
                JSONObject userJson = jsonResponse.getJSONObject("user");
                String userName = userJson.getString("username");
                Gender gender = Gender.valueOf(userJson.getString("gender").toLowerCase());
                Role role = Role.valueOf(userJson.getString("role").toLowerCase());
                int age = userJson.getInt("age");
                return new User(userName, role, age, gender, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用户注销方法。
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果注销成功返回 true，否则返回 false
     */
    public boolean register_out(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "register_out");
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

    /**
     * 检查用户权限。
     *
     * @param username   用户名
     * @param permission 权限
     * @return 如果用户有权限返回 true，否则返回 false
     */
    public boolean hasPermission(String username, String permission) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "checkPermission");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("permission", permission));

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

    /**
     * 用户信息更新方法。
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果更新成功返回 true，否则返回 false
     */
    public boolean update(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "update");
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

    /**
     * 修改用户密码。
     *
     * @param username 用户名
     * @param oldPwd   旧密码
     * @param newPwd   新密码
     * @return 如果密码修改成功返回 true，否则返回 false
     */
    public boolean updateUserPwd(String username, String oldPwd, String newPwd) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "updateUserPwd");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("oldPwd", oldPwd)
                    .put("newPwd", newPwd));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

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

    /**
     * 根据用户名获取用户邮箱地址，用于重置密码。
     *
     * @param username 用户名
     * @return 用户的邮箱地址
     */
    public String getEmailByUsername(String username) {
        String email = null;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getEmailByUsername");
            request.put("parameters", new JSONObject()
                    .put("username", username));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            email = jsonResponse.getString("email");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return email;
    }

    /**
     * 忘记密码时重置用户密码。
     *
     * @param username 用户名
     * @param newPwd   新密码
     * @return 如果密码重置成功返回 true，否则返回 false
     */
    public boolean forgetUserPwd(String username, String newPwd) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "forgetUserPwd");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("newPwd", newPwd));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

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
