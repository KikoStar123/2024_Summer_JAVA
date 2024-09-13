package client.ui;

import client.service.CourseSelection;
import client.service.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * CourseSelectionUI 类用于处理学生选课的图形用户界面，包括可选课程、已选课程和我的课表等功能。
 */
public class CourseSelectionUI {
    /**
     * 当前登录的用户实例，用于获取用户相关的选课信息。
     */
    private User user;

    /**
     * CourseSelection 实例，用于与选课系统交互，包括获取课程信息、选课、退课等功能。
     */
    private CourseSelection courseSelection;

    /**
     * ListView 用于显示课程列表，可以显示可选课程或已选课程。
     */
    private ListView<CourseInfo> courseListView;

    /**
     * ObservableList 课程列表，存储从 CourseSelection 获取的所有课程信息。
     */
    private ObservableList<CourseInfo> courseList;

    /**
     * ObservableList 已选课程列表，存储当前用户已选择的课程信息。
     */
    private ObservableList<CourseInfo> selectedCourses;

    /**
     * HBox 顶部工具栏，包含可选课程、已选课程和我的课表按钮。
     */
    private HBox topBar;

    /**
     * Random 对象用于在显示课程表时随机分配颜色。
     */
    private Random random;

    /**
     * 颜色列表，用于在显示课程表时为课程块随机分配背景颜色。
     */
    private List<String> colors;

    /**
     * 构造函数，初始化 CourseSelectionUI 类的实例，并设置当前用户和课程选择服务。
     *
     * @param user 当前登录的用户
     */
    public CourseSelectionUI(User user) {

        this.user = user;
        this.courseSelection = new CourseSelection();
        this.courseList = FXCollections.observableArrayList();
        this.selectedCourses = FXCollections.observableArrayList();
        this.random = new Random();
        this.colors = Arrays.asList("#DCDCDC", "#BCCED9","#B4D8B4", "#F5C6C6", "#D8B4A6", "#6B7C8C","#A8B2A6","#E8B6B6");
        updateCourseList();
        updateSelectedCourses();
    }

    /**
     * 创建选课系统的封面界面，包括欢迎图片和进入选课系统按钮。
     *
     * @return 返回封面界面的 BorderPane 布局
     */
    public BorderPane createCover() {

        ImageView photo = new ImageView(new Image(getClass().getResource("/cover-course.jpg").toExternalForm()));
        photo.setFitWidth(440); // 你可以根据窗口大小调整这个值
        photo.setFitHeight(550); // 你可以根据窗口大小调整这个值
        //photo.setPreserveRatio(true);//保持图片的宽高比例不变

        Button loginButton=new Button("进 入 选 课 系 统");
        loginButton.getStyleClass().add("cover-button"); // 应用CSS中的按钮样式
        loginButton.setOnAction(e->{
            MainUI.borderPane.setCenter(createCourseSelectionView());
        });

        ImageView icon = new ImageView(new Image(getClass().getResource("/icon-course.jpg").toExternalForm()));
        icon.setFitWidth(200); // 你可以根据窗口大小调整这个值
        icon.setFitHeight(200); // 你可以根据窗口大小调整这个值

        // 创建阴影效果
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.5)); // 设置阴影颜色
        dropShadow.setRadius(10); // 设置阴影半径
        dropShadow.setOffsetX(5); // 设置阴影水平偏移
        dropShadow.setOffsetY(5); // 设置阴影垂直偏移
        // 将阴影效果应用到 ImageView
        icon.setEffect(dropShadow);

        VBox vbox = new VBox(80); // 创建一个垂直布局，间距为10
        vbox.getChildren().addAll(icon, loginButton);
        vbox.setAlignment(Pos.CENTER); // 设置对齐方式为居中

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
    /**
     * 创建选课系统主界面，包括可选课程、已选课程和我的课表等功能。
     *
     * @return 返回选课系统主界面的 BorderPane 布局
     */
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


        // 绑定按钮点击事件
        btnAvailableCourses.setOnAction(e -> displayCourses(courseList, "选择", btnAvailableCourses));
        btnSelectedCourses.setOnAction(e -> displayCourses(selectedCourses, "退选", btnSelectedCourses));
        btnMySchedule.setOnAction(e -> showMySchedule());

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

        // 默认显示可选课程
        refreshCourses(btnAvailableCourses);
        displayCourses(courseList, "选择", btnAvailableCourses);

        return borderPane;
    }

    /**
     * 更新课程列表，将所有未被选中的课程添加到可选课程列表中。
     */
    private void updateCourseList() {

        CourseSelection.oneCourseinfo[] courseInfoArray = courseSelection.GetAllCourses();
        courseList.clear();
        for (CourseSelection.oneCourseinfo course : courseInfoArray) {
            // 检查课程是否已经在已选课程列表中
            if (!selectedCourses.stream().anyMatch(c -> c.getCourseID().equals(course.getCourseID()))) {
                courseList.add(new CourseInfo(course, courseSelection));
            }
        }
    }

    /**
     * 更新已选课程列表，将当前用户已选择的课程信息添加到已选课程列表中。
     */
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


    /**
     * 显示当前用户的课表，弹出一个新窗口以显示已选课程的时间安排。
     */
    public void showMySchedule() {

        Stage scheduleStage = new Stage();
        Image image1 = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        scheduleStage.getIcons().add(image1);
        scheduleStage.setTitle("我的课表");
        scheduleStage.initModality(Modality.APPLICATION_MODAL);

        GridPane gridPane = createScheduleView();
        VBox vbox = new VBox(gridPane);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);

        Scene scene = new Scene(vbox, 680, 500);
        scheduleStage.setScene(scene);
        scheduleStage.showAndWait();


    }
    /**
     * 创建课表视图，显示当前用户的已选课程及其对应的时间安排。
     *
     * @return 返回创建好的课表 GridPane 布局
     */
    private GridPane createScheduleView() {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(10));
        gridPane.setStyle("-fx-background-color: #f0f0f0; -fx-background-image: url('/table.png'); -fx-background-repeat: no-repeat; -fx-background-position: center center; -fx-background-size: cover;");

        // 添加时间段标签，每个时间段占用两行
        String[] periods = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        for (int i = 0; i < periods.length; i++) {
            Label periodLabel = new Label(periods[i]);
            periodLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
            gridPane.add(periodLabel, 0, i  + 1); // 每个时间段占用奇数行，偶数行为分割线或空行
        }

        // 添加星期标签
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
             //使用Platform.runLater来获取Label的宽度和高度
            gridPane.add(dayLabel, i + 1, 0); // 将星期标签添加到第一行
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(70);
            gridPane.getColumnConstraints().add(column);
            GridPane.setHalignment(dayLabel, HPos.CENTER);
            GridPane.setValignment(dayLabel, VPos.CENTER);
        }
        // 设置每行的最大高度（每个时间段行和分割线行都设置最大高度）
        for (int i = 0; i < periods.length ; i++) {
            RowConstraints row = new RowConstraints();
            row.setMaxHeight(50); // 设置每行的最大高度为 50 像素（根据需要调整）
            gridPane.getRowConstraints().add(row);
        }
