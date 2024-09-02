package testclient.library;

import java.util.ArrayList;
import java.util.List;

import client.service.LibRecord;
import client.service.Library;

public class TestGetAllLibRecords {

    public static void main(String[] args) {
        Library client = new Library();
        List<String> bookNameList = new ArrayList<>();
        List<LibRecord> records = client.getAllLibRecords(bookNameList);

        System.out.println("Found records:");
        for (int i = 0; i < records.size(); i++) {
            LibRecord record = records.get(i);
            String bookName = bookNameList.get(i);
            System.out.println("Borrow ID: " + record.getBorrowId());
            System.out.println("Username: " + record.getUsername());
            System.out.println("Book ID: " + record.getBookID());
            System.out.println("Book Name: " + bookName);
            System.out.println("Borrow Date: " + record.getBorrowDate());
            System.out.println("Return Date: " + record.getReturnDate());
            System.out.println("Is Return: " + record.getisReturn());
            System.out.println("Renewable: " + record.getRenewable());
            System.out.println("Record Status: " + record.getRecordStatus());
            System.out.println("---------------------------");
        }
    }
}
