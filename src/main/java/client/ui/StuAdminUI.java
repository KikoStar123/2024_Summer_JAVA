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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.net.URL;

import static client.service.Role.StuInfoManager;
import static client.service.StudentInformation.modifyOneStudentInfo;

/**
 * StuAdminUI 类为管理员提供了一个管理学生信息的用户界面，管理员可以查看、编辑和修改学生信息。
 * 学生信息包括姓名、ID、性别、籍贯、生日和学院信息，所有信息均可在表格中直接编辑。
 */
public class StuAdminUI extends Application {
    private User user;

    /**
     * 构造函数，初始化用户。
     *
     * @param user 当前登录的管理员用户
     */
    public StuAdminUI(User user) {
        this.user = user;
    }

    /**
     * 创建一个表格视图，用于显示和编辑学生信息。学生信息包括姓名、ID、性别、籍贯、生日和学院。
     * 管理员可以在表格中直接修改每个字段，修改后会同步更新数据库。
     *
     * @return 返回一个包含学生信息表格的 VBox 布局
     */
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

        ScrollPane scrollPane=new ScrollPane(table);
        VBox vbox = new VBox();
        vbox.getChildren().add(scrollPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // 使 ListView 高度适应 ScrollPane

// 确保 ListView 填充 ScrollPane
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        return vbox;
    }


    /**
     * 显示提示信息，用于通知用户信息修改是否成功。
     *
     * @param success 一个布尔值，表示修改操作是否成功
     * @param id 学生的 ID，用于在提示中显示具体的学生
     */
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
    /**
     * 处理登出操作，关闭当前窗口并打开登录界面。
     *
     * @param primaryStage 当前的主窗口
     */
    private void handleLogout(Stage primaryStage) {
        primaryStage.close(); // 关闭当前主界面

        // 打开 LoginUI 界面
        LoginUI loginUI = new LoginUI();
        Stage loginStage = new Stage();
        try {
            loginUI.start(loginStage); // 显示登录界面
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置并启动 JavaFX 应用程序的主界面，包含登出按钮和学生信息表格。
     *
     * @param primaryStage 主窗口
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("管理员界面");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));
        primaryStage.getIcons().add(image);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        HBox hbox = new HBox();
        Button logoutButton = new Button("登出");
        logoutButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        logoutButton.setOnAction(e -> handleLogout(primaryStage)); // 添加登出逻辑
        Region region=new Region();
        region.setMinWidth(700);
        hbox.getChildren().add(region);
        hbox.getChildren().add(logoutButton);

        VBox studentInfoView = createStudentInfoView();
        root.setCenter(studentInfoView);
        root.setTop(hbox);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

