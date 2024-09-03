package client.ui;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.net.URL;

import static client.service.StudentInformation.modifyOneStudentInfo;

public class StuUI extends Application {
    private User user;

    public StuUI(User user) {
        this.user = user;
    }


    public VBox createStudentInfoView() {
        if(user.getUsername().charAt(0)=='0'){
            StudentInformation.oneStudentInformation[] students = StudentInformation.viewAllStudentInfo();

            TableView<StudentInformation.oneStudentInformation> table = new TableView<>();

            table.setEditable(true); // 设置表格为可编辑
            // 清除现有的列，防止重复添加
            table.getColumns().clear();

            TableColumn<StudentInformation.oneStudentInformation, String> nameColumn = new TableColumn<>("Name");
            TableColumn<StudentInformation.oneStudentInformation, String> idColumn = new TableColumn<>("ID");
            TableColumn<StudentInformation.oneStudentInformation, String> genderColumn = new TableColumn<>("Gender");
            TableColumn<StudentInformation.oneStudentInformation, String> originColumn = new TableColumn<>("Origin");
            TableColumn<StudentInformation.oneStudentInformation, String> birthdayColumn = new TableColumn<>("Birthday");
            TableColumn<StudentInformation.oneStudentInformation, String> academyColumn = new TableColumn<>("Academy");

            nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
            genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
            originColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrigin()));
            birthdayColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthday()));
            academyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAcademy()));

// 设置列为可编辑
            nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            nameColumn.setOnEditCommit(event -> {
                StudentInformation.oneStudentInformation student = event.getRowValue();
                student.setName(event.getNewValue());
                boolean success = modifyOneStudentInfo(student.getId(), student);
                showAlert(success, student.getId());
            });

            idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            idColumn.setOnEditCommit(event -> {
                StudentInformation.oneStudentInformation student = event.getRowValue();
                student.setId(event.getNewValue());
                boolean success = modifyOneStudentInfo(student.getId(), student);
                showAlert(success, student.getId());
            });

            genderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            genderColumn.setOnEditCommit(event -> {
                StudentInformation.oneStudentInformation student = event.getRowValue();
                student.setGender(event.getNewValue());
                boolean success = modifyOneStudentInfo(student.getId(), student);
                showAlert(success, student.getId());
            });

            originColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            originColumn.setOnEditCommit(event -> {
                StudentInformation.oneStudentInformation student = event.getRowValue();
                student.setOrigin(event.getNewValue());
                boolean success = modifyOneStudentInfo(student.getId(), student);
                showAlert(success, student.getId());
            });

            birthdayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            birthdayColumn.setOnEditCommit(event -> {
                StudentInformation.oneStudentInformation student = event.getRowValue();
                student.setBirthday(event.getNewValue());
                boolean success = modifyOneStudentInfo(student.getId(), student);
                showAlert(success, student.getId());
            });

            academyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            academyColumn.setOnEditCommit(event -> {
                StudentInformation.oneStudentInformation student = event.getRowValue();
                student.setAcademy(event.getNewValue());
                boolean success = modifyOneStudentInfo(student.getId(), student);
                showAlert(success, student.getId());
            });
            table.getColumns().addAll(nameColumn, idColumn, genderColumn, originColumn, birthdayColumn, academyColumn);
            table.setItems(FXCollections.observableArrayList(students));

            VBox vbox = new VBox();
            vbox.getChildren().add(table);
            return vbox;

        }else {
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

            return vbox;

        }
    }
    //学籍修改成功后显示提示框
    private void showAlert(boolean success, String id) {
        Alert alert;
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("修改成功");
            alert.setHeaderText(null);
            alert.setContentText("学生信息修改成功，ID: " + id);
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("修改失败");
            alert.setHeaderText(null);
            alert.setContentText("学生信息修改失败，ID: " + id);
        }
        alert.showAndWait();
    }
    public static void main(String[]args){
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

    }
}

