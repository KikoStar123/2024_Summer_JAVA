package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

public class TestGetBookDetailsById {

    public static void main(String[] args) {
        Library client = new Library();
        Book book = client.getBookDetailsById("978-7-121-33462-7");

        if (book != null) {
            System.out.println("Book ID: " + book.getBookID());
            System.out.println("Name: " + book.getName());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publish House: " + book.getPublishHouse());
            System.out.println("Publication Year: " + book.getPublicationYear());
            System.out.println("Classification: " + book.getClassification());
            System.out.println("Current Number: " + book.getCurNumber());
            System.out.println("Library Number: " + book.getLibNumber());
            System.out.println("Location: " + book.getLocation());
        } else {
            System.out.println("Book not found.");
        }
    }
}
