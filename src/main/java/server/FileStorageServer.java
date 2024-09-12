package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileStorageServer {

    private static final int FILE_SERVER_PORT = 8081;
    private static final int HTTP_SERVER_PORT = 8082;
    private static final String UPLOAD_DIR = "uploads/";
    private static final String RESOURCE_DIR = "src/main/resources/";

    public static void startFileServer() {
        try (ServerSocket serverSocket = new ServerSocket(FILE_SERVER_PORT)) {
            System.out.println("File storage server is running on port " + FILE_SERVER_PORT);

            // 启动HTTP服务器
            startHttpServer();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new FileUploadHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_SERVER_PORT), 0);
        server.createContext("/files", new HttpFileHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("HTTP server is running on port " + HTTP_SERVER_PORT);
    }

    private static class HttpFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String filePath = UPLOAD_DIR + exchange.getRequestURI().getPath().substring("/files/".length());
            File file = new File(filePath);

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] fileBytes = fis.readAllBytes();
                fis.close();

                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Content-Type", "application/pdf");
                exchange.sendResponseHeaders(200, fileBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileBytes);
                os.close();
            } else {
                exchange.sendResponseHeaders(404, -1); // 文件未找到
            }
        }
    }

    private static class FileUploadHandler implements Runnable {
        private final Socket clientSocket;

        public FileUploadHandler(Socket clientSocket) {
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
                        byte[] buffer = new byte[8192]; // 增加缓冲区大小
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
                            byte[] buffer = new byte[8192]; // 增加缓冲区大小
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                clientSocket.getOutputStream().write(buffer, 0, bytesRead);
                            }
                        }
                    } else {
                        out.println("File not found: " + fileName);
                    }
                } else if (request.startsWith("EXISTS")) {
                    String fileName = request.split(" ")[1];
                    File file = new File(UPLOAD_DIR + fileName);
                    if (file.exists()) {
                        out.println("File exists");
                    } else {
                        out.println("File does not exist");
                    }
                } else if (request.startsWith("DELETE")) {
                    String fileName = request.split(" ")[1];
                    File file = new File(UPLOAD_DIR + fileName);
                    if (file.exists() && file.delete()) {
                        System.out.println("delete success");
                        out.println("File deleted successfully");
                    } else {
                        System.out.println("delete fail");
                        out.println("Failed to delete file");
                    }
                } else if (request.startsWith("HASH")) {
                    String fileName = request.split(" ")[1];
                    File file = new File(UPLOAD_DIR + fileName);
                    if (file.exists()) {
                        String fileHash = calculateFileHash(file);
                        out.println(fileHash);
                    } else {
                        out.println("File not found");
                    }
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        private String calculateFileHash(File file) throws IOException, NoSuchAlgorithmException {
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

    public static void main(String[] args) {
        startFileServer();
    }
}
