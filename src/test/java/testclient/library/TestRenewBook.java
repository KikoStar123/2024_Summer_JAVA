package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;
public class TestRenewBook {

    public static void main(String[] args) {
        Library client = new Library();
        boolean success = client.renewBook(4); // 假设借阅ID为1

        if (success) {
            System.out.println("Book renewed successfully.");
        } else {
            System.out.println("Failed to renew book.");
        }
    }
}
