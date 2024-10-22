package client.ui;

import client.service.ShoppingStore;
import javafx.application.Application;
import client.service.ShoppingProduct;
import client.service.ShoppingOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.scene.input.ScrollEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import client.service.ShoppingComment;
public class ShopUI_Manager extends Application {
    private String username; // 成员变量，用于存储用户名
    ShoppingStore shoppingStore = new ShoppingStore(); // 借用商店服务来实现
    ShoppingProduct shoppingProduct = new ShoppingProduct(); // 借用商品服务
    ShoppingComment shoppingComment = new ShoppingComment();
   // ShoppingProduct.oneProduct oneProduct= new ShoppingProduct.oneProduct();
    BorderPane root;
    Tab productsTab;
    Tab ordersTab;
    private double zoomFactor = 1.0;
    // 构造函数，接收用户名
    public ShopUI_Manager(String username) {
        this.username = username;
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("商家");
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png")); // 加载图标
        primaryStage.getIcons().add(image);

        // 创建主BorderPane
        root = new BorderPane();
        root.setPrefSize(400, 300); // 设置宽度为400，高度为300

        // 创建按钮
        Button shopInfoButton = new Button("商店信息");
        shopInfoButton.getStyleClass().add("main-button");
        Button productsButton = new Button("商品");
        productsButton.getStyleClass().add("main-button");
        Button ordersButton = new Button("订单");
        ordersButton.getStyleClass().add("main-button");
        Button logoutButton = new Button("登出");
        logoutButton.getStyleClass().add("main-button");

        // 设置登出按钮的点击事件
        logoutButton.setOnAction(e -> handleLogout(primaryStage)); // 添加登出逻辑

        // 设置商店信息按钮的点击事件
        shopInfoButton.setOnAction(e -> {
            String storeID = null;
            try {
                storeID = shoppingStore.getStoreIDByUsername(username); // 获取商店ID
                ShoppingStore.oneStore onestore = shoppingStore.oneStore(storeID); // 获取商店信息
                root.setCenter(createShopInfoPane(onestore)); // 显示商店信息内容
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // 设置商品按钮的点击事件
        productsButton.setOnAction(e -> root.setCenter(createProductsPane())); // 显示商品内容

        // 设置订单按钮的点击事件
        ordersButton.setOnAction(e -> root.setCenter(createOrdersPane())); // 显示订单内容

        // 创建一个HBox来布局这些按钮
        HBox buttonBox = new HBox(10); // 设置按钮之间的间距为10
        buttonBox.setPadding(new Insets(10)); // 设置HBox的内边距
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBox.getChildren().addAll(shopInfoButton, productsButton, ordersButton, spacer,logoutButton);

        // 将HBox设置为BorderPane的顶部
        root.setTop(buttonBox);

        // 设置默认显示的中心内容
        //root.setCenter(createShopInfoPane(null)); // 初始显示商店信息（根据实际需求调整）

        // 设置场景
        Scene scene = new Scene(root, 1000, 618); // 调整尺寸以适应新布局
        primaryStage.setMinWidth(1000); // 最小宽度为1000像素
        primaryStage.setMinHeight(618); // 最小高度为618像素
        // 加载CSS样式表
        scene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }





    private void handleScrollEvent(ScrollEvent event) {
        if (event.isControlDown()) {
            double delta = event.getDeltaY();
            if (delta > 0) {
                zoomOut();
            } else {
                zoomIn();
            }
            event.consume(); // 阻止事件进一步传播
        }
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
    //用于窗口的缩放
    private void zoomIn() {
        zoomFactor *= 1.15;
        root.getTransforms().clear();
        root.getTransforms().add(new Scale(zoomFactor, zoomFactor, 0, 0));
    }
    private void zoomOut() {
        zoomFactor /= 1.15;
        root.getTransforms().clear();
        root.getTransforms().add(new Scale(zoomFactor, zoomFactor, 0, 0));
    }
    // 创建商店信息界面
    private BorderPane createShopInfoPane(ShoppingStore.oneStore storeInfo) {
        BorderPane shopInfoPane = new BorderPane();
        // 创建一个文本区域来显示商店信息
        TextArea infoArea = new TextArea();
        infoArea.setEditable(false); // 设置为不可编辑
        String status;
        if(storeInfo.getStoreStatus()==true)
            status="营业中";
        else
            status="停业中";

        infoArea.setText("商店ID: " + storeInfo.getStoreID() + "\n" +
                "商店名称: " + storeInfo.getStoreName() + "\n" +
                "联系电话: " + storeInfo.getStorePhone() + "\n" +
                "评分: " + storeInfo.getStoreRate() + "\n" +
                "状态: " + status);

        // 将文本区域设置为界面的中心内容
        shopInfoPane.setCenter(infoArea);
        // 创建修改商家信息的按钮
        Button editButton = new Button("修改商家信息");
        editButton.getStyleClass().add("main-button");
        editButton.setOnAction(e -> showEditShopInfoDialog(storeInfo));

        // 将按钮添加到BorderPane的底部
        shopInfoPane.setBottom(editButton);
        BorderPane.setAlignment(editButton, Pos.CENTER);
        return shopInfoPane;

    }
    // 显示修改商家信息的对话框
    private void showEditShopInfoDialog(ShoppingStore.oneStore storeInfo) {
        // 创建一个弹窗
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("修改商家信息");
        alert.setHeaderText("请填写新的商家信息");

        // 创建表单
        DialogPane dialogPane = alert.getDialogPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        // 创建表单字段
        TextField storeIDField = new TextField(storeInfo.getStoreID());
        TextField storeNameField = new TextField(storeInfo.getStoreName());
        TextField storePhoneField = new TextField(storeInfo.getStorePhone());
        TextField storeRateField = new TextField(String.format("%.2f", storeInfo.getStoreRate()));
        TextField storeStatusField = new TextField(storeInfo.getStoreStatus() ? "营业中" : "休息");

        // 创建标签
        Label idLabel = new Label("商店ID:");
        Label nameLabel = new Label("商店名称:");
        Label phoneLabel = new Label("联系电话:");
        Label rateLabel = new Label("评分:");
        Label statusLabel = new Label("状态:");

        // 将标签和字段添加到网格中
        gridPane.add(idLabel, 0, 0);
        gridPane.add(storeIDField, 1, 0);
        gridPane.add(nameLabel, 0, 1);
        gridPane.add(storeNameField, 1, 1);
        gridPane.add(phoneLabel, 0, 2);
        gridPane.add(storePhoneField, 1, 2);
       // gridPane.add(rateLabel, 0, 3);
       // gridPane.add(storeRateField, 1, 3);
        gridPane.add(statusLabel, 0, 3);
        gridPane.add(storeStatusField, 1, 3);

        // 设置弹窗的内容
        dialogPane.setContent(gridPane);

        // 设置弹窗的确认按钮
        ButtonType okButtonType = new ButtonType("确认");
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButtonType, cancelButtonType);

        // 确认按钮的事件处理器
        alert.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // 在这里处理用户输入的新信息
                // 调用 updateStoreInfo 方法来更新商店信息
                try {
                    boolean success = ShoppingStore.updateStore(
                            storeIDField.getText(),
                            storeNameField.getText(),
                            storePhoneField.getText(),
                            Float.parseFloat(storeRateField.getText()),
                            storeStatusField.getText().equals("营业中")
                    );
                    if (success) {
                        // 更新成功，更新界面上的显示
                        //updateShopInfoDisplay(storeIDField.getText(), storeNameField.getText(), storePhoneField.getText(), Float.parseFloat(storeRateField.getText()), storeStatusField.getText().equals("营业中"));
                    } else {
                        // 更新失败，显示错误信息
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setContentText("更新失败，请重试。");
                        errorAlert.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("更新失败，请确保输入格式正确。");
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        // 显示弹窗
        alert.showAndWait();
    }

    // 创建商品界面//---每一个加入查看评论按钮---
    // 商品界面的根布局// 存放商品和评论的主Pane
    private BorderPane productView;
    // 评论界面的根布局
    private BorderPane commentView;
    // 初始化界面布局
//    public Node initialize() {
//        // 商品界面布局的初始化
//        productView = createProductsPane();
//        commentView = new BorderPane();  // 评论界面将在显示评论时创建
//
//        root.setCenter(productView); // 设置初始界面为商品界面
//        return productView;
//    }
    private BorderPane createProductsPane() {
        System.out.println("Creating products pane...");
        BorderPane productsPane = new BorderPane();
        // 商品界面布局使用VBox
        VBox productView = new VBox(10);
        productView.setPadding(new Insets(10));
        System.out.println("Initializing productView...");
        // 加载商品信息
        loadProducts(productView); // 传入 productView
        // 创建底部添加商品按钮
        Button addButton = new Button("添加商品");
        addButton.getStyleClass().add("main-button");
        addButton.setOnAction(e -> showEditProductInfoDialog()); // 替换为添加商品的逻辑

        // 添加商品图片按钮
        Button uploadButton = new Button("上传图片");
        uploadButton.getStyleClass().add("main-button");

        uploadButton.setOnAction(e -> showUploadDialog());

        // 创建刷新按钮
        Button refreshButton = new Button("刷新");
        refreshButton.getStyleClass().add("main-button");
        refreshButton.setOnAction(e -> {
            productView.getChildren().clear(); // 清空当前商品列表
            loadProducts(productView); // 重新加载商品列表
        });

        // 创建一个HBox来容纳三个按钮
        HBox buttonBox = new HBox(10); // 间距为10
        buttonBox.setAlignment(Pos.CENTER); // 居中对齐
        buttonBox.getChildren().addAll(addButton, uploadButton, refreshButton);

        // 将buttonBox设置为productView的底部
        productsPane.setCenter(productView);
        productsPane.setBottom(buttonBox);
        // 创建 ScrollPane 并设置其包含 VBox（productView）
        ScrollPane scrollPane = new ScrollPane(productView);
        scrollPane.setFitToWidth(true); // 让 ScrollPane 自动调整宽度以适应内容
        productsPane.setCenter(scrollPane);

        System.out.println("Products pane created with productView: " + productView);
        return productsPane;
    }

    private void loadProducts(VBox productVBox) {
        String storeID = null;
        try {
            storeID = shoppingStore.getStoreIDByUsername(username);
            if (storeID != null) {
                ShoppingProduct.oneProduct[] shoppingProductList = shoppingProduct.getAllProductsByStore(storeID);
                for (int i = 0; i < shoppingProductList.length; i++) {
                    ShoppingProduct.oneProduct product = shoppingProductList[i];
                    createProductItem(productVBox, product);
                    // 在每个商品项之后添加分割线，除了最后一个商品
                    if (i < shoppingProductList.length - 1) {
                        Separator separator = new Separator();
                        separator.setStyle("-fx-border-style: solid; -fx-border-width: 1; -fx-border-color: lightgrey;");
                        productVBox.getChildren().add(separator); // 添加分割线用于区分
                    }
                }
            } else {
                System.out.println("无法获取商店ID");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("获取商店信息或商品列表时发生错误");
        }
    }
    private void showUploadDialog() {
        Stage dialog = new Stage();
        Image image = new Image(getClass().getResourceAsStream("/东南大学校徽.png"));// 加载图标
        dialog.getIcons().add(image);
        dialog.setTitle("上传图片");

        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));

        TextField productIDField = new TextField();
        productIDField.setPromptText("输入商品ID");

        Button uploadImageButton = new Button("上传图片");
        uploadImageButton.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        uploadImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
            File selectedFile = fileChooser.showOpenDialog(dialog);
            if (selectedFile != null) {
                String productID = productIDField.getText();
                if (!productID.isEmpty()) {
                    boolean success = ShoppingProduct.uploadProductImage(selectedFile, productID);
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "图片上传成功！");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "图片上传失败，请重试。");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "请输入商品ID");
                }
            }
        });


