package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

public class TestBookBorrow {

    public static void main(String[] args) {
        Library client = new Library();
        boolean success = client.bookBorrow("200000001", "book1"); // 假设用户名为user1，书籍ID为book1

        if (success) {
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Failed to borrow book.");
        }
    }
}
