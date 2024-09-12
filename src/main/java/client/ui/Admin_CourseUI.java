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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
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

        BorderPane borderPane = new BorderPane(); // 确保每次调用都创建新的实例
        topBar = new HBox(30);
        topBar.setPadding(new Insets(10));
        Button btnCheckCourses = new Button("查看课程");
        btnCheckCourses.getStyleClass().add("main-button");
        Button btnAddCourses = new Button("添加课程");
        btnAddCourses.getStyleClass().add("main-button");
        Button btnRefresh = new Button("刷新");
        btnRefresh.getStyleClass().add("main-button");
        Button logoutButton = new Button("登出");
        logoutButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

        logoutButton.setOnAction(e -> handleLogout(primaryStage)); // 添加登出逻辑
        btnCheckCourses.setOnAction(e -> displayCourses());
        btnAddCourses.setOnAction(e -> showAddCourseDialog());
        btnRefresh.setOnAction(e -> refreshCourses());
        Region region = new Region();
        region.setMinWidth(600);
        topBar.getChildren().addAll(btnCheckCourses, btnAddCourses, btnRefresh, region, logoutButton);

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

        // 设置场景
        Scene scene = new Scene(borderPane, 1000, 618); // 调整尺寸以适应新布局
        primaryStage.setMinWidth(1000); // 最小宽度为800像素
        primaryStage.setMinHeight(618); // 最小高度为600像素

        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
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

        courseTableView.getColumns().addAll(courseIDColumn, courseNameColumn, courseTeacherColumn, courseCapacityColumn, selectedCountColumn);
        courseTableView.setItems(courseList);
    }

    private void displayCourses() {
        courseTableView.setItems(courseList);
    }

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
