package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

public class TestGetAllLibRecords {

    public static void main(String[] args) {
        Library client = new Library();
        List<LibRecord> records = client.getAllLibRecords();

        System.out.println("Found records:");
        for (LibRecord record : records) {
            System.out.println("Borrow ID: " + record.getBorrowId());
            System.out.println("Username: " + record.getUsername());
            System.out.println("Book ID: " + record.getBookID());
            System.out.println("Borrow Date: " + record.getBorrowDate());
            System.out.println("Return Date: " + record.getReturnDate());
            System.out.println("Is Return: " + record.getisReturn());
            System.out.println("Renewable: " + record.getRenewable());
            System.out.println("Record Status: " + record.getRecordStatus());
            System.out.println("---------------------------");
        }
    }
}
