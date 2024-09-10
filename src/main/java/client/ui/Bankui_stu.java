package client.ui;

import client.service.BankUser;
import client.service.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import client.service.Bank;
import javafx.stage.Stage;

public class Bankui_stu {
    private static BorderPane bankBox;
    private static HBox buttonsBox;
    private static VBox loginBox;
    private static VBox registerBox;
    private static User user;

    public Bankui_stu(User user) {
        this.user = user;
    }

    static BorderPane createBankUI() {
        bankBox = new BorderPane();
        bankBox.setPadding(new Insets(10));

        // 创建按钮栏
        buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(0, 0, 10, 0));

        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        //loginButton.setStyle("-fx-min-width: 100px; -fx-min-height: 50px; -fx-padding: 10px; -fx-font-size: 16px;-fx-font-size: 22px;-fx-font-weight: bold;");// 覆盖CSS样式，使按钮变大
        loginButton.setOnAction(e -> showLoginForm());
        buttonsBox.getChildren().add(loginButton);

        Button registerButton = new Button("注册");
        registerButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        //registerButton.setStyle("-fx-min-width: 100px; -fx-min-height: 50px; -fx-padding: 10px; -fx-font-size: 16px;-fx-font-size: 22px;-fx-font-weight: bold;");// 覆盖CSS样式，使按钮变大
        registerButton.setOnAction(e -> showRegisterForm());
        buttonsBox.getChildren().add(registerButton);

        // 将按钮栏放置在顶部
        bankBox.setTop(buttonsBox);
        // 设置 buttonsBox 在 bankBox 的顶部居中对齐
       // bankBox.setAlignment(Pos.CENTER);

        // 创建登录表单
        loginBox = createLoginForm(user);
        // 将登录表单放置在中心
        bankBox.setCenter(loginBox);

        // 创建注册表单
        registerBox = createRegisterForm();
        // 初始时隐藏注册表单
        registerBox.setVisible(false);

        // 创建确认和返回按钮
        HBox actionBox = new HBox(10);

        Button backButton = new Button("返回");
        backButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        //backButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> handleBack());
        actionBox.getChildren().add(backButton);

        // 将操作按钮栏放置在底部
        bankBox.setBottom(actionBox);

        return bankBox;
    }

    private static VBox createLoginForm(User user) {
        VBox loginForm = new VBox(10);
        loginForm.setPadding(new Insets(10));
        Label welcomeLabel = new Label("登录银行账号");
        welcomeLabel.getStyleClass().add("body-font");
        Label accountLabel = new Label("账号: " + user.getUsername()); // 显示用户名
        accountLabel.getStyleClass().add("body-font");
        loginForm.getChildren().addAll(welcomeLabel, accountLabel);

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

        Label passwordLabel = new Label("密码:");
        passwordLabel.getStyleClass().add("body-font");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        registerForm.getChildren().addAll(welcomeLabel,passwordLabel, passwordField);
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
        bankBox.setCenter(loginBox);
    }

    private static void showRegisterForm() {
        loginBox.setVisible(false);
        registerBox.setVisible(true);
        bankBox.setCenter(registerBox);
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
            bankBox.setCenter(showBankMainView(Bank.getBankUser(username, password)));
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
        Button pwdchange=new Button("修改密码");
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

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label oldPwdLabel = new Label("旧密码:");
        GridPane.setConstraints(oldPwdLabel, 0, 0);

        PasswordField oldPwdField = new PasswordField();
        GridPane.setConstraints(oldPwdField, 1, 0);

        Label newPwdLabel = new Label("新密码:");
        GridPane.setConstraints(newPwdLabel, 0, 1);

        PasswordField newPwdField = new PasswordField();
        GridPane.setConstraints(newPwdField, 1, 1);

        Button confirmButton = new Button("确定");
        GridPane.setConstraints(confirmButton, 1, 2);

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
            }
        });

        grid.getChildren().addAll(oldPwdLabel, oldPwdField, newPwdLabel, newPwdField, confirmButton);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }


    private static void showBankRecord(BankUser user) {

        Stage recordStage = new Stage();
        recordStage.setTitle("收支明细");

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


    private static void handleBack() {
        // 处理返回逻辑
        buttonsBox.setVisible(true);
        loginBox.setVisible(false);
        registerBox.setVisible(false);
        bankBox.setCenter(null);
    }
}

