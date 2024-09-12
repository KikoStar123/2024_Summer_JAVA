package client.ui;

import client.service.Role;
import client.service.StudentInformation;
import client.service.User;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.io.IOException;
import java.net.URL;

import static client.service.Role.StuInfoManager;
import static client.service.StudentInformation.modifyOneStudentInfo;

public class StuUI extends Application {
    private User user;

    public StuUI(User user) {
        this.user = user;
    }

    //管理员界面
    public VBox createStudentInfoView_StuInfoManager() {
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
    }

    //学生界面
    public BorderPane createStudentInfoView_Student() {
        //学生只需要显示信息

        //左侧图片栏
        ImageView photo = new ImageView(new Image(getClass().getResource("/cover-stuinfo.jpg").toExternalForm()));
        photo.setFitWidth(440); // 你可以根据窗口大小调整这个值
        photo.setFitHeight(550); // 你可以根据窗口大小调整这个值

        //右侧信息栏
        VBox vbox = new VBox(10); // 设置间距
        vbox.setAlignment(Pos.CENTER); // 设置对齐方式

        // 创建标题文本
        Text titleText = new Text("我的学籍");
        titleText.getStyleClass().add("title-font");

        // 设置用户信息
        StudentInformation.oneStudentInformation student = StudentInformation.viewOneStudentInfo(user.getUsername());

        // 创建并添加标签
        Label nameLabel = new Label("姓名: " + student.getName());
        nameLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        vbox.getChildren().add(nameLabel);

        Label idLabel = new Label("学号: " + student.getId());
        idLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        vbox.getChildren().add(idLabel);

        Label genderLabel = new Label("性别: " + student.getGender());
        genderLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        vbox.getChildren().add(genderLabel);

        Label birthLabel = new Label("出生日期: " + student.getBirthday());
        birthLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        vbox.getChildren().add(birthLabel);

        Label originLabel = new Label("籍贯: " + student.getOrigin());
        originLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        vbox.getChildren().add(originLabel);

        Label academyLabel = new Label("院系: " + student.getAcademy());
        academyLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        vbox.getChildren().add(academyLabel);


        StackPane stackPaneLeft = new StackPane(photo);
        stackPaneLeft.setPrefSize(440, 550); // 设置小框框的大小

        StackPane stackPaneRight = new StackPane(vbox);
        stackPaneRight.setPrefSize(440, 550); // 设置小框框的大小

        // 设置边框和圆角
        stackPaneLeft.setBorder(new Border(new BorderStroke(
                Color.rgb(205, 237, 222), // 边框颜色
                BorderStrokeStyle.SOLID, // 边框样式
                new CornerRadii(10), // 圆角半径
                new BorderWidths(4) // 边框宽度
        )));
        stackPaneRight.setBorder(new Border(new BorderStroke(
                Color.rgb(205, 237, 222), // 边框颜色
                BorderStrokeStyle.SOLID, // 边框样式
                new CornerRadii(10), // 圆角半径
                new BorderWidths(4) // 边框宽度
        )));

        // 设置背景颜色为浅灰色
        stackPaneRight.setBackground(new Background(new BackgroundFill(
                Color.rgb(245, 245, 245), // 背景颜色
                new CornerRadii(10), // 圆角半径
                Insets.EMPTY // 内边距
        )));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); // 设置GridPane居中
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // 将photo放置在GridPane的第一列第一行，并居中
        GridPane.setConstraints(stackPaneLeft, 0, 0);
        GridPane.setHalignment(stackPaneLeft, HPos.LEFT);
        GridPane.setValignment(stackPaneLeft, VPos.CENTER);
        gridPane.getChildren().add(stackPaneLeft);

        // 将loginButton放置在GridPane的第二列第一行，并靠右
        GridPane.setConstraints(stackPaneRight, 1, 0);
        GridPane.setHalignment(stackPaneRight, HPos.RIGHT);
        GridPane.setValignment(stackPaneLeft, VPos.CENTER);
        gridPane.getChildren().add(stackPaneRight);

        BorderPane Pane = new BorderPane();
        Pane.setCenter(gridPane); // 将GridPane放置在BorderPane的中心

        return Pane;
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

