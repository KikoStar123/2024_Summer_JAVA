package client.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.text.html.HTML;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class GradientBackgroundAnimation extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Firefly Background Animation");

        // 创建 WebView 实例
        WebView webView = new WebView();//WebView 是用于展示 Web 内容的 JavaFX 组件
        WebEngine webEngine = webView.getEngine();//WebEngine 用于加载和操作 Web 内容
         // HTML 文件路径
        String htmlFilePath = getClass().getResource("/Fireflies.html").toExternalForm();
        // 加载内容
        webEngine.load(htmlFilePath);
        // 设置 WebView 的大小
        webView.setPrefSize(800, 600);

        // 创建主布局
        // 使用 StackPane 布局来将 WebView 放置在你的应用中，可以方便地将它作为背景显示
        StackPane root = new StackPane();
        root.getChildren().add(webView);

        // 创建场景并添加到舞台
        // 将 StackPane 设置为场景的根节点，然后将场景设置到舞台上并显示
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


