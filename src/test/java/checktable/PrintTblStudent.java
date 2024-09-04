package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblStudent {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblStudent";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblStudent table...");
                System.out.println("--------------------------------------------------------------");
                System.out.println("|  studentId  |  origin  |  birthday  |  academy  |  username  |");
                System.out.println("--------------------------------------------------------------");

                while (rs.next()) {
                    String studentId = rs.getString("studentId");
                    String origin = rs.getString("origin");
                    String birthday = rs.getString("birthday");
                    String academy = rs.getString("academy");
                    String username = rs.getString("username");

                    System.out.println("|   " + studentId + "   |   " + origin + "   |   " + birthday + "   |   " + academy + "   |   " + username + "   |");
                }

                System.out.println("--------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}