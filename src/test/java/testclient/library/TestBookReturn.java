package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

public class TestBookReturn {

    public static void main(String[] args) {
        Library client = new Library();
        boolean success = client.bookReturn("200000001", "978-7-121-33462-7"); // 假设借阅ID为1

        if (success) {
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Failed to return book.");
        }
    }
}
