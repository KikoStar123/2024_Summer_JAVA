package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblShoppingCart {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblshoppingcart";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblshoppingcart table...");
                System.out.println("-------------------------------------------------------------");
                System.out.println("|  PRODUCTID  |  PRODUCTNUMBER  |  USERNAME  |");
                System.out.println("-------------------------------------------------------------");

                while (rs.next()) {
                    String productId = rs.getString("PRODUCTID");
                    int productNumber = rs.getInt("PRODUCTNUMBER");
                    String username = rs.getString("USERNAME");

                    System.out.println("|   " + productId + "   |   " + productNumber + "   |   " + username + "   |");
                }

                System.out.println("-------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}