package client.ui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import client.service.CourseSelection;
import client.service.User;
public class AddCourseUI {
    private User user;
    private CourseSelection courseSelection;
    private Admin_CourseUI adminCourseUI; // 添加一个引用到 Admin_CourseUI

    public AddCourseUI(User user, CourseSelection courseSelection, Admin_CourseUI adminCourseUI) {
        this.user = user;
        this.courseSelection = courseSelection;
        this.adminCourseUI = adminCourseUI; // 初始化引用
    }

    public void showAddCourseDialog() {
        // 创建一个新的舞台（窗口）来作为对话框
        Stage dialogStage = new Stage();
        dialogStage.setTitle("添加新课程");
        dialogStage.initModality(Modality.APPLICATION_MODAL); // 模态对话框

        // 创建一个垂直盒布局来组织控件
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        // 创建一个网格布局来组织输入字段
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        // 创建标签和文本字段
        Label courseIDLabel = new Label("课程号:");
        TextField courseIDTextField = new TextField();
        Label courseNameLabel = new Label("课程名:");
        TextField courseNameTextField = new TextField();
        Label courseTeacherLabel = new Label("上课教师:");
        TextField courseTeacherTextField = new TextField();
        Label courseCreditsLabel = new Label("学分:");
        TextField courseCreditsTextField = new TextField();
        Label courseTimeLabel = new Label("时间:");
        TextField courseTimeTextField = new TextField();
        Label courseCapacityLabel = new Label("课容量:");
        TextField courseCapacityTextField = new TextField();
        Label courseRoomLabel = new Label("上课教室:");
        TextField courseRoomTextField = new TextField();
        Label courseTypeLabel = new Label("课程类型:");
        TextField courseTypeTextField = new TextField();

        // 将标签和文本字段添加到网格布局
        gridPane.add(courseIDLabel, 0, 0);
        gridPane.add(courseIDTextField, 1, 0);
        gridPane.add(courseNameLabel, 0, 1);
        gridPane.add(courseNameTextField, 1, 1);
        gridPane.add(courseTeacherLabel, 0, 2);
        gridPane.add(courseTeacherTextField, 1, 2);
        gridPane.add(courseCreditsLabel, 0, 3);
        gridPane.add(courseCreditsTextField, 1, 3);
        gridPane.add(courseTimeLabel, 0, 4);
        gridPane.add(courseTimeTextField, 1, 4);
        gridPane.add(courseCapacityLabel, 0, 5);
        gridPane.add(courseCapacityTextField, 1, 5);
        gridPane.add(courseRoomLabel, 0, 6);
        gridPane.add(courseRoomTextField, 1, 6);
        gridPane.add(courseTypeLabel, 0, 7);
        gridPane.add(courseTypeTextField, 1, 7);

        // 创建确认按钮
        Button confirmButton = new Button("确认添加");
        confirmButton.setOnAction(e -> {
            // 获取用户输入的值
            String courseID = courseIDTextField.getText();
            String courseName = courseNameTextField.getText();
            String courseTeacher = courseTeacherTextField.getText();
            int courseCredits = Integer.parseInt(courseCreditsTextField.getText());
            String courseTime = courseTimeTextField.getText();
            int courseCapacity = Integer.parseInt(courseCapacityTextField.getText());
            String courseRoom = courseRoomTextField.getText();
            String courseType = courseTypeTextField.getText();

            // 调用 addCourse 方法添加课程
            boolean success = courseSelection.addCourse(courseID, courseName, courseTeacher, courseCredits, courseTime, courseCapacity, courseRoom, courseType);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("添加成功");
                alert.setHeaderText(null);
                alert.setContentText("课程添加成功");
                alert.showAndWait();
                dialogStage.close(); // 关闭对话框
                // 刷新课程列表
                refreshCourses();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("添加失败");
                alert.setHeaderText(null);
                alert.setContentText("课程添加失败");
                alert.showAndWait();
            }
        });

        // 创建水平盒布局来组织按钮
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(confirmButton);

        // 将网格布局和按钮布局添加到垂直盒布局
        vbox.getChildren().addAll(gridPane, hbox);

        // 设置对话框的场景
        Scene dialogScene = new Scene(vbox, 400, 600);
        dialogStage.setScene(dialogScene);

        // 显示对话框
        dialogStage.showAndWait();
    }

    private void refreshCourses() {
        // 调用 Admin_CourseUI 中的 refreshCourses 方法来刷新课程列表
        adminCourseUI.refreshCourses();
    }
}