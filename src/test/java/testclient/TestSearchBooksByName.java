package testclient;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;
public class TestSearchBooksByName {

    public static void main(String[] args) {
        Library client = new Library();
//        List<Book> books = client.searchBooksByName("明朝那些事儿");
//        Book testbook= client.getBookDetailsById("978-7-121-33462-7");
//        testbook.toString();
//        System.out.println("Found books:");
//        for (Book book : books) {
//            System.out.println("Book ID: " + book.getBookID());
//            System.out.println("Name: " + book.getName());
//            System.out.println("Author: " + book.getAuthor());
//            System.out.println("Publish House: " + book.getPublishHouse());
//            System.out.println("Publication Year: " + book.getPublicationYear());
//            System.out.println("Classification: " + book.getClassification());
//            System.out.println("Current Number: " + book.getCurNumber());
//            System.out.println("Library Number: " + book.getLibNumber());
//            System.out.println("Location: " + book.getLocation());
//            System.out.println("---------------------------");
        //测试bookborrow
        //boolean isborrow= client.bookBorrow(Integer.toString(200000001),Integer.toString(978-7-121-33462-7));
        //测试getLibRecordsByUsername
        List<LibRecord> records=client.getLibRecordsByUsername(Integer.toString(200000001));
        for(LibRecord record :records){
            System.out.println("Borrow ID: " + record.getBorrowId());
            System.out.println("Username: " + record.getUsername());
            System.out.println("Book ID: " + record.getBookID());
            System.out.println("Borrow Date: " + record.getBorrowDate());
            System.out.println("Return Date: " + record.getReturnDate());
            System.out.println("Renewable: " + (record.getRenewable()?"true":"false"));//坏
            boolean isreturn=record.getisReturn();
            if(isreturn){
                System.out.println("Is Returned: " + "True");
            }else{
                System.out.println("Is Returned: " + "False");
            }
            System.out.println("Is Returned: " + record.getisReturn());
            System.out.println("Record Status: " + record.getRecordStatus());
            System.out.println("---------------------------");
        }

        }



}
