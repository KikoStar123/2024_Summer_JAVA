package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;
public class TestUpdateBookImagePath {

    public static void main(String[] args) {
        Library client = new Library();
        String bookID = "978-7-121-33462-7"; // 替换为实际书籍ID
        String imagePath = "uploads/" + bookID + ".jpg"; // 替换为实际图片路径

        boolean success = client.updateBookImagePath(bookID, imagePath);

        if (success) {
            System.out.println("Image path updated successfully in the database.");
        } else {
            System.out.println("Failed to update image path in the database.");
        }
    }
}
