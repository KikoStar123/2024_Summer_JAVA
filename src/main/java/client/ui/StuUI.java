package client.ui;

import client.service.StudentInformation;
import client.service.User;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StuUI extends Application {
    private User user;

    public StuUI(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
//        StudentInformation student = new StudentInformation();
//        StudentInformation.oneStudentInformation onestudent = student.viewStudentInfo(user.getRole(), user.getId());

        primaryStage.setTitle("学生学籍管理");

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label nameLabel = new Label("name: " );
        Button backButton = new Button("返回");

        backButton.setOnAction(e -> {
            primaryStage.close();
            MainUI mainUI = new MainUI(user);
            mainUI.display();
        });

        vbox.getChildren().addAll(nameLabel, backButton);

        Scene scene = new Scene(vbox, 300, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

