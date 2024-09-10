package main;

import client.ui.LoginUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import server.MainServer;

public class MainClass extends Application {
    @Override
    public void start(Stage primaryStage) {

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
}
