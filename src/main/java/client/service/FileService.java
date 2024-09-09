package client.service;

import java.io.*;
import java.net.Socket;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class FileService {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8081;

    public boolean uploadFile(File file, String fileName) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             FileInputStream fis = new FileInputStream(file);
             OutputStream os = socket.getOutputStream();
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("UPLOAD " + fileName);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.flush();
            socket.shutdownOutput();

            String response = in.readLine();
            return response.contains("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFile(String filePath) {
        // 去掉 "uploads/" 前缀
        String fileName = filePath.replaceFirst("^uploads/", "");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("DELETE " + fileName);
            String response = in.readLine();
            return response.contains("File deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean fileExists(String filePath) {
        // 去掉 "uploads/" 前缀
        String fileName = filePath.replaceFirst("^uploads/", "");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("EXISTS " + fileName);
            String response = in.readLine();
            return response.contains("File exists");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
