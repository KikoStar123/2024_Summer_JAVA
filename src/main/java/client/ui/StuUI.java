package client.ui;

import client.service.Role;
import client.service.StudentInformation;
import client.service.User;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.net.URL;

import static client.service.Role.StuInfoManager;
import static client.service.StudentInformation.modifyOneStudentInfo;

public class StuUI extends Application {
    private User user;

    public StuUI(User user) {
        this.user = user;
    }

    public VBox createStudentInfoView() {

            HBox hBox=new HBox();
            ImageView photo = new ImageView(new Image(getClass().getResource("/background-seu-2.jpg").toExternalForm()));
            photo.setFitWidth(400); // 你可以根据窗口大小调整这个值
            photo.setFitHeight(200); // 你可以根据窗口大小调整这个值
            photo.setPreserveRatio(true);

            VBox vbox = new VBox(10); // 设置间距
            vbox.setAlignment(Pos.CENTER); // 设置对齐方式

            // 创建标题文本
            Text titleText = new Text("我的学籍");
            titleText.setFont(new Font("Segoe UI Black", 24));
            vbox.getChildren().add(titleText);

            // 设置用户信息
            StudentInformation.oneStudentInformation student = StudentInformation.viewOneStudentInfo(user.getUsername());

            // 创建并添加标签
            Label nameLabel = new Label("姓名: " + student.getName());
            vbox.getChildren().add(nameLabel);

            Label idLabel = new Label("学号: " + student.getId());
            vbox.getChildren().add(idLabel);

            Label genderLabel = new Label("性别: " + student.getGender());
            vbox.getChildren().add(genderLabel);

            Label birthLabel = new Label("出生日期: " + student.getBirthday());
            vbox.getChildren().add(birthLabel);

            Label originLabel = new Label("籍贯: " + student.getOrigin());
            vbox.getChildren().add(originLabel);

            Label academyLabel = new Label("院系: " + student.getAcademy());
            vbox.getChildren().add(academyLabel);

            hBox.getChildren().addAll(photo,vbox);
            VBox Vboxxx=new VBox(hBox);
            return Vboxxx;
        }

    public static void main(String[]args){
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

    }
}

