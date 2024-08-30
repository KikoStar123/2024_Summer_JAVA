package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

public class TestUpdateBook {

    public static void main(String[] args) {
        Library client = new Library();
        boolean success = client.updateBook("book1", 11); // 假设书籍ID为book1，新的馆藏数量为10

        if (success) {
            System.out.println("Book updated successfully.");
        } else {
            System.out.println("Failed to update book.");
        }
    }
}
