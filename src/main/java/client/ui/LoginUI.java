package client.ui;

import client.service.ClientService;
import client.service.Gender;
import client.service.Role;
import client.service.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import static client.service.Role.BankManager;
import static client.service.Role.Librarian;
import java.net.URL;
import static client.service.Role.BankManager;
import static client.service.Role.Librarian;
import static client.service.Role.CourseManager;
import static client.service.Role.StuInfoManager;
import java.util.Date;
import java.util.UUID;
import java.util.Random;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import javax.websocket.Session;



import client.service.ClientService;
public class LoginUI extends Application {
    private TextField usernameField;
    private PasswordField passwordField;
    private BorderPane root;
    private String instanceName = "Default";
    private GridPane grid;
    ClientService clientService= new ClientService();//lzy,please change function in ClientService;
//尝试更改忘记密码功能
    String token = UUID.randomUUID().toString();
    Date expirationDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
    private String smtpHost = "smtp.qq.com"; // SMTP服务器地址
    private String smtpPort = "587"; // SMTP服务器端口
    //private String smtpUsername = "1782427252@qq.com"; // 发件人邮箱用户名
    //private String smtpPassword = "sqwkryakuykxdagc"; // 发件人邮箱的鉴权码
    private String smtpUsername;
    private String smtpPassword;//需要加一个授权码
    //private String smtpPassword;
    public void setInstanceName(String name) {
        instanceName = name;
    }
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("登录");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        primaryStage.getIcons().add(image);

        root = new BorderPane();

        // 创建 Scene，并将根布局添加到其中
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // 创建 WebView 实例
        WebView webView = new WebView();//WebView 是用于展示 Web 内容的 JavaFX 组件
        WebEngine webEngine = webView.getEngine();//WebEngine 用于加载和操作 Web 内容
        String htmlFilePath = getClass().getResource("/Fireflies.html").toExternalForm();// HTML 文件路径
        webEngine.load(htmlFilePath);// 加载内容
        webView.setPrefSize(450, 300);// 设置 WebView 的大小

        // 创建登录表单的标题
        Text title = new Text("用户登录");
        title.getStyleClass().add("title-font");

        // 创建登录表单的标签和输入框
        Label userLabel = new Label("账号:");
        userLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        usernameField = new TextField();
        usernameField.setPromptText("请输入用户名");//这里可以加字体
        usernameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Label passwordLabel = new Label("密码:");
        passwordLabel.getStyleClass().add("body-font");
        passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        passwordField.getStyleClass().add("input-field");

        // 创建登录和注册按钮
        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button registerButton = new Button("注册");
        registerButton.getStyleClass().add("main-button");

