package main;

import client.ui.LoginUI;
import javafx.application.Application;
import javafx.stage.Stage;
import server.MainServer;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * MainClass 是应用程序的主类，负责启动客户端和服务器实例。
 * 它会在启动时解压 jar 文件中的资源（如数据库文件和上传目录），
 * 然后启动多个客户端实例和一个服务器实例。
 */
public class MainClass extends Application {

    /**
     * 启动 JavaFX 应用程序的主界面。
     * 在启动过程中，会提取资源文件并启动两个 LoginUI 实例和一个服务器实例。
     *
     * @param primaryStage 主舞台，应用的根窗口
     */
    @Override
    public void start(Stage primaryStage) {
        // 解压 uploads 文件夹和 test.mv.db 文件
        try {
            String resourceDir = "uploads/";
            String targetDir = System.getProperty("user.dir") + "/uploads";
            extractFilesFromJar(resourceDir, targetDir);
            System.out.println("Files extracted successfully to " + targetDir);

            String dbResource = "testdb.mv.db";
            String dbTargetDir = System.getProperty("user.dir");
            extractFileFromJar(dbResource, dbTargetDir);
            System.out.println("Database extracted successfully to " + dbTargetDir);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        // 启动第一个 LoginUI 实例
        LoginUI loginUI1 = new LoginUI();
        Stage stage1 = new Stage();
        loginUI1.start(stage1);

        // 启动第二个 LoginUI 实例
        LoginUI loginUI2 = new LoginUI();
        Stage stage2 = new Stage();
        loginUI2.start(stage2);

        // 启动 MainServer 实例
        new Thread(() -> {
            MainServer.main(new String[]{});
        }).start();
    }

    /**
     * 程序入口，启动 JavaFX 应用程序。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * 从 jar 文件中提取资源文件夹（例如 uploads 文件夹），将其解压到目标目录。
     *
     * @param resourceDir 资源文件夹在 jar 文件中的路径
     * @param targetDir   目标目录的路径
     * @throws IOException            读取 jar 文件时可能抛出的异常
     * @throws URISyntaxException     jar 文件路径的 URI 语法错误
     */
    public static void extractFilesFromJar(String resourceDir, String targetDir) throws IOException, URISyntaxException {
        File targetDirectory = new File(targetDir);
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        String jarPath = new File(MainClass.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        try (JarFile jar = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(resourceDir) && !entry.isDirectory()) {
                    File file = new File(targetDir + File.separator + entry.getName().substring(resourceDir.length()));
                    if (!file.exists()) {
                        try (InputStream is = jar.getInputStream(entry); FileOutputStream fos = new FileOutputStream(file)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 从 jar 文件中提取单个文件（例如数据库文件），将其解压到目标目录。
     *
     * @param resource  资源文件在 jar 文件中的路径
     * @param targetDir 目标目录的路径
     * @throws IOException            读取 jar 文件时可能抛出的异常
     * @throws URISyntaxException     jar 文件路径的 URI 语法错误
     */
    public static void extractFileFromJar(String resource, String targetDir) throws IOException, URISyntaxException {
        File targetDirectory = new File(targetDir);
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        String jarPath = new File(MainClass.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        try (JarFile jar = new JarFile(jarPath)) {
            JarEntry entry = jar.getJarEntry(resource);
            if (entry != null && !entry.isDirectory()) {
                File file = new File(targetDir + File.separator + resource);
                if (!file.exists()) {
                    try (InputStream is = jar.getInputStream(entry); FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        }
    }
}