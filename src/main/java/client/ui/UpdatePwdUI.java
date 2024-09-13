package client.ui;

import client.service.ClientService;
import client.service.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * UpdatePwdUI 类用于为用户提供一个界面来修改密码。
 * 用户可以输入旧密码、新密码和确认新密码，并通过点击“更新密码”按钮来修改密码。
 * 提供了显示/隐藏密码的功能，方便用户查看输入的密码。
 */
public class UpdatePwdUI extends Application {
    User user=new User();
    /**
     * 构造函数，初始化用户信息。
     *
     * @param user 当前登录的用户对象
     */
    public UpdatePwdUI(User user) {
        this.user=user;
    }
    /**
     * 显示修改密码的窗口，调用 start 方法启动新窗口。
     */
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

    /**
     * 设置并启动 JavaFX 应用程序的主界面，用于修改密码。
     *
     * @param primaryStage 主窗口
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("修改密码");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        primaryStage.getIcons().add(image);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label oldPasswordLabel = new Label("旧密码:");
        oldPasswordLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        oldPasswordField.setPromptText("请输入旧密码");//这一行是lmy
        TextField oldPasswordVisibleField = new TextField();
        oldPasswordVisibleField.setVisible(false);

        Label newPasswordLabel = new Label("新密码:");
        newPasswordLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        newPasswordField.setPromptText("请输入新密码");
        TextField newPasswordVisibleField = new TextField();
        newPasswordVisibleField.setVisible(false);

        Label confirmPasswordLabel = new Label("确认新密码:");
        confirmPasswordLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
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
        updateButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        updateButton.setOnAction(e -> {
            String oldPassword = oldPasswordField.isVisible() ? oldPasswordField.getText() : oldPasswordVisibleField.getText();
            String newPassword = newPasswordField.isVisible() ? newPasswordField.getText() : newPasswordVisibleField.getText();
            String confirmPassword = confirmPasswordField.isVisible() ? confirmPasswordField.getText() : confirmPasswordVisibleField.getText();

            if (!newPassword.equals(confirmPassword)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("密码修改");
                alert.setHeaderText(null);
                alert.setContentText("新密码和确认密码不匹配！!");
                alert.showAndWait();
                return;
            }
            ClientService clientService=new ClientService();
            boolean issuccess = clientService.updateUserPwd(user.getUsername(),oldPassword,newPassword);
            if(issuccess){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("密码修改");
                alert.setHeaderText(null);
                alert.setContentText("密码修改成功!");
                alert.showAndWait();}
            else{ Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("密码修改");
                alert.setHeaderText(null);
                alert.setContentText("密码修改失败!");
                alert.showAndWait();}
        });


        int dispx=5;
        int dispy=3;

        grid.add(oldPasswordLabel, dispx+0, dispy+0);
        grid.add(oldPasswordField, dispx+1, dispy+0);
        grid.add(oldPasswordVisibleField, dispx+1, dispy+0);
        grid.add(showOldPasswordButton, dispx+2, dispy+0);

        grid.add(newPasswordLabel, dispx+0, dispy+1);
        grid.add(newPasswordField, dispx+1, dispy+1);
        grid.add(newPasswordVisibleField, dispx+1, dispy+1);
        grid.add(showNewPasswordButton, dispx+2, dispy+1);

        grid.add(confirmPasswordLabel, dispx+0, dispy+2);
        grid.add(confirmPasswordField, dispx+1, dispy+2);
        grid.add(confirmPasswordVisibleField, dispx+1, dispy+2);
        grid.add(showConfirmPasswordButton, dispx+2, dispy+2);

        grid.add(updateButton, dispx+2, dispy+5);

        Scene scene = new Scene(grid, 400, 210);
        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * 创建带图标的按钮，用于显示/隐藏密码。
     *
     * @param imagePath 图标的路径
     * @return 返回一个 Button 对象
     */
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
