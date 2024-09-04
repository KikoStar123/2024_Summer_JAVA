package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblCourse {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblCourse";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblCourse table...");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("|  COURSEID  |  COURSENAME  |  COURSETEACHER  |  COURSECREDITS  |  COURSETIME  |  COURSECAPACITY  |  SELECTEDCOUNT  |");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String courseId = rs.getString("COURSEID");
                    String courseName = rs.getString("COURSENAME");
                    String courseTeacher = rs.getString("COURSETEACHER");
                    int courseCredits = rs.getInt("COURSECREDITS");
                    String courseTime = rs.getString("COURSETIME");
                    int courseCapacity = rs.getInt("COURSECAPACITY");
                    int selectedCount = rs.getInt("SELECTEDCOUNT");

                    System.out.println("|   " + courseId + "   |   " + courseName + "   |   " + courseTeacher + "   |   " + courseCredits + "   |   " + courseTime + "   |   " + courseCapacity + "   |   " + selectedCount + "   |");
                }

                System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
