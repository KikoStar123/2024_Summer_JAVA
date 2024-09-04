package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

import java.io.File;

public class TestUploadBookPDF {

    public static void main(String[] args) {
        Library client = new Library();
        File pdfFile = new File("toUpload/1.pdf"); // 替换为实际PDF文件路径
        String bookID = "978-7-121-33462-7"; // 替换为实际书籍ID

        boolean success = client.uploadBookPDF(pdfFile, bookID);

        if (success) {
            System.out.println("PDF uploaded and database updated successfully.");
        } else {
            System.out.println("Failed to upload PDF or update database.");
        }
    }
}
