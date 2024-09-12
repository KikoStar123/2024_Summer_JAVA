package client.ui;

import client.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import com.dansoftware.pdfdisplayer.PDFDisplayer;
import javafx.concurrent.Worker;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class LibraryUI_Manager extends Application {

    private TableView<Book> resultTable;
    private User user;
    private Library library;

    public LibraryUI_Manager(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("图书馆管理界面");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        primaryStage.getIcons().add(image);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        this.library = new Library(); // 初始化 Library 实例

        Label searchTipLabel = new Label("书名或作者:");
        searchTipLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式

        // 搜索栏和按钮
        TextField nameField = new TextField();
        nameField.setPromptText("输入书名或作者");
        nameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Button searchButton = new Button("查询");
        searchButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

        Button borrowButton = new Button("借阅记录");
        borrowButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

        // 管理员特有的按钮
        Button addButton = new Button("借阅/归还");
        addButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button addbButton = new Button("添加新书");
        addbButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button updateButton = new Button("更新数目");
        updateButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button uploadButton = new Button("上传文件");
        uploadButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button logoutButton = new Button("登出");
        logoutButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        logoutButton.setOnAction(e -> handleLogout(primaryStage)); // 添加登出逻辑
        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchTipLabel, nameField, searchButton, borrowButton);
        searchBox.getChildren().addAll(addButton, addbButton, updateButton, uploadButton);
        //添加空白
        Region region=new Region();
        region.setMinWidth(160);
        searchBox.getChildren().addAll(region,logoutButton);

        Region spacersearchBoxPro = new Region();//设置间隔
        spacersearchBoxPro.setMinHeight(2);  // 设置宽度为2
        VBox searchBoxPro = new VBox(searchBox, spacersearchBoxPro);

        logoutButton.setAlignment(Pos.TOP_RIGHT);//fuck
        addButton.setOnAction(e -> showAddDeleteWindow());
        addbButton.setOnAction(e -> showAddbWindow());
        updateButton.setOnAction(e -> showUpdateWindow());
        uploadButton.setOnAction(e -> showUploadDialog());

        // 结果显示区域
        resultTable = new TableView<>();
        TableColumn<Book, String> titleColumn = new TableColumn<>("书名");
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        TableColumn<Book, String> authorColumn = new TableColumn<>("作者");
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBookID()));
        TableColumn<Book, String> libnumColumn = new TableColumn<>("馆藏数");
        libnumColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLibNumber())));
        TableColumn<Book, String> curnumColumn = new TableColumn<>("剩余数");
        curnumColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCurNumber())));

        // 在表格中显示图片
        TableColumn<Book, String> imageColumn = new TableColumn<>("图片");
        imageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImagePath()));
        imageColumn.setCellFactory(new Callback<TableColumn<Book, String>, TableCell<Book, String>>() {
            @Override
            public TableCell<Book, String> call(TableColumn<Book, String> param) {
                return new TableCell<Book, String>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);
                        if (empty || imagePath == null || imagePath.isEmpty()) {
                            setGraphic(null);
                        } else {
                            // 去掉前缀 "uploads/"
                            String relativePath = imagePath.replace("uploads/", "");
                            Image image = new Image("http://localhost:8082/files/" + relativePath);
                            imageView.setImage(image);
                            imageView.setFitWidth(50); // 设置图片宽度
                            imageView.setFitHeight(50); // 设置图片高度
                            imageView.setPreserveRatio(true); // 保持图片比例
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        resultTable.getColumns().addAll(imageColumn, titleColumn, authorColumn, isbnColumn,libnumColumn, curnumColumn);

        ScrollPane scrollPane=new ScrollPane(resultTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // 使 ListView 高度适应 ScrollPane

// 确保 ListView 填充 ScrollPane
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // 布局
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(searchBoxPro);
        mainLayout.setCenter(scrollPane);

        //mainLayout.setRight(logoutBox); // 将登出按钮添加到右上角
        // 查询按钮事件
        searchButton.setOnAction(e -> {
            String bookName = nameField.getText();
            List<Book> books = library.searchBooksByName(bookName); // 调用 Library 类中的方法
            updateTableData(books);
        });

        // 借阅记录按钮事件
        borrowButton.setOnAction(e -> showBorrowWindow());

        // 表格行点击事件
        resultTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) { // 双击事件
                Book selectedBook = resultTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    showBookDetails(selectedBook);
                }
            }
        });

        root.setTop(searchBoxPro);
        root.setCenter(scrollPane);
        //root.setRight(logoutBox);
        //root.setRight(logoutButton);

        // 设置场景
        Scene scene = new Scene(root, 1000, 618); // 调整尺寸以适应新布局
        primaryStage.setMinWidth(1000); // 最小宽度为800像素
        primaryStage.setMinHeight(618); // 最小高度为600像素

        // 加载CSS样式表
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

    private void showUpdateWindow() {
        Stage updateStage = new Stage();
        updateStage.setTitle("更新书籍");
        Label isbnLabel = new Label("Bookid:");
        TextField isbnField = new TextField();
        Label finalLibNumberLabel = new Label("最终数目:");
        TextField finalLibNumberField = new TextField();
        Button confirmButton = new Button("确认");
        confirmButton.setOnAction(e -> {
            String isbn = isbnField.getText();
            int finalLibNumber = Integer.parseInt(finalLibNumberField.getText());
            // 处理更新书籍的逻辑
            library.updateBook(isbn, finalLibNumber);
            updateStage.close();
        });
        // 创建一个垂直布局
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(isbnLabel, isbnField, finalLibNumberLabel, finalLibNumberField, confirmButton);

        // 设置场景
        Scene scene = new Scene(vbox, 300, 200);
        updateStage.setScene(scene);

        // 显示窗口
        updateStage.show();

    }

    private void showAddbWindow() {
        Stage addbStage = new Stage();
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        addbStage.getIcons().add(image);

        addbStage.setTitle("增加书籍");

        Label nameLabel = new Label("书名:");
        nameLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField nameField = new TextField();
        nameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label isbnLabel = new Label("ISBN:");
        isbnLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField isbnField = new TextField();
        isbnField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label authorLabel = new Label("作者:");
        authorLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField authorField = new TextField();
        authorField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label publishHouseLabel = new Label("出版社:");
        publishHouseLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField publishHouseField = new TextField();
        publishHouseField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label publicationYearLabel = new Label("出版日期:");
        publicationYearLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField publicationYearField = new TextField();
        publicationYearField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label classificationLabel = new Label("分类:");
        classificationLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField classificationField = new TextField();
        classificationField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label libNumberLabel = new Label("馆藏数量:");
        libNumberLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField libNumberField = new TextField();
        libNumberField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label curNumberLabel = new Label("现有数量:");
        curNumberLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField curNumberField = new TextField();
        curNumberField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        Label locationLabel = new Label("地点:");
        locationLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField locationField = new TextField();
        locationField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Button confirmButton = new Button("确认");
        confirmButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        confirmButton.setOnAction(e -> {
            String name = nameField.getText();
            String isbn = isbnField.getText();
            String author = authorField.getText();
            String publishHouse = publishHouseField.getText();
            String publicationYear = publicationYearField.getText();
            String classification = classificationField.getText();
            int libNumber = Integer.parseInt(libNumberField.getText());
            int curNumber = Integer.parseInt(curNumberField.getText());
            String location = locationField.getText();

            // 处理添加书籍的逻辑
            library.addBook(new Book(isbn, name, author, publishHouse, publicationYear, classification, curNumber, libNumber, location));
            addbStage.close();
        });
        VBox layout = new VBox(10, nameLabel, nameField, isbnLabel, isbnField, authorLabel, authorField,
                publishHouseLabel, publishHouseField, publicationYearLabel, publicationYearField, classificationLabel,
                classificationField, libNumberLabel, libNumberField, curNumberLabel, curNumberField, locationLabel, locationField, confirmButton);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 300, 650);
        // 加载CSS样式表
        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        addbStage.setScene(scene);
        addbStage.show();
    }

    private void showBorrowWindow() {
        Stage borrowStage = new Stage();
        borrowStage.setTitle("借阅记录");

        //Label userLabel = new Label("用户: " + user.getUsername());
        TableView<LibRecord> borrowedBooksTable = new TableView<>();

// 设置 titleColumn 的单元格值工厂
        TableColumn<LibRecord, String> titleColumn = new TableColumn<>("书名");
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        TableColumn<LibRecord, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBookID()));
        TableColumn<LibRecord, String> borrowdataColumn = new TableColumn<>("借阅时间");
        borrowdataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBorrowDate()));
        TableColumn<LibRecord, String> returndataColumn = new TableColumn<>("归还时间");
        returndataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReturnDate()));
        TableColumn<LibRecord, String> statusColumn = new TableColumn<>("借阅状态");
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRecordStatus()));

        borrowedBooksTable.getColumns().addAll(titleColumn, isbnColumn, borrowdataColumn, returndataColumn, statusColumn);
        if (user.getRole().equals(Role.student)) {
            // 学生用户，添加续借栏
            TableColumn<LibRecord, Void> renewColumn = new TableColumn<>("续借");
            renewColumn.setCellFactory(col -> new TableCell<LibRecord, Void>() {
                private final Button renewButton = new Button("续借");

                {
                    renewButton.setOnAction(e -> {
                        LibRecord record = getTableView().getItems().get(getIndex());
                        boolean success = library.renewBook(record.getBorrowId());
                        if (success) {
                            System.out.println("续借成功: " + record.getBookID());
                            renewButton.setDisable(true); // 点击后禁用按钮
                        } else {
                            System.out.println("续借失败: " + record.getBookID());
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        LibRecord record = getTableView().getItems().get(getIndex());
                        renewButton.setDisable(!record.getRenewable()); // 根据是否可续借设置按钮状态
                        setGraphic(renewButton);
                    }
                }
            });
            borrowedBooksTable.getColumns().add(renewColumn);
        }
        //管理员，添加用户名栏
        if (user.getUsername().charAt(0) == '0') {
            // 管理员，添加用户名列
            TableColumn<LibRecord, String> usernameColumn = new TableColumn<>("用户名");
            usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
            borrowedBooksTable.getColumns().add(usernameColumn);
        }


        TextField searchField = new TextField();
        searchField.setPromptText("输入用户名搜索");
        Button searchButton = new Button("查询");
        Button refreshButton = new Button("刷新");

        searchButton.setOnAction(e -> {
            String searchText = searchField.getText();
            // 根据用户名搜索借阅记录的逻辑
            borrowedBooksTable.setItems(FXCollections.observableArrayList(library.getLibRecordsByUsername(searchText)));
            borrowedBooksTable.setItems(FXCollections.observableArrayList(library.getAllLibRecords()));
        });

        refreshButton.setOnAction(e -> {
            // 刷新表格数据的逻辑
            borrowedBooksTable.setItems(FXCollections.observableArrayList(library.getAllLibRecords()));
        });
        HBox searchBox = new HBox(10, searchField, searchButton, refreshButton);
        VBox borrowLayout;
        if (user.getUsername().charAt(0) == '0') {
            // 管理员，不显示用户名和统计标签
            borrowLayout = new VBox(10, searchBox, borrowedBooksTable);
            borrowedBooksTable.setItems(FXCollections.observableArrayList(library.getAllLibRecords()));
        } else {
            // 学生，显示用户名和统计标签
            Label usernameLabel = new Label("用户: " + user.getUsername());
            List<LibRecord> borrowedRecordsList = library.getLibRecordsByUsername(user.getUsername());
            borrowedBooksTable.setItems(FXCollections.observableArrayList(borrowedRecordsList));

            // 统计已归还和未归还的书籍数量
            long returnedBooksCount = borrowedRecordsList.stream().filter(LibRecord::getisReturn).count();
            long notReturnedBooksCount = borrowedRecordsList.size() - returnedBooksCount;

            Label returnedBooksLabel = new Label("已归还: " + returnedBooksCount);
            Label notReturnedBooksLabel = new Label("未归还: " + notReturnedBooksCount);

            borrowLayout = new VBox(10, usernameLabel, borrowedBooksTable, returnedBooksLabel, notReturnedBooksLabel);
        }
        borrowLayout.setPadding(new Insets(10));
        Scene borrowScene = new Scene(borrowLayout, 600, 400);
        borrowStage.setScene(borrowScene);
        borrowStage.show();
    }

    private void updateTableData(List<Book> books) {
        resultTable.getItems().clear();
        resultTable.getItems().addAll(books);
    }


    private void showBookDetails(Book book) {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("书籍详情");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png")); // 加载图标
        detailStage.getIcons().add(image);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER); // 设置居中对齐
        vbox.setPadding(new Insets(20)); // 添加内边距

        vbox.getChildren().add(new Label("书名: " + book.getName()) {{
            getStyleClass().add("body-font");
        }});
        vbox.getChildren().add(new Label("作者: " + book.getAuthor()) {{
            getStyleClass().add("body-font");
        }});
        vbox.getChildren().add(new Label("出版社: " + book.getPublishHouse()) {{
            getStyleClass().add("body-font");
        }});
        vbox.getChildren().add(new Label("ISBN: " + book.getBookID()) {{
            getStyleClass().add("body-font");
        }});
        vbox.getChildren().add(new Label("出版年份: " + book.getPublicationYear()) {{
            getStyleClass().add("body-font");
        }});
        vbox.getChildren().add(new Label("分类: " + book.getClassification()) {{
            getStyleClass().add("body-font");
        }});
        vbox.getChildren().add(new Label("当前数量: " + book.getCurNumber()) {{
            getStyleClass().add("body-font");
        }});
        vbox.getChildren().add(new Label("馆藏数量: " + book.getLibNumber()) {{
            getStyleClass().add("body-font");
        }});
        vbox.getChildren().add(new Label("位置: " + book.getLocation()) {{
            getStyleClass().add("body-font");
        }});

        // 添加 ImageView 来显示书籍图片
        ImageView bookImageView = new ImageView();
        String imagePath = book.getImagePath(); // 获取图片路径
        if (imagePath != null && !imagePath.isEmpty()) {
            // 去掉前缀 "uploads/"
            String relativePath = imagePath.replace("uploads/", "");
            System.out.println("http://localhost:8082/files/" + relativePath);
            Image bookImage = new Image("http://localhost:8082/files/" + relativePath);
            bookImageView.setImage(bookImage);
            bookImageView.setFitWidth(200); // 设置图片宽度
            bookImageView.setPreserveRatio(true); // 保持图片比例
        }
        vbox.getChildren().add(bookImageView);

        // 添加按钮来预览 PDF 文件
        String pdfPath = book.getPdfPath(); // 获取PDF路径
        if (pdfPath != null && !pdfPath.isEmpty()) {
            Button previewPdfButton = new Button("预览PDF");
            previewPdfButton.setOnAction(e -> {
                Stage pdfStage = new Stage();
                pdfStage.initModality(Modality.APPLICATION_MODAL);
                pdfStage.setTitle("PDF预览");

                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();

                // 加载HTML文件
                webEngine.load(getClass().getResource("/pdf_viewer.html").toExternalForm());

                // 页面加载完成时调用JavaScript函数
                webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        // 在JavaScript中重写console.log以输出到Java控制台
                        webEngine.executeScript(
                                "console.log = (function(oldLog) {" +
                                        "  return function(message) {" +
                                        "    oldLog(message);" +
                                        "    alert(message);" + // 使用alert输出到Java控制台
                                        "  };" +
                                        "})(console.log);"
                        );

                        // 去掉前缀 "uploads/"
                        String relativePdfPath = pdfPath.replace("uploads/", "");
                        webEngine.executeScript("displayPdf('http://localhost:8082/files/" + relativePdfPath + "');");
                    }
                });

                VBox pdfBox = new VBox(10);
                pdfBox.setPadding(new Insets(10));
                pdfBox.getChildren().add(webView);

                Scene pdfScene = new Scene(pdfBox, 800, 600);
                pdfStage.setScene(pdfScene);
                pdfStage.show();
            });
            vbox.getChildren().add(previewPdfButton);
        }

        Scene scene = new Scene(vbox, 400, 600);
        detailStage.setScene(scene);
        detailStage.show();
    }



    private void showAddDeleteWindow() {
        Stage addDeleteStage = new Stage();
        addDeleteStage.setTitle("借阅/归还书籍");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        addDeleteStage.getIcons().add(image);

        Label nameLabel = new Label("用户名:");
        nameLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField usernameField = new TextField();
        usernameField.setPromptText("输入书籍ID");
        usernameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Label bookIDLabel = new Label("书籍ID:");
        bookIDLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
        TextField bookIdField = new TextField();
        bookIdField.setPromptText("输入书籍ID");
        bookIdField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Button borrowButton = new Button("借阅");
        borrowButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button returnButton = new Button("归还");
        returnButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

        HBox buttonBox = new HBox(10, borrowButton, returnButton);
        VBox layout = new VBox(10, nameLabel, usernameField, bookIDLabel, bookIdField, buttonBox);
        layout.setPadding(new Insets(10));

        borrowButton.setOnAction(e -> {
            String username = usernameField.getText();
            String bookId = bookIdField.getText();
            boolean success = library.bookBorrow(username, bookId);
            Alert alert;
            if (success) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("成功");
                alert.setHeaderText(null);
                alert.setContentText("书籍借阅成功！");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("失败");
                alert.setHeaderText(null);
                alert.setContentText("书籍借阅失败！");
            }
            alert.showAndWait();
        });

        returnButton.setOnAction(e -> {
            String username = usernameField.getText();
            String bookId = bookIdField.getText();
            //可能有问题
            boolean success = library.bookReturn(username, bookId);
            Alert alert;
            if (success) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("成功");
                alert.setHeaderText(null);
                alert.setContentText("书籍归还成功！");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("失败");
                alert.setHeaderText(null);
                alert.setContentText("书籍归还失败！");
            }
            alert.showAndWait();
        });

        Scene scene = new Scene(layout, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        addDeleteStage.setScene(scene);
        addDeleteStage.show();
    }

    private void showUploadDialog() {
        Stage dialog = new Stage();
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        dialog.getIcons().add(image);
        dialog.setTitle("上传文件");

        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));

        Label bookIDLabel = new Label("书籍ID:");
        bookIDLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式

        TextField bookIDField = new TextField();
        bookIDField.setPromptText("输入书籍ID");
        bookIDField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Button uploadImageButton = new Button("上传图片");
        uploadImageButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button uploadPDFButton = new Button("上传PDF");
        uploadPDFButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

        uploadImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
            File selectedFile = fileChooser.showOpenDialog(dialog);
            if (selectedFile != null) {
                String bookID = bookIDField.getText();
                if (!bookID.isEmpty()) {
                    boolean success = library.uploadBookImage(selectedFile, bookID);
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "图片上传成功！");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "图片上传失败，请重试。");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "请输入书籍ID");
                }
            }
        });

        uploadPDFButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File selectedFile = fileChooser.showOpenDialog(dialog);
            if (selectedFile != null) {
                String bookID = bookIDField.getText();
                if (!bookID.isEmpty()) {
                    boolean success = library.uploadBookPDF(selectedFile, bookID);
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "PDF上传成功！");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "PDF上传失败，请重试。");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "请输入书籍ID");
                }
            }
        });

        dialogVBox.getChildren().addAll(bookIDLabel, bookIDField, uploadImageButton, uploadPDFButton);

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialogScene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    // 显示消息对话框
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}