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

        lock.lock(); // 获取锁
        try {
            String query = "SELECT COUNT(*) FROM tblUser WHERE USERNAME = ? AND PWD = ?";

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
            lock.unlock(); // 释放锁
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

        lock.lock(); // 获取锁
        try {
            String query = "SELECT * FROM tblUser WHERE username = ? AND pwd = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                // 输出接收到的用户名和密码
                System.out.println("Checking login for username: " + username + " with password: " + password);

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println("User authenticated successfully.");

                        // 构建用户信息的JSON对象
                        userJson = new JSONObject();
                        userJson.put("id", resultSet.getString("username"));
                        userJson.put("username", resultSet.getString("truename"));
                        userJson.put("password", resultSet.getString("pwd"));
                        userJson.put("age", resultSet.getInt("age"));
                        userJson.put("role", resultSet.getString("role"));
                        userJson.put("gender", resultSet.getString("gender"));
                    } else {
                        System.out.println("Authentication failed.");
                    }
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
            lock.unlock(); // 释放锁
        }

        return userJson;
    }

    public String logout(JSONObject parameters) {
        JSONObject jsonResponse = new JSONObject();
        lock.lock(); // 获取锁
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
        } finally {
            lock.unlock(); // 释放锁
        }

        return jsonResponse.toString();
    }
}
