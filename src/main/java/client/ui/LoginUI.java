package client.ui;

import client.service.ClientService;
import client.service.Gender;
import client.service.Role;
import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

import static client.service.Role.BankManager;
import static client.service.Role.Librarian;

import java.net.URL;

import static client.service.Role.BankManager;
import static client.service.Role.Librarian;
import static client.service.Role.CourseManager;

public class LoginUI extends Application {
    private TextField usernameField;
    private PasswordField passwordField;
    private BorderPane root;
    private String instanceName = "Default";
    private GridPane grid;

    public void setInstanceName(String name) {
        instanceName = name;
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("登录");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        primaryStage.getIcons().add(image);

        root = new BorderPane();

        // 创建 Scene，并将根布局添加到其中
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // 创建 WebView 实例
        WebView webView = new WebView();//WebView 是用于展示 Web 内容的 JavaFX 组件
        WebEngine webEngine = webView.getEngine();//WebEngine 用于加载和操作 Web 内容
        String htmlFilePath = getClass().getResource("/Fireflies.html").toExternalForm();// HTML 文件路径
        webEngine.load(htmlFilePath);// 加载内容
        webView.setPrefSize(450, 300);// 设置 WebView 的大小

        // 创建登录表单的标题
        Text title = new Text("用户登录");
        title.getStyleClass().add("title-font");

        // 创建登录表单的标签和输入框
        Label userLabel = new Label("账号:");
        userLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        usernameField = new TextField();
        usernameField.setPromptText("请输入用户名");//这里可以加字体
        usernameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Label passwordLabel = new Label("密码:");
        passwordLabel.getStyleClass().add("body-font");
        passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
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
        GridPane.setValignment(title, VPos.CENTER);
        grid.add(userLabel, 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(passwordLabel, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(buttonBox, 0, 5, 2, 1);

        root.setLeft(webView); // 左侧放置 WebView
        root.setCenter(grid); // 右侧放置登录表单

        // 应用CSS样式
        Scene scene = new Scene(root, 800, 495);
        scene.getRoot().getStyleClass().add("background-animate");
        scene.getStylesheets().add(getClass().getResource("/Loginui-styles.css").toExternalForm());

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
                Platform.runLater(() -> {
                    LibraryUI_Manager librarymanagerui=new LibraryUI_Manager();
                    try {
                        librarymanagerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
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
            } else if (user.getRole() == CourseManager) {
                Platform.runLater(() -> {
                    Admin_CourseUI coursemanagerui = new Admin_CourseUI(user);
                    try {
                        coursemanagerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
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
        RegisterUI registerUI = new RegisterUI();
        GridPane grid = registerUI.showRegisterUI(this);
        root.setCenter(grid);
    }

    public void showLoginUI() {
        Stage stage = new Stage();
        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public GridPane getGrid() {
        return grid;
    }
}