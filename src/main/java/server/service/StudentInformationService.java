package server.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class StudentInformationService {
    public JSONObject checkStudentInfo() {
        JSONObject studentInfo = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM studentInfo";
            ResultSet rs = stmt.executeQuery(query);

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

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM studentInfo WHERE id = " + id;
            ResultSet rs = stmt.executeQuery(query);

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

        try {
            Statement stmt = conn.createStatement();
            JSONObject studentData = studentInfo.getJSONObject("data");
            String query = "UPDATE studentInfo SET " +
                    "name = '" + studentData.getString("name") + "', " +
                    "gender = '" + studentData.getString("gender") + "', " +
                    "origin = '" + studentData.getString("origin") + "', " +
                    "birthday = '" + studentData.getString("birthday") + "', " +
                    "academy = '" + studentData.getString("academy") + "' " +
                    "WHERE id = " + studentData.getString("id");
            int rowsAffected = stmt.executeUpdate(query);

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
