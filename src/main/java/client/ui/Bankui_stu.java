package client.ui;

import client.service.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import client.service.Bank;

public class Bankui_stu {
    private static BorderPane bankBox;
    private static HBox buttonsBox;
    private static VBox loginBox;
    private static VBox registerBox;
    private static User user;

    public Bankui_stu(User user) {
        this.user = user;
    }

    static BorderPane createBankUI() {
        bankBox = new BorderPane();
        bankBox.setPadding(new Insets(10));

        // 创建按钮栏
        buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(0, 0, 10, 0));

        Button loginButton = new Button("登录");
        loginButton.setOnAction(e -> showLoginForm());
        buttonsBox.getChildren().add(loginButton);

        Button registerButton = new Button("注册");
        registerButton.setOnAction(e -> showRegisterForm());
        buttonsBox.getChildren().add(registerButton);

        // 将按钮栏放置在顶部
        bankBox.setTop(buttonsBox);

        // 创建登录表单
        loginBox = createLoginForm(user);
        // 将登录表单放置在中心
        bankBox.setCenter(loginBox);

        // 创建注册表单
        registerBox = createRegisterForm();
        // 初始时隐藏注册表单
        registerBox.setVisible(false);

        // 创建确认和返回按钮
        HBox actionBox = new HBox(10);

        Button backButton = new Button("返回");
        backButton.setOnAction(e -> handleBack());
        actionBox.getChildren().add(backButton);

        // 将操作按钮栏放置在底部
        bankBox.setBottom(actionBox);
        return bankBox;
    }

    private static VBox createLoginForm(User user) {
        VBox loginForm = new VBox(10);
        loginForm.setPadding(new Insets(10));
        Label welcomeLabel = new Label("登录银行账号");
        Label accountLabel = new Label("账号: " + user.getUsername()); // 显示用户名
        loginForm.getChildren().addAll(welcomeLabel, accountLabel);
        Label passwordLabel = new Label("密码:");
        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        Button confirmButton = new Button("确认");
        confirmButton.setOnAction(e -> handleConfirm(user, passwordField)); // 传递 user 和 passwordField 到 handleConfirm
        loginForm.getChildren().addAll(passwordLabel, passwordField, confirmButton);

        return loginForm;
    }

    private static VBox createRegisterForm() {
        VBox registerForm = new VBox(10);
        registerForm.setPadding(new Insets(10));
        Label welcomeLabel=new Label("注册银行账号");

        Label passwordLabel = new Label("密码:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        registerForm.getChildren().addAll(welcomeLabel,passwordLabel, passwordField);


        Label confirmPasswordLabel = new Label("确认密码:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("请再次输入密码");
        registerForm.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);

        Button confirmButtonb = new Button("确认");
        confirmButtonb.setOnAction(e -> handleConfirmb(user, passwordField, confirmPasswordField));

        registerForm.getChildren().add(confirmButtonb);

        return registerForm;
    }

    private static void handleConfirmb(User user, PasswordField passwordField, PasswordField confirmPasswordField) {
        String username = user.getUsername();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!password.equals(confirmPassword)) {
            // 密码不匹配
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("注册失败");
            alert.setHeaderText(null);
            alert.setContentText("两次输入的密码不一致，请重新输入。");
            alert.showAndWait();
        } else {
            // 密码匹配，继续注册逻辑
            boolean success = Bank.bankRegister(username, password);
            if (success) {
                // 登录成功
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("登录成功");
                alert.setHeaderText(null);
                alert.setContentText("欢迎回来, " + username);
                alert.showAndWait();
            } else {
                // 登录失败
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("注册失败");
                alert.setHeaderText(null);
                alert.setContentText("注册失败");
                alert.showAndWait();
            }
        }
    }

    private static void showLoginForm() {
        loginBox.setVisible(true);
        registerBox.setVisible(false);
        bankBox.setCenter(loginBox);
    }

    private static void showRegisterForm() {
        loginBox.setVisible(false);
        registerBox.setVisible(true);
        bankBox.setCenter(registerBox);
    }

    private static void handleConfirm(User user, PasswordField passwordField) {
        // 处理登录确认逻辑
        String username = user.getUsername();
        String password = passwordField.getText();
        boolean success = Bank.bankLogin(username, password); // 调用 bankLogin 方法
        if (success) {
            // 登录成功
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("登录成功");
            alert.setHeaderText(null);
            alert.setContentText("欢迎回来, " + username);
            alert.showAndWait();
        } else {
            // 登录失败
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("登录失败");
            alert.setHeaderText(null);
            alert.setContentText("用户名或密码错误");
            alert.showAndWait();
        }

    }


    private static void handleBack() {
        // 处理返回逻辑
        buttonsBox.setVisible(true);
        loginBox.setVisible(false);
        registerBox.setVisible(false);
        bankBox.setCenter(null);
    }
}

