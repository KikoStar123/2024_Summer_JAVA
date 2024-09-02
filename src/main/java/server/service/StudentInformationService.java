package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StudentInformationService {

    private final Lock lock = new ReentrantLock();

    public JSONObject checkStudentInfo() {
        JSONObject studentInfo = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return studentInfo;
        }

        lock.lock(); // 获取锁
        try {
            String query = "SELECT * FROM tblStudent s INNER JOIN tblUser u ON s.username = u.username";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int index = 1;
            while (rs.next()) {
                JSONObject student = new JSONObject();
                student.put("id", rs.getString("studentId"));
                student.put("name", rs.getString("truename"));
                student.put("gender", rs.getString("gender"));
                student.put("origin", rs.getString("origin"));
                student.put("birthday", rs.getString("birthday"));
                student.put("academy", rs.getString("academy"));

                studentInfo.put(String.valueOf(index), student);
                index++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

        return studentInfo;
    }

    public JSONObject viewStudentInfo(String id) {
        JSONObject student = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return student;
        }

        lock.lock(); // 获取锁
        try {
            String query = "SELECT * FROM tblStudent s INNER JOIN tblUser u ON s.username = u.username WHERE u.username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.println(rs.toString());
            if (rs.next()) {
                student.put("id", rs.getString("studentid"));
                student.put("name", rs.getString("truename"));
                student.put("gender", rs.getString("gender"));
                student.put("origin", rs.getString("origin"));
                student.put("birthday", rs.getString("birthday"));
                student.put("academy", rs.getString("academy"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        System.out.println(student.toString());
        return student;
    }

    public boolean modifyStudentInfo(JSONObject studentInfo) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return false;
        }

        lock.lock(); // 获取锁
        try {
            // Update tblUser information
            String updateUserQuery = "UPDATE tblUser SET truename = ?, gender = ? WHERE username = (SELECT username FROM tblStudent WHERE studentId = ?)";
            PreparedStatement updateUserStmt = conn.prepareStatement(updateUserQuery);
            JSONObject studentData = studentInfo.getJSONObject("data");
            updateUserStmt.setString(1, studentData.getString("name"));
            updateUserStmt.setString(2, studentData.getString("gender"));
            updateUserStmt.setString(3, studentData.getString("id"));
            int userRowsAffected = updateUserStmt.executeUpdate();

            String updateStudentQuery = "UPDATE tblStudent SET origin = ?, birthday = ?, academy = ? WHERE studentId = ?";
            PreparedStatement updateStudentStmt = conn.prepareStatement(updateStudentQuery);
            updateStudentStmt.setString(1, studentData.getString("origin"));
            updateStudentStmt.setString(2, studentData.getString("birthday"));
            updateStudentStmt.setString(3, studentData.getString("academy"));
            updateStudentStmt.setString(4, studentData.getString("id"));
            int studentRowsAffected = updateStudentStmt.executeUpdate();

            return userRowsAffected > 0 && studentRowsAffected > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
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
    }


    public boolean createStudent(String studentId, String origin, String birthday, String academy, String username) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return false;
        }

        lock.lock(); // 获取锁
        try {
            String query = "INSERT INTO tblStudent (studentId, origin, birthday, academy, username) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, origin);
            preparedStatement.setString(3, birthday);
            preparedStatement.setString(4, academy);
            preparedStatement.setString(5, username);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student created successfully.");
                return true;
            } else {
                System.out.println("Failed to create student.");
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
            lock.unlock(); // 释放锁
        }
    }
}
