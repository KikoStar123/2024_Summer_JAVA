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

public class MainClass extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }

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