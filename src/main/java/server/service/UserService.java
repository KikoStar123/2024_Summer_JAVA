package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import client.service.User;
import client.service.Role;
import client.service.Gender;
public class UserService {

    public boolean login(String username, String password) {
        boolean isAuthenticated = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return isAuthenticated;
        }

        String query = "SELECT COUNT(*) FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    isAuthenticated = true;
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
        }

        return isAuthenticated;
    }

    public User login_return(String username, String password) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        String query = "SELECT ID, USERNAME, ROLE, AGE, GENDER, PWD FROM USERS WHERE USERNAME = ? AND PWD = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // 假设User类有一个构造函数，接受用户名、密码、年龄、性别和角色作为参数
                    String id = resultSet.getString("ID");
                    String pwd = resultSet.getString("PWD");
                    Role role = Role.valueOf(resultSet.getString("ROLE"));
                    int age = resultSet.getInt("AGE");
                    Gender gender = Gender.valueOf(resultSet.getString("GENDER"));
                    return new User(username, role, age, gender, pwd);
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
        }

        return null; // 如果没有找到用户或发生异常，返回null
    }
    public String logout(JSONObject parameters) {
        JSONObject jsonResponse = new JSONObject();

        try {
            String username = parameters.getString("username");
            if (username != null && !username.isEmpty()) {
                jsonResponse.put("status", "success").put("message", "Logout successful");
            } else {
                jsonResponse.put("status", "fail").put("message", "Invalid username");
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error").put("message", "An error occurred during logout");
        }

        return jsonResponse.toString();
    }
}
