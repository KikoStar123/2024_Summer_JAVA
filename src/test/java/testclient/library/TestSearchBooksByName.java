package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;
public class TestSearchBooksByName {

    public static void main(String[] args) {
        Library client = new Library();
        List<Book> books = client.searchBooksByName("明朝那些事儿");

        System.out.println("Found books:");
        for (Book book : books) {
            System.out.println("Book ID: " + book.getBookID());
            System.out.println("Name: " + book.getName());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publish House: " + book.getPublishHouse());
            System.out.println("Publication Year: " + book.getPublicationYear());
            System.out.println("Classification: " + book.getClassification());
            System.out.println("Current Number: " + book.getCurNumber());
            System.out.println("Library Number: " + book.getLibNumber());
            System.out.println("Location: " + book.getLocation());
            System.out.println("---------------------------");
        }
    }
}
