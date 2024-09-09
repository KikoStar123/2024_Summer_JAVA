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
        Platform.runLater(() -> {
            new LoginUI().start(new Stage());
        });

        // 启动第二个 LoginUI 实例
        Platform.runLater(() -> {
            new LoginUI().start(new Stage());
        });

        // 启动 MainServer 实例
        new Thread(() -> {
            MainServer.main(new String[]{});
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
