package client.ui;

import client.service.BankUser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import client.service.Bank;
import javafx.scene.control.Alert;
public class Bankui_Manager extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Bank bank = new Bank(); // 创建Bank类的实例

        HBox searchBox = new HBox(5);
        Label searchLabel = new Label("搜索账号:");
        TextField searchField = new TextField();
        Button searchButton = new Button("搜索");
        searchButton.setOnAction(e -> showPasswordDialog(searchField.getText(), primaryStage));
        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);

        HBox accountInfoBox = new HBox(5);
        accountInfoBox.setVisible(false); // 初始时不显示
        Label userLabel = new Label();
        Label balanceLabel = new Label();
        accountInfoBox.getChildren().addAll(userLabel, balanceLabel);

        VBox depositBox = new VBox(5);
        //depositBox.setVisible(false); // 初始时不显示
        Label depositLabel = new Label("存款金额:");
        TextField depositField = new TextField();
        Button depositButton = new Button("存款");
        depositBox.getChildren().addAll(depositLabel, depositField, depositButton);
        depositButton.setOnAction(e -> {
             bank.deposit(searchField.getText(), Float.parseFloat(depositField.getText()));
        });

        VBox withdrawBox = new VBox(5);
        //withdrawBox.setVisible(false); // 初始时不显示
        Label withdrawLabel = new Label("取款金额:");
        TextField withdrawField = new TextField();
        Button withdrawButton = new Button("取款");
        withdrawBox.getChildren().addAll(withdrawLabel, withdrawField, withdrawButton);
        withdrawButton.setOnAction(e -> {
             bank.withdraw(searchField.getText(), Float.parseFloat(withdrawField.getText()));
        });

        root.getChildren().addAll(searchBox, accountInfoBox, depositBox, withdrawBox);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("银行管理系统");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showPasswordDialog(String username, Stage primaryStage) {
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("输入密码");
        passwordDialog.setHeaderText(null);
        passwordDialog.setContentText("密码:");

        passwordDialog.showAndWait().ifPresent(password -> {
            Bank bank = new Bank();
            BankUser user = bank.getBankUser(username, password); // 调用 getBankUser 并传入密码
            if (user != null) {
                // 更新UI显示用户信息
                Label userLabel = new Label("用户名: " + user.getUsername());
                Label balanceLabel = new Label("余额: " + user.getBalance());
                ((VBox) primaryStage.getScene().getRoot()).getChildren().addAll(userLabel, balanceLabel);
                HBox accountInfoBox = (HBox) ((VBox) primaryStage.getScene().getRoot()).getChildren().get(1);
                accountInfoBox.setVisible(true);
                accountInfoBox.getChildren().set(0, userLabel);
                accountInfoBox.getChildren().set(1, balanceLabel);
            } else {
                // 处理用户未找到的情况
                Alert alert = new Alert(Alert.AlertType.ERROR, "用户未找到或登录失败");
                alert.show();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
