package main.java.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileStorageServer {

    private static final int FILE_SERVER_PORT = 8081;
    private static final String UPLOAD_DIR = "uploads/";

    public static void startFileServer() {
        try (ServerSocket serverSocket = new ServerSocket(FILE_SERVER_PORT)) {
            System.out.println("File storage server is running on port " + FILE_SERVER_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new FileHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class FileHandler implements Runnable {
        private final Socket clientSocket;

        public FileHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = in.readLine();
                if (request.startsWith("UPLOAD")) {
                    String fileName = request.split(" ")[1];
                    File uploadDir = new File(UPLOAD_DIR);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    File uploadFile = new File(UPLOAD_DIR + fileName);
                    try (FileOutputStream fos = new FileOutputStream(uploadFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                    out.println("File uploaded successfully: " + uploadFile.getAbsolutePath());
                } else if (request.startsWith("DOWNLOAD")) {
                    String fileName = request.split(" ")[1];
                    File downloadFile = new File(UPLOAD_DIR + fileName);
                    if (downloadFile.exists()) {
                        try (FileInputStream fis = new FileInputStream(downloadFile)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                clientSocket.getOutputStream().write(buffer, 0, bytesRead);
                            }
                        }
                    } else {
                        out.println("File not found: " + fileName);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

