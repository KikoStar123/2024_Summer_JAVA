package client.ui;

import client.service.ClientService;
import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
//import jdk.javadoc.internal.doclets.formats.html.DocFilesHandlerImpl;


public class LoginUI extends Application {
    private TextField usernameField;
    private PasswordField passwordField;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("用户登录");
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        // 创建一个布局容器（比如GridPane）
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        // grid.setAlignment(Pos.CENTER);

        // 创建登录表单的标题(。。。)
        Text title = new Text("用户登录");
        title.getStyleClass().add("title-font");// 主色调

        // 创建登录表单的标签和输入框，使用CSS样式
        Label userLabel = new Label("用户账号:");
        userLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        usernameField = new TextField();
        usernameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Label passwordLabel = new Label("       密码:");
        passwordLabel.getStyleClass().add("body-font");
        passwordField = new PasswordField();
        passwordField.getStyleClass().add("input-field");

        // 创建登录和注册按钮
        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button registerButton = new Button("注册");
        registerButton.getStyleClass().add("main-button");

        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());

        HBox buttonBox = new HBox(80); // 间距为10
        buttonBox.setAlignment(Pos.CENTER); // 水平居中

//        // 布局设置
//        grid.add(userLabel, 0, 0);
//        grid.add(userTextField, 1, 0);
//        grid.add(passwordLabel, 0, 1);
//        grid.add(passwordField, 1, 1);
//        grid.add(loginButton, 0, 2);
//        grid.add(registerButton, 1, 2);

        //将组件添加到网格中
        grid.add(title, 0, 0, 2, 1); // 标题居中
        grid.add(userLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        // 将buttonBox添加到GridPane中，占据两列
        grid.add(buttonBox, 0, 3, 2, 1); // 从第0列开始，跨越2列，从第3行开始

        // 应用CSS样式
        Scene scene = new Scene(grid, 300, 200);
        //scene.getStylesheets().add(getClass().getResource("/styles/LoginUIstyles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText(); // JavaFX的PasswordField没有getPassword方法，使用getText

        ClientService clientService = new ClientService();
        boolean success = clientService.login(username, password);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Login successful!");
            alert.showAndWait();

            // 关闭当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();

            // 启动主界面
            //User user = new User(username, Role.student, 12, Gender.male, "123");
            User user = clientService.login_return(username, password);
            Platform.runLater(() -> {
                MainUI mainUI = new MainUI(user);
                try {
                    mainUI.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Login failed. Please try again.");
            alert.showAndWait();
        }
    }
    private void handleRegister() {
        Platform.runLater(() -> {
            RegisterUI registerUI = new RegisterUI();
            registerUI.display();
        });
    }
}