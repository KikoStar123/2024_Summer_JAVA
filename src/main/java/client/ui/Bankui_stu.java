package client.ui;

import client.service.BankUser;
import client.service.StudentInformation;
import client.service.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import client.service.Bank;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Bankui_stu {
    private static BorderPane bankBox;
    private static BorderPane rightBox;
    private static HBox buttonsBox;
    private static VBox loginBox;
    private static VBox registerBox;
    private static User user;

    public Bankui_stu(User user) {
        this.user = user;
    }

    static BorderPane createBankUI() {

        //左侧图片栏
        ImageView photo = new ImageView(new Image(Bankui_stu.class.getResource("/cover-bank.jpg").toExternalForm()));
        photo.setFitWidth(440); // 你可以根据窗口大小调整这个值
        photo.setFitHeight(550); // 你可以根据窗口大小调整这个值

        //右侧交互栏

        // 创建按钮栏，结构为HBox
        buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(5, 0, 10, 10));
        buttonsBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        loginButton.setOnAction(e -> showLoginForm());
        buttonsBox.getChildren().add(loginButton);

        Button registerButton = new Button("注册");
        registerButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        registerButton.setOnAction(e -> showRegisterForm());
        buttonsBox.getChildren().add(registerButton);

        //右侧布局
        rightBox = new BorderPane();
        // 将按钮栏放置在顶部
        rightBox.setTop(buttonsBox);
        // 设置 buttonsBox 在 rightBox 的顶部居中对齐
        //BorderPane.setAlignment(buttonsBox, Pos.CENTER);

        // 创建登录表单，结构为VBox
        loginBox = createLoginForm(user);
        // 将登录表单放置在中心
        rightBox.setCenter(loginBox);
        BorderPane.setAlignment(loginBox, Pos.CENTER);

        // 创建注册表单，结构为VBox
        registerBox = createRegisterForm();
        BorderPane.setAlignment(registerBox, Pos.CENTER);
        // 初始时隐藏注册表单
        registerBox.setVisible(false);

        StackPane stackPaneLeft = new StackPane(photo);
        stackPaneLeft.setPrefSize(440, 550); // 设置小框框的大小

        StackPane stackPaneRight = new StackPane(rightBox);
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

        // 设置右侧背景颜色为浅灰色
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

        bankBox = new BorderPane();
        bankBox.setCenter(gridPane); // 将GridPane放置在BorderPane的中心

        return bankBox;
    }

    private static VBox createLoginForm(User user) {
        VBox loginForm = new VBox(10);
        loginForm.setPadding(new Insets(10));
        Region region=new Region();
        region.setMinHeight(30);
        Label welcomeLabel = new Label("登录银行账号");
        welcomeLabel.getStyleClass().add("body-font");
        Label accountLabel = new Label("账号: " + user.getUsername()); // 显示用户名
        accountLabel.getStyleClass().add("body-font");
        loginForm.getChildren().addAll(region,welcomeLabel, accountLabel);

        Label passwordLabel = new Label("密码:");
        passwordLabel.getStyleClass().add("body-font");
        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        passwordField.getStyleClass().add("input-field");

        Button confirmButton = new Button("确认");
        confirmButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        //confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
        confirmButton.setOnAction(e -> handleConfirm(user, passwordField)); // 传递 user 和 passwordField 到 handleConfirm

        loginForm.getChildren().addAll(passwordLabel, passwordField, confirmButton);
        return loginForm;
    }

    private static VBox createRegisterForm() {
        VBox registerForm = new VBox(10);
        registerForm.setPadding(new Insets(10));
        Label welcomeLabel=new Label("注册银行账号");
        welcomeLabel.getStyleClass().add("body-font");

        Region region=new Region();
        region.setMinHeight(30);

        Label passwordLabel = new Label("密码:");
        passwordLabel.getStyleClass().add("body-font");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        registerForm.getChildren().addAll(region,welcomeLabel,passwordLabel, passwordField);
        passwordField.getStyleClass().add("input-field");


        Label confirmPasswordLabel = new Label("确认密码:");
        confirmPasswordLabel.getStyleClass().add("body-font");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("请再次输入密码");
        registerForm.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);
        confirmPasswordField.getStyleClass().add("input-field");

        Button confirmButtonb = new Button("确认");
        confirmButtonb.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        //confirmButtonb.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
        confirmButtonb.setOnAction(e -> handleConfirmb(user, passwordField, confirmPasswordField));

        registerForm.getChildren().add(confirmButtonb);

        return registerForm;
    }

    private static void handleConfirmb(User user, PasswordField passwordField, PasswordField confirmPasswordField) {
        String username = user.getUsername();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!password.equals(confirmPassword)) {
            // 密码不匹配
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("注册失败");
            alert.setHeaderText(null);
            alert.setContentText("两次输入的密码不一致，请重新输入。");
            alert.showAndWait();
        } else {
            // 密码匹配，继续注册逻辑
            boolean success = Bank.bankRegister(username, password);
            if (success) {
                // 登录成功
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("注册成功");
                alert.setHeaderText(null);
                alert.setContentText("注册成功, " + username);
                alert.showAndWait();
            } else {
                // 登录失败
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("注册失败");
                alert.setHeaderText(null);
                alert.setContentText("注册失败");
                alert.showAndWait();
            }
        }
    }

    private static void showLoginForm() {
        loginBox.setVisible(true);
        registerBox.setVisible(false);
        rightBox.setCenter(loginBox);
    }

    private static void showRegisterForm() {
        loginBox.setVisible(false);
        registerBox.setVisible(true);
        rightBox.setCenter(registerBox);
    }

    private static void handleConfirm(User user, PasswordField passwordField) {
        // 处理登录确认逻辑
        String username = user.getUsername();
        String password = passwordField.getText();
        boolean success = Bank.bankLogin(username, password); // 调用 bankLogin 方法
        if (success) {
            // 登录成功
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("登录成功");
            alert.setHeaderText(null);
            alert.setContentText("欢迎回来, " + username);
            alert.showAndWait();
            rightBox.setCenter(showBankMainView(Bank.getBankUser(username, password)));
        } else {
            // 登录失败
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("登录失败");
            alert.setHeaderText(null);
            alert.setContentText("用户名或密码错误");
            alert.showAndWait();
        }

    }

    private static VBox showBankMainView(BankUser user) {

        VBox bankMainView = new VBox(10);
        bankMainView.setPadding(new Insets(10));
        Label usernameLabel = new Label("我的账户");
        Label welcomeLabel = new Label("欢迎, " + user.getUsername() + "!");
        Label balanceLabel = new Label("总资产: " + user.getBalance());
        Label currentBalanceLabel = new Label("活期资产：" + user.getCurrentBalance());
        Button bankrecord=new Button("收支明细");
        bankrecord.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button pwdchange=new Button("修改密码");
        pwdchange.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Label currentRateLabel=new Label("活期利率: " + Bank.getInterestRate("活期"));
        Label fixedDepositRateLabel = new Label("定期利率：" + Bank.getInterestRate("定期"));
        bankMainView.getChildren().addAll(usernameLabel,welcomeLabel,balanceLabel,currentBalanceLabel, bankrecord,pwdchange, currentRateLabel, fixedDepositRateLabel);
        bankrecord.setOnAction(e -> showBankRecord(user));
        pwdchange.setOnAction(e->showChangepwd(user));
        return bankMainView;
    }

    private static void showChangepwd(BankUser user) {
        Stage stage = new Stage();
        stage.setTitle("修改密码");
        Image image = new Image(Bankui_stu.class.getResourceAsStream("/东南大学校徽.png"));// 加载图标
        stage.getIcons().add(image);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label oldPwdLabel = new Label("旧密码:");
        GridPane.setConstraints(oldPwdLabel, 0, 0);
        oldPwdLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式

        PasswordField oldPwdField = new PasswordField();
        GridPane.setConstraints(oldPwdField, 1, 0);
        oldPwdField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Label newPwdLabel = new Label("新密码:");
        GridPane.setConstraints(newPwdLabel, 0, 1);
        newPwdLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式

        PasswordField newPwdField = new PasswordField();
        GridPane.setConstraints(newPwdField, 1, 1);
        newPwdField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Button confirmButton = new Button("确定");
        GridPane.setConstraints(confirmButton, 1, 2);
        confirmButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

        confirmButton.setOnAction(e -> {
            String oldPwd = oldPwdField.getText();
            String newPwd = newPwdField.getText();
            boolean isSuccess = Bank.updatePwd(user.getUsername(), oldPwd, newPwd);
            if (isSuccess) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("密码修改成功！");
                alert.showAndWait().ifPresent(response -> {
                    stage.close();
                    showLoginForm();
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("原密码输入错误，密码修改失败！");
                alert.showAndWait();
                //alert.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
//                Alert alert1 = new Alert(AlertType.INFORMATION);
//                alert.setTitle("Information Dialog");
//                alert.setHeaderText("Look, an Information Dialog");
//                alert.setContentText("I have a great message for you!");
            }
        });

        grid.getChildren().addAll(oldPwdLabel, oldPwdField, newPwdLabel, newPwdField, confirmButton);

        Scene scene = new Scene(grid, 300, 200);
        scene.getStylesheets().add(Bankui_stu.class.getResource("/main-styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    private static void showBankRecord(BankUser user) {

        Stage recordStage = new Stage();
        recordStage.setTitle("收支明细");
        Image image = new Image(Bankui_stu.class.getResourceAsStream("/东南大学校徽.png"));// 加载图标
        recordStage.getIcons().add(image);

        TableView<BankUser.BankRecord> table = new TableView<>();
        ObservableList<BankUser.BankRecord> data = FXCollections.observableArrayList(Bank.getAllBankRecords(user.getUsername()));

        TableColumn<BankUser.BankRecord, String> usernameCol = new TableColumn<>("用户名");
        usernameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<BankUser.BankRecord, Float> balanceChangeCol = new TableColumn<>("余额变动");
        balanceChangeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getBalanceChange()).asObject());

        TableColumn<BankUser.BankRecord, String> balanceReasonCol = new TableColumn<>("原因");
        balanceReasonCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBalanceReason()));

        TableColumn<BankUser.BankRecord, String> curDateCol = new TableColumn<>("日期");
        curDateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCurDate()));

        table.setItems(data);
        table.getColumns().addAll(usernameCol, balanceChangeCol, balanceReasonCol, curDateCol);

        VBox vbox = new VBox(table);
        Scene scene = new Scene(vbox);
        recordStage.setScene(scene);
        recordStage.show();

    }
}

