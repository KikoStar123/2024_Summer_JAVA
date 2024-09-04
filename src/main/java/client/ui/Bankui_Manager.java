package client.ui;

import client.service.BankUser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import client.service.Bank;

public class Bankui_Manager extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建根布局，使用垂直布局
        VBox root = new VBox(10);
        root.setSpacing(10);

        Bank bank = new Bank(); // 创建Bank类的实例

        HBox searchBox = new HBox(5);
        Label searchLabel = new Label("搜索账号:");
        TextField searchField = new TextField();
        Button searchButton = new Button("搜索");
        searchButton.setOnAction(e -> {
            String username = searchField.getText();//符合逻辑
            BankUser user = bank.getBankUser(username); // 假设这里不需要密码，或者你有其他方式获取密码
            if (user != null) {
                // 更新UI显示用户信息
                Label userLabel = new Label("用户名: " + user.getUsername());
                Label balanceLabel = new Label("余额: " + user.getBalance());
                root.getChildren().addAll(userLabel, balanceLabel);
            } else {
                // 处理用户未找到的情况
                Label errorLabel = new Label("用户未找到或登录失败");
                root.getChildren().add(errorLabel);
            }
        });
        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);
        HBox accountInfoBox = new HBox(5);
        accountInfoBox.setVisible(false); // 初始时不显示
        Label userLabel = new Label();
        Label balanceLabel = new Label();
        accountInfoBox.getChildren().addAll(userLabel, balanceLabel);

        VBox depositBox = new VBox(5);
        depositBox.setVisible(false); // 初始时不显示
        Label depositLabel = new Label("存款金额:");
        TextField depositField = new TextField();
        Button depositButton = new Button("存款");
       // depositButton.setOnAction(e -> bank.deposit(depositField.getText()));

        VBox withdrawBox = new VBox(5);
        withdrawBox.setVisible(false); // 初始时不显示
        Label withdrawLabel = new Label("取款金额:");
        TextField withdrawField = new TextField();
        Button withdrawButton = new Button("取款");
       // withdrawButton.setOnAction(e -> bank.withdraw(withdrawField.getText()));

        root.getChildren().addAll(searchBox, accountInfoBox, depositBox, withdrawBox);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("银行管理系统");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // 启动 JavaFX 应用程序
    }
}
