package client.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class GradientBackgroundAnimation extends Application {
    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        //webView.setPrefSize(450, 300);// 设置 WebView 的大小
        String htmlFilePath=getClass().getResource("/dynamic-button.html").toExternalForm();// HTML 文件路径
        webView.getEngine().load(htmlFilePath);// 加载内容

        Scene scene = new Scene(webView, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



