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
/**
 * 文件存储服务器类，负责文件的上传、下载、删除以及获取文件哈希值。
 */
public class FileStorageServer {

    private static final int FILE_SERVER_PORT = 8081;
    private static final int HTTP_SERVER_PORT = 8082;
    private static final String UPLOAD_DIR = "uploads/";
    private static final String RESOURCE_DIR = "src/main/resources/";

    /**
     * 启动文件存储服务器。
     * 启动两个服务：一个用于处理文件上传和下载，另一个用于通过 HTTP 处理文件请求。
     */
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
    /**
     * 启动 HTTP 服务器，用于处理文件的 HTTP 请求。
     * 文件请求会由 {@link HttpFileHandler} 处理。
     * @throws IOException 如果服务器启动失败
     */
    private static void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_SERVER_PORT), 0);
        server.createContext("/files", new HttpFileHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("HTTP server is running on port " + HTTP_SERVER_PORT);
    }
    /**
     * HTTP 处理器，用于处理文件请求。
     */
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
    /**
     * 文件上传处理器，处理文件上传、下载、检查文件是否存在及删除文件的请求。
     */
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
        /**
         * 计算文件的 SHA-256 哈希值。
         * @param file 要计算哈希值的文件。
         * @return 文件的 SHA-256 哈希值。
         * @throws IOException 如果读取文件时发生错误。
         * @throws NoSuchAlgorithmException 如果计算哈希值时指定的算法不可用。
         */
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
