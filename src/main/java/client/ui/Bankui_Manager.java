package client.ui;

import client.service.BankUser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import client.service.Bank;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.util.Optional;


import java.time.LocalDate;

public class Bankui_Manager extends Application {

    BorderPane root;
    HBox accountInfoBox;
    Label userLabel;
    Label balanceLabel;
    Label currentBalanceLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("银行管理界面");

        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        primaryStage.getIcons().add(image);

        root = new BorderPane();

        root.setPadding(new Insets(10));
        Bank bank = new Bank(); // 创建Bank类的实例

        // 业务部分
        VBox businessBox = new VBox(10);
        businessBox.setPadding(new Insets(10));

        HBox searchBox = new HBox(5);
        Label searchLabel = new Label("搜索账号:");
        searchLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField searchField = new TextField();
        searchField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Button searchButton = new Button("搜索");
        searchButton.getStyleClass().add("main-button");
        searchButton.setOnAction(e -> showPasswordDialog(searchField.getText(), primaryStage, root));
        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);
        Button logoutButton = new Button("登出");
        logoutButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        logoutButton.setOnAction(e -> handleLogout(primaryStage)); // 添加登出逻辑
        accountInfoBox = new HBox(5);
        accountInfoBox.setVisible(false); // 初始时不显示
        userLabel = new Label();
        balanceLabel = new Label();
        currentBalanceLabel = new Label();


        accountInfoBox.getChildren().addAll(userLabel, balanceLabel, currentBalanceLabel);

        VBox depositBox = new VBox(5);
        Label depositLabel = new Label("存款金额:");
        depositLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField depositField = new TextField();
        depositField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label depositTypeLabel = new Label("存款类型:");
        depositTypeLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        ComboBox<String> depositTypeComboBox = new ComboBox<>();
        depositTypeComboBox.getStyleClass().add("main-button");
        depositTypeComboBox.getItems().addAll("活期", "定期");
        Label depositTermLabel = new Label("存款期限（月）:");
        depositTermLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField depositTermField = new TextField();
        depositTermField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Button depositButton = new Button("存款");
        depositButton.getStyleClass().add("main-button");
        depositBox.getChildren().addAll(depositLabel, depositField, depositTypeLabel, depositTypeComboBox, depositTermLabel, depositTermField, depositButton);

        // 根据存款类型显示或隐藏存款期限输入框
        depositTypeComboBox.setOnAction(e -> {
            String depositType = depositTypeComboBox.getValue();
            if ("活期".equals(depositType)) {
                depositTermLabel.setVisible(false);
                depositTermField.setVisible(false);
            } else {
                depositTermLabel.setVisible(true);
                depositTermField.setVisible(true);
            }
        });

        depositButton.setOnAction(e -> {
            try {
                String depositType = depositTypeComboBox.getValue();
                if (depositType == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "请选择存款类型");
                    alert.show();
                    return;
                }

                if ("定期".equals(depositType)) {
                    int depositTerm = Integer.parseInt(depositTermField.getText());
                    if (depositTerm < 3) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "定期存款期限不能低于3个月");
                        alert.show();
                        return;
                    }
                    bank.deposit(searchField.getText(), Float.parseFloat(depositField.getText()), depositType, depositTerm);
                } else {
                    bank.deposit(searchField.getText(), Float.parseFloat(depositField.getText()), depositType, 0);
                }

                updateAccountInfo(bank, searchField.getText());
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "请输入有效的存款金额和期限");
                alert.show();
            }
        });

        VBox withdrawBox = new VBox(5);
        Label withdrawLabel = new Label("取款金额:");
        withdrawLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField withdrawField = new TextField();
        withdrawField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Button withdrawButton = new Button("取款");
        withdrawButton.getStyleClass().add("main-button");
        withdrawBox.getChildren().addAll(withdrawLabel, withdrawField, withdrawButton);
        withdrawButton.setOnAction(e -> {
            bank.withdraw(searchField.getText(), Float.parseFloat(withdrawField.getText()));
            updateAccountInfo(bank, searchField.getText());
        });

        businessBox.getChildren().addAll(searchBox, accountInfoBox, depositBox, withdrawBox);

        // 管理部分
        VBox managementBox = new VBox(10);
        managementBox.setPadding(new Insets(10));

        // 活期利率部分
        Label currentRateLabel = new Label("当前活期利率: " + Bank.getInterestRate("活期") + "%");
        currentRateLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        Label hintCurrent = new Label("请输入更改后的活期利率:");
        hintCurrent.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField newCurrentRateField = new TextField();
        newCurrentRateField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Button confirmCurrentRateButton = new Button("确认活期利率");
        confirmCurrentRateButton.getStyleClass().add("main-button");
        managementBox.getChildren().addAll(currentRateLabel, hintCurrent, newCurrentRateField, confirmCurrentRateButton);

        // 定期利率部分
        Label fixedDepositRateLabel = new Label("当前定期利率: " + Bank.getInterestRate("定期") + "%");
        fixedDepositRateLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        Label hintFixed = new Label("请输入更改后的定期利率:");
        hintFixed.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField newFixedRateField = new TextField();
        newFixedRateField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Button confirmFixedRateButton = new Button("确认定期利率");
        confirmFixedRateButton.getStyleClass().add("main-button");
        managementBox.getChildren().addAll(fixedDepositRateLabel, hintFixed, newFixedRateField, confirmFixedRateButton);

        // 模拟过月和模拟过年按钮
        Button simulateMonthEndButton = new Button("模拟过月");
        simulateMonthEndButton.getStyleClass().add("main-button");
        Button simulateYearEndButton = new Button("模拟过年");
        simulateYearEndButton.getStyleClass().add("main-button");
        managementBox.getChildren().addAll(simulateMonthEndButton, simulateYearEndButton);

        // 活期利率按钮事件
        confirmCurrentRateButton.setOnAction(e -> {
            try {
                double newRate = Double.parseDouble(newCurrentRateField.getText());
                boolean updateCurrentRateSuccess = bank.updateInterestRate("活期", newRate);
                if (updateCurrentRateSuccess) {
                    currentRateLabel.setText("当前活期利率: " + newRate + "%");
                    newCurrentRateField.clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "活期利率修改成功");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "活期利率修改失败");
                    alert.show();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "请输入有效的利率");
                alert.show();
            }
        });

        // 定期利率按钮事件
        confirmFixedRateButton.setOnAction(e -> {
            try {
                double newRate = Double.parseDouble(newFixedRateField.getText());
                boolean updateFixedRateSuccess = bank.updateInterestRate("定期", newRate);
                if (updateFixedRateSuccess) {
                    fixedDepositRateLabel.setText("当前定期利率: " + newRate + "%");
                    newFixedRateField.clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "定期利率修改成功");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "定期利率修改失败");
                    alert.show();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "请输入有效的利率");
                alert.show();
            }
        });

        // 模拟过月按钮事件
        simulateMonthEndButton.setOnAction(e -> {
            boolean success = bank.simulateMonthEnd();
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "模拟过月成功");
                updateAccountInfo(bank, searchField.getText());
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "模拟过月失败");
                alert.show();
            }
        });

        // 模拟过年按钮事件
        simulateYearEndButton.setOnAction(e -> {
            boolean success = bank.simulateYearEnd();
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "模拟过年成功");
                updateAccountInfo(bank, searchField.getText());
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "模拟过年失败");
                alert.show();
            }
        });

        root.setCenter(businessBox);//默认打开业务页面

        // 将业务和管理部分添加到TabPane
        HBox hBox = new HBox();
        Button businessButton = new Button("业务");
        businessButton.getStyleClass().add("main-button");
        businessButton.setOnAction(e -> {
            root.setCenter(businessBox);
        });
        Button managementButton = new Button("管理");
        managementButton.getStyleClass().add("main-button");
        managementButton.setOnAction(e -> {
            root.setCenter(managementBox);
        });
        Region region=new Region();
        region.setMinWidth(830);//登出按钮做出的更改。
        hBox.getChildren().addAll(businessButton, managementButton,region,logoutButton);
        root.setTop(hBox);

        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setMinWidth(1000); // 最小宽度为800像素
        primaryStage.setMinHeight(618); // 最小高度为600像素

        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
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
    private void showPasswordDialog(String username, Stage primaryStage, BorderPane root) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("输入密码");
        dialog.setHeaderText(null);

        ButtonType loginButtonType = new ButtonType("确认", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("密码");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("密码:"), 0, 0);
        grid.add(passwordField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> passwordField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(password -> {
            Bank bank = new Bank();
            BankUser user = bank.getBankUser(username, password); // 调用 getBankUser 并传入密码
            if (user != null) {
                // 更新UI显示用户信息
                userLabel.setText("用户名: " + user.getUsername());
                userLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
                balanceLabel.setText("总资产: " + user.getBalance());
                balanceLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
                currentBalanceLabel.setText("活期资产: " + user.getCurrentBalance());
                currentBalanceLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
                accountInfoBox.setVisible(true);
            } else {
                // 处理用户未找到的情况
                Alert alert = new Alert(Alert.AlertType.ERROR, "用户未找到或登录失败");
                alert.show();
            }
        });
    }


    private void updateAccountInfo(Bank bank, String username) {
        BankUser user = bank.searchByUsername(username);
        if (user != null) {
            balanceLabel.setText("总余额: " + user.getBalance());
            currentBalanceLabel.setText("活期余额: " + user.getCurrentBalance());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
