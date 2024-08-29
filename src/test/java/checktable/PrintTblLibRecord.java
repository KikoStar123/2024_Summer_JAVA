package checktable;

import server.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintTblLibRecord {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM tblLibRecord";
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("Printing tblLibRecord table...");
                System.out.println("---------------------------------------------------------------------------------------------------------");
                System.out.println("|  borrowId  |  username  |  bookId  |  borrowDate  |  returnDate  |  renewable  |");
                System.out.println("---------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    int borrowId = rs.getInt("borrowId");
                    String username = rs.getString("username");
                    String bookId = rs.getString("bookId");
                    String borrowDate = rs.getString("borrowDate");
                    String returnDate = rs.getString("returnDate");
                    String renewable = rs.getString("renewable");

                    System.out.println("|   " + borrowId + "   |   " + username + "   |   " + bookId + "   |   " + borrowDate + "   |   " + returnDate + "   |   " + renewable + "   |");
                }

                System.out.println("---------------------------------------------------------------------------------------------------------");

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}