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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import com.dansoftware.pdfdisplayer.PDFDisplayer;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LibraryUI {

    private TableView<Book> resultTable;
    private User user;
    private Library library; // 添加 Library 类的实例

    public LibraryUI(User user) {
        this.user = user;
        this.library = new Library(); // 初始化 Library 实例
    }


    public BorderPane createLibraryView() {

        // 搜索栏和按钮
        TextField nameField = new TextField();
        nameField.setPromptText("输入书名或作者");
        nameField.setFont(Font.font("Segoe UI", 14));
        nameField.setStyle("-fx-text-fill: #4B0082;");

        Button searchButton = new Button("查询");
        searchButton.setFont(Font.font("Segoe UI", 14));
        searchButton.setStyle("-fx-text-fill: #4B0082;");

        Button borrowButton = new Button("借阅记录");
        borrowButton.setFont(Font.font("Segoe UI", 14));
        borrowButton.setStyle("-fx-text-fill: #4B0082;");

        HBox searchBox = new HBox(10, new Label("书名或作者:"), nameField, searchButton, borrowButton);
        searchBox.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #4B0082;");
        // 设置按钮样式
        String buttonStyle = "-fx-background-color: #FFFFFF; -fx-text-fill: #4B0082; -fx-border-color: #6A0DAD; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-font-family: 'Segoe UI'; -fx-font-size: 16px; -fx-font-weight: normal; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 10, 0, 4, 4);";
        String buttonHoverStyle = "-fx-background-color: rgba(255, 255, 255, 0.3); -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 15, 0, 0, 0); -fx-opacity: 0.9;";
        String buttonPressedStyle = "-fx-background-color: #D8BFD8; -fx-border-color: #6A0DAD;";
        searchButton.setStyle(buttonStyle);
        borrowButton.setStyle(buttonStyle);
        searchButton.setOnMouseEntered(e -> searchButton.setStyle(buttonHoverStyle));
        searchButton.setOnMouseExited(e -> searchButton.setStyle(buttonStyle));
        searchButton.setOnMousePressed(e -> searchButton.setStyle(buttonPressedStyle));
        searchButton.setOnMouseReleased(e -> searchButton.setStyle(buttonHoverStyle));

        borrowButton.setOnMouseEntered(e -> borrowButton.setStyle(buttonHoverStyle));
        borrowButton.setOnMouseExited(e -> borrowButton.setStyle(buttonStyle));
        borrowButton.setOnMousePressed(e -> borrowButton.setStyle(buttonPressedStyle));
        borrowButton.setOnMouseReleased(e -> borrowButton.setStyle(buttonHoverStyle));

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

        VBox resultBox = new VBox(10, resultTable);

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

                PDFDisplayer pdfDisplayer = new PDFDisplayer();
                // 去掉前缀 "uploads/"
                String relativePdfPath = pdfPath.replace("uploads/", "");
                try {
                    System.out.printf("http://localhost:8082/files/" + relativePdfPath);
                    pdfDisplayer.loadPDF(new URL("http://localhost:8082/files/" + relativePdfPath));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                Scene pdfScene = new Scene(pdfDisplayer.toNode());
                pdfStage.setScene(pdfScene);
                pdfStage.show();
            });
            vbox.getChildren().add(previewPdfButton);
        }

        Scene scene = new Scene(vbox, 600, 800);
        detailStage.setScene(scene);
        detailStage.show();
    }

}