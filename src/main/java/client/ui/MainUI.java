package client.ui;

import client.service.Role;
import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

import static client.service.Role.Librarian;
import static client.service.Role.StuInfoManager;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.layout.Region;


public class MainUI extends Application {
    private User user;
    public static BorderPane borderPane;

    public MainUI(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("主界面");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        primaryStage.getIcons().add(image);

        // 创建 BorderPane 作为主布局
        borderPane = new BorderPane();

        // 创建左侧的按钮栏
        VBox leftBox = new VBox(5); // 设置间距
        leftBox.setPadding(new Insets(10)); // 设置内边距
//        leftBox.setStyle("-fx-border-color: #009b9f; -fx-border-width: 1.5; " +
//                "-fx-border-style: solid; -fx-padding: 10; -fx-background-color: rgba(205, 237, 222, 0.8);-fx-border-radius: 2px;");
        leftBox.setStyle("-fx-border-style: none; -fx-padding: 10; -fx-background-color: rgba(205, 237, 222, 0.8);");

        // 添加功能按钮
        VBox libBox = new VBox();
        libBox.getStyleClass().add("item-box");
        FontIcon libicon = new FontIcon(FontAwesome.BANK);
        libicon.setIconSize(20); // 设置图标大小
        libicon.setIconColor(Color.web("#009b9f")); // 设置图标颜色
        Label liblabel = new Label("图书馆");
        liblabel.getStyleClass().add("label");
        Tooltip libtip = new Tooltip("图书馆");
        Tooltip.install(liblabel, libtip);
        libBox.getChildren().addAll(libicon, liblabel);
        leftBox.getChildren().add(libBox);
        libBox.setOnMouseClicked(e -> handleLibrary(user.getUsername()));

        VBox courseBox = new VBox();
        courseBox.getStyleClass().add("item-box");
        FontIcon courseicon = new FontIcon(FontAwesome.LIST);
        courseicon.setIconSize(20); // 设置图标大小
        courseicon.setIconColor(Color.web("#009b9f")); // 设置图标颜色
        Label courselabel = new Label("选课系统");
        courselabel.getStyleClass().add("label");
        Tooltip coursetip = new Tooltip("选课系统");
        Tooltip.install(courselabel, coursetip);
        courseBox.getChildren().addAll(courseicon, courselabel);
        leftBox.getChildren().add(courseBox);
        courseBox.setOnMouseClicked(e -> handleCourse(user.getUsername()));

        VBox stuBox = new VBox();
        stuBox.getStyleClass().add("item-box");
        FontIcon stuicon = new FontIcon(FontAwesome.USER);
        stuicon.setIconSize(20); // 设置图标大小
        stuicon.setIconColor(Color.web("#009b9f")); // 设置图标颜色
        Label stulabel = new Label("学籍管理");
        stulabel.getStyleClass().add("label");
        Tooltip stutip = new Tooltip("学籍管理");
        Tooltip.install(stulabel, stutip);
        stuBox.getChildren().addAll(stuicon, stulabel);
        leftBox.getChildren().add(stuBox);
        stuBox.setOnMouseClicked(e -> handleStudent(user.getUsername()));

        VBox shopBox = new VBox();
        shopBox.getStyleClass().add("item-box");
        FontIcon shopicon = new FontIcon(FontAwesome.SHOPPING_BAG);
        shopicon.setIconSize(20); // 设置图标大小
        shopicon.setIconColor(Color.web("#009b9f")); // 设置图标颜色
        Label shoplabel = new Label("商店");
        shoplabel.getStyleClass().add("label");
        Tooltip shoptip = new Tooltip("商店");
        Tooltip.install(shoplabel, shoptip);
        shopBox.getChildren().addAll(shopicon, shoplabel);
        leftBox.getChildren().add(shopBox);
        shopBox.setOnMouseClicked(e -> {
            try {
                handleShop(user.getUsername());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox bankBox = new VBox();
        bankBox.getStyleClass().add("item-box");
        FontIcon bankicon = new FontIcon(FontAwesome.BANK);// 使用 Ikonli FontIcon 替代 Circle
        bankicon.setIconSize(20); // 设置图标大小
        bankicon.setIconColor(Color.web("#009b9f")); // 设置图标颜色
        Label banklabel = new Label("银行");
        banklabel.getStyleClass().add("label");
        Tooltip banktip = new Tooltip("银行");
        Tooltip.install(banklabel, banktip);
        bankBox.getChildren().addAll(bankicon, banklabel);
        leftBox.getChildren().add(bankBox);
        bankBox.setOnMouseClicked(e -> {handleBank(user.getUsername());});

        VBox updateBox = new VBox();
        updateBox.getStyleClass().add("item-box");
        FontIcon updateicon = new FontIcon(FontAwesome.LOCK);// 使用 Ikonli FontIcon 替代 Circle
        updateicon.setIconSize(20); // 设置图标大小
        updateicon.setIconColor(Color.web("#009b9f")); // 设置图标颜色
        Label updatelabel = new Label("修改密码");
        updatelabel.getStyleClass().add("label");
        Tooltip updatetip = new Tooltip("修改密码");
        Tooltip.install(updatelabel, updatetip);
        updateBox.getChildren().addAll(updateicon, updatelabel);
        leftBox.getChildren().add(updateBox);
        updateBox.setOnMouseClicked(e -> handleupdatepwd(user.getUsername()));

        // 添加登出按钮
        VBox logoutBox = new VBox();
        logoutBox.getStyleClass().add("item-box");
        FontIcon logoutIcon = new FontIcon(FontAwesome.SIGN_OUT);// 使用 Ikonli FontIcon 替代 Circle
        logoutIcon.setIconSize(20); // 设置图标大小
        logoutIcon.setIconColor(Color.web("#009b9f")); // 设置图标颜色
        Label logoutLabel = new Label("登出");
        logoutLabel.getStyleClass().add("label");
        Tooltip logoutTip = new Tooltip("登出");
        Tooltip.install(logoutLabel, logoutTip);
        logoutBox.getChildren().addAll(logoutIcon, logoutLabel);
        leftBox.getChildren().add(logoutBox);
        logoutBox.setOnMouseClicked(e -> handleLogout(primaryStage));


        // 添加标签
        Label welcomeLabel = new Label("用户名: " + user.getUsername() + "\t身份: " + user.getRole() + "\t年龄: " + user.getAge());

        // 将左侧按钮栏添加到 BorderPane 的左侧（设置分割线和与右侧间隔）
        Separator verticalSeparator = new Separator();//设置竖向分割线
        verticalSeparator.setOrientation(javafx.geometry.Orientation.VERTICAL);
        verticalSeparator.setPrefHeight(200); // 设置分隔线的高度
        Region spacer = new Region();//设置间隔
        spacer.setMinWidth(2);  // 设置宽度为2
        leftBox.getChildren().add(verticalSeparator);
        HBox leftBoxPro = new HBox(leftBox, verticalSeparator, spacer);
        borderPane.setLeft(leftBoxPro);

        // 将搜索栏添加到 BorderPane 的顶部（并添加分割线）
        Separator separatorUnderMain = new Separator();
        separatorUnderMain.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        VBox welcomeLabelPro = new VBox(separatorUnderMain, welcomeLabel);
        borderPane.setBottom(welcomeLabelPro);

        // 添加左侧导航栏按钮的分割线
        Separator separatorUnderButton = new Separator();
        separatorUnderButton.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        leftBox.getChildren().add(separatorUnderButton);

        // 创建中心区域，用于显示其他内容
        VBox centerBox = new VBox();
        centerBox.setSpacing(10);
        borderPane.setCenter(centerBox);

        // 设置场景
        Scene scene = new Scene(borderPane, 1000, 618); // 调整尺寸以适应新布局
        primaryStage.setMinWidth(1050); // 最小宽度为800像素
        primaryStage.setMinHeight(650); // 最小高度为600像素

        // 加载CSS样式表
        scene.getStylesheets().add("current-button.css");
        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleCourse(String username) {
        Platform.runLater(() -> {
            CourseSelectionUI CourseSelectionUI = new CourseSelectionUI(user);
            BorderPane CourseSelection=CourseSelectionUI.createCover();
            borderPane.setCenter(CourseSelection);
        });
    }

    private void handleShop(String username) throws IOException {
        ShopUI_stu shopUI = new ShopUI_stu(user, borderPane);
        BorderPane shopLayout = shopUI.createCover();
        borderPane.setCenter(shopLayout);
    }

    private void handleupdatepwd(String username) {
        UpdatePwdUI updatePwdUI = new UpdatePwdUI(user);
        updatePwdUI.showUpdatePwdWindow();  // 显示新窗口
    }
    private void handleBank(String username) {//解决学生的bankui
        Platform.runLater(() -> {
            Bankui_stu bankUI = new Bankui_stu(user);
            BorderPane bankstu=Bankui_stu.createBankUI();
            borderPane.setCenter(bankstu);
        });

    }

    private void handleLibrary(String username) {
        Platform.runLater(() -> {
            LibraryUI libraryUI = new LibraryUI(user);
           BorderPane library=libraryUI.createCover();
           borderPane.setCenter(library);
        });
    }

    private void handleStudent(String username) {
        Platform.runLater(() -> {
                    StuUI stuUI = new StuUI(user);
            if(user.getRole()== Role.StuInfoManager){//管理员
                VBox studentInfoView = stuUI.createStudentInfoView_StuInfoManager();
                borderPane.setCenter(studentInfoView); // 假设 borderPane 是 MainUI 的一部分
            }
            else{//学生
                BorderPane stu_studentInfoView= stuUI.createStudentInfoView_Student();
                borderPane.setCenter(stu_studentInfoView); // 假设 borderPane 是 MainUI 的一部分
            }
        });
    }
    // 处理登出操作，关闭当前窗口并返回登录界面
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

    public void display() {
        launch();
    }

}
