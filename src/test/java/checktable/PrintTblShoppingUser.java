package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblShoppingUser {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblshoppinguser";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblshoppinguser table...");
                System.out.println("-------------------------------------------------------------");
                System.out.println("|  USERNAME  |  ADDRESS  |  TELEPHONE  |");
                System.out.println("-------------------------------------------------------------");

                while (rs.next()) {
                    String username = rs.getString("USERNAME");
                    String address = rs.getString("ADDRESS");
                    String telephone = rs.getString("TELEPHONE");

                    System.out.println("|   " + username + "   |   " + address + "   |   " + telephone + "   |");
                }

                System.out.println("-------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}