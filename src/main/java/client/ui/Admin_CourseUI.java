package client.ui;

import client.service.CourseSelection;
import client.service.User;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class Admin_CourseUI {
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

    public BorderPane createAdminCourseSelectionView() {
        BorderPane borderPane = new BorderPane(); // 确保每次调用都创建新的实例
        topBar = new HBox(30);
        topBar.setPadding(new Insets(10));
        Button btnCheckCourses = new Button("查看课程");
        btnCheckCourses.getStyleClass().add("main-button");
        Button btnAddCourses = new Button("添加课程");
        btnAddCourses.getStyleClass().add("main-button");

        btnCheckCourses.setOnAction(e -> displayCourses());
        btnAddCourses.setOnAction(e -> showAddCourseDialog());

        topBar.getChildren().addAll(btnCheckCourses, btnAddCourses);

        searchField = new TextField();
        searchField.setPromptText("搜索课程...");
        Button btnSearch = new Button("搜索");
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

        courseTableView.getColumns().addAll(courseIDColumn, courseNameColumn, courseTeacherColumn, courseCapacityColumn, selectedCountColumn);
        courseTableView.setItems(courseList);
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
}