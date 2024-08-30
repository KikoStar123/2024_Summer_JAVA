package client.ui;

import client.service.ClientService;
import client.service.Role;
import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginUI extends Application {
    private TextField usernameField;
    private PasswordField passwordField;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label userLabel = new Label("Username:");
        usernameField = new TextField();
        grid.add(userLabel, 0, 0);
        grid.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("登录");
        Button registerButton = new Button("注册");
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);

        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());

        Scene scene = new Scene(grid, 300, 150);
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


        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Login failed. Please try again.");
            alert.showAndWait();

            // 关闭当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();

            // 启动主界面
            User user = new User(username, Role.student, 12, Gender.male, "123");
            Platform.runLater(() -> {
                MainUI mainUI = new MainUI(user);
                try {
                    mainUI.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    private void handleRegister() {
        // 注册处理逻辑
    }

}
