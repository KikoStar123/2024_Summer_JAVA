package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.ArrayList;

public class CourseService {

    private final Lock lock = new ReentrantLock();

    // 添加课程
    public JSONObject addCourse(String courseID, String courseName, String courseTeacher, int courseCredits, String courseTime, int courseCapacity, String courseRoom, String courseType) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "fail").put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock(); // 获取锁
        try {
            // 解析新课程时间
            onePeriod[] newCoursePeriods = parseCourseTime(courseTime);

            boolean timeConflict = false;
            boolean teacherConflict = false;
            boolean roomConflict = false;
            String conflictingCourseID = null;

            // 检查必修课程时间冲突
            if (courseType.equalsIgnoreCase("required")) {
                String conflictCheckQuery = "SELECT courseID, courseTime, courseTeacher, courseRoom FROM tblCourse WHERE courseType = 'required'";
                try (PreparedStatement checkStmt = conn.prepareStatement(conflictCheckQuery);
                     ResultSet rs = checkStmt.executeQuery()) {

                    while (rs.next()) {
                        String existingCourseID = rs.getString("courseID");
                        String existingCourseTime = rs.getString("courseTime");
                        String existingCourseTeacher = rs.getString("courseTeacher");
                        String existingCourseRoom = rs.getString("courseRoom");

                        onePeriod[] existingPeriods = parseCourseTime(existingCourseTime);
                        if (isTimeConflict(newCoursePeriods, existingPeriods)) {
                            timeConflict = true;
                            conflictingCourseID = existingCourseID;
                            if (existingCourseTeacher.equals(courseTeacher)) {
                                teacherConflict = true;
                            }
                            if (existingCourseRoom.equals(courseRoom)) {
                                roomConflict = true;
                            }
                        }

                        // 如果时间冲突且教师冲突，或者时间冲突且教室冲突，返回失败
                        if (timeConflict && (teacherConflict || roomConflict)) {
                            response.put("status", "fail")
                                    .put("message", "Time conflict with course: " + conflictingCourseID)
                                    .put("conflictType", teacherConflict ? "Teacher conflict" : "Room conflict");
                            return response;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.put("status", "fail").put("message", "SQL Exception during conflict check: " + e.getMessage());
                    return response;
                }
            }

            // 如果没有时间和教室冲突或教师冲突，插入课程
            String insertQuery = "INSERT INTO tblCourse (courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity, courseRoom, courseType, selectedCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, courseID);
                preparedStatement.setString(2, courseName);
                preparedStatement.setString(3, courseTeacher);
                preparedStatement.setInt(4, courseCredits);
                preparedStatement.setString(5, courseTime);
                preparedStatement.setInt(6, courseCapacity);
                preparedStatement.setString(7, courseRoom);
                preparedStatement.setString(8, courseType);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    response.put("status", "success").put("message", "Course added successfully.");
                } else {
                    response.put("status", "fail").put("message", "Failed to add course.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.put("status", "fail").put("message", "SQL Exception during course insertion: " + e.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            lock.unlock(); // 释放锁
        }

        return response;
    }

    // 解析课程时间字符串为 onePeriod 对象数组
    private onePeriod[] parseCourseTime(String timeString) {
        List<onePeriod> thePeriods = new ArrayList<>();
        String[] timeSegments = timeString.split(";");
        for (String segment : timeSegments) {
            String[] parts = segment.split("\\|");
            String weeks = parts[0];
            String day = parts[1];
            String periods = parts[2];

            onePeriod thePeriod = new onePeriod();
            thePeriod.staWeek = Integer.parseInt(weeks.split("-")[0]);
            thePeriod.endWeek = Integer.parseInt(weeks.split("-")[1]);
            thePeriod.Day = Integer.parseInt(day);
            thePeriod.stasection = Integer.parseInt(periods.split("-")[0]);
            thePeriod.endsection = Integer.parseInt(periods.split("-")[1]);

            thePeriods.add(thePeriod);
        }
        return thePeriods.toArray(new onePeriod[0]);
    }

    // 检查时间冲突
    private boolean isTimeConflict(onePeriod[] periods1, onePeriod[] periods2) {
        for (onePeriod p1 : periods1) {
            for (onePeriod p2 : periods2) {
                if (p1.Day == p2.Day) {
                    if ((p1.staWeek <= p2.endWeek && p1.endWeek >= p2.staWeek) &&
                            (p1.stasection <= p2.endsection && p1.endsection >= p2.stasection)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 定义课程时间段的内部类
    private class onePeriod {
        int staWeek;
        int endWeek;
        int Day;
        int stasection;
        int endsection;
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

        lock.lock(); // 获取锁
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
            lock.unlock(); // 释放锁
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

        lock.lock(); // 获取锁
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
            lock.unlock(); // 释放锁
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

        lock.lock(); // 获取锁
        try {
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
                        courseJson.put("courseTime", resultSet.getString("courseTime"));
                        courseJson.put("courseCapacity", resultSet.getInt("courseCapacity"));
                        courseJson.put("selectedCount", resultSet.getInt("selectedCount"));
                        courseJson.put("courseRoom", resultSet.getString("courseRoom"));  // 添加课程教室
                        courseJson.put("courseType", resultSet.getString("courseType"));  // 添加课程类型
                    }
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
            lock.unlock(); // 释放锁
        }

        return courseJson;
    }

    // 查询已经选的课程
    public JSONObject getEnrolledCourses(String username) {
        JSONObject coursesJson = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        lock.lock(); // 获取锁
        try {
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
                        courseJson.put("courseRoom", resultSet.getString("courseRoom"));  // 添加课程教室
                        courseJson.put("courseType", resultSet.getString("courseType"));  // 添加课程类型

                        coursesJson.append("courses", courseJson); // 使用 append 将多个课程对象添加到 JSON 数组中
                    }
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
            lock.unlock(); // 释放锁
        }

        return coursesJson;
    }

    // 检索课程
    public JSONObject searchCourses(String courseName, String courseTeacher) {
        JSONObject coursesJson = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        lock.lock(); // 获取锁
        try {
            StringBuilder query = new StringBuilder("SELECT * FROM tblCourse WHERE 1=1");

            if (courseName != null && !courseName.isEmpty()) {
                query.append(" AND courseName LIKE ?");
            }

            if (courseTeacher != null && !courseTeacher.isEmpty()) {
                query.append(" AND courseTeacher LIKE ?");
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(query.toString())) {
                int paramIndex = 1;
                if (courseName != null && !courseName.isEmpty()) {
                    preparedStatement.setString(paramIndex++, "%" + courseName + "%");
                }
                if (courseTeacher != null && !courseTeacher.isEmpty()) {
                    preparedStatement.setString(paramIndex, "%" + courseTeacher + "%");
                }

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
                        courseJson.put("courseRoom", resultSet.getString("courseRoom"));  // 添加课程教室
                        courseJson.put("courseType", resultSet.getString("courseType"));  // 添加课程类型

                        coursesJson.append("courses", courseJson);
                    }
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
            lock.unlock(); // 释放锁
        }

        return coursesJson;
    }

    // 查看所有课程信息
    public JSONObject getAllCourses() {
        JSONObject coursesJson = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return null;
        }

        lock.lock(); // 获取锁
        try {
            String query = "SELECT * FROM tblCourse";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    JSONObject courseJson = new JSONObject();
                    courseJson.put("courseID", resultSet.getString("courseID"));
                    courseJson.put("courseName", resultSet.getString("courseName"));
                    courseJson.put("courseTeacher", resultSet.getString("courseTeacher"));
                    courseJson.put("courseCredits", resultSet.getInt("courseCredits"));
                    courseJson.put("courseTime", resultSet.getString("courseTime"));
                    courseJson.put("courseCapacity", resultSet.getInt("courseCapacity"));
                    courseJson.put("selectedCount", resultSet.getInt("selectedCount"));
                    courseJson.put("courseRoom", resultSet.getString("courseRoom"));  // 添加课程教室
                    courseJson.put("courseType", resultSet.getString("courseType"));  // 添加课程类型

                    coursesJson.append("courses", courseJson); // 将每门课程添加到 "courses" 数组中
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
            lock.unlock(); // 释放锁
        }

        return coursesJson;
    }
}
