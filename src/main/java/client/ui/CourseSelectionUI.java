package client.ui;

import client.service.CourseSelection;
import client.service.User;
import com.sun.javafx.stage.EmbeddedWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CourseSelectionUI {
    private User user;
    private CourseSelection courseSelection;
    private ListView<CourseInfo> courseListView;
    private ObservableList<CourseInfo> courseList;
    private ObservableList<CourseInfo> selectedCourses;
    private HBox topBar; // 保存顶部栏的引用，以便重新显示

    public CourseSelectionUI(User user) {
        this.user = user;
        this.courseSelection = new CourseSelection();
        this.courseList = FXCollections.observableArrayList();
        this.selectedCourses = FXCollections.observableArrayList();
        updateCourseList();
        updateSelectedCourses();
    }

    public BorderPane createCourseSelectionView() {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 600, 400);
        topBar = new HBox(10);  // 调整为VBox，按钮垂直排列，更加紧凑
        topBar.setPadding(new Insets(10));

        Button btnAvailableCourses = new Button("可选课程");
        Button btnSelectedCourses = new Button("已选课程");
        Button btnMySchedule = new Button("我的课表");
        btnAvailableCourses.getStyleClass().add("main-button");
        btnSelectedCourses.getStyleClass().add("main-button");
        btnMySchedule.getStyleClass().add("main-button");

//        // 调整每个按钮的最大宽度
//        btnAvailableCourses.setMaxWidth(Double.MAX_VALUE);
//        btnSelectedCourses.setMaxWidth(Double.MAX_VALUE);
//        btnMySchedule.setMaxWidth(Double.MAX_VALUE);

        // 绑定按钮点击事件
        btnAvailableCourses.setOnAction(e -> displayCourses(courseList, "选择", btnAvailableCourses));
        btnSelectedCourses.setOnAction(e -> displayCourses(selectedCourses, "退选", btnSelectedCourses));
        btnMySchedule.setOnAction(e -> displayCourses(selectedCourses, "查看", btnMySchedule));

        // 添加按钮到 topBar
        topBar.getChildren().addAll(btnAvailableCourses, btnSelectedCourses, btnMySchedule);

        // 创建课程列表
        courseListView = new ListView<>();
        courseListView.setPrefHeight(300);  // 减少ListView的高度

//        // 将课程列表应用样式
//        courseListView.getStyleClass().add("gray-border");

        // 将 topBar 放在界面的顶部，将课程列表放在中央
        borderPane.setTop(topBar);
        borderPane.setCenter(courseListView);

        // 加载样式表
//        Scene scene = new Scene(borderPane, 600, 400);
//        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
//        Stage primaryStage = new Stage();
//        primaryStage.setScene(scene); // 将 Scene 添加到 Stage
//        primaryStage.show();
        // 默认显示可选课程
        displayCourses(courseList, "选择", btnAvailableCourses);

        return borderPane;
    }

    private void updateCourseList() {
        CourseSelection.oneCourseinfo[] courseInfoArray = courseSelection.GetAllCourses();
        courseList.clear();
        for (CourseSelection.oneCourseinfo course : courseInfoArray) {
            // 使用 courseSelection 实例创建 CourseInfo 类的实例
            courseList.add(new CourseInfo(course, courseSelection));
        }
    }

    private void updateSelectedCourses() {
        CourseSelection.oneCourseinfo[] enrolledCourses = courseSelection.viewEnrolledCourses(user.getUsername());
        selectedCourses.clear();

        if (!(enrolledCourses == null || enrolledCourses.length == 0)) {
            for (CourseSelection.oneCourseinfo course : enrolledCourses) {
                // 使用 courseSelection 实例创建 CourseInfo 类的实例
                selectedCourses.add(new CourseInfo(course, courseSelection));
            }
        }
    }

    private void displayCourses(ObservableList<CourseInfo> courses, String buttonLabel, Button activeButton) {
        courseListView.setItems(FXCollections.observableArrayList(courses));
        courseListView.setCellFactory(listView -> new ListCell<CourseInfo>() {
            private final HBox hbox = new HBox(5);
            private final Label courseLabel = new Label();
            private final Button actionButton = new Button(buttonLabel);

            {
                courseLabel.setPrefWidth(200);  // 调整宽度以防止过多空白
                courseLabel.getStyleClass().add("body-font");
                HBox.setHgrow(courseLabel, Priority.ALWAYS);
                hbox.getChildren().addAll(courseLabel, actionButton);
                hbox.getStyleClass().add("gray-border");  // 使用灰色边框样式
            }

            @Override
            protected void updateItem(CourseInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    courseLabel.setText(item.getCourseName());
                    actionButton.setText(buttonLabel);
                    actionButton.setOnAction(e -> handleCourseAction(item, buttonLabel, activeButton));
                    setGraphic(hbox);
                }
            }
        });
    }

    private void handleCourseAction(CourseInfo course, String buttonLabel, Button activeButton) {
        if ("选择".equals(buttonLabel)) {
            // 检查课程是否已经在已选课程列表中
            if (selectedCourses.stream().anyMatch(c -> c.getCourseID().equals(course.getCourseID()))) {
                alert("选课失败", "您已经选择了这门课程，不能重复选择。");
            } else {
                boolean success = courseSelection.enrollInCourse(user.getUsername(), course.getCourseID());
                if (success) {
                    selectedCourses.add(course);
                    courseList.remove(course);
                    alert("选课成功", "您已成功选择课程: " + course.getCourseName());
                } else {
                    alert("选课失败", "选课失败，请重试或联系管理员。");
                }
            }
        } else if ("退选".equals(buttonLabel)) {
            boolean success = courseSelection.dropCourse(user.getUsername(), course.getCourseID());
            if (success) {
                courseList.add(course);
                selectedCourses.remove(course);
                alert("退选成功", "您已成功退选课程: " + course.getCourseName());
            } else {
                alert("退选失败", "退选失败，请重试或联系管理员。");
            }
        } else if ("查看".equals(buttonLabel)) {
            // 调用 viewCourseInfo 方法来获取课程详细信息
            CourseSelection.oneCourseinfo courseInfo = courseSelection.viewCourseInfo(course.getCourseID());
            if (courseInfo != null) {
                // 如果获取成功，显示课程详细信息对话框
                showCourseDetailsDialog(courseInfo);
            } else {
                // 如果获取失败，显示错误提示
                alert("查看课程信息失败", "无法获取课程详细信息，请重试或联系管理员。");
            }
        }
        // Refresh the course list view
        refreshCourses(activeButton);
    }

    private void showCourseDetailsDialog(CourseSelection.oneCourseinfo course) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("课程详细信息");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        // 创建并添加标签以显示课程信息
        Label courseIDLabel = new Label("课程号: " + course.getCourseID());
        Label courseNameLabel = new Label("课程名: " + course.getCourseName());
        Label courseTeacherLabel = new Label("教师: " + course.getCourseTeacher());
        Label courseCreditLabel = new Label("学分: " + course.getCourseCredits());

        // 调用 parseCourseTime 方法来解析课程时间
        String formattedCourseTime = formatCourseTime(course.getCourseTime());
        Label courseTimeLabel = new Label("上课时间: " + formattedCourseTime);

        Label courseCapacityLabel = new Label("课容量: " + course.getCourseCapacity());
        Label courseSelectLabel = new Label("已选人数: " + course.getSelectedCount());
        Label courseRoomLabel = new Label("上课教室: " + course.getCourseRoom());
        Label courseTypeLabel = new Label("课程类型: " + course.getCourseType());

        vbox.getChildren().addAll(courseIDLabel, courseNameLabel, courseTeacherLabel, courseCreditLabel, courseTimeLabel, courseCapacityLabel, courseSelectLabel, courseRoomLabel, courseTypeLabel);

        Scene dialogScene = new Scene(vbox, 300, 500);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private String formatCourseTime(CourseSelection.onePeriod[] periods) {
        StringBuilder timeStringBuilder = new StringBuilder();
        for (CourseSelection.onePeriod period : periods) {
            if (timeStringBuilder.length() > 0) {
                timeStringBuilder.append("; ");
            }
            timeStringBuilder.append(period.toString());
        }
        return timeStringBuilder.toString();
    }

    private void refreshCourses(Button activeButton) {
        if (activeButton == topBar.getChildren().get(0)) { // 可选课程按钮
            displayCourses(courseList, "选择", activeButton);
        } else if (activeButton == topBar.getChildren().get(1)) { // 已选课程按钮
            displayCourses(selectedCourses, "退选", activeButton);
        }
    }

    private void alert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class CourseInfo {
        private final CourseSelection.oneCourseinfo courseInfo;
        private final CourseSelection courseSelection;

        public CourseInfo(CourseSelection.oneCourseinfo courseInfo, CourseSelection courseSelection) {
            this.courseInfo = courseInfo;
            this.courseSelection = courseSelection;
        }

        public String getCourseName() {
            return courseInfo.getCourseName();
        }

        public String getCourseID() {
            return courseInfo.getCourseID();
        }

    }
}