package client.ui;

import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainUI extends Application {
    private User user;

    public MainUI(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("欢迎");

        // 创建外层 BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: #808080; " // 灰色边框
                + "-fx-border-width: 2px; " // 边框宽度
                + "-fx-border-style: solid; " // 边框样式
                + "-fx-border-radius: 10px;"); // 边框圆角

        // 创建内层 HBox 用于水平排列按钮
        HBox buttonsBox = new HBox(10); // 按钮之间的间距
        buttonsBox.setAlignment(Pos.CENTER); // 水平居中

        // 创建按钮并添加到 HBox
        Button libButton = createButton("图书馆", e -> handleLibrary());
        Button courseButton = createButton("选课系统", e -> handleCourse(user.getUsername()));
        Button studentButton = createButton("学籍管理", e -> handleStudent(user.getUsername()));

        buttonsBox.getChildren().addAll(libButton, courseButton, studentButton);

        // 将按钮 HBox 添加到 BorderPane 的中心
        borderPane.setLeft(buttonsBox);

        // 添加欢迎标签
        Label welcomeLabel = new Label("用户名: " + user.getUsername() + "\t身份: " + user.getRole() + "\t年龄: " + user.getAge());
        welcomeLabel.setStyle("-fx-font-weight: bold;");
        VBox labelBox = new VBox(10); // 标签和按钮之间的间距
        labelBox.getChildren().add(welcomeLabel);

        // 将标签 VBox 添加到 BorderPane 的顶部
        borderPane.setTop(labelBox);

        Scene scene = new Scene(borderPane, 500, 300); // 根据需要调整尺寸
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private Button createButton(String text, EventHandler<ActionEvent> onAction) {
        Button button = new Button(text);
        button.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        button.setOnAction(onAction);
        return button;
    }
    private void handleLibrary() {
        // 图书馆处理逻辑
        Platform.runLater(() -> {
            User currentUser = this.user; // 假设 user 已经正确设置
            LibraryUI libraryUI = new LibraryUI(currentUser);
            libraryUI.display();
        });
    }
    private void handleCourse(String username) {
        // 选课系统处理逻辑
        Platform.runLater(() -> {
            User currentUser = getUserFromUsername(username); // 根据用户名获取用户信息
            Stage courseStage = new Stage();
            CourseSelectionUI courseSelectionUI = new CourseSelectionUI(currentUser);
            courseSelectionUI.display(courseStage); // 传递 Stage 实例
        });
    }
    private User getUserFromUsername(String username) {
        // 这里应该是调用服务层的方法来获取用户信息
        // 暂时用一个假的用户对象来代替
        User user = new User();
        user.setUsername(username);
        // 设置其他用户信息...
        return user;
    }
    private void handleStudent(String username) {
        // 学籍管理处理逻辑

    }

    public void display() {
        launch();
    }

}
