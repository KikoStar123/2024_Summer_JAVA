package client.service;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileService {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8081;

    public boolean uploadFile(File file, String fileName) {
        try {
            String originalHash = calculateFileHash(file);
            System.out.println("Original file hash: " + originalHash);
            boolean uploadSuccess = false;
            int retryCount = 0;
            final int maxRetries = 1000;

            while (!uploadSuccess && retryCount < maxRetries) {
                // 删除服务器端已有的同名文件
                deleteFile("uploads/" + fileName);

                uploadSuccess = uploadFileOnce(file, fileName);
                if (uploadSuccess) {
                    String uploadedFileHash = getFileHashFromServer(fileName);
                    System.out.println("Uploaded file hash: " + uploadedFileHash);
                    if (originalHash.equals(uploadedFileHash)) {
                        return true;
                    } else {
                        uploadSuccess = false;
                        retryCount++;
                        System.out.println("Hash mismatch, retrying upload... (" + retryCount + "/" + maxRetries + ")");
                    }
                } else {
                    retryCount++;
                    System.out.println("Upload failed, retrying... (" + retryCount + "/" + maxRetries + ")");
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean uploadFileOnce(File file, String fileName) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             FileInputStream fis = new FileInputStream(file);
             OutputStream os = socket.getOutputStream();
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("UPLOAD " + fileName);

            byte[] buffer = new byte[8192]; // 增加缓冲区大小
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

    private String getFileHashFromServer(String fileName) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("HASH " + fileName);
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteFile(String filePath) {
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

    public String calculateFileHash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
