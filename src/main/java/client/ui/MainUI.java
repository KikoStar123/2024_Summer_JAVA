package client.ui;

import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import client.ui.LibraryUI;
import javafx.scene.control.Button;

public class MainUI extends Application {
    private User user;

    public MainUI(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("主界面");

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        // 添加标签
        Label welcomeLabel = new Label("用户名: " + user.getUsername() + "\t身份: " + user.getRole() + "\t年龄: " + user.getAge());
        vbox.getChildren().add(welcomeLabel);

        // 添加按钮
        Button libButton = new Button("图书馆");
        libButton.setOnAction(e -> handleLibrary(user.getUsername()));
        vbox.getChildren().add(libButton);

        Button button2 = new Button("选课系统");
        vbox.getChildren().add(button2);

        Button stubutton = new Button("学籍管理");
        stubutton.setOnAction(e -> handleStudent(user.getUsername()));
        vbox.getChildren().add(stubutton);

        Scene scene = new Scene(vbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLibrary(String username) {
        Platform.runLater(() -> {
            LibraryUI libraryUI = new LibraryUI(user);
            try {
                libraryUI.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void handleStudent(String username) {
        // 学籍管理处理逻辑
        Platform.runLater(() -> {
            StuUI stuUI = new StuUI(user);
            try {
                stuUI.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void display() {
        launch();
    }

}
