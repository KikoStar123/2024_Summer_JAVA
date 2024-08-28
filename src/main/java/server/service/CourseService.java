package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseService {

    // 添加课程
    public boolean addCourse(String courseID, String courseName, String courseTeacher, int courseCredits, String courseTime, int courseCapacity) {
        boolean isAdded = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return isAdded;
        }

        String insertQuery = "INSERT INTO tblCourse (courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity, selectedCount) VALUES (?, ?, ?, ?, ?, ?, 0)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, courseID);
            preparedStatement.setString(2, courseName);
            preparedStatement.setString(3, courseTeacher);
            preparedStatement.setInt(4, courseCredits);
            preparedStatement.setString(5, courseTime);
            preparedStatement.setInt(6, courseCapacity);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                isAdded = true;
                System.out.println("Course added successfully.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }

        return isAdded;
    }

    // 选课
    public boolean enrollInCourse(String username, String courseID) {
        boolean isEnrolled = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return isEnrolled;
        }

        try {
            // 检查课程是否存在及其容量是否已满
            System.out.println("Checking course capacity for courseID: " + courseID);
            String checkCourseQuery = "SELECT selectedCount, courseCapacity FROM tblCourse WHERE courseID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkCourseQuery)) {
                checkStmt.setString(1, courseID);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int selectedCount = rs.getInt("selectedCount");
                        int courseCapacity = rs.getInt("courseCapacity");
                        if (selectedCount >= courseCapacity) {
                            System.out.println("Course is full. SelectedCount: " + selectedCount + ", Capacity: " + courseCapacity);
                            return isEnrolled;
                        }
                    } else {
                        System.out.println("Course not found.");
                        return isEnrolled;
                    }
                }
            }

            // 开始事务
            System.out.println("Starting transaction for enrolling student: " + username);
            conn.setAutoCommit(false);

            // 插入选课记录
            String enrollQuery = "INSERT INTO tblEnrollment (username, courseID) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(enrollQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, courseID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    isEnrolled = true;
                    System.out.println("Student enrolled in course successfully.");
                } else {
                    System.out.println("Failed to enroll student in course.");
                }
            }

            if (isEnrolled) {
                // 更新课程的已选人数
                String updateCountQuery = "UPDATE tblCourse SET selectedCount = selectedCount + 1 WHERE courseID = ?";
                try (PreparedStatement preparedStatement = conn.prepareStatement(updateCountQuery)) {
                    preparedStatement.setString(1, courseID);
                    preparedStatement.executeUpdate();
                }
            }

            // 提交事务
            conn.commit();
            System.out.println("Transaction committed successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Exception during enrollment: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();  // 如果有错误则回滚
                System.out.println("Transaction rolled back.");
            } catch (SQLException ex) {
                System.err.println("SQL Exception during rollback: " + ex.getMessage());
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }

        return isEnrolled;
    }

    // 退课
    public boolean dropCourse(String username, String courseID) {
        boolean isDropped = false;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return isDropped;
        }

        try {
            // 开始事务
            conn.setAutoCommit(false);

            // 删除选课记录
            String dropQuery = "DELETE FROM tblEnrollment WHERE username = ? AND courseID = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(dropQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, courseID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    isDropped = true;
                    System.out.println("Student dropped course successfully.");
                } else {
                    System.out.println("Failed to drop course.");
                }
            }

            if (isDropped) {
                // 更新课程的已选人数
                String updateCountQuery = "UPDATE tblCourse SET selectedCount = selectedCount - 1 WHERE courseID = ?";
                try (PreparedStatement preparedStatement = conn.prepareStatement(updateCountQuery)) {
                    preparedStatement.setString(1, courseID);
                    preparedStatement.executeUpdate();
                }
            }

            // 提交事务
            conn.commit();
            System.out.println("Transaction committed successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Exception during drop: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();  // 如果有错误则回滚
                System.out.println("Transaction rolled back.");
            } catch (SQLException ex) {
                System.err.println("SQL Exception during rollback: " + ex.getMessage());
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }

        return isDropped;
    }

    // 查询课程信息
    public JSONObject getCourseInfo(String courseID) {
        JSONObject courseJson = null;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        String query = "SELECT * FROM tblCourse WHERE courseID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, courseID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    courseJson = new JSONObject();
                    courseJson.put("courseID", resultSet.getString("courseID"));
                    courseJson.put("courseName", resultSet.getString("courseName"));
                    courseJson.put("courseTeacher", resultSet.getString("courseTeacher"));
                    courseJson.put("courseCredits", resultSet.getInt("courseCredits"));
                    courseJson.put("courseTime", resultSet.getString("courseTime"));  // 获取课程时间
                    courseJson.put("courseCapacity", resultSet.getInt("courseCapacity"));
                    courseJson.put("selectedCount", resultSet.getInt("selectedCount"));
                } else {
                    System.out.println("Course not found for courseID: " + courseID);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception during course info retrieval: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }

        return courseJson;
    }
}
