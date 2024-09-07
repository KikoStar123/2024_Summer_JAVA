package client.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class ShopUI_Manager extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建主BorderPane
        BorderPane root = new BorderPane();

        // 创建顶部的按钮
        Button shopInfoButton = new Button("商店信息");
        Button productsButton = new Button("商品");
        Button ordersButton = new Button("订单");

        // 将按钮放入一个HBox中，以便它们水平排列
        BorderPane topBar = new BorderPane();
        topBar.setCenter(productsButton);
        topBar.setRight(ordersButton);
        topBar.setLeft(shopInfoButton);
        topBar.setPadding(new Insets(5)); // 添加一些内边距

        // 为按钮设置事件处理器
        shopInfoButton.setOnAction(e -> changeCenter(root, createShopInfoPane()));
        productsButton.setOnAction(e -> changeCenter(root, createProductsPane()));
        ordersButton.setOnAction(e -> changeCenter(root, createOrdersPane()));

        // 设置BorderPane的顶部为按钮栏
        root.setTop(topBar);

        // 设置默认显示的中心内容
        root.setCenter(createShopInfoPane());

        // 创建场景并设置到舞台上
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("BorderPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 创建商店信息界面
    private BorderPane createShopInfoPane() {
        BorderPane shopInfoPane = new BorderPane();
        shopInfoPane.setCenter(new Button("商店信息内容"));
        return shopInfoPane;
    }

    // 创建商品界面
    private BorderPane createProductsPane() {
        BorderPane productsPane = new BorderPane();

        // 创建商品信息的VBox
        VBox productVBox = new VBox(10); // 间距为10
        productVBox.setPadding(new Insets(10)); // 内边距为10

        // 创建商品信息
        createProductItem(productVBox, "商品1", "商品描述1", "100", "90", "5", "在库", "95%");
        createProductItem(productVBox, "商品2", "商品描述2", "200", "180", "10", "在库", "90%");

        // 创建底部添加商品按钮
        Button addButton = new Button("添加商品");
        addButton.setOnAction(e -> System.out.println("添加商品")); // 替换为添加商品的逻辑
        productVBox.getChildren().add(addButton);

        // 设置BorderPane的中心为商品信息VBox
        productsPane.setCenter(productVBox);

        return productsPane;
    }
    // 创建商品信息的方法
    private void createProductItem(VBox productVBox, String id, String name, String originalPrice, String currentPrice, String stock, String status, String rating) {
        HBox productItem = new HBox(10); // 间距为10
        productItem.setPadding(new Insets(5)); // 内边距为5
        productItem.setStyle("-fx-border-color: #b61c94; -fx-border-width: 1; -fx-border-style: solid; -fx-background-color: #10ead8; -fx-background-radius: 5; -fx-border-radius: 5;");

        // 商品图片
//        ImageView imageView = new ImageView(new Image("path/to/image.png")); // 替换为实际图片路径
//        imageView.setFitHeight(50);
//        imageView.setFitWidth(50);

        // 商品信息
        Label idLabel = new Label("ID: " + id);
        Label nameLabel = new Label("名称: " + name);
        Label originalPriceLabel = new Label("原价: " + originalPrice);
        Label currentPriceLabel = new Label("现价: " + currentPrice);
        Label stockLabel = new Label("库存: " + stock);
        Label statusLabel = new Label("状态: " + status);
        Label ratingLabel = new Label("好评率: " + rating);

        // 查看评论按钮
        Button viewCommentsButton = new Button("查看评论");
        viewCommentsButton.setOnAction(e -> System.out.println("查看评论: " + id)); // 替换为查看评论的逻辑

        // 将所有组件添加到HBox中//暂时无法添加图片imageView
        productItem.getChildren().addAll(idLabel, nameLabel, originalPriceLabel, currentPriceLabel, stockLabel, statusLabel, ratingLabel, viewCommentsButton);

        // 将商品信息HBox添加到根VBox中
        productVBox.getChildren().add(productItem);
    }

    // 创建订单界面
    private BorderPane createOrdersPane() {
        BorderPane ordersPane = new BorderPane();
        ordersPane.setCenter(new Button("订单内容"));
        return ordersPane;
    }

    // 切换中心内容的方法
    private void changeCenter(BorderPane root, BorderPane newCenter) {
        root.setCenter(newCenter);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

