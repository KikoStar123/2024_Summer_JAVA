package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblShoppingComment {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblshoppingcomment";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblshoppingcomment table...");
                System.out.println("-------------------------------------------------------------");
                System.out.println("|  COMMENTID  |  PRODUCTID  |  COMMENTATTITUDE  |  COMMENTCONTENT  |  USERNAME  |");
                System.out.println("-------------------------------------------------------------");

                while (rs.next()) {
                    String commentId = rs.getString("COMMENTID");
                    String productId = rs.getString("PRODUCTID");
                    int commentAttitude = rs.getInt("COMMENTATTITUDE");
                    String commentContent = rs.getString("COMMENTCONTENT");
                    String username = rs.getString("USERNAME");

                    System.out.println("|   " + commentId + "   |   " + productId + "   |   " + commentAttitude + "   |   " + commentContent + "   |   " + username + "   |");
                }

                System.out.println("-------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
