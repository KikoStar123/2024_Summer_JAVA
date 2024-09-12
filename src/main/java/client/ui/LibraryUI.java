package client.ui;

import client.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.concurrent.Worker;


import java.util.List;

import javafx.scene.paint.Color;

public class LibraryUI {

    private TableView<Book> resultTable;
    private User user;
    private Library library; // 添加 Library 类的实例

    public LibraryUI(User user) {
        this.user = user;
        this.library = new Library(); // 初始化 Library 实例
    }


    public static class JavaMethod {
        public void handleClick() {
            System.out.println("Button clicked from JavaFX!");
        }
    }
    private JavaMethod javaMethod = new JavaMethod();


    public BorderPane createCover(){
        ImageView photo = new ImageView(new Image(getClass().getResource("/cover-library.jpg").toExternalForm()));
        photo.setFitWidth(440); // 你可以根据窗口大小调整这个值
        photo.setFitHeight(550); // 你可以根据窗口大小调整这个值
        //photo.setPreserveRatio(true);//保持图片的宽高比例不变

        Button loginButton=new Button("进 入 图 书 馆");
        loginButton.getStyleClass().add("cover-button"); // 应用CSS中的按钮样式
        loginButton.setOnAction(e->{
            MainUI.borderPane.setCenter(createLibraryView());
        });

        StackPane stackPaneLeft = new StackPane(photo);
        stackPaneLeft.setPrefSize(440, 550); // 设置小框框的大小

        StackPane stackPaneRight = new StackPane(loginButton);
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

        // 设置背景颜色为浅灰色
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

        BorderPane Pane = new BorderPane();
        Pane.setCenter(gridPane); // 将GridPane放置在BorderPane的中心

        return Pane;
    }
    public BorderPane createLibraryView() {

//        WebView webView = new WebView();//test
//        WebEngine webEngine = webView.getEngine();//test
//
//        // Load your HTML file
//        String htmlFilePath = getClass().getResource("/dynamic-button.html").toExternalForm();//test
//        webEngine.load(htmlFilePath);//test
//
//        // 监听网页加载状态 //test
//        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
//            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
//                // Bind the Java object to JavaScript
//                JSObject window = (JSObject) webEngine.executeScript("window");
//                window.setMember("javaMethod", javaMethod);
//
//                // Execute JavaScript to manipulate the button
//
//                webEngine.executeScript(
//                        "var button = document.getElementById('dynamicButton');" +
//                                "if (button) {" +
//                                "   button.setAttribute('data-text', '查询');" + //添加文字
//                                "   var buttonText = button.getAttribute('data-text');" +
//                                "   button.innerHTML = '<span>' + buttonText + '</span>';" +
//                                "   button.addEventListener('click', function() {" +
//                                "       window.javaMethod.handleClick();" + //添加响应
//                                "   });" +
//                                "} else {" +
//                                "   console.error('ID 为 dynamicButton 的元素未找到');" +
//                                "}"
//                );
//            }
//        });

//        WebView webView = new WebView();
//        //webView.setPrefSize(450, 300);// 设置 WebView 的大小
//        String htmlFilePath=getClass().getResource("/dynamic-button.html").toExternalForm();// HTML 文件路径
//        webView.getEngine().load(htmlFilePath);// 加载内容

        // 搜索栏和按钮
        TextField nameField = new TextField();
        nameField.setPromptText("输入书名或作者");
        nameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        Button searchButton = new Button("查询");
        searchButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

        Button borrowButton = new Button("借阅记录");
        borrowButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

//        //borrowButton.setMaxWidth(Region.USE_PREF_SIZE);
//        webView.setMaxWidth(borrowButton.getMaxWidth());
//        webView.setMinWidth(borrowButton.getMinWidth());
//        webView.setStyle("-fx-border-radius: 5; -fx-background-radius: 5;");

        Label searchLabel = new Label("书名或作者:");
        searchLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式

        HBox searchBox = new HBox(10, searchLabel, nameField, searchButton, borrowButton);
        //HBox searchBox = new HBox(10, new Label("书名或作者:"), nameField, webView, borrowButton);

        searchBox.setAlignment(Pos.CENTER);//居中
        searchBox.setStyle("-fx-spacing: 40;");//设置了子节点之间的间距

        // 结果显示区域
        resultTable = new TableView<>();
        TableColumn<Book, String> titleColumn = new TableColumn<>("书名");
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        TableColumn<Book, String> authorColumn = new TableColumn<>("作者");
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
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
        resultTable.getColumns().addAll(imageColumn, titleColumn, authorColumn, libnumColumn, curnumColumn);

        ScrollPane scrollPane=new ScrollPane(resultTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // 使 ListView 高度适应 ScrollPane

        Region spacer = new Region();
        spacer.setMinHeight(2); // 设置你想要的距离
        VBox.setVgrow(scrollPane, Priority.ALWAYS);


        VBox resultBox = new VBox(spacer, scrollPane);

        // 布局
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(searchBox);
        mainLayout.setCenter(resultBox);

        // 查询按钮事件
        searchButton.setOnAction(e -> {
            String bookName = nameField.getText();
            List<Book> books = library.searchBooksByName(bookName); // 调用 Library 类中的方法
            updateTableData(books);
        });

        //借阅记录按钮事件
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

        return mainLayout;
    }

    private void showUpdateWindow() {
        Stage updateStage =new Stage();
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
            library.updateBook(isbn,finalLibNumber);
            updateStage.close();
        });
    }

    private void showAddbWindow() {
        Stage addbStage=new Stage();
        addbStage.setTitle("增加书籍");
        Label nameLabel = new Label("书名:");
        TextField nameField = new TextField();
        Label isbnLabel = new Label("ISBN:");
        TextField isbnField = new TextField();
        Label authorLabel = new Label("作者:");
        TextField authorField = new TextField();
        Label publishHouseLabel = new Label("出版社:");
        TextField publishHouseField = new TextField();
        Label publicationYearLabel = new Label("出版日期:");
        TextField publicationYearField = new TextField();
        Label classificationLabel = new Label("分类:");
        TextField classificationField = new TextField();
        Label libNumberLabel = new Label("馆藏数量:");
        TextField libNumberField = new TextField();
        Label curNumberLabel = new Label("现有数量:");
        TextField curNumberField = new TextField();
        Label locationLabel = new Label("地点:");
        TextField locationField = new TextField();

        Button confirmButton = new Button("确认");
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
            library.addBook(new Book(isbn,name,author,publishHouse,publicationYear,classification,curNumber,libNumber,location));
            addbStage.close();
        });
        VBox layout = new VBox(10, nameLabel, nameField, isbnLabel, isbnField, authorLabel,authorField,
                publishHouseLabel,publishHouseField,publicationYearLabel,publicationYearField,classificationLabel,
                classificationField,libNumberLabel,libNumberField,curNumberLabel,curNumberField,locationLabel,locationField,confirmButton);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 300, 650);
        addbStage.setScene(scene);
        addbStage.show();

    }

    private void showBorrowWindow() {
        Stage borrowStage = new Stage();
        borrowStage.setTitle("借阅记录");

        Label userLabel = new Label("用户: " + user.getUsername());
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

        borrowedBooksTable.getColumns().addAll(titleColumn, isbnColumn, borrowdataColumn, returndataColumn,statusColumn);
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
                        }else {
                            System.out.println("续借失败: " + record.getBookID());
                        }
                    });
                }
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        LibRecord record = getTableView().getItems().get(getIndex());
                        boolean isReturned = "已还".equals(record.getRecordStatus());
                        renewButton.setDisable(!record.getRenewable() || isReturned); // 根据是否可续借设置按钮状态
                        setGraphic(renewButton);
                    }
                }
            });
            borrowedBooksTable.getColumns().add(renewColumn);
        }
        //管理员，添加用户名栏
        if (user.getRole() == Role.Librarian) {
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

    void updateTableData(List<Book> books) {
        resultTable.getItems().clear();
        resultTable.getItems().addAll(books);
    }


    void showBookDetails(Book book) {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("书籍详情");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER); // 设置居中对齐
        vbox.setPadding(new Insets(20)); // 添加内边距

        vbox.getChildren().add(new Label("书名: " + book.getName()));
        vbox.getChildren().add(new Label("作者: " + book.getAuthor()));
        vbox.getChildren().add(new Label("出版社: " + book.getPublishHouse()));
        vbox.getChildren().add(new Label("ISBN: "+book.getBookID()));
        vbox.getChildren().add(new Label("出版年份: " + book.getPublicationYear()));
        vbox.getChildren().add(new Label("分类: " + book.getClassification()));
        vbox.getChildren().add(new Label("当前数量: " + book.getCurNumber()));
        vbox.getChildren().add(new Label("馆藏数量: " + book.getLibNumber()));
        vbox.getChildren().add(new Label("位置: " + book.getLocation()));

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

                // 捕获JavaScript的console.log输出
                webEngine.setOnAlert(event -> {
                    System.out.println("JS Alert: " + event.getData());
                });

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

}