package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

public class TestUpdateBookPDFPath {

    public static void main(String[] args) {
        Library client = new Library();
        String bookID = "978-7-121-33462-7"; // 替换为实际书籍ID
        String pdfPath = "uploads/" + bookID + ".pdf"; // 替换为实际PDF路径

        boolean success = client.updateBookPDFPath(bookID, pdfPath);

        if (success) {
            System.out.println("PDF path updated successfully in the database.");
        } else {
            System.out.println("Failed to update PDF path in the database.");
        }
    }
}