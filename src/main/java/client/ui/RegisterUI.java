package client.ui;

import client.service.ClientService;
import client.service.Gender;
import client.service.StudentInformation;
import client.service.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.JSONObject;

public class RegisterUI {
    private TextField name;
    private TextField id;
    private ComboBox<String> genderComboBox;
    private TextField origin;
    private TextField birthday;
    private TextField academy;
    private PasswordField passwordField;
    private Button registerButton;
    private Button backButton;

    private GridPane grid;

    public GridPane showRegisterUI(LoginUI loginUI) {
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label nameLabel = new Label("姓名:");
        GridPane.setConstraints(nameLabel, 0, 0);
        name = new TextField();
        GridPane.setConstraints(name, 1, 0);

        Label idLabel = new Label("学号:");
        GridPane.setConstraints(idLabel, 0, 1);
        id = new TextField();
        GridPane.setConstraints(id, 1, 1);

        Label genderLabel = new Label("性别:");
        GridPane.setConstraints(genderLabel, 0, 2);
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("male", "female");
        GridPane.setConstraints(genderComboBox, 1, 2);

        Label originLabel = new Label("籍贯:");
        GridPane.setConstraints(originLabel, 0, 3);
        origin = new TextField();
        GridPane.setConstraints(origin, 1, 3);

        Label birthdayLabel = new Label("生日:");
        GridPane.setConstraints(birthdayLabel, 0, 4);
        birthday = new TextField();
        GridPane.setConstraints(birthday, 1, 4);

        Label academyLabel = new Label("学院:");
        GridPane.setConstraints(academyLabel, 0, 5);
        academy = new TextField();
        GridPane.setConstraints(academy, 1, 5);

        Label passwordLabel = new Label("密码:");
        GridPane.setConstraints(passwordLabel, 0, 6);
        passwordField = new PasswordField();
        GridPane.setConstraints(passwordField, 1, 6);

        registerButton = new Button("注册");
        GridPane.setConstraints(registerButton, 1, 7);

        backButton = new Button("返回");
        GridPane.setConstraints(backButton, 1, 8);
        backButton.setOnAction(e -> {
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

            JSONObject result = ClientService.register(truename, Gender.valueOf(gender.toLowerCase()), stuid, originText, birthdayText, academyText, passwordText);
            if (result.getString("status").equals("success")) {
                showAlert(Alert.AlertType.INFORMATION, "注册成功", "注册成功！你的账号是：" + result.getString("username"));
            } else {
                showAlert(Alert.AlertType.ERROR, "注册失败", "注册失败，请重试。");
            }
        });

        grid.getChildren().addAll(nameLabel, name, idLabel, id, genderLabel, genderComboBox, originLabel, origin, birthdayLabel, birthday, academyLabel, academy, passwordLabel, passwordField, registerButton, backButton);
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
