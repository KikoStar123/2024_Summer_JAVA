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

public class UpdatePwdUI extends Application {
    User user=new User();
    public UpdatePwdUI(User user) {
        this.user=user;
    }
    //显示修改密码的窗口
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
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label oldPasswordLabel = new Label("旧密码:");
        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("请输入旧密码");//这一行是lmy
        TextField oldPasswordVisibleField = new TextField();
        oldPasswordVisibleField.setVisible(false);

        Label newPasswordLabel = new Label("新密码:");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("请输入新密码");
        TextField newPasswordVisibleField = new TextField();
        newPasswordVisibleField.setVisible(false);

        Label confirmPasswordLabel = new Label("确认新密码:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("请确认新密码");
        TextField confirmPasswordVisibleField = new TextField();
        confirmPasswordVisibleField.setVisible(false);

        // 添加“显示”按钮
        // 创建“显示/隐藏”旧密码的按钮，并设置图标
        Button showOldPasswordButton = createIconButton("/eye-open.png");
        showOldPasswordButton.setOnAction(e -> {
            if (oldPasswordVisibleField.isVisible()) {
                oldPasswordField.setText(oldPasswordVisibleField.getText());
                oldPasswordVisibleField.setVisible(false);
                oldPasswordField.setVisible(true);
                showOldPasswordButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/eye-open.png"))));
            } else {
                oldPasswordVisibleField.setText(oldPasswordField.getText());
                oldPasswordField.setVisible(false);
                oldPasswordVisibleField.setVisible(true);
                showOldPasswordButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/eye-closed.png"))));
            }
        });

// 添加“显示”按钮
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

// 添加“显示”按钮
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
                showConfirmPasswordButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/eye-closed.png")))); // 切换为隐藏图标
            }
        });
//更新密码的具体逻辑，修改函数
        Button updateButton = new Button("更新密码");
        updateButton.setOnAction(e -> {
            String oldPassword = oldPasswordField.isVisible() ? oldPasswordField.getText() : oldPasswordVisibleField.getText();
            String newPassword = newPasswordField.isVisible() ? newPasswordField.getText() : newPasswordVisibleField.getText();
            String confirmPassword = confirmPasswordField.isVisible() ? confirmPasswordField.getText() : confirmPasswordVisibleField.getText();

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("新密码和确认密码不匹配！");
                return;
            }
          ClientService clientService=new ClientService();
           boolean issuccess = clientService.updateUserPwd(user.getUsername(),oldPassword,newPassword);
            if(issuccess){System.out.println("密码更新成功！");}
            else{System.out.println("密码更新失败！");}

        });

        grid.add(oldPasswordLabel, 0, 0);
        grid.add(oldPasswordField, 1, 0);
        grid.add(oldPasswordVisibleField, 1, 0);
        grid.add(showOldPasswordButton, 2, 0);

        grid.add(newPasswordLabel, 0, 1);
        grid.add(newPasswordField, 1, 1);
        grid.add(newPasswordVisibleField, 1, 1);
        grid.add(showNewPasswordButton, 2, 1);

        grid.add(confirmPasswordLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.add(confirmPasswordVisibleField, 1, 2);
        grid.add(showConfirmPasswordButton, 2, 2);

        grid.add(updateButton, 1, 3);

        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setTitle("修改密码");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // 创建一个只有图标没有边框的按钮
    private Button createIconButton(String imagePath) {
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        button.setGraphic(imageView);
        button.setMinSize(24, 24); // 设置按钮的最小尺寸以适应图标
        button.setMaxSize(24, 24); // 设置按钮的最大尺寸以适应图标
        return button;
    }
    public static void main() {
        launch();
    }
}
