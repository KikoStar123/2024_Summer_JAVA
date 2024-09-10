package client.ui;

import client.service.ClientService;
import client.service.Gender;
import client.service.Role;
import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;

import java.net.URL;

import static client.service.Role.BankManager;
import static client.service.Role.Librarian;

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
        primaryStage.setTitle("Login UI - " + instanceName);

        root = new BorderPane();
        root.setStyle("-fx-border-radius: 20px; "
                + "-fx-border-color: #ffffff; "
                + "-fx-border-width: 2px; "
                + "-fx-background-color: #ffffff;");

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Text title = new Text("用户登录");
        title.setFont(new Font("Segoe UI", 24));
        title.setStyle("-fx-fill: #1138cf;");

        Label userLabel = new Label("Username:");
        userLabel.getStyleClass().add("body-font");
        usernameField = new TextField();
        usernameField.setPromptText("请输入用户名");
        usernameField.getStyleClass().add("input-field");

        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("body-font");
        passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        passwordField.getStyleClass().add("input-field");

        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("main-button");
        Button registerButton = new Button("注册");
        registerButton.getStyleClass().add("main-button");

        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());

        HBox buttonBox = new HBox(80);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, registerButton);

        grid.add(title, 0, 0, 2, 1);
        grid.add(userLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(buttonBox, 0, 3, 2, 1);

        root.setCenter(grid);
        Scene scene = new Scene(root, 350, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        ClientService clientService = new ClientService();
        boolean success = clientService.login(username, password);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Login successful!");
            alert.showAndWait();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();

            User user = clientService.login_return(username, password);
            if (user.getRole() == BankManager) {
                Platform.runLater(() -> {
                    Bankui_Manager managerui = new Bankui_Manager();
                    try {
                        managerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (user.getRole() == Librarian) {
                Platform.runLater(() -> {
                    LibraryUI_Manager librarymanagerui = new LibraryUI_Manager();
                    try {
                        librarymanagerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (user.getRole() == Role.ShopAssistant) {
                Platform.runLater(() -> {
                    ShopUI_Manager shopmanagerui = new ShopUI_Manager(user.getUsername());
                    try {
                        shopmanagerui.start(new Stage());
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