        dialogVBox.getChildren().addAll(new Label("商品ID:"), productIDField, uploadImageButton);

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialogScene.getStylesheets().add(getClass().getResource("/main-styles.css").toExternalForm());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // 显示修改商家信息的对话框
    private void showEditProductInfoDialog() {
        // 创建一个弹窗
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("编辑商品信息");
        alert.setHeaderText("请填写新的商品信息");

        // 创建表单
        DialogPane dialogPane = alert.getDialogPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        // 创建表单字段
        TextField productIDField = new TextField();
        productIDField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        TextField productNameField = new TextField();
        productNameField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        TextArea productDetailTextArea = new TextArea();
        productDetailTextArea.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        productDetailTextArea.setPrefColumnCount(20); // 例如，设置为20列
        productDetailTextArea.setPrefRowCount(3); // 例如，设置为4行
        //productDetailTextArea.setWrapText(true);
        //productDetailTextArea.setMaxWidth(Double.MAX_VALUE);
        TextField productOriginalPriceField = new TextField();
        productOriginalPriceField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式

        TextField productCurrentPriceField = new TextField();
        productCurrentPriceField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        TextField productInventoryField = new TextField();
        productInventoryField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        TextField productAddressField = new TextField();
        productAddressField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        //TextField productCommentRateField = new TextField();
        CheckBox productStatusCheckBox = new CheckBox();
        productStatusCheckBox.setSelected(true);//bumingbai----

        // 创建标签
        Label idLabel = new Label("商品ID:");
        Label nameLabel = new Label("商品名称:");
        Label detailLabel = new Label("描述:");
        Label originalPriceLabel = new Label("原价:");
        Label currentPriceLabel = new Label("现价:");
        Label inventoryLabel = new Label("库存:");
        Label addressLabel = new Label("地址:");
        //Label commentRateLabel = new Label("好评率(%):");
        Label statusLabel = new Label("状态:");

        // 将标签和字段添加到网格中
        gridPane.add(idLabel, 0, 0);
        gridPane.add(productIDField, 1, 0);
        gridPane.add(nameLabel, 0, 1);
        gridPane.add(productNameField, 1, 1);
        gridPane.add(detailLabel, 0, 2);
        gridPane.add(productDetailTextArea, 1, 2, 2, 1);
        gridPane.add(originalPriceLabel, 0, 3);
        gridPane.add(productOriginalPriceField, 1, 3);
        gridPane.add(currentPriceLabel, 0, 4);
        gridPane.add(productCurrentPriceField, 1, 4);
        gridPane.add(inventoryLabel, 0, 5);
        gridPane.add(productInventoryField, 1, 5);
        gridPane.add(addressLabel, 0, 6);
        gridPane.add(productAddressField, 1, 6);
        //gridPane.add(commentRateLabel, 0, 7);
        //gridPane.add(productCommentRateField, 1, 7);
        gridPane.add(statusLabel, 0, 7);
        gridPane.add(productStatusCheckBox, 1, 7);

        // 设置弹窗的内容
        dialogPane.setContent(gridPane);

        // 设置弹窗的确认按钮
        ButtonType okButtonType = new ButtonType("确认");
        //okButtonType.getStyleClass().add("main-button");
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButtonType, cancelButtonType);