//        // 添加分割线到第 5 和第 6 时间段之间
//        Separator separator = new Separator();
//        separator.setStyle("-fx-background-color: #6B8E23;"); // 分割线颜色，可以根据需求调整
//        gridPane.add(separator, 0, 10, days.length + 1, 1); // 在时间段5-6之间添加分割线（插入在第11行）

        // 获取已选课程信息并填充课程表
        ObservableList<CourseSelection.oneCourseinfo> enrolledCourses = FXCollections.observableArrayList(
                Arrays.asList(courseSelection.viewEnrolledCourses(user.getUsername()))
        );

        Set<String> addedCourses = new HashSet<>();

        int colorIndex = 0; // 颜色索引

        for (CourseSelection.oneCourseinfo course : enrolledCourses) {
            if (addedCourses.contains(course.getCourseID())) {
                continue;
            }

            String courseName = course.getCourseName();
            CourseSelection.onePeriod[] periods_course = course.getCourseTime();
            String dayName = getDayOfWeek(periods_course[0].getDay());
            int dayIndex = Arrays.asList(days).indexOf(dayName) + 1;
            int startPeriod = periods_course[0].getStasection();
            int endPeriod = periods_course[0].getEndsection();

            Label courseLabel = new Label(courseName);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(courseLabel);
            // 设置圆角和背景色
            String backgroundColor = colors.get(colorIndex % colors.size());
            borderPane.setStyle(String.format("-fx-border-color: white; -fx-border-radius: 10; -fx-border-width: 1px; -fx-background-color: %s;", backgroundColor));

            // 确保只添加一次，并设置正确的位置和跨度
            if (endPeriod - startPeriod + 1 > 0) {
                gridPane.add(borderPane, dayIndex, startPeriod );
                GridPane.setRowSpan(borderPane, endPeriod - startPeriod + 1);
            } else {
                gridPane.add(borderPane, dayIndex, startPeriod );
            }

            addedCourses.add(course.getCourseID());
            colorIndex++; // 更新颜色索引
        }
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: #6B8E23; -fx-border-width: 3px; -fx-border-style: solid;");
        borderPane.setCenter(gridPane);

        // 将 BorderPane 放入一个新的 GridPane 中
        GridPane finalGridPane = new GridPane();
        finalGridPane.add(borderPane, 0, 0); // 将 BorderPane 添加到新的 GridPane 中
        return finalGridPane; // 返回包含边框的 GridPane
    }

    /**
     * 显示课程列表，包括可选课程或已选课程，并为每门课程添加操作按钮。
     *
     * @param courses          要显示的课程列表
     * @param selectButtonLabel 操作按钮的标签（选择或退选）
     * @param activeButton      当前激活的按钮（可选课程或已选课程按钮）
     */
    private void displayCourses(ObservableList<CourseInfo> courses, String selectButtonLabel, Button activeButton) {
        courseListView.setItems(FXCollections.observableArrayList(courses));
        courseListView.setCellFactory(listView -> new ListCell<CourseInfo>() {
            private final HBox hbox = new HBox(5);
            private final Label courseLabel = new Label();
            private final Button viewButton = new Button("查看");
            private final Button selectButton = new Button(selectButtonLabel);

            {
                courseLabel.setPrefWidth(250);
                HBox.setHgrow(courseLabel, Priority.ALWAYS);
                hbox.getChildren().addAll(courseLabel, viewButton, selectButton);
            }

            @Override
            protected void updateItem(CourseInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    courseLabel.setText(item.getCourseName());
                    viewButton.setOnAction(e -> handleCourseAction(item, "查看", activeButton));
                    selectButton.setOnAction(e -> handleCourseAction(item, selectButtonLabel, activeButton));
                    setGraphic(hbox);
                }
            }
        });
    }

    /**
     * 处理课程操作，例如选择课程、退选课程或查看课程详细信息。
     *
     * @param course       课程信息
     * @param buttonLabel  按钮标签（选择、退选或查看）
     * @param activeButton 当前激活的按钮（可选课程或已选课程按钮）
     */
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
        }else if ("我的课表".equals(buttonLabel)) {
            showMySchedule();
        }
        // Refresh the course list view
        refreshCourses(activeButton);
    }

    /**
     * 显示课程详细信息的对话框，展示选中的课程的具体信息。
     *
     * @param course 要显示详细信息的课程
     */
    private void showCourseDetailsDialog(CourseSelection.oneCourseinfo course) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("课程详细信息");
        Image image1 = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        dialogStage.getIcons().add(image1);

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
        String courseType = course.getCourseType();
        String courseTypeLabelText = "课程类型: " + (courseType.equals("required") ? "必修" : "选修");
        Label courseTypeLabel = new Label(courseTypeLabelText);

        vbox.getChildren().addAll(courseIDLabel, courseNameLabel, courseTeacherLabel, courseCreditLabel, courseTimeLabel, courseCapacityLabel, courseSelectLabel, courseRoomLabel, courseTypeLabel);

        Scene dialogScene = new Scene(vbox, 300, 500);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    /**
     * 格式化课程时间，将课程时间段转换为可读的字符串形式。
     *
     * @param periods 课程时间段数组
     * @return 格式化后的课程时间字符串
     */
    private String formatCourseTime(CourseSelection.onePeriod[] periods) {
        StringBuilder timeStringBuilder = new StringBuilder();
        for (int i = 0; i < periods.length; i++) {
            CourseSelection.onePeriod period = periods[i];
            // 拼接单个时间段的字符串表示
            String singlePeriodTime = period.getStaWeek() + "-" + period.getEndWeek() + " 周 "
                    + getDayOfWeek(period.getDay()) + " "
                    + period.getStasection() + "-" + period.getEndsection() + "节";
            // 将单个时间段的字符串添加到总的字符串构建器中
            timeStringBuilder.append(singlePeriodTime);
            // 如果不是最后一个时间段，添加分隔符
            if (i < periods.length - 1) {
                timeStringBuilder.append("; ");
            }
        }
        return timeStringBuilder.toString();
    }

    // 辅助方法，将数字星期转换为文字描述
    /**
     * 根据数字星期获取对应的中文描述，例如将数字1转换为“周一”。
     *
     * @param day 数字星期
     * @return 对应的中文星期描述
     */
    private String getDayOfWeek(int day) {
        switch (day) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 7:
                return "周日";
            default:
                return "未知";
        }
    }

    /**
     * 刷新课程列表，更新并显示可选课程或已选课程列表。
     *
     * @param activeButton 当前激活的按钮（可选课程或已选课程按钮）
     */
    private void refreshCourses(Button activeButton) {
       if (activeButton == topBar.getChildren().get(0)) { // 可选课程按钮
            updateCourseList(); // 更新课程列表
            displayCourses(courseList, "选择", activeButton);
        } else if (activeButton == topBar.getChildren().get(1)) { // 已选课程按钮
            updateSelectedCourses(); // 更新已选课程列表
            displayCourses(selectedCourses, "退选", activeButton);
        }
    }

    /**
     * 显示提示框，弹出一个提示信息对话框。
     *
     * @param title   对话框的标题
     * @param content 对话框的内容
     */
    private void alert(String title, String content) {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * CourseInfo 类用于封装课程信息，包含课程名称和课程ID。
     */
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