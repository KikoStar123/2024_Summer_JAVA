package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblBankUser {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblBankUser";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblBankUser table...");
                System.out.println("-------------------------------------------------------------");
                System.out.println("|  username  |  balance  |  bankpwd  |");
                System.out.println("-------------------------------------------------------------");

                while (rs.next()) {
                    String username = rs.getString("username");
                    double balance = rs.getDouble("balance");
                    String bankpwd = rs.getString("bankpwd");

                    System.out.println("|   " + username + "   |   " + balance + "   |   " + bankpwd + "   |");
                }

                System.out.println("-------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