        alert.getDialogPane().lookupButton(cancelButtonType).setStyle(
                "-fx-background-color: rgba(220,245,240, 0.1);" +
                        "-fx-border-color: #718771;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "transition: background-color 3s ease;"
        );

        // 设置鼠标悬停和按下时的样式
        alert.getDialogPane().lookupButton(cancelButtonType).setOnMouseEntered(e -> {
            alert.getDialogPane().lookupButton(cancelButtonType).setStyle(
                    "-fx-background-color: rgba(255, 217, 163, 0.8);" +
                            "-fx-border-color: #718771;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "transition: background-color 3s ease;"
            );
        });

        alert.getDialogPane().lookupButton(cancelButtonType).setOnMouseExited(e -> {
            alert.getDialogPane().lookupButton(cancelButtonType).setStyle(
                    "-fx-background-color: rgba(220,245,240, 0.1);" +
                            "-fx-border-color: #718771;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "transition: background-color 3s ease;"
            );
        });

        alert.getDialogPane().lookupButton(cancelButtonType).setOnMousePressed(e -> {
            alert.getDialogPane().lookupButton(cancelButtonType).setStyle(
                    "-fx-background-color: rgba(0, 155, 159, 0.3);" +
                            "-fx-border-color: #003153;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "transition: background-color 3s ease;"
            );
        });

