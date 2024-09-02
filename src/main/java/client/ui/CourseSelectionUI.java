package client.ui;

import client.service.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CourseSelectionUI extends Application {
    private User user;
    private Stage primaryStage;

    public CourseSelectionUI(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("选课系统");

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // 课程列表
        ListView<String> courseList = new ListView<>();
        courseList.setItems(getCourseList());
        root.getChildren().add(new Label("可选课程:"));
        root.getChildren().add(courseList);

        // 已选课程
        ListView<String> selectedCourses = new ListView<>();
        selectedCourses.setItems(getSelectedCourses());
        root.getChildren().add(new Label("已选课程:"));
        root.getChildren().add(selectedCourses);

        // 按钮
        Button enrollButton = new Button("选课");
        enrollButton.setOnAction(e -> enrollCourse(courseList, selectedCourses));
        root.getChildren().add(enrollButton);

        Button dropButton = new Button("退选");
        dropButton.setOnAction(e -> dropCourse(selectedCourses));
        root.getChildren().add(dropButton);

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObservableList<String> getCourseList() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        courses.add("课程1 - 教师A - 周一 1-2节 - 3学分");
        courses.add("课程2 - 教师B - 周三 3-4节 - 2学分");
        return courses;
    }

    private ObservableList<String> getSelectedCourses() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        courses.add("课程3 - 教师C - 周二 5-6节 - 4学分");
        return courses;
    }

    private void enrollCourse(ListView<String> courseList, ListView<String> selectedCourses) {
        String selectedCourse = courseList.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            selectedCourses.getItems().add(selectedCourse);
        }
    }

    private void dropCourse(ListView<String> selectedCourses) {
        String selectedCourse = selectedCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            selectedCourses.getItems().remove(selectedCourse);
        }
    }

    public void display(Stage primaryStage) {
        this.primaryStage = primaryStage;
        start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}