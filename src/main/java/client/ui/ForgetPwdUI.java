package client.ui;

import client.service.ClientService;
import client.service.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ForgetPwdUI extends Application {
    User user = new User();

    public ForgetPwdUI(String username) {
        //this.getusername = username;//不知道咋写
        //user.setUsername(username);
    }

    // 显示修改密码的窗口
    public void showUpdatePwdWindow() {
        Stage stage = new Stage();
        try {
            start(stage);  // 调用 start 方法显示新窗口
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("重置密码");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png")); // 加载图标
        primaryStage.getIcons().add(image);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // 新密码输入
        Label newPasswordLabel = new Label("新密码:");
        newPasswordLabel.getStyleClass().add("body-font");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.getStyleClass().add("input-field");
        newPasswordField.setPromptText("请输入新密码");
        TextField newPasswordVisibleField = new TextField();
        newPasswordVisibleField.setVisible(false);

        // 确认新密码输入
        Label confirmPasswordLabel = new Label("确认新密码:");
        confirmPasswordLabel.getStyleClass().add("body-font");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.getStyleClass().add("input-field");
        confirmPasswordField.setPromptText("请确认新密码");
        TextField confirmPasswordVisibleField = new TextField();
        confirmPasswordVisibleField.setVisible(false);

        // 新密码“显示/隐藏”按钮
        Button showNewPasswordButton = createIconButton("/eye-open.png");
        showNewPasswordButton.setOnAction(e -> {
            if (newPasswordVisibleField.isVisible()) {
                newPasswordField.setText(newPasswordVisibleField.getText());
                newPasswordVisibleField.setVisible(false);
                newPasswordField.setVisible(true);
                showNewPasswordButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/eye-open.png"))));
            } else {
                newPasswordVisibleField.setText(newPasswordField.getText());
                newPasswordField.setVisible(false);
                newPasswordVisibleField.setVisible(true);
                showNewPasswordButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/eye-closed.png"))));
            }
        });

        // 确认新密码“显示/隐藏”按钮
        Button showConfirmPasswordButton = createIconButton("/eye-open.png");
        showConfirmPasswordButton.setOnAction(e -> {
            if (confirmPasswordVisibleField.isVisible()) {
                confirmPasswordField.setText(confirmPasswordVisibleField.getText());
                confirmPasswordVisibleField.setVisible(false);
                confirmPasswordField.setVisible(true);
                showConfirmPasswordButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/eye-open.png"))));
            } else {
                confirmPasswordVisibleField.setText(confirmPasswordField.getText());
                confirmPasswordField.setVisible(false);
                confirmPasswordVisibleField.setVisible(true);
                showConfirmPasswordButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/eye-closed.png"))));
            }
        });

        // 更新密码按钮及其事件处理
        Button updateButton = new Button("更新密码");
        updateButton.getStyleClass().add("main-button");
        updateButton.setOnAction(e -> {
            String newPassword = newPasswordField.isVisible() ? newPasswordField.getText() : newPasswordVisibleField.getText();
            String confirmPassword = confirmPasswordField.isVisible() ? confirmPasswordField.getText() : confirmPasswordVisibleField.getText();

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("新密码和确认密码不匹配！");
                return;
            }

            ClientService clientService = new ClientService();
            boolean isSuccess = clientService.updateUserPwd(user.getUsername(), null, newPassword);  // null 是旧密码的占位符
            if (isSuccess) {
                System.out.println("密码更新成功！");
            } else {
                System.out.println("密码更新失败！");
            }
        });

        // 布局
        int dispx = 5;
        int dispy = 3;

        grid.add(newPasswordLabel, dispx + 0, dispy + 0);
        grid.add(newPasswordField, dispx + 1, dispy + 0);
        grid.add(newPasswordVisibleField, dispx + 1, dispy + 0);
        grid.add(showNewPasswordButton, dispx + 2, dispy + 0);

        grid.add(confirmPasswordLabel, dispx + 0, dispy + 1);
        grid.add(confirmPasswordField, dispx + 1, dispy + 1);
        grid.add(confirmPasswordVisibleField, dispx + 1, dispy + 1);
        grid.add(showConfirmPasswordButton, dispx + 2, dispy + 1);

        grid.add(updateButton, dispx + 2, dispy + 3);

        Scene scene = new Scene(grid, 400, 210);
        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 创建一个只有图标没有边框的按钮
    private Button createIconButton(String imagePath) {
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        button.setGraphic(imageView);
        button.setMinSize(24, 24);  // 设置按钮的最小尺寸以适应图标
        button.setMaxSize(24, 24);  // 设置按钮的最大尺寸以适应图标
        return button;
    }

    public static void main() {
        launch();
    }
}

