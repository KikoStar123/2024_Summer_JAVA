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

public class UpdatePwdUI extends Application {
    User user=new User();
    public UpdatePwdUI(User user) {
        this.user=user;
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
        Button showOldPasswordButton = new Button("显示");
        showOldPasswordButton.setOnAction(e -> {
            if (oldPasswordVisibleField.isVisible()) {
                oldPasswordField.setText(oldPasswordVisibleField.getText());
                oldPasswordVisibleField.setVisible(false);
                oldPasswordField.setVisible(true);
                showOldPasswordButton.setText("显示");
            } else {
                oldPasswordVisibleField.setText(oldPasswordField.getText());
                oldPasswordField.setVisible(false);
                oldPasswordVisibleField.setVisible(true);
                showOldPasswordButton.setText("隐藏");
            }
        });

        Button showNewPasswordButton = new Button("显示");
        showNewPasswordButton.setOnAction(e -> {
            if (newPasswordVisibleField.isVisible()) {
                newPasswordField.setText(newPasswordVisibleField.getText());
                newPasswordVisibleField.setVisible(false);
                newPasswordField.setVisible(true);
                showNewPasswordButton.setText("显示");
            } else {
                newPasswordVisibleField.setText(newPasswordField.getText());
                newPasswordField.setVisible(false);
                newPasswordVisibleField.setVisible(true);
                showNewPasswordButton.setText("隐藏");
            }
        });

        Button showConfirmPasswordButton = new Button("显示");
        showConfirmPasswordButton.setOnAction(e -> {
            if (confirmPasswordVisibleField.isVisible()) {
                confirmPasswordField.setText(confirmPasswordVisibleField.getText());
                confirmPasswordVisibleField.setVisible(false);
                confirmPasswordField.setVisible(true);
                showConfirmPasswordButton.setText("显示");
            } else {
                confirmPasswordVisibleField.setText(confirmPasswordField.getText());
                confirmPasswordField.setVisible(false);
                confirmPasswordVisibleField.setVisible(true);
                showConfirmPasswordButton.setText("隐藏");
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
    public void showUpdatePwdWindow() {
        Stage stage = new Stage();
        try {
            start(stage);  // 调用 start 方法显示新窗口
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main() {
        launch();
    }
}
