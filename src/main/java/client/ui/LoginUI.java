package client.ui;

import client.service.ClientService;
import client.service.Gender;
import client.service.Role;
import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
//import jdk.javadoc.internal.doclets.formats.html.DocFilesHandlerImpl;

import java.net.URL;

import static client.service.Role.BankManager;
import static client.service.Role.Librarian;

public class LoginUI extends Application {
    private TextField usernameField;
    private PasswordField passwordField;

    private static String instanceName = "Default";

    public static void setInstanceName(String name) {
        instanceName = name;
    }
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login UI - " + instanceName);


        BorderPane root = new BorderPane();

        // 设置根布局的样式
        root.setStyle("-fx-border-radius: 20px; " // 设置圆角边框半径
                + "-fx-border-color: #6A0DAD; "  // 设置边框颜色
                + "-fx-border-width: 2px; "        // 设置边框宽度
                + "-fx-background-color: #808080;"); // 设置背景颜色

        // 创建 Scene，并将根布局添加到其中

        Scene scene_login = new Scene(root, 300, 200);
        primaryStage.setScene(scene_login);
        scene_login.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("登陆");
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // 创建登录表单的标题
        Text title = new Text("用户登录");
        title.setFont(new Font("Segoe UI", 24));
        title.setStyle("-fx-fill: #6A0DAD;"); // 主色调

        // 创建登录表单的标签和输入框
        Label userLabel = new Label("Username:");
        userLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        usernameField = new TextField();
        usernameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Label passwordLabel = new Label("Password:");
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

// 将按钮添加到buttonBox
        buttonBox.getChildren().addAll(loginButton, registerButton);

        // 将组件添加到网格中
        grid.add(title, 0, 0, 2, 1); // 标题居中
        grid.add(userLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
// 将buttonBox添加到GridPane中，占据两列
        grid.add(buttonBox, 0, 3, 2, 1); // 从第0列开始，跨越2列，从第3行开始

        // 应用CSS样式
        Scene scene = new Scene(grid, 350, 200);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

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
            if(user.getRole()==BankManager)
            {
                Platform.runLater(() -> {
                    Bankui_Manager managerui=new Bankui_Manager();
                    try {
                        managerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            else if(user.getRole()==Librarian)
            {

            }
            else if(user.getRole()==Role.ShopAssistant){
                Platform.runLater(() -> {
                    ShopUI_Manager shopmanagerui=new ShopUI_Manager(user.getUsername());
                    try {
                        shopmanagerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            else
            {
                Platform.runLater(() -> {
                    MainUI mainUI = new MainUI(user);
                    try {
                        mainUI.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
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