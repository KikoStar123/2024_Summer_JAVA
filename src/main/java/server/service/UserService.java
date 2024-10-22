package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserService {

    private final Lock lock = new ReentrantLock();

    public boolean login(String username, String password) {
        boolean isAuthenticated = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return isAuthenticated;
        }

        String query = "SELECT COUNT(*) FROM tblUser WHERE USERNAME = ? AND PWD = ?";

        lock.lock();
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            // 输出接收到的用户名和密码
            System.out.println("Checking login for username: " + username + " with password: " + password);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    isAuthenticated = true;
                    System.out.println("User authenticated successfully.");
                } else {
                    System.out.println("Authentication failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            lock.unlock();
        }

        return isAuthenticated;
    }
    public JSONObject loginReturn(String username, String password) {
        JSONObject userJson = null;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        String query = "SELECT * FROM tblUser WHERE username = ? AND pwd = ?";

        lock.lock();

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            System.out.println("Checking login for username: " + username + " with password: " + password);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("User authenticated successfully.");
                    userJson = new JSONObject();
                    userJson.put("username", resultSet.getString("username"));
                    userJson.put("truename", resultSet.getString("truename"));
                    userJson.put("password", resultSet.getString("pwd"));
                    userJson.put("age", resultSet.getInt("age"));
                    userJson.put("role", resultSet.getString("role"));
                    userJson.put("gender", resultSet.getString("gender"));
                } else {
                    System.out.println("Authentication failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            lock.unlock();
        }
        return userJson;
    }

    public JSONObject register(JSONObject parameters){
        String truename = parameters.getString("truename");
        String gender = parameters.getString("gender");
        String birthday = parameters.getString("birthday");
        String origin = parameters.getString("origin");
        String pwd = parameters.getString("password");
        String academy = parameters.getString("academy");
        String studentId = parameters.getString("stuid");
        int age = parameters.getInt("age");
        String email = parameters.getString("email");

        String allocatedId = null;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        lock.lock();

        String query = "SELECT MAX(CAST(username AS INT)) AS max_username FROM tblUser";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int maxUsername = resultSet.getInt("max_username");
                int newUsername = maxUsername + 1;

                // Padding with zeros to ensure 9 digits
                allocatedId = String.valueOf(newUsername);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            lock.unlock();
        }

        boolean newUser = createUser(allocatedId, truename, "student", age, pwd, gender,email);

        StudentInformationService studentInformationService = new StudentInformationService();
        boolean newStudent = studentInformationService.createStudent(studentId, origin, birthday, academy, allocatedId);

        JSONObject userJson = new JSONObject();
        System.out.println("newuser:"+(newUser ? "yes" : "no") );
        System.out.println("newstu:"+(newStudent ? "yes" : "no" ));
        if(newUser && newStudent){
            userJson.put("username", allocatedId);
            userJson.put("truename", truename);
            userJson.put("role", "student");
            userJson.put("gender", gender);
            userJson.put("pwd", pwd);
            userJson.put("age", pwd);
            userJson.put("email", email);
        }

        return userJson;
    }

    private boolean createUser(String username, String truename, String role, int age, String pwd, String gender,String email) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return false;
        }

        String query = "INSERT INTO tblUser (username, truename, role, age, pwd, gender,email) VALUES (?, ?, ?, ?, ?, ?,?)";

        lock.lock();

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, truename);
            preparedStatement.setString(3, role);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, pwd);
            preparedStatement.setString(6, gender);
            preparedStatement.setString(7, email);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User created successfully.");
                return true;
            } else {
                System.out.println("Failed to create user.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            lock.unlock();
        }
    }
    public JSONObject updateUserPwd(String username, String oldPwd, String newPwd) {//更改用户密码****
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();

        try {
            String query = "UPDATE tblUser SET pwd = ? WHERE username = ? AND pwd = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newPwd);
            stmt.setString(2, username);
            stmt.setString(3, oldPwd);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                response.put("status", "success");
                response.put("message", "Password updated successfully.");
            } else {
                response.put("status", "error");
                response.put("message", "Old password is incorrect.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
            lock.unlock();
        }

        return response;
    }
    public String logout(JSONObject parameters) {
        JSONObject jsonResponse = new JSONObject();

        try {
            String username = parameters.getString("username");
            if (username != null && !username.isEmpty()) {
                jsonResponse.put("status", "success").put("message", "Logout successful");
                System.out.println("Logout successful for username: " + username);
            } else {
                jsonResponse.put("status", "fail").put("message", "Invalid username");
                System.out.println("Logout failed: Invalid username");
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error").put("message", "An error occurred during logout");
            System.out.println("An error occurred during logout: " + e.getMessage());
        }

        return jsonResponse.toString();
    }

    public JSONObject forgetPwd(String username, String newPwd) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();

        try {
            String query = "UPDATE tblUser SET pwd = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newPwd);
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                response.put("status", "success");
                response.put("message", "Password reset successfully.");
            } else {
                response.put("status", "error");
                response.put("message", "Username not found.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
            lock.unlock();
        }

        return response;
    }


    public JSONObject getEmailByUsername(String username) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        try {
            String query = "SELECT email FROM tblUser WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                response.put("status", "success");
                response.put("email", rs.getString("email"));
            } else {
                response.put("status", "error");
                response.put("message", "Username not found.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
        }

        return response;
    }

}