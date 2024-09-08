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

public class Bankui_Manager extends Application {

    BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("银行管理界面");
        root=new BorderPane();

        root.setPadding(new Insets(10));
        Bank bank = new Bank(); // 创建Bank类的实例

        // 业务部分
        HBox searchBox = new HBox(5);
        Label searchLabel = new Label("搜索账号:");
        TextField searchField = new TextField();
        Button searchButton = new Button("搜索");
        searchButton.setOnAction(e -> showPasswordDialog(searchField.getText(), primaryStage, root));
        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);

        HBox accountInfoBox = new HBox(5);
        accountInfoBox.setVisible(false); // 初始时不显示
        Label userLabel = new Label();
        Label balanceLabel = new Label();
        accountInfoBox.getChildren().addAll(userLabel, balanceLabel);

        VBox depositBox = new VBox(5);
        Label depositLabel = new Label("存款金额:");
        TextField depositField = new TextField();
        Button depositButton = new Button("存款");
        depositBox.getChildren().addAll(depositLabel, depositField, depositButton);
        depositButton.setOnAction(e -> {
            bank.deposit(searchField.getText(), Float.parseFloat(depositField.getText()));
        });

        VBox withdrawBox = new VBox(5);
        Label withdrawLabel = new Label("取款金额:");
        TextField withdrawField = new TextField();
        Button withdrawButton = new Button("取款");
        withdrawBox.getChildren().addAll(withdrawLabel, withdrawField, withdrawButton);
        withdrawButton.setOnAction(e -> {
            bank.withdraw(searchField.getText(), Float.parseFloat(withdrawField.getText()));
        });

        // 管理部分
        VBox managementBox = new VBox(10);
        managementBox.setPadding(new Insets(10));
        Label currentRateLabel = new Label("当前利率: 5%");
        Label hint=new Label("请输入更改后利率");
        TextField newRateField = new TextField();
        Button confirmRateButton = new Button("确认");
        managementBox.getChildren().addAll(currentRateLabel,hint, newRateField, confirmRateButton);
        confirmRateButton.setOnAction(e -> {
            try {
                double newRate = Double.parseDouble(newRateField.getText());
                // 更新利率逻辑
                currentRateLabel.setText("当前利率: " + newRate + "%");
                // 这里可以添加更新利率到银行系统的逻辑
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "请输入有效的利率");
                alert.show();
            }
        });
        root.setCenter(new VBox(searchBox,accountInfoBox,depositBox,withdrawBox));
        // 将业务和管理部分添加到TabPane
        HBox hBox = new HBox();
        Button businessbutton = new Button("业务");
        businessbutton.setOnAction(e->{
            root.setCenter(new VBox(searchBox,accountInfoBox,depositBox,withdrawBox));
        });
        Button managementbutton=new Button("管理");
        managementbutton.setOnAction(e->{
            root.setCenter(managementBox);
        });
        hBox.getChildren().addAll(businessbutton,managementbutton);
        root.setTop(hBox);

        Scene scene = new Scene(root, 1000, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void showPasswordDialog(String username, Stage primaryStage, BorderPane root) {
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
                HBox accountInfoBox = new HBox();
                accountInfoBox.getChildren().clear(); // 清除旧的标签
                accountInfoBox.getChildren().addAll(userLabel, balanceLabel);
                root.setBottom(accountInfoBox);
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
