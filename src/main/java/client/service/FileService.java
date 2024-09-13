package client.service;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件服务类，用于处理文件的上传、删除、检查存在与否以及计算文件的哈希值等功能。
 */
public class FileService {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8081;

    /**
     * 上传文件到服务器，并进行哈希值校验。
     *
     * @param file     要上传的文件
     * @param fileName 文件在服务器上的名称
     * @return 如果上传成功并且哈希值校验通过，返回 true；否则返回 false
     */
    public boolean uploadFile(File file, String fileName) {
        try {
            String originalHash = calculateFileHash(file);
            System.out.println("原文件哈希值: " + originalHash);
            boolean uploadSuccess = false;
            int retryCount = 0;
            final int maxRetries = 1000;

            while (!uploadSuccess && retryCount < maxRetries) {
                // 删除服务器端已有的同名文件
                deleteFile("uploads/" + fileName);

                uploadSuccess = uploadFileOnce(file, fileName);
                if (uploadSuccess) {
                    String uploadedFileHash = getFileHashFromServer(fileName);
                    System.out.println("上传文件哈希值: " + uploadedFileHash);
                    if (originalHash.equals(uploadedFileHash)) {
                        return true;
                    } else {
                        uploadSuccess = false;
                        retryCount++;
                        System.out.println("哈希值不匹配，重试上传... (" + retryCount + "/" + maxRetries + ")");
                    }
                } else {
                    retryCount++;
                    System.out.println("上传失败，重试... (" + retryCount + "/" + maxRetries + ")");
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 上传文件一次，不进行哈希校验。
     *
     * @param file     要上传的文件
     * @param fileName 文件在服务器上的名称
     * @return 如果上传成功返回 true，否则返回 false
     */
    private boolean uploadFileOnce(File file, String fileName) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             FileInputStream fis = new FileInputStream(file);
             OutputStream os = socket.getOutputStream();
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("UPLOAD " + fileName);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.flush();
            socket.shutdownOutput();

            String response = in.readLine();
            return response.contains("文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从服务器获取已上传文件的哈希值。
     *
     * @param fileName 文件在服务器上的名称
     * @return 文件的哈希值字符串
     */
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

    /**
     * 删除服务器上的文件。
     *
     * @param filePath 文件路径
     * @return 如果文件删除成功返回 true，否则返回 false
     */
    public boolean deleteFile(String filePath) {
        String fileName = filePath.replaceFirst("^uploads/", "");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("DELETE " + fileName);
            String response = in.readLine();
            return response.contains("文件删除成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查服务器上是否存在指定文件。
     *
     * @param filePath 文件路径
     * @return 如果文件存在返回 true，否则返回 false
     */
    public boolean fileExists(String filePath) {
        String fileName = filePath.replaceFirst("^uploads/", "");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("EXISTS " + fileName);
            String response = in.readLine();
            return response.contains("文件存在");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 计算文件的SHA-256哈希值。
     *
     * @param file 要计算哈希值的文件
     * @return 文件的SHA-256哈希值字符串
     * @throws IOException                 如果文件读取失败
     * @throws NoSuchAlgorithmException    如果SHA-256算法不可用
     */
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
