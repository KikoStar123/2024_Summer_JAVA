package client.ui;

import client.service.BankUser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import client.service.Bank;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
        root = new BorderPane();

        root.setPadding(new Insets(10));
        Bank bank = new Bank(); // 创建Bank类的实例

// 业务部分
        HBox searchBox = new HBox(5);
        Label searchLabel = new Label("搜索账号:");
        TextField searchField = new TextField();
        Button searchButton = new Button("搜索");
        searchButton.setOnAction(e -> showPasswordDialog(searchField.getText(), primaryStage, root));
        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);

        accountInfoBox = new HBox(5);
        accountInfoBox.setVisible(false); // 初始时不显示
        userLabel = new Label();
        balanceLabel = new Label();
        currentBalanceLabel = new Label();
        accountInfoBox.getChildren().addAll(userLabel, balanceLabel, currentBalanceLabel);

        VBox depositBox = new VBox(5);
        Label depositLabel = new Label("存款金额:");
        TextField depositField = new TextField();
        Label depositTypeLabel = new Label("存款类型:");
        ComboBox<String> depositTypeComboBox = new ComboBox<>();
        depositTypeComboBox.getItems().addAll("活期", "定期");
        Label depositTermLabel = new Label("存款期限（月）:");
        TextField depositTermField = new TextField();
        Button depositButton = new Button("存款");
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
        TextField withdrawField = new TextField();
        Button withdrawButton = new Button("取款");
        withdrawBox.getChildren().addAll(withdrawLabel, withdrawField, withdrawButton);
        withdrawButton.setOnAction(e -> {
            bank.withdraw(searchField.getText(), Float.parseFloat(withdrawField.getText()));
            updateAccountInfo(bank, searchField.getText());
        });

        // 管理部分
        VBox managementBox = new VBox(10);
        managementBox.setPadding(new Insets(10));

        // 活期利率部分
        Label currentRateLabel = new Label("当前活期利率: " + Bank.getInterestRate("活期") + "%");
        Label hintCurrent = new Label("请输入更改后的活期利率");
        TextField newCurrentRateField = new TextField();
        Button confirmCurrentRateButton = new Button("确认活期利率");
        managementBox.getChildren().addAll(currentRateLabel, hintCurrent, newCurrentRateField, confirmCurrentRateButton);

        // 定期利率部分
        Label fixedDepositRateLabel = new Label("当前定期利率: " + Bank.getInterestRate("定期") + "%");
        Label hintFixed = new Label("请输入更改后的定期利率");
        TextField newFixedRateField = new TextField();
        Button confirmFixedRateButton = new Button("确认定期利率");
        managementBox.getChildren().addAll(fixedDepositRateLabel, hintFixed, newFixedRateField, confirmFixedRateButton);

        // 模拟过月和模拟过年按钮
        Button simulateMonthEndButton = new Button("模拟过月");
        Button simulateYearEndButton = new Button("模拟过年");
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

        root.setCenter(new VBox(searchBox, accountInfoBox, depositBox, withdrawBox));
        // 将业务和管理部分添加到TabPane
        HBox hBox = new HBox();
        Button businessButton = new Button("业务");
        businessButton.setOnAction(e -> {
            root.setCenter(new VBox(searchBox, accountInfoBox, depositBox, withdrawBox));
        });
        Button managementButton = new Button("管理");
        managementButton.setOnAction(e -> {
            root.setCenter(managementBox);
        });
        hBox.getChildren().addAll(businessButton, managementButton);
        root.setTop(hBox);

        Scene scene = new Scene(root, 1000, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
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
                balanceLabel.setText("总资产: " + user.getBalance());
                currentBalanceLabel.setText("活期资产: " + user.getCurrentBalance());
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
