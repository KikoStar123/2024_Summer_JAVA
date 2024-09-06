package client.ui;

import client.service.CourseSelection;
import client.service.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class CourseSelectionUI {
    private User user;
    private CourseSelection courseSelection;

    public CourseSelectionUI(User user) {
        this.user = user;
        this.courseSelection = new CourseSelection();
    }

    public BorderPane createLibraryView() {
        BorderPane borderPane = new BorderPane();

        // 搜索栏和按钮
        TextField searchField = new TextField();
        searchField.setPromptText("输入课程名称或教师");
        searchField.setFont(javafx.scene.text.Font.font("Segoe UI", 14));
        searchField.setStyle("-fx-text-fill: #4B0082;");

        Button searchButton = new Button("查询");
        searchButton.setFont(javafx.scene.text.Font.font("Segoe UI", 14));
        searchButton.setStyle("-fx-text-fill: #4B0082;");

        HBox searchBox = new HBox(10, new Label("课程名称或教师:"), searchField, searchButton);
        searchBox.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #4B0082;");
        searchBox.setPadding(new Insets(10));

        // 设置按钮样式
        String buttonStyle = "-fx-background-color: #FFFFFF; -fx-text-fill: #4B0082; -fx-border-color: #6A0DAD; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-font-family: 'Segoe UI'; -fx-font-size: 16px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 10, 0, 4, 4);";
        String buttonHoverStyle = "-fx-background-color: rgba(255, 255, 255, 0.3); -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 15, 0, 0, 0); -fx-opacity: 0.9;";
        String buttonPressedStyle = "-fx-background-color: #D8BFD8; -fx-border-color: #6A0DAD;";

        searchButton.setStyle(buttonStyle);
        searchButton.setOnMouseEntered(e -> searchButton.setStyle(buttonHoverStyle));
        searchButton.setOnMouseExited(e -> searchButton.setStyle(buttonStyle));
        searchButton.setOnMousePressed(e -> searchButton.setStyle(buttonPressedStyle));
        searchButton.setOnMouseReleased(e -> searchButton.setStyle(buttonHoverStyle));

        // 课程列表
        ListView<String> courseList = new ListView<>();
        courseList.setItems(getCourseList());
        Label courseListLabel = new Label("可选课程:");
        courseListLabel.setFont(javafx.scene.text.Font.font("Segoe UI", 14));
        courseListLabel.setStyle("-fx-text-fill: #4B0082;");

        // 已选课程
        ListView<String> selectedCourses = new ListView<>();
        selectedCourses.setItems(getSelectedCourses());
        Label selectedCoursesLabel = new Label("已选课程:");
        selectedCoursesLabel.setFont(javafx.scene.text.Font.font("Segoe UI", 14));
        selectedCoursesLabel.setStyle("-fx-text-fill: #4B0082;");

        // 按钮
        Button enrollButton = new Button("选课");
        enrollButton.setFont(javafx.scene.text.Font.font("Segoe UI", 14));
        enrollButton.setStyle(buttonStyle);
        enrollButton.setOnAction(e -> enrollCourse(courseList, selectedCourses));

        Button dropButton = new Button("退选");
        dropButton.setFont(javafx.scene.text.Font.font("Segoe UI", 14));
        dropButton.setStyle(buttonStyle);
        dropButton.setOnAction(e -> dropCourse(selectedCourses));

        HBox buttonBox = new HBox(10, enrollButton, dropButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        // 布局
        borderPane.setTop(searchBox);
        borderPane.setCenter(new BorderPane(courseListLabel, null, null, null, courseList));
        borderPane.setBottom(new BorderPane(selectedCoursesLabel, null, null, null, selectedCourses));
        borderPane.setRight(buttonBox);

        return borderPane;
    }

    private ObservableList<String> getCourseList() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        CourseSelection.oneCourseinfo[] courseInfoArray = courseSelection.GetAllCourses();
        for (CourseSelection.oneCourseinfo course : courseInfoArray) {
            courses.add(course.getCourseName() + " - " + course.getCourseTeacher() + " - 学分: " + course.getCourseCredits());
        }
        return courses;
    }

    private ObservableList<String> getSelectedCourses() {
        ObservableList<String> selected = FXCollections.observableArrayList();
        CourseSelection.oneCourseinfo[] enrolledCourses = courseSelection.viewEnrolledCourses(user.getUsername());
        for (CourseSelection.oneCourseinfo course : enrolledCourses) {
            selected.add(course.getCourseName() + " - " + course.getCourseTeacher() + " - 学分: " + course.getCourseCredits());
        }
        return selected;
    }

    private void enrollCourse(ListView<String> courseList, ListView<String> selectedCourses) {
        String selectedCourse = courseList.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            String courseID = extractCourseID(selectedCourse);
            if (courseID != null) {
                boolean success = courseSelection.enrollInCourse(user.getUsername(), courseID);
                if (success) {
                    selectedCourses.getItems().add(selectedCourse);
                    courseList.getSelectionModel().clearSelection();
                    alert("选课成功", "您已成功选择课程: " + selectedCourse);
                } else {
                    alert("选课失败", "选课失败，请重试或联系管理员。");
                }
            } else {
                alert("选课失败", "无法识别课程 ID，请重试。");
            }
        }
    }

    private void dropCourse(ListView<String> selectedCourses) {
        String selectedCourse = selectedCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            String courseID = extractCourseID(selectedCourse);
            if (courseID != null) {
                boolean success = courseSelection.dropCourse(user.getUsername(), courseID);
                if (success) {
                    selectedCourses.getItems().remove(selectedCourse);
                    alert("退选成功", "您已成功退选课程: " + selectedCourse);
                } else {
                    alert("退选失败", "退选失败，请重试或联系管理员。");
                }
            } else {
                alert("退选失败", "无法识别课程 ID，请重试。");
            }
        }
    }

    private String extractCourseID(String courseItem) {
        String[] parts = courseItem.split(" - ");
        return parts.length > 1 ? parts[parts.length - 1].trim() : null;
    }

    private void alert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}