package client.ui;

import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

import static client.service.Role.Librarian;
import static client.service.Role.StuInfoManager;

public class MainUI extends Application {
    private User user;

    public MainUI(User user) {
        this.user = user;
    }

    static BorderPane borderPane;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("主界面");

        // 创建 BorderPane 作为主布局
        borderPane = new BorderPane();

        // 创建左侧的按钮栏
        VBox leftBox = new VBox(0); // 设置间距
        leftBox.setPadding(new Insets(10)); // 设置内边距

        // 添加功能按钮
        Button libButton = new Button("图书馆");
        libButton.setPrefSize(150, 40);
        libButton.setStyle("-fx-background-color: #346fb6; -fx-text-fill: white; -fx-font-size: 16px;");
        libButton.setOnAction(e -> handleLibrary(user.getUsername()));
        leftBox.getChildren().add(libButton);

        Button courseButton = new Button("选课系统");
        courseButton.setPrefSize(150, 40);
        courseButton.setStyle("-fx-background-color: #b61c94; -fx-text-fill: white; -fx-font-size: 16px;");
        leftBox.getChildren().add(courseButton);

        Button stuButton = new Button("学籍管理");
        stuButton.setPrefSize(150, 40);
        stuButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
        stuButton.setOnAction(e -> handleStudent(user.getUsername()));
        leftBox.getChildren().add(stuButton);

        Button shopButton = new Button("商店");
        shopButton.setPrefSize(150, 40);
        shopButton.setStyle("-fx-background-color: #b66921; -fx-text-fill: white; -fx-font-size: 16px;");
        shopButton.setOnAction(e -> {
            try {
                handleShop(user.getUsername());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        leftBox.getChildren().add(shopButton);

        Button bankButton = new Button("银行");
        bankButton.setPrefSize(150, 40);
        bankButton.setStyle("-fx-background-color: #ca1036; -fx-text-fill: white; -fx-font-size: 16px;");
        bankButton.setOnAction(e -> handleBank(user.getUsername()));
        leftBox.getChildren().add(bankButton);

        Button updateButton = new Button("修改密码");
        updateButton.setPrefSize(150, 40);
        updateButton.setStyle("-fx-background-color: #18bcaf; -fx-text-fill: white; -fx-font-size: 16px;");
        updateButton.setOnAction(e -> handleupdatepwd(user.getUsername()));
        leftBox.getChildren().add(updateButton);

        if(user.getRole()==Librarian)
        {
            courseButton.setVisible(false);
            stuButton.setVisible(false);
            shopButton.setVisible(false);
            bankButton.setVisible(false);
            updateButton.setVisible(false);
        }
        if(user.getRole()==StuInfoManager)
        {
            courseButton.setVisible(false);
            stuButton.setVisible(false);
            shopButton.setVisible(false);
            bankButton.setVisible(false);
            updateButton.setVisible(false);
        }

        // 添加标签
        Label welcomeLabel = new Label("用户名: " + user.getUsername() + "\t身份: " + user.getRole() + "\t年龄: " + user.getAge());


        // 将左侧按钮栏添加到 BorderPane 的左侧
        borderPane.setLeft(leftBox);

        // 将搜索栏添加到 BorderPane 的顶部
        borderPane.setBottom(welcomeLabel);
        // 添加分割线
        Separator separator = new Separator();
        separator.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        leftBox.getChildren().add(separator);

        // 创建中心区域，用于显示其他内容
        VBox centerBox = new VBox();
        centerBox.setSpacing(10);
        borderPane.setCenter(centerBox);

        // 设置场景
        Scene scene = new Scene(borderPane, 1000, 500); // 调整尺寸以适应新布局
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void handleShop(String username) throws IOException {
        ShopUI_stu shopUI = new ShopUI_stu(user);
        VBox shopLayout = shopUI.getShopLayout();
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
           BorderPane library=libraryUI.createLibraryView();
           borderPane.setCenter(library);
        });
    }

    private void handleStudent(String username) {
        Platform.runLater(() -> {
                    StuUI stuUI = new StuUI(user);
            VBox studentInfoView = stuUI.createStudentInfoView();
            borderPane.setCenter(studentInfoView); // 假设 borderPane 是 MainUI 的一部分
        });
    }

    public void display() {
        launch();
    }

}
