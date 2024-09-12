package client.ui;

import client.service.CourseSelection;
import client.service.User;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Admin_CourseUI extends Application {
    private User user;
    private CourseSelection courseSelection;
    private TableView<CourseSelection.oneCourseinfo> courseTableView;
    private ObservableList<CourseSelection.oneCourseinfo> courseList;
    private HBox topBar; // 保存顶部栏的引用，以便重新显示
    private TextField searchField;
    private AddCourseUI addCourseUI; // 添加一个成员变量来持有 AddCourseUI 的实例

    public Admin_CourseUI(User user) {
        this.user = user;
        this.courseSelection = new CourseSelection();
        this.courseList = FXCollections.observableArrayList();
        // 获取所有课程信息并填充到 courseList
        CourseSelection.oneCourseinfo[] allCourses = courseSelection.GetAllCourses();
        if (allCourses != null) {
            for (CourseSelection.oneCourseinfo course : allCourses) {
                courseList.add(course);
            }
        }
        this.addCourseUI = new AddCourseUI(user, courseSelection, this);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("课程管理");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        primaryStage.getIcons().add(image);

        BorderPane borderPane = createAdminCourseSelectionView();

        // 设置场景
        Scene scene = new Scene(borderPane, 1000, 618); // 调整尺寸以适应新布局
        primaryStage.setMinWidth(1000); // 最小宽度为800像素
        primaryStage.setMinHeight(618); // 最小高度为600像素

        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public BorderPane createAdminCourseSelectionView() {
        BorderPane borderPane = new BorderPane(); // 确保每次调用都创建新的实例
        topBar = new HBox(30);
        topBar.setPadding(new Insets(10));
        Button btnAddCourses = new Button("添加课程");
        btnAddCourses.getStyleClass().add("main-button");
        Button btnRefresh = new Button("刷新");
        btnRefresh.getStyleClass().add("main-button");

        btnAddCourses.setOnAction(e -> showAddCourseDialog());
        btnRefresh.setOnAction(e -> refreshCourses());

        topBar.getChildren().addAll(btnAddCourses, btnRefresh);

        searchField = new TextField();
        searchField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        searchField.setPromptText("搜索课程...");
        Button btnSearch = new Button("搜索");
        btnSearch.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        btnSearch.setOnAction(e -> searchCourses());

        HBox searchBox = new HBox(10, searchField, btnSearch);
        searchBox.setPadding(new Insets(10));

        courseTableView = new TableView<>();
        configureCourseTableView();

        VBox vBox = new VBox(20, topBar, searchBox, courseTableView);
        borderPane.setCenter(vBox);

        return borderPane;
    }

    private void configureCourseTableView() {
        TableColumn<CourseSelection.oneCourseinfo, String> courseIDColumn = new TableColumn<>("课程号");
        TableColumn<CourseSelection.oneCourseinfo, String> courseNameColumn = new TableColumn<>("课程名称");
        TableColumn<CourseSelection.oneCourseinfo, String> courseTeacherColumn = new TableColumn<>("上课教师");
        TableColumn<CourseSelection.oneCourseinfo, Number> courseCapacityColumn = new TableColumn<>("课容量");
        TableColumn<CourseSelection.oneCourseinfo, Number> selectedCountColumn = new TableColumn<>("已选人数");

        courseIDColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCourseID()));
        courseNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCourseName()));
        courseTeacherColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCourseTeacher()));
        courseCapacityColumn.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getCourseCapacity()));
        selectedCountColumn.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getSelectedCount()));

        // 创建动作列
        TableColumn<CourseSelection.oneCourseinfo, Void> actionColumn = new TableColumn<>("操作");
        actionColumn.setCellFactory(column -> {
            return new TableCell<CourseSelection.oneCourseinfo, Void>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button btn = new Button("查看");
                        btn.setOnAction(event -> {
                            int index = getIndex();
                            CourseSelection.oneCourseinfo course = getTableView().getItems().get(index);
                            showCourseDetailsDialog(course);
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                };
            };
        });

        actionColumn.prefWidthProperty().bind(courseTableView.widthProperty().divide(4)); // 设置动作列的宽度

        courseTableView.getColumns().addAll(courseIDColumn, courseNameColumn, courseTeacherColumn, courseCapacityColumn, selectedCountColumn, actionColumn);
        courseTableView.setItems(courseList);
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

    private void displayCourses() {
        courseTableView.setItems(courseList);
    }

    public void refreshCourses() {
        // 清除当前的课程列表
        courseList.clear();
        // 重新获取所有课程信息并填充到 courseList
        CourseSelection.oneCourseinfo[] allCourses = courseSelection.GetAllCourses();
        if (allCourses != null) {
            for (CourseSelection.oneCourseinfo course : allCourses) {
                courseList.add(course);
            }
        }
        // 更新 TableView 的显示
        courseTableView.setItems(courseList);
    }

    private void searchCourses() {
        String searchText = searchField.getText();
        if (searchText == null || searchText.isEmpty()) {
            courseTableView.setItems(courseList);
        } else {
            courseTableView.setItems(courseList.filtered(course -> course.getCourseID().toLowerCase().contains(searchText.toLowerCase())
                    || course.getCourseName().toLowerCase().contains(searchText.toLowerCase())
                    || course.getCourseTeacher().toLowerCase().contains(searchText.toLowerCase())));
        }
    }

    private void showAddCourseDialog() {
        addCourseUI.showAddCourseDialog();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
