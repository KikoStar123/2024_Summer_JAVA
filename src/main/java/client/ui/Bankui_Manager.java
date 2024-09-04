package client.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Bankui_Manager extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建根布局，使用垂直布局
        VBox root = new VBox(10);
        root.setSpacing(10);

        // 创建搜索区域
        HBox searchBox = new HBox(5);
        Label searchLabel = new Label("搜索账号:");
        TextField searchField = new TextField();
        Button searchButton = new Button("搜索");
        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);

        // 创建存款区域
        HBox depositBox = new HBox(5);
        Label depositLabel = new Label("存款金额:");
        TextField depositField = new TextField();
        Button depositButton = new Button("存款");
        depositBox.getChildren().addAll(depositLabel, depositField, depositButton);

        // 创建取款区域
        HBox withdrawBox = new HBox(5);
        Label withdrawLabel = new Label("取款金额:");
        TextField withdrawField = new TextField();
        Button withdrawButton = new Button("取款");
        withdrawBox.getChildren().addAll(withdrawLabel, withdrawField, withdrawButton);

        // 将所有区域添加到根布局
        root.getChildren().addAll(searchBox, depositBox, withdrawBox);

        // 创建场景并设置舞台
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("银行管理系统");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // 启动 JavaFX 应用程序
    }
}
