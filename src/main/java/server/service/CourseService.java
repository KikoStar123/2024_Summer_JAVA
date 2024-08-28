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

        return isAdded;
    }

    // 选课
    public JSONObject enrollInCourse(String username, String courseID) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
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
                            response.put("status", "error");
                            response.put("message", "Course is full.");
                            return response;
                        }
                    } else {
                        response.put("status", "error");
                        response.put("message", "Course not found.");
                        return response;
                    }
                }
            }

            // 检查学生是否已经选了该课程
            String checkEnrollmentQuery = "SELECT * FROM tblEnrollment WHERE username = ? AND courseID = ?";
            try (PreparedStatement checkEnrollmentStmt = conn.prepareStatement(checkEnrollmentQuery)) {
                checkEnrollmentStmt.setString(1, username);
                checkEnrollmentStmt.setString(2, courseID);
                try (ResultSet rs = checkEnrollmentStmt.executeQuery()) {
                    if (rs.next()) {
                        response.put("status", "error");
                        response.put("message", "Student is already enrolled in this course.");
                        return response;
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
                    response.put("status", "success");
                    response.put("message", "Student enrolled in course successfully.");
                } else {
                    response.put("status", "error");
                    response.put("message", "Failed to enroll student in course.");
                    conn.rollback(); // 在失败时回滚
                    return response;
                }
            }

            if (response.getString("status").equals("success")) {
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
            e.printStackTrace();
            try {
                conn.rollback();  // 如果有错误则回滚
                System.out.println("Transaction rolled back.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            response.put("status", "error");
            response.put("message", "SQL Exception during enrollment.");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return response;
    }



    // 退课
    public JSONObject dropCourse(String username, String courseID) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        try {
            // 检查学生是否已经选了该课程
            String checkEnrollmentQuery = "SELECT * FROM tblEnrollment WHERE username = ? AND courseID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEnrollmentQuery)) {
                checkStmt.setString(1, username);
                checkStmt.setString(2, courseID);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    response.put("status", "error");
                    response.put("message", "Student is not enrolled in this course.");
                    return response;
                }
            }

            // 开始事务
            conn.setAutoCommit(false);

            // 删除选课记录
            String dropQuery = "DELETE FROM tblEnrollment WHERE username = ? AND courseID = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(dropQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, courseID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    response.put("status", "success");
                    response.put("message", "Student dropped course successfully.");
                } else {
                    response.put("status", "error");
                    response.put("message", "Failed to drop course.");
                    conn.rollback();
                    return response;
                }
            }

            // 更新课程的已选人数
            String updateCountQuery = "UPDATE tblCourse SET selectedCount = selectedCount - 1 WHERE courseID = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(updateCountQuery)) {
                preparedStatement.setString(1, courseID);
                int updateResult = preparedStatement.executeUpdate();
                if (updateResult == 0) {
                    response.put("status", "error");
                    response.put("message", "Failed to update selected count.");
                    conn.rollback();
                    return response;
                }
            }

            // 提交事务
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();  // 如果有错误则回滚
                System.out.println("Transaction rolled back.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            response.put("status", "error");
            response.put("message", "SQL Exception during course drop.");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return response;
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
                }
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
        }

        return courseJson;
    }

//查询已经选的课程
    public JSONObject getEnrolledCourses(String username) {
        JSONObject coursesJson = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        String query = "SELECT * FROM tblCourse WHERE courseID IN (SELECT courseID FROM tblEnrollment WHERE username = ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    JSONObject courseJson = new JSONObject();
                    courseJson.put("courseID", resultSet.getString("courseID"));
                    courseJson.put("courseName", resultSet.getString("courseName"));
                    courseJson.put("courseTeacher", resultSet.getString("courseTeacher"));
                    courseJson.put("courseCredits", resultSet.getInt("courseCredits"));
                    courseJson.put("courseTime", resultSet.getString("courseTime"));
                    courseJson.put("courseCapacity", resultSet.getInt("courseCapacity"));
                    courseJson.put("selectedCount", resultSet.getInt("selectedCount"));

                    coursesJson.append("courses", courseJson); // 使用 append 将多个课程对象添加到 JSON 数组中
                }
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
        }

        return coursesJson;
    }

}