        alert.getDialogPane().lookupButton(cancelButtonType).setOnMouseReleased(e -> {
            alert.getDialogPane().lookupButton(cancelButtonType).setStyle(
                    "-fx-background-color: rgba(255, 217, 163, 0.8);" +
                            "-fx-border-color: #718771;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "transition: background-color 3s ease;"
            );
        });
        alert.getDialogPane().lookupButton(okButtonType).setStyle(
                "-fx-background-color: rgba(220,245,240, 0.1);" +
                        "-fx-border-color: #718771;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "transition: background-color 3s ease;"
        );

        // 设置鼠标悬停和按下时的样式
        alert.getDialogPane().lookupButton(okButtonType).setOnMouseEntered(e -> {
            alert.getDialogPane().lookupButton(okButtonType).setStyle(
                    "-fx-background-color: rgba(255, 217, 163, 0.8);" +
                            "-fx-border-color: #718771;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "transition: background-color 3s ease;"
            );
        });

        alert.getDialogPane().lookupButton(okButtonType).setOnMouseExited(e -> {
            alert.getDialogPane().lookupButton(okButtonType).setStyle(
                    "-fx-background-color: rgba(220,245,240, 0.1);" +
                            "-fx-border-color: #718771;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "transition: background-color 3s ease;"
            );
        });

