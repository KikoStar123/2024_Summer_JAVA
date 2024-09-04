package testclient.library;

import java.util.List;

import client.service.LibRecord;
import client.service.Library;
import client.service.Book;

import java.io.File;

public class TestUploadBookImage {

    public static void main(String[] args) {
        Library client = new Library();
        File imageFile = new File("toUpload/1.jpg"); // 替换为实际图片文件路径
        String bookID = "978-7-80165-501-1"; // 替换为实际书籍ID

        boolean success = client.uploadBookImage(imageFile, bookID);

        if (success) {
            System.out.println("Image uploaded and database updated successfully.");
        } else {
            System.out.println("Failed to upload image or update database.");
        }
    }
}
