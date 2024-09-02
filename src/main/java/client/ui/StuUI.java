package client.ui;

import client.service.StudentInformation;
import client.service.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class StuUI extends Application {
    private User user;

    public StuUI(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        StudentInformation student = new StudentInformation();
        StudentInformation.oneStudentInformation onestudent = student.viewOneStudentInfo(user.getUsername());

        if(user.getUsername().charAt(0)=='0'){


        }else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Studentui.fxml"));
            Parent root = loader.load();

            Studentuicontroller controller = loader.getController();
            controller.setUser(user);

            primaryStage.setTitle("学生学籍管理");
            primaryStage.setScene(new Scene(root, 300, 500));
            primaryStage.show();
        }
    }
    public static void main(String[]args){
        launch(args);
    }

}

