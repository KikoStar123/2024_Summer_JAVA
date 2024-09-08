package client.ui;

import client.service.ShoppingStore;
import javafx.application.Application;
import client.service.ShoppingProduct;
import client.service.ShoppingOrder;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.util.List;

public class ShopUI_Manager extends Application {
    private String username; // 成员变量，用于存储用户名
    ShoppingStore shoppingStore = new ShoppingStore();//借用商店服务来实现
    ShoppingProduct shoppingProduct = new ShoppingProduct();//借用商品服务

    // 构造函数，接收用户名
    public ShopUI_Manager(String username) {
        this.username = username;
    }
    @Override
    public void start(Stage primaryStage) {
        // 创建主BorderPane
        BorderPane root = new BorderPane();

        // 创建TabPane作为顶部的容器
        TabPane topTabs = new TabPane();

        // 创建按钮并添加到TabPane中
        Button shopInfoButton = new Button("商店信息");
        shopInfoButton.setId("商店信息");
        Button productsButton = new Button("商品");
        productsButton.setId("商品");
        Button ordersButton = new Button("订单");
        ordersButton.setId("订单");

        // 创建Tab并添加按钮
        Tab shopInfoTab = new Tab("商店信息", shopInfoButton);
        shopInfoButton.setMaxWidth(Double.MAX_VALUE); // 使按钮填充整个Tab的空间
        Tab productsTab = new Tab("商品", productsButton);
        productsButton.setMaxWidth(Double.MAX_VALUE);
        Tab ordersTab = new Tab("订单", ordersButton);
        ordersButton.setMaxWidth(Double.MAX_VALUE);

        topTabs.getTabs().addAll(shopInfoTab, productsTab, ordersTab);

        // 设置TabPane样式，使其看起来像按钮
        topTabs.setStyle("-fx-tab-max-height: 50; -fx-tab-min-height: 50; -fx-tab-max-width: 150; -fx-tab-min-width: 150;");
        topTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // 禁止关闭Tab
        // 在 start 方法中设置 TabPane 的 SelectionListener
        topTabs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getContent() instanceof Node) {
                Node content = (Node) newValue.getContent();
                if (content.getId().equals("商店信息")) {
                    changeCenter(root, createShopInfoPane());
                } else if (content.getId().equals("商品")) {
                    changeCenter(root, createProductsPane());
                } else if (content.getId().equals("订单")) {
                    changeCenter(root, createOrdersPane());
                }
            }
        });
        // 为按钮设置事件处理器
        shopInfoButton.setOnAction(e -> {
            // 获取商店ID，这里需要你根据实际情况来获取或定义
            try {
                String storeID = shoppingStore.getStoreIDByUsername(username); // 替换为实际的商店ID//******
                ShoppingStore.oneStore storeInfo = ShoppingStore.oneStore(storeID);
                if (storeInfo != null) {
                    // 如果成功获取商店信息，则更新商店信息界面
                    BorderPane shopInfoPane = createShopInfoPane(storeInfo);
                    changeCenter(root, shopInfoPane);
                } else {
                    // 处理获取信息失败的情况
                    System.out.println("无法获取商店信息");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("获取商店信息时发生错误");
            }
        });//1按钮
        productsButton.setOnAction(e -> changeCenter(root, createProductsPane()));//2
        ordersButton.setOnAction(e -> changeCenter(root, createOrdersPane()));//3

        // 设置BorderPane的顶部为按钮栏
        root.setTop(topTabs);

        // 设置默认显示的中心内容
        //root.setCenter(createShopInfoPane());
        topTabs.getSelectionModel().selectFirst();
        // 创建场景并设置到舞台上
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("商家");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 创建商店信息界面//默认界面
    private BorderPane createShopInfoPane() {
        BorderPane shopInfoPane = new BorderPane();
        shopInfoPane.setCenter(new Button("商店信息内容"));//
        return shopInfoPane;
    }
    // 创建商店具体信息界面用于点击按钮的效果
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

        return shopInfoPane;
    }

    // 创建商品界面
    private BorderPane createProductsPane() {
        BorderPane productsPane = new BorderPane();

        // 创建商品信息的VBox
        VBox productVBox = new VBox(10); // 间距为10
        productVBox.setPadding(new Insets(10)); // 内边距为10

        // 创建商品信息
        String storeID = null; // 替换为实际的商店ID//******
        try {
            storeID = shoppingStore.getStoreIDByUsername(username);
            if (storeID != null) {
                ShoppingProduct.oneProduct[] shoppingProductList = shoppingProduct.getAllProductsByStore(storeID);
                for (int i = 0; i < shoppingProductList.length; i++) {
                    ShoppingProduct.oneProduct product = shoppingProductList[i];
                    String productID = product.getProductID();
                    String productName = product.getProductName();
                    String productDetail = product.getProductDetail();
                    float productOriginalPrice = product.getProductOriginalPrice();
                    float productCurrentPrice = product.getProductCurrentPrice();
                    int productInventory = product.getProductInventory();
                    String productStatus = product.isProductStatus() ? "在库" : "缺货";
                    float productCommentRate = product.getProductCommentRate();

                    // 创建商品信息项
                    createProductItem(productVBox,product);
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
        addButton.setOnAction(e -> System.out.println("添加商品")); // 替换为添加商品的逻辑
        productVBox.getChildren().add(addButton);

        // 设置BorderPane的中心为商品信息VBox
        productsPane.setCenter(productVBox);

        return productsPane;
    }
    // 创建商品信息的方法
    //用于商品信息的显示
    private void createProductItem(VBox productVBox, ShoppingProduct.oneProduct product) {
        HBox productItem = new HBox(10); // 间距为10
        productItem.setPadding(new Insets(5)); // 内边距为5
        productItem.setStyle("-fx-border-color: #b61c94; -fx-border-width: 1; -fx-border-style: solid; -fx-background-color: rgba(16,234,216,0.5); -fx-background-radius: 5; -fx-border-radius: 5;");

        // 商品图片
//        ImageView imageView = new ImageView(new Image(product.getProductImage())); // 假设 product.getProductImage() 返回有效的图片URL
//        imageView.setFitHeight(50);
//        imageView.setFitWidth(50);
//        imageView.setPreserveRatio(true);

        // 商品信息
        Label idLabel = new Label("ID: " + product.getProductID());
        Label nameLabel = new Label("名称: " + product.getProductName());
        Label originalPriceLabel = new Label("原价: " + String.format("%.2f", product.getProductOriginalPrice()));
        Label currentPriceLabel = new Label("现价: " + String.format("%.2f", product.getProductCurrentPrice()));
        Label stockLabel = new Label("库存: " + product.getProductInventory());
        Label statusLabel = new Label("状态: " + (product.isProductStatus() ? "在库" : "缺货"));
        Label ratingLabel = new Label("好评率: " + String.format("%.0f%%", product.getProductCommentRate() * 100));
        Label detailLabel = new Label("描述: " + product.getProductDetail());
        Label addressLabel = new Label("发货地址: " + product.getProductAddress());

        // 查看评论按钮
        Button viewCommentsButton = new Button("查看评论");
        viewCommentsButton.setOnAction(e -> System.out.println("查看评论: " + product.getProductID())); // 替换为查看评论的逻辑

        // 将所有组件添加到HBox中
        productItem.getChildren().addAll(idLabel, nameLabel, originalPriceLabel, currentPriceLabel,
                stockLabel, statusLabel, ratingLabel, detailLabel, addressLabel, viewCommentsButton);

        // 将商品信息HBox添加到根VBox中
        productVBox.getChildren().add(productItem);
    }

    private BorderPane createOrdersPane() {
        BorderPane ordersPane = new BorderPane();
        VBox orderVBox = new VBox(10); // 创建一个垂直布局的VBox，用于存放所有订单信息
        orderVBox.setPadding(new Insets(10)); // 设置内边距

        String storeID = null; // 替换为实际的商店ID//******
        try {
            storeID = shoppingStore.getStoreIDByUsername(username);
            ShoppingOrder.oneOrder[] shoppingOrderList = ShoppingOrder.getAllOrdersByStore(storeID);

            for (int i = 0; i < shoppingOrderList.length; i++) {
                ShoppingOrder.oneOrder order = shoppingOrderList[i];
                String orderID = order.getOrderID();
                String username = order.getUsername();
                String productID = order.getProductID();
                String productName = order.productName(); // 注意这里需要使用正确的方法名
                int productNumber = order.getProductNumber();
                boolean whetherComment = order.isWhetherComment();
                float paidMoney = order.getPaidMoney();
                boolean paidStatus = order.getpaidStatus();

                // 假设我们有一个方法 getProductDetailsByProductID(String productID) 来获取商品详情
                //ShoppingOrder.oneProduct productDetails = getProductDetailsByProductID(productID);

                // 创建订单信息项
                createOrderItem(orderVBox, order);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 将订单信息VBox设置为BorderPane的中心内容
        ordersPane.setCenter(orderVBox);
        return ordersPane;
    }
    // 创建订单界面
    private void createOrderItem(VBox orderVBox, ShoppingOrder.oneOrder order) {
        HBox orderItem = new HBox(10); // 间距为10
        orderItem.setPadding(new Insets(5)); // 内边距为5
        orderItem.setStyle("-fx-border-color: #b61c94; -fx-border-width: 1; -fx-border-style: solid; -fx-background-color: rgba(16,234,216,0.5); -fx-background-radius: 5; -fx-border-radius: 5;");

        // 订单信息
        Label orderIDLabel = new Label("订单号: " + order.getOrderID());
        Label usernameLabel = new Label("用户: " + order.getUsername());
        Label productIDLabel = new Label("商品ID: " + order.getProductID());
        Label productNameLabel = new Label("商品名称: " + order.productName()); // 确保方法名正确
        Label productNumberLabel = new Label("数量: " + order.getProductNumber());
        Label paidStatusLabel = new Label("支付状态: " + (order.getpaidStatus() ? "已支付" : "未支付"));
        Label paidMoneyLabel = new Label("支付金额: " + String.format("%.2f", order.getPaidMoney()));
        Label commentStatusLabel = new Label("是否评价: " + (order.isWhetherComment() ? "已评价" : "未评价"));

        // 查看订单详情按钮
        Button viewOrderDetailsButton = new Button("查看订单详情");
        viewOrderDetailsButton.setOnAction(e -> System.out.println("查看订单详情: " + order.getOrderID())); // 替换为查看订单详情的逻辑

        // 将所有组件添加到HBox中
        orderItem.getChildren().addAll(orderIDLabel, usernameLabel, productIDLabel, productNameLabel,
                productNumberLabel, paidStatusLabel, paidMoneyLabel, commentStatusLabel, viewOrderDetailsButton);

        // 将订单信息HBox添加到根VBox中
        orderVBox.getChildren().add(orderItem);
    }

    // 切换中心内容的方法
    private void changeCenter(BorderPane root, BorderPane newCenter) {
        root.setCenter(newCenter);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

