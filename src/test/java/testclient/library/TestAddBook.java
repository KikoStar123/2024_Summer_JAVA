package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

public class TestAddBook {

    public static void main(String[] args) {
        Library client = new Library();
        Book book = new Book("book2", "Test Book2", "Test Author2", "Test Publish House2", "2023", "Test Classification", 10, 10, "Test Location");
        boolean success = client.addBook(book);

        if (success) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Failed to add book.");
        }
    }
}
