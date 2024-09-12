package client.ui;

import client.service.Role;
import client.service.StudentInformation;
import client.service.User;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.net.URL;

import static client.service.Role.StuInfoManager;
import static client.service.StudentInformation.modifyOneStudentInfo;

public class StuAdminUI extends Application {
    private User user;

    public StuAdminUI(User user) {
        this.user = user;
    }

    public VBox createStudentInfoView() {
        StudentInformation.oneStudentInformation[] students = StudentInformation.viewAllStudentInfo();

        TableView<StudentInformation.oneStudentInformation> table = new TableView<>();
        table.setEditable(true);
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
    }

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("管理员界面");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));
        primaryStage.getIcons().add(image);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox studentInfoView = createStudentInfoView();
        root.setCenter(studentInfoView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
