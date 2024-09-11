package client.ui;

import client.service.ClientService;
import client.service.Gender;
import client.service.StudentInformation;
import client.service.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.event.ActionListener;

import static antlr.build.ANTLR.root;


public class RegisterUI  {
    private TextField name;
    private TextField id;
    private ComboBox<String> genderComboBox;
    private TextField origin;
    private TextField birthday;
    private TextField academy;
    private PasswordField passwordField;
    private TextField emailField;
    private TextArea introduction;
    private Button registerButton;
    private Button backButton;

    private GridPane grid;

    public GridPane showRegisterUI(LoginUI loginUI) {
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        int dispx=5;
        int dispy=3;

        // 创建登录表单的标题
        Text title = new Text("用户注册");
        title.getStyleClass().add("title-font");
        grid.add(title, dispx+0, dispy+2, 2, 1); // 标题居中
        GridPane.setValignment(title, VPos.CENTER);

        Label nameLabel = new Label("姓名:");
        nameLabel.getStyleClass().add("body-font");
        name = new TextField();
        name.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        GridPane.setConstraints(nameLabel, dispx+0, dispy+4);
        GridPane.setConstraints(name, dispx+1, dispy+4);

        Label idLabel = new Label("学号:");
        idLabel.getStyleClass().add("body-font");
        id = new TextField();
        id.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        GridPane.setConstraints(idLabel, dispx+0, dispy+5);
        GridPane.setConstraints(id, dispx+1, dispy+5);

        Label genderLabel = new Label("性别:");
        genderLabel.getStyleClass().add("body-font");
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("male", "female");
        genderComboBox.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
//        genderComboBox.setPrefWidth(name.getPrefWidth());// 设置ComboBox的宽度与TextField相同
//        genderComboBox.setPrefHeight(name.getPrefHeight());// 设置ComboBox的高度与TextField相同

        GridPane.setConstraints(genderLabel, dispx+0, dispy+6);
        GridPane.setConstraints(genderComboBox, dispx+1, dispy+6);

        Label originLabel = new Label("籍贯:");
        originLabel.getStyleClass().add("body-font");
        origin = new TextField();
        origin.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        GridPane.setConstraints(originLabel, dispx+0, dispy+7);
        GridPane.setConstraints(origin, dispx+1, dispy+7);

        Label birthdayLabel = new Label("生日:");
        birthdayLabel.getStyleClass().add("body-font");
        birthday = new TextField();
        birthday.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        GridPane.setConstraints(birthdayLabel, dispx+0, dispy+8);
        GridPane.setConstraints(birthday, dispx+1, dispy+8);

        Label academyLabel = new Label("学院:");
        academyLabel.getStyleClass().add("body-font");
        academy = new TextField();
        academy.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        GridPane.setConstraints(academyLabel, dispx+0, dispy+9);
        GridPane.setConstraints(academy, dispx+1, dispy+9);

        Label passwordLabel = new Label("密码:");
        passwordLabel.getStyleClass().add("body-font");
        passwordField = new PasswordField();
        passwordField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        GridPane.setConstraints(passwordLabel, dispx+0, dispy+10);
        GridPane.setConstraints(passwordField, dispx+1, dispy+10);

        Label emailLabel = new Label("邮箱:");
        emailLabel.getStyleClass().add("body-font");
        emailField = new TextField();
        emailField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        GridPane.setConstraints(emailLabel, dispx+0, dispy+11);
        GridPane.setConstraints(emailField, dispx+1, dispy+11);

        registerButton = new Button("注册");
        registerButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        GridPane.setConstraints(registerButton, dispx+1, dispy+13);
        GridPane.setHalignment(registerButton, HPos.CENTER); // 水平居中对齐

        backButton=new Button("返回");
        backButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        GridPane.setConstraints(backButton, dispx+1, dispy+14);
        GridPane.setHalignment(backButton, HPos.CENTER); // 水平居中对齐

        backButton.setOnAction(e->{

            loginUI.getRoot().setCenter(loginUI.getGrid());

        });
        registerButton.setOnAction(e -> {
            String truename = name.getText();
            String stuid = id.getText();
            String gender = genderComboBox.getValue();
            String originText = origin.getText();
            String birthdayText = birthday.getText();
            String academyText = academy.getText();
            String passwordText = passwordField.getText();
            String emailText = emailField.getText();

            JSONObject result = ClientService.register(truename, Gender.valueOf(gender.toLowerCase()), stuid, originText, birthdayText, academyText, passwordText,emailText);
            System.out.println(result.toString());
            if (result.getString("status").equals("success")) {
                showAlert(Alert.AlertType.INFORMATION, "注册成功", "注册成功！你的账号是：" + result.getString("username"));
                System.out.println(result.toString());
            } else {
                showAlert(Alert.AlertType.ERROR, "注册失败", "注册失败，请重试。");
            }
        });

        grid.getChildren().addAll(nameLabel, name, idLabel, id, genderLabel, genderComboBox, originLabel, origin, birthdayLabel, birthday, academyLabel, academy, passwordLabel, passwordField, emailLabel, emailField, registerButton,backButton);
        return grid;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}