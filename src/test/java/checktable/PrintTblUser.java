package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblUser {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblUser";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblUser table...");
                System.out.println("----------------------------------------------------------------------------------------------");
                System.out.println("|    username    |    truename    |    role    |    age    |    pwd    |    gender    |");
                System.out.println("----------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String username = rs.getString("username");
                    String truename = rs.getString("truename");
                    String role = rs.getString("role");
                    int age = rs.getInt("age");
                    String pwd = rs.getString("pwd");
                    String gender = rs.getString("gender");
                    System.out.println("|   " + username + "   |   " + truename + "   |   " + role + "   |   " + age + "   |   " + pwd + "   |   " + gender + "   |");
                }

                System.out.println("----------------------------------------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}