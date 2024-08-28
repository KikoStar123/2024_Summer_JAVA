package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseService {

    public boolean enrollInCourse(String studentId, String courseId) {
        boolean isEnrolled = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return isEnrolled;
        }

        String query = "INSERT INTO ENROLLMENTS (STUDENT_ID, COURSE_ID) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, courseId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                isEnrolled = true;
                System.out.println("Student enrolled in course successfully.");
            } else {
                System.out.println("Failed to enroll student in course.");
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

        return isEnrolled;
    }

    public JSONObject viewEnrolledCourses(String studentId) {
        JSONObject coursesJson = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        String query = "SELECT C.COURSE_ID, C.COURSE_NAME FROM COURSES C " +
                "INNER JOIN ENROLLMENTS E ON C.COURSE_ID = E.COURSE_ID " +
                "WHERE E.STUDENT_ID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    JSONObject courseJson = new JSONObject();
                    courseJson.put("courseId", resultSet.getString("COURSE_ID"));
                    courseJson.put("courseName", resultSet.getString("COURSE_NAME"));
                    coursesJson.append("courses", courseJson);
                }
                System.out.println("Retrieved enrolled courses for student.");
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

        return coursesJson;
    }

    public boolean dropCourse(String studentId, String courseId) {
        boolean isDropped = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return isDropped;
        }

        String query = "DELETE FROM ENROLLMENTS WHERE STUDENT_ID = ? AND COURSE_ID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, courseId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                isDropped = true;
                System.out.println("Student dropped course successfully.");
            } else {
                System.out.println("Failed to drop course.");
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

        return isDropped;
    }
}
