package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentInformationService {
    public JSONObject checkStudentInfo() {
        JSONObject studentInfo = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return studentInfo;
        }

        try {
            String query = "SELECT * FROM studentInfo";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            int index = 1;
            while (rs.next()) {
                JSONObject student = new JSONObject();
                student.put("id", rs.getString("id"));
                student.put("name", rs.getString("name"));
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

        try {
            String query = "SELECT * FROM studentInfo WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                student.put("id", rs.getString("id"));
                student.put("name", rs.getString("name"));
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
        }

        return student;
    }

    public boolean modifyStudentInfo(JSONObject studentInfo) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return false;
        }

        try {
            String query = "UPDATE studentInfo SET name = ?, gender = ?, origin = ?, birthday = ?, academy = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            JSONObject studentData = studentInfo.getJSONObject("data");
            pstmt.setString(1, studentData.getString("name"));
            pstmt.setString(2, studentData.getString("gender"));
            pstmt.setString(3, studentData.getString("origin"));
            pstmt.setString(4, studentData.getString("birthday"));
            pstmt.setString(5, studentData.getString("academy"));
            pstmt.setString(6, studentData.getString("id"));
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
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
        }
    }
}