        alert.getDialogPane().lookupButton(okButtonType).setOnMousePressed(e -> {
            alert.getDialogPane().lookupButton(okButtonType).setStyle(
                    "-fx-background-color: rgba(0, 155, 159, 0.3);" +
                            "-fx-border-color: #003153;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "transition: background-color 3s ease;"
            );
        });

        alert.getDialogPane().lookupButton(okButtonType).setOnMouseReleased(e -> {
            alert.getDialogPane().lookupButton(okButtonType).setStyle(
                    "-fx-background-color: rgba(255, 217, 163, 0.8);" +
                            "-fx-border-color: #718771;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "transition: background-color 3s ease;"
            );
        });
        // 确认按钮的事件处理器
        alert.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // 在这里处理用户输入的新信息
                // 调用 updateProductInfo 方法来更新商品信息
                try {
                    String store = shoppingStore.getStoreIDByUsername(username);
                    boolean success = ShoppingProduct.addProduct(
                            productIDField.getText(),
                            productNameField.getText(),
                            productDetailTextArea.getText(),
                            Float.parseFloat(productOriginalPriceField.getText()),
                            Float.parseFloat(productCurrentPriceField.getText()),
                            Integer.parseInt(productInventoryField.getText()),
                            productAddressField.getText(),
                           // Float.parseFloat(productCommentRateField.getText()) / 100, // Convert percentage back to decimal
                            productStatusCheckBox.isSelected(),//可以不传true吗，利用输入框嘎嘎嘎
                            store
                    );
                    if (success) {
                        // 更新成功，关闭弹窗
                        alert.close();
                    } else {
                        // 更新失败，显示错误信息
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setContentText("更新失败，请重试。");
                        errorAlert.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("更新失败，请确保输入格式正确。");
                    errorAlert.showAndWait();
                }
            }
            // 关闭弹窗
            alert.close();
            return null;
        });

        // 显示弹窗
        alert.showAndWait();
    }

    // 创建商品信息的方法
    private void createProductItem(VBox productVBox, ShoppingProduct.oneProduct product) {
        // 创建一个VBox，用于垂直堆叠两个HBox
        productVBox.setPadding(new Insets(5)); // 内边距为5
        productVBox.setStyle("-fx-border-color: #b61c94; -fx-border-width: 1; -fx-border-style: solid; -fx-background-color: rgb(255,255,255); -fx-background-radius: 5; -fx-border-radius: 5;");

        // 创建第一个HBox，用于第一行显示
        HBox productItemTop = new HBox(10); // 间距为10
        // 第一行商品信息
        Label idLabel = new Label("ID: " + product.getProductID());
        Label nameLabel = new Label("名称: " + product.getProductName());
        Label originalPriceLabel = new Label("原价: " + String.format("%.2f", product.getProductOriginalPrice()));
        Label currentPriceLabel = new Label("现价: " + String.format("%.2f", product.getProductCurrentPrice()));
        productItemTop.getChildren().addAll(idLabel, nameLabel, originalPriceLabel, currentPriceLabel);

        // 创建第二个HBox，用于第二行显示
        HBox productItemBottom = new HBox(10); // 间距为10
        // 第二行商品信息
        Label stockLabel = new Label("库存: " + product.getProductInventory());
        Label statusLabel = new Label("状态: " + (product.isProductStatus() ? "在库" : "缺货"));
        Label ratingLabel = new Label("好评率: " + String.format("%.0f%%", product.getProductCommentRate() * 100));
        Label detailLabel = new Label("描述: " + product.getProductDetail());
        productItemBottom.getChildren().addAll(stockLabel, statusLabel, ratingLabel, detailLabel);

        // 创建“查看评论”按钮
        Button viewCommentsButton = new Button("查看评论");
        viewCommentsButton.getStyleClass().add("main-button");
        viewCommentsButton.setOnAction(e -> {
            System.out.println("查看评论按钮被点击"); // 这可以帮助调试
            showProductComments(product.getProductID());
        });

        // 创建图片显示
        ImageView productImageView = new ImageView();
        String relativePath = product.getProductImage().replace("uploads/", "");
        Image productImage = new Image("http://localhost:8082/files/" + relativePath);
        System.out.println("http://localhost:8082/files/" + relativePath);
        productImageView.setImage(productImage);
        productImageView.setFitWidth(200); // 设置图片宽度
        productImageView.setFitHeight(200); // 设置图片高度
        productImageView.setPreserveRatio(true); // 保持图片比例

        // 将图片添加到VBox的顶部
        productVBox.getChildren().addAll(productImageView, productItemTop, productItemBottom, viewCommentsButton);
    }

    // 显示评论界面，跳转到评论界面
    private void showProductComments(String productID) {
        // 创建评论界面
        System.out.println("显示评论: " + productID); // 调试日志
        commentView = new BorderPane();
        // 添加返回按钮
        Button backButton = new Button("返回");
        backButton.getStyleClass().add("main-button");
        backButton.setOnAction(e -> {
            System.out.println("返回到商品页面");  // 调试日志
            root.setCenter(createProductsPane());
        });


        ListView<VBox> commentList = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();
        commentList.setItems(items);
        ScrollPane scrollPane = new ScrollPane(commentList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);


        commentView.setBottom(backButton);
        commentView.setCenter(scrollPane);
        BorderPane.setAlignment(backButton, Pos.CENTER);

        try {
            ShoppingComment.oneComment[] comments = shoppingComment.getProductComments(productID, 0);
            items.clear();
            if (comments != null && comments.length > 0) {
                for (ShoppingComment.oneComment comment : comments) {
                    VBox commentItem=new VBox();
                    Label username=new Label("用户账号: "+comment.getUsername());
                    Label commentAttitude=new Label("评论态度: "+comment.getCommentAttitude());
                    Label commentContent=new Label("评论内容: "+comment.getCommentContent());
                    commentItem.getChildren().addAll(username,commentAttitude,commentContent);
                    items.add(commentItem);
                }
                root.setCenter(commentView);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "没有找到评论或者无法加载评论。");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "获取评论时发生错误。");
            alert.showAndWait();
        }
    }
    // 创建订单界面
    private BorderPane createOrdersPane() {
        BorderPane ordersPane = new BorderPane();
        ScrollPane scrollPane = new ScrollPane();
        HBox hbox = new HBox(20); // 创建一个水平布局的HBox，间距为20
        hbox.setAlignment(Pos.CENTER); // 居中对齐

        VBox leftVBox = new VBox(10); // 左侧列
        VBox rightVBox = new VBox(10); // 右侧列
        leftVBox.setPadding(new Insets(10));
        rightVBox.setPadding(new Insets(10));

        String storeID = null; // 替换为实际的商店ID
        try {
            storeID = shoppingStore.getStoreIDByUsername(username);
            ShoppingOrder.oneOrder[] shoppingOrderList = ShoppingOrder.getAllOrdersByStore(storeID);

            for (int i = 0; i < shoppingOrderList.length; i++) {
                ShoppingOrder.oneOrder order = shoppingOrderList[i];
                if (i % 2 == 0) {
                    createOrderItem(leftVBox, order); // 将偶数索引的订单添加到左列
                } else {
                    createOrderItem(rightVBox, order); // 将奇数索引的订单添加到右列
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        hbox.getChildren().addAll(leftVBox, rightVBox);
        scrollPane.setContent(hbox); // 将HBox放入滚动面板中
        ordersPane.setCenter(scrollPane); // 将滚动面板设置为BorderPane的中心
        return ordersPane;
    }

    private void createOrderItem(VBox orderVBox, ShoppingOrder.oneOrder order) {
        VBox orderItem = new VBox(5); // 每行间距为5
        orderItem.setPadding(new Insets(10)); // 设置内边距为10
        orderItem.setStyle("-fx-border-color: #b61c94; -fx-border-width: 1; -fx-border-style: solid; " +
                "-fx-background-color: rgb(255,255,255); -fx-background-radius: 5; -fx-border-radius: 5;");

        // 第一行：订单号和用户名
        HBox row1 = new HBox(20); // 设置列之间的间距
        Label orderIDLabel = new Label("订单号: " + order.getOrderID());
        Label usernameLabel = new Label("用户: " + order.getUsername());
        row1.getChildren().addAll(orderIDLabel, usernameLabel);

        // 第二行：商品ID和名称
        HBox row2 = new HBox(20);
        Label productIDLabel = new Label("商品ID: " + order.getProductID());
        Label productNameLabel = new Label("商品名称: " + order.productName());
        row2.getChildren().addAll(productIDLabel, productNameLabel);

        // 第三行：商品数量和支付状态
        HBox row3 = new HBox(20);
        Label productNumberLabel = new Label("数量: " + order.getProductNumber());
        Label paidStatusLabel = new Label("支付状态: " + (order.getpaidStatus() ? "已支付" : "未支付"));
        row3.getChildren().addAll(productNumberLabel, paidStatusLabel);

        // 第四行：支付金额和是否评价
        HBox row4 = new HBox(20);
        Label paidMoneyLabel = new Label("支付金额: " + String.format("%.2f", order.getPaidMoney()));
        Label commentStatusLabel = new Label("是否评价: " + (order.isWhetherComment() ? "已评价" : "未评价"));
        row4.getChildren().addAll(paidMoneyLabel, commentStatusLabel);

        // 查看订单详情按钮
        Button viewOrderDetailsButton = new Button("查看订单详情");
        viewOrderDetailsButton.getStyleClass().add("main-button");
        viewOrderDetailsButton.setOnAction(e -> viewOrderDetails(orderVBox, order));

        // 将所有行添加到订单信息的VBox中
        orderItem.getChildren().addAll(row1, row2, row3, row4, viewOrderDetailsButton);

        // 将订单信息VBox添加到根VBox中
        orderVBox.getChildren().add(orderItem);
    }


    private void viewOrderDetails(VBox orderVBox, ShoppingOrder.oneOrder order) {
        // 创建一个新的 TextArea 来显示订单的详细信息
        TextArea detailsArea = new TextArea();
        detailsArea.setEditable(false); // 设置为不可编辑
        detailsArea.setText("订单号: " + order.getOrderID() + "\n" +
                "用户: " + order.getUsername() + "\n" +
                "商品ID: " + order.getProductID() + "\n" +
                "商品名称: " + order.productName() + "\n" +
                "数量: " + order.getProductNumber() + "\n" +
                "支付状态: " + (order.getpaidStatus() ? "已支付" : "未支付") + "\n" +
                "支付金额: " + String.format("%.2f", order.getPaidMoney()) + "\n" +
                "是否评价: " + (order.isWhetherComment() ? "已评价" : "未评价"));

        // 为返回按钮设置事件处理器，以便返回到订单列表
        Button backButton = new Button("返回");
        backButton.getStyleClass().add("main-button");
        backButton.setOnAction(e -> {
            root.setCenter(createOrdersPane());
        });

        HBox buttonBox = new HBox(10); // 创建一个水平布局的 HBox，用于存放按钮
        buttonBox.getChildren().add(backButton); // 将返回按钮添加到 HBox 中
        VBox detailsVBox = new VBox(10);
        detailsVBox.getChildren().addAll(detailsArea, buttonBox);

        // 保存当前订单列表布局到历史栈（如果需要）
        // history.push(root.getCenter());
        // 将当前订单列表布局压入历史栈
        //history.push((BorderPane) root.getCenter());

        // 将 detailsVBox 设置为根布局的中心内容
        root.setCenter(detailsVBox);

    }

    public static void main(String[] args) {
        launch(args);
    }
}