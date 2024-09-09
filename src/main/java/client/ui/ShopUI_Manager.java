package client.ui;

import client.service.ShoppingStore;
import javafx.application.Application;
import client.service.ShoppingProduct;
import client.service.ShoppingOrder;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

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
    private Stack<BorderPane> history = new Stack<>(); // 用于保存历史布局
    // 构造函数，接收用户名
    public ShopUI_Manager(String username) {
        this.username = username;
    }
    @Override
    public void start(Stage primaryStage) {
        // 创建主BorderPane
        root = new BorderPane();

        // 创建TabPane作为顶部的容器
        TabPane topTabs = new TabPane();
        String storeID =null;
        try {
            storeID = shoppingStore.getStoreIDByUsername(username);//修改成功！
            ShoppingStore.oneStore onestore =shoppingStore.oneStore(storeID);
            // 创建商店信息Tab，并设置内容
            Tab shopInfoTab = new Tab("商店信息", createShopInfoPane(onestore));
            // 创建商品Tab，并设置内容
            Tab productsTab = new Tab("商品", createProductsPane());
            // 创建订单Tab，并设置内容
            Tab ordersTab = new Tab("订单", createOrdersPane());

            topTabs.getTabs().addAll(shopInfoTab, productsTab, ordersTab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 设置TabPane样式，使其看起来像标签页
        topTabs.setStyle("-fx-tab-max-height: 50; -fx-tab-min-height: 50; -fx-tab-max-width: 150; -fx-tab-min-width: 150;");
        topTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // 禁止关闭Tab

        // 设置BorderPane的顶部为TabPane
        root.setTop(topTabs);

        // 设置默认显示的中心内容
        topTabs.getSelectionModel().selectFirst();

        // 创建场景并设置到舞台上
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("商家");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 创建商店信息界面
    private BorderPane createShopInfoPane(ShoppingStore.oneStore storeInfo) {
        BorderPane shopInfoPane = new BorderPane();
        // 创建一个文本区域来显示商店信息
        TextArea infoArea = new TextArea();
        infoArea.setEditable(false); // 设置为不可编辑
        infoArea.setText("商店ID: " + storeInfo.getStoreID() + "\n" +
                "商店名称: " + storeInfo.getStoreName() + "\n" +
                "联系电话: " + storeInfo.getStorePhone() + "\n" +
                "评分: " + storeInfo.getStoreRate() + "\n" +
                "状态: " + (storeInfo.getStoreStatus()));

        // 将文本区域设置为界面的中心内容
        shopInfoPane.setCenter(infoArea);
        // 创建修改商家信息的按钮
        Button editButton = new Button("修改商家信息");
        editButton.setOnAction(e -> showEditShopInfoDialog(storeInfo));

        // 将按钮添加到BorderPane的底部
        shopInfoPane.setBottom(editButton);
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
        gridPane.add(rateLabel, 0, 3);
        gridPane.add(storeRateField, 1, 3);
        gridPane.add(statusLabel, 0, 4);
        gridPane.add(storeStatusField, 1, 4);

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
    private StackPane mainPane;
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
        // 创建 StackPane 用于切换商品界面和评论界面
        mainPane = new StackPane();

        // 商品界面布局
        productView = new BorderPane();

        // 创建商品信息的VBox
        VBox productVBox = new VBox(10); // 间距为10
        productVBox.setPadding(new Insets(10)); // 内边距为10

        // 创建商品信息
        String storeID = null;
        try {
            storeID = shoppingStore.getStoreIDByUsername(username);
            if (storeID != null) {
                ShoppingProduct.oneProduct[] shoppingProductList = shoppingProduct.getAllProductsByStore(storeID);
                for (int i = 0; i < shoppingProductList.length; i++) {
                    ShoppingProduct.oneProduct product = shoppingProductList[i];
                    createProductItem(productVBox, product);
//
//                    // 为每个商品项添加“查看评论”按钮
//                    Button viewCommentsButton = new Button("查看评论");
//                    viewCommentsButton.setOnAction(e -> showProductComments(product.getProductID()));
//                    productVBox.getChildren().add(viewCommentsButton);

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
        // 创建底部添加商品按钮
        Button addButton = new Button("添加商品");
        addButton.setOnAction(e -> showEditProductInfoDialog()); // 替换为添加商品的逻辑
        productView.setCenter(productVBox);
        productView.setBottom(addButton);
        // 添加商品界面到主Pane
        mainPane.getChildren().add(productView);

        return productView;
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
        TextField productNameField = new TextField();
        TextArea productDetailTextArea = new TextArea();
        productDetailTextArea.setWrapText(true);
        productDetailTextArea.setMaxWidth(Double.MAX_VALUE);
        TextField productOriginalPriceField = new TextField();
        TextField productCurrentPriceField = new TextField();
        TextField productInventoryField = new TextField();
        TextField productAddressField = new TextField();
        TextField productCommentRateField = new TextField();
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
        Label commentRateLabel = new Label("好评率(%):");
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
        gridPane.add(commentRateLabel, 0, 7);
        gridPane.add(productCommentRateField, 1, 7);
        gridPane.add(statusLabel, 0, 8);
        gridPane.add(productStatusCheckBox, 1, 8);

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
                // 调用 updateProductInfo 方法来更新商品信息
                try {
                    String store = shoppingStore.getStoreIDByUsername(username);
                    boolean success = ShoppingProduct.addProduct(
                            productIDField.getText(),
                            productNameField.getText(),
                            productDetailTextArea.getText(),
                            "123",
                            Float.parseFloat(productOriginalPriceField.getText()),
                            Float.parseFloat(productCurrentPriceField.getText()),
                            Integer.parseInt(productInventoryField.getText()),
                            productAddressField.getText(),
                            Float.parseFloat(productCommentRateField.getText()) / 100, // Convert percentage back to decimal
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
        //VBox productItemBox = new VBox(10); // 间距为10
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
        viewCommentsButton.setOnAction(e ->  {
            System.out.println("查看评论按钮被点击"); // 这可以帮助调试
            showProductComments(product.getProductID());
        });
// 将两个HBox添加到VBox中，实现两行效果
        productVBox.getChildren().addAll(productItemTop, productItemBottom, viewCommentsButton);
    }
    // 显示评论界面，跳转到评论界面
    private void showProductComments(String productID) {
        // 创建评论界面
        System.out.println("显示评论: " + productID); // 调试日志
        commentView = new BorderPane();
        VBox commentsVBox = new VBox(10); // 间距为10
        commentsVBox.setPadding(new Insets(10)); // 内边距为10

        try {
            ShoppingComment.oneComment[] comments = shoppingComment.getProductComments(productID, 0);
            if (comments != null && comments.length > 0) {
                for (ShoppingComment.oneComment comment : comments) {
                    // 创建单个评论的视图
                    AnchorPane commentPane = new AnchorPane();
                    commentPane.setPrefSize(400, 100); // 设置视图的尺寸

                    // 创建并添加用户名标签
                    Label usernameLabel = new Label("用户: " + comment.getUsername());
                    usernameLabel.setLayoutX(10);
                    usernameLabel.setLayoutY(10);
                    commentPane.getChildren().add(usernameLabel);

                    // 创建并添加评论内容标签
                    Label contentLabel = new Label("评论内容: " + comment.getCommentContent());
                    contentLabel.setLayoutX(10);
                    contentLabel.setLayoutY(40);
                    contentLabel.setPrefWidth(380);
                    contentLabel.setWrapText(true);
                    commentPane.getChildren().add(contentLabel);

                    // 显示评分
                    Label attitudeLabel = new Label();
                    switch (comment.getCommentAttitude()) {
                        case 1:
                            attitudeLabel.setText("评分: 差评");
                            break;
                        case 2:
                            attitudeLabel.setText("评分: 中评");
                            break;
                        case 3:
                            attitudeLabel.setText("评分: 好评");
                            break;
                        default:
                            attitudeLabel.setText("评分: 未知");
                    }
                    attitudeLabel.setLayoutX(10);
                    attitudeLabel.setLayoutY(70);
                    commentPane.getChildren().add(attitudeLabel);

                    // 添加单个评论到 VBox
                    commentsVBox.getChildren().add(commentPane);
                    System.out.println("已找到相关评论");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "没有找到评论或者无法加载评论。");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "获取评论时发生错误。");
            alert.showAndWait();
        }

        // 添加返回按钮
        Button backButton = new Button("返回");
        backButton.setOnAction(e -> {
            System.out.println("返回到商品页面");  // 调试日志
            mainPane.getChildren().remove(commentView);
        });
        System.out.println("返回按钮正确添加");  // 调试日志
        // 将返回按钮和评论VBox添加到评论界面
        commentView.setTop(backButton);
        commentView.setCenter(commentsVBox);

        // 显示评论界面，添加到主StackPane
        mainPane.getChildren().setAll(commentView);
    }


    // 创建订单界面
    private BorderPane createOrdersPane() {
        BorderPane ordersPane = new BorderPane();
        VBox orderVBox = new VBox(10); // 创建一个垂直布局的VBox，用于存放所有订单信息
        orderVBox.setPadding(new Insets(10)); // 设置内边距

        String storeID = null; // 替换为实际的商店ID
        try {
            storeID = shoppingStore.getStoreIDByUsername(username);
            ShoppingOrder.oneOrder[] shoppingOrderList = ShoppingOrder.getAllOrdersByStore(storeID);

            for (int i = 0; i < shoppingOrderList.length; i++) {
                ShoppingOrder.oneOrder order = shoppingOrderList[i];
                createOrderItem(orderVBox, order);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 将订单信息VBox设置为BorderPane的中心内容
        ordersPane.setCenter(orderVBox);
        // 将初始订单列表布局压入历史栈
        history.push(ordersPane);
        return ordersPane;
    }

    private void createOrderItem(VBox orderVBox, ShoppingOrder.oneOrder order) {
        HBox orderItem = new HBox(10); // 间距为10
        orderItem.setPadding(new Insets(5)); // 内边距为5
        orderItem.setStyle("-fx-border-color: #b61c94; -fx-border-width: 1; -fx-border-style: solid; -fx-background-color: rgba(16,234,216,0.5); -fx-background-radius: 5; -fx-border-radius: 5;");

        // 订单信息
        Label orderIDLabel = new Label("订单号: " + order.getOrderID());
        Label usernameLabel = new Label("用户: " + order.getUsername());
        Label productIDLabel = new Label("商品ID: " + order.getProductID());
        Label productNameLabel = new Label("商品名称: " + order.productName());
        Label productNumberLabel = new Label("数量: " + order.getProductNumber());
        Label paidStatusLabel = new Label("支付状态: " + (order.getpaidStatus() ? "已支付" : "未支付"));
        Label paidMoneyLabel = new Label("支付金额: " + String.format("%.2f", order.getPaidMoney()));
        Label commentStatusLabel = new Label("是否评价: " + (order.isWhetherComment() ? "已评价" : "未评价"));

        // 查看订单详情按钮
        Button viewOrderDetailsButton = new Button("查看订单详情");
        viewOrderDetailsButton.setOnAction(e -> viewOrderDetails(orderVBox, order));
        // 将所有组件添加到HBox中
        orderItem.getChildren().addAll(orderIDLabel, usernameLabel, productIDLabel, productNameLabel,
                productNumberLabel, paidStatusLabel, paidMoneyLabel, commentStatusLabel, viewOrderDetailsButton);

        // 将订单信息HBox添加到根VBox中
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
        backButton.setOnAction(e -> {
            if (!history.isEmpty()) {
                root.setCenter(history.pop());
            }
        });

        HBox buttonBox = new HBox(10); // 创建一个水平布局的 HBox，用于存放按钮
        buttonBox.getChildren().add(backButton); // 将返回按钮添加到 HBox 中
        VBox detailsVBox = new VBox(10);
        detailsVBox.getChildren().addAll(detailsArea, buttonBox);

        // 保存当前订单列表布局到历史栈（如果需要）
        // history.push(root.getCenter());
        // 将当前订单列表布局压入历史栈
        history.push((BorderPane) root.getCenter());

        // 将 detailsVBox 设置为根布局的中心内容
        root.setCenter(detailsVBox);
    }

    // 切换中心内容的方法
    private void changeCenter(BorderPane root, BorderPane newCenter) {
        root.setCenter(newCenter);
    }
    public static void main(String[] args) {
        launch(args);
    }
}