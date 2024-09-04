package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblEnrollment {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblEnrollment";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblEnrollment table...");
                System.out.println("------------------------------------------------------");
                System.out.println("|  USERNAME  |  COURSEID  |");
                System.out.println("------------------------------------------------------");

                while (rs.next()) {
                    String username = rs.getString("USERNAME");
                    String courseId = rs.getString("COURSEID");

                    System.out.println("|   " + username + "   |   " + courseId + "   |");
                }

                System.out.println("------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