        // 创建"忘记密码"标签
        Label forgetLabel = new Label("忘记密码");
        forgetLabel.getStyleClass().add("link-label"); // 使用CSS样式模拟链接效果
        forgetLabel.setOnMouseClicked(e -> handleforget());

        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());

        HBox buttonBox = new HBox(80); // 间距为10
        buttonBox.setAlignment(Pos.CENTER); // 水平居中

        // 将按钮添加到buttonBox
        buttonBox.getChildren().addAll(loginButton, registerButton);

        // 将组件添加到网格中
        grid.add(title, 0, 0, 2, 1); // 标题居中
        GridPane.setValignment(title, VPos.CENTER);
        grid.add(userLabel, 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(passwordLabel, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(buttonBox, 0, 5, 2, 1);
        // 将"忘记密码"标签添加到按钮下方，并居中
        GridPane.setHalignment(forgetLabel, HPos.CENTER); // 设置水平居中
        grid.add(forgetLabel, 0, 6, 2, 1); // 添加到网格中，占据2列，位于第6行

        root.setLeft(webView); // 左侧放置 WebView
        root.setCenter(grid); // 右侧放置登录表单

        // 应用CSS样式
        Scene scene = new Scene(root, 800, 495);
        scene.getRoot().getStyleClass().add("background-animate");
        scene.getStylesheets().add(getClass().getResource("/Loginui-styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText(); // JavaFX的PasswordField没有getPassword方法，使用getText

        ClientService clientService = new ClientService();
        boolean success = clientService.login(username, password);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("登录信息");
            alert.setHeaderText(null);
            alert.setContentText("登录成功!");
            alert.showAndWait();

            // 关闭当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();

            // 启动主界面
            //User user = new User(username, Role.student, 12, Gender.male, "123");
            User user = clientService.login_return(username, password);
            if(user.getRole()==BankManager)
            {
                Platform.runLater(() -> {
                    Bankui_Manager managerui=new Bankui_Manager();
                    try {
                        managerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            else if(user.getRole()==Librarian)
            {
                Platform.runLater(() -> {
                    LibraryUI_Manager librarymanagerui=new LibraryUI_Manager(user);
                    try {
                        librarymanagerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            else if(user.getRole()==Role.ShopAssistant){
                Platform.runLater(() -> {
                    ShopUI_Manager shopmanagerui=new ShopUI_Manager(user.getUsername());
                    try {
                        shopmanagerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (user.getRole() == CourseManager) {
                Platform.runLater(() -> {
                    Admin_CourseUI coursemanagerui = new Admin_CourseUI(user);
                    try {
                        coursemanagerui.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if(user.getRole() == StuInfoManager) {
                Platform.runLater(() -> {
                    StuAdminUI adminUI = new StuAdminUI(user);
                    try {
                        adminUI.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Platform.runLater(() -> {
                    MainUI mainUI = new MainUI(user);
                    try {
                        mainUI.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("登录信息");
            alert.setHeaderText(null);
            alert.setContentText("登录失败，请重试~");
            alert.showAndWait();
        }
    }
    private void handleRegister() {
        RegisterUI registerUI = new RegisterUI();
        GridPane grid = registerUI.showRegisterUI(this);
        root.setCenter(grid);
    }
    private void handleforget() {
        // 创建一个新的弹出窗口 (Stage)
        Stage forgetStage = new Stage();
        forgetStage.setTitle("忘记密码");

        // 创建一个垂直布局 VBox
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        // 创建账号输入行
        HBox accountRow = new HBox(10);
        accountRow.setAlignment(Pos.CENTER_LEFT);
        Label accountLabel = new Label("账号:");
        accountLabel.setMinWidth(Control.USE_PREF_SIZE); // 设置Label的最小宽度
        TextField accountField = new TextField();
        accountField.setPromptText("请输入你的账号");
        accountRow.getChildren().addAll(accountLabel, accountField);

        // 创建邮箱输入行
        HBox emailRow = new HBox(10);
        emailRow.setAlignment(Pos.CENTER_LEFT);
        Label emailLabel = new Label("邮箱:");
        emailLabel.setMinWidth(Control.USE_PREF_SIZE); // 设置Label的最小宽度
        TextField emailField = new TextField();
        emailField.setPromptText("请输入你的邮箱");
        emailRow.getChildren().addAll(emailLabel, emailField);

        // 创建邮箱授权码
        HBox pwdRow = new HBox(10);
        pwdRow.setAlignment(Pos.CENTER_LEFT);
        Label pwdLabel = new Label("授权码:");
        pwdLabel.setMinWidth(Control.USE_PREF_SIZE); // 设置Label的最小宽度
        TextField pwdField = new TextField();
        pwdField.setPromptText("请输入你的授权码");
        pwdRow.getChildren().addAll(pwdLabel, pwdField);

        // 创建验证码输入行
        HBox codeRow = new HBox(10);
        codeRow.setAlignment(Pos.CENTER_LEFT);
        Label codeLabel = new Label("验证码:");
        codeLabel.setMinWidth(Control.USE_PREF_SIZE); // 设置Label的最小宽度
        TextField codeField = new TextField();
        codeField.setPromptText("请输入验证码");
        Button sendCodeButton = new Button("发送验证码");
        var ref = new Object() {
            int returnvalue = 0;
        };
        // 发送验证码按钮事件
        sendCodeButton.setOnAction(e -> {
            String email = emailField.getText();
            if (!email.isEmpty()) {
                ref.returnvalue = Forget(email,pwdField.getText());  // 调用 Forget 函数，发送验证码到邮箱
            } else {
                // 处理无效的邮箱输入 (可以弹出提示框)
                Alert alert = new Alert(Alert.AlertType.ERROR, "请输入有效的邮箱地址！");
                alert.showAndWait();
            }
        });
        codeRow.getChildren().addAll(codeLabel, codeField, sendCodeButton);

        // 创建“开始验证”按钮
        Button verifyButton = new Button("开始验证");
        verifyButton.setOnAction(e -> {//-------------------------------------
            //重要的逻辑放在这里了，匹配邮箱同时匹配发送的验证码
            //System.out.println(emailField.getText());
            //System.out.println(clientService.getEmailByUsername(accountField.getText()));
            if((Integer.parseInt(codeField.getText())== ref.returnvalue)&&
                    (emailField.getText().equals(clientService.getEmailByUsername(accountField.getText())))){
                System.out.println("验证码匹配！请更改密码！" );
                String username = accountField.getText();  // 假设通过账号获取用户信息
                ForgetPwdUI forgetPwdUI = new ForgetPwdUI(username);
                forgetPwdUI.showUpdatePwdWindow(); // 显示新窗口
            }
            else{
                System.out.println("验证码不匹配，请重新发送！！！" );
            }
        });

        // 将账号输入行、邮箱输入行、验证码行和“开始验证”按钮添加到 VBox
        vbox.getChildren().addAll(accountRow, emailRow, pwdRow,codeRow, verifyButton);

        // 设置窗口的场景
        Scene scene = new Scene(vbox, 400, 250);
        forgetStage.setScene(scene);
        forgetStage.show();
    }
    //完成向邮箱发送正确的验证码；
    public int Forget(String toEmail,String toPassword) {
        smtpUsername = toEmail;//确保用户输入的邮箱与发送的邮箱保持一致。
        smtpPassword = toPassword;//-----
        Random random = new Random();
        int verificationCode = 100000 + random.nextInt(900000); // 900000 表
        //System.out.println("Verification Code: " + verificationCode);//------打印验证码
        // 设置邮件服务器的属性
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS

        // 创建Session并配置用户名和密码
//        Session session = Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(smtpUsername, smtpPassword);
//            }
////          @Override
////          protected PasswordAuthentication getPasswordAuthentication() {
////              // 返回null，表示不进行身份验证
////              return null;
////          }
//        });
        //try {
            // 创建邮件消息
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(smtpUsername));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//            message.setSubject("重置密码验证码");
//            message.setText("您的验证码是：" + verificationCode);
//
//            // 发送邮件
//            Transport.send(message);
//
//            // 将验证码缓存起来，以便后续验证
//            // 这里可以使用任何你喜欢的缓存机制，例如Ehcache、Redis等
//            // cache.put(toEmail, verificationCode, 3600); // 假设方法，实际代码需要替换
//
//            System.out.println("验证码已发送到：" + toEmail);
//            //if(verificationCode==codeField)
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            System.out.println("发送验证码失败。");
//            System.out.println("Error message: " + e.getMessage());
//        }
            return verificationCode;
        }


    public void showLoginUI() {
        Stage stage = new Stage();
        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public GridPane getGrid() {
        return grid;
    }
}