package client.ui;

import client.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static client.service.Bank.payment;
import static client.service.ShoppingOrder.createOrder;


public class ShopUI_stu {
    private User user;
    private BorderPane borderPane;

    public ShopUI_stu(User user, BorderPane borderPane) {
        this.user = user;
        this.borderPane = borderPane;
    }
    public VBox getShopLayout() throws IOException {
        VBox shopLayout = new VBox();

        // 创建顶部按钮
        HBox topMenu = new HBox();
        Button btnProducts = new Button("商品");
        btnProducts.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button btnCart = new Button("购物车");
        btnCart.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button btnOrders = new Button("订单");
        btnOrders.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        topMenu.getChildren().addAll(btnProducts, btnCart, btnOrders);
        btnCart.setOnAction(e -> {
            try {
                showShoppingCart(user);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnOrders.setOnAction(e -> {
            try {
                showOrders();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // 创建商品搜索栏
        HBox searchBox = new HBox();
        TextField searchField = new TextField();
        searchField.setPromptText("搜索商品");
        searchField.getStyleClass().add("input-field");
        ComboBox<String> sortByComboBox = new ComboBox<>();
        sortByComboBox.setItems(FXCollections.observableArrayList("按价格排序", "按好评率排序"));
        sortByComboBox.setPromptText("排序方式");
        sortByComboBox.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        ComboBox<String> sortOrderComboBox = new ComboBox<>();
        sortOrderComboBox.setItems(FXCollections.observableArrayList("升序", "降序"));
        sortOrderComboBox.setPromptText("排序顺序");
        sortOrderComboBox.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        Button searchButton = new Button("搜索");
        searchButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        searchBox.getChildren().addAll(searchField, sortByComboBox, sortOrderComboBox, searchButton);

        // 创建商品列表
        ListView<HBox> productList = new ListView<>();
        ObservableList<HBox> items = FXCollections.observableArrayList();
        productList.setItems(items);

        // 将商品列表放入滚动面板
        ScrollPane scrollPane = new ScrollPane(productList);
        scrollPane.setFitToWidth(true); // 设置滚动面板的宽度适应内容
        scrollPane.setFitToHeight(true); // 设置滚动面板的高度适应内容

        shopLayout.getChildren().addAll(topMenu, searchBox, scrollPane);

        VBox.setVgrow(scrollPane, Priority.ALWAYS); // 设置滚动面板优先填充垂直空间

        ShoppingProduct shoppingProduct1 = new ShoppingProduct();
        ShoppingProduct.oneProduct[] products1 = shoppingProduct1.getAllProducts("price","ASC");
        items.clear();
        for(ShoppingProduct.oneProduct product:products1){
            HBox productItem = new HBox(10); // 间距为10
            productItem.setPadding(new Insets(10)); // 内边距为10

            // 创建商品图片
                    ImageView productImage = new ImageView();
                    String relativePath = product.getProductImage().replace("uploads/", "");
                    Image image = new Image("http://localhost:8082/files/" + relativePath);
                    productImage.setImage(image);
                    productImage.setFitWidth(150); // 设置图片宽度
                    productImage.setFitHeight(150); // 设置图片高度
                    productImage.setPreserveRatio(true); // 保持图片比例

            // 创建商品详情
            VBox productDetails = new VBox(5); // 间距为5
            Label lblName = new Label("名称: " + product.getProductName());
            Label lblRating = new Label("好评率: " + product.getProductCommentRate() * 100 + "%");
            Label lblOriginalPrice = new Label("原价: ¥" + product.getProductOriginalPrice());
            Label lblCurrentPrice = new Label("现价: ¥" + product.getProductCurrentPrice());
            Label lblSeller = new Label("商家名称: " + product.getStoreName());
            productDetails.getChildren().addAll(productImage, lblName, lblRating, lblOriginalPrice, lblCurrentPrice, lblSeller);

            productItem.getChildren().addAll( productDetails);
            items.add(productItem);

            // 为每个商品栏设置双击事件
            productItem.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    // 显示商品详情信息
                    try {
                        borderPane.setCenter(new VBox(showProductDetails(product)));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        // 搜索按钮点击事件
        searchButton.setOnAction(e -> {
            String searchTerm = searchField.getText();
            String sortBy;
            if(Objects.equals(sortByComboBox.getValue(), "按价格排序"))
                sortBy="price";
            else
                sortBy="rate";
            String sortOrder;
            if(Objects.equals(sortOrderComboBox.getValue(), "升序"))
                sortOrder="ASC";
            else
                sortOrder="DESC";

            try {
                ShoppingProduct shoppingProduct = new ShoppingProduct();
                ShoppingProduct.oneProduct[] products = shoppingProduct.searchProducts(searchTerm, sortBy, sortOrder);
                items.clear();
                for (ShoppingProduct.oneProduct product : products) {
                    HBox productItem = new HBox(10); // 间距为10
                    productItem.setPadding(new Insets(10)); // 内边距为10

                    // 创建商品图片
                    ImageView productImage = new ImageView();
                    String relativePath = product.getProductImage().replace("uploads/", "");
                    Image image = new Image("http://localhost:8082/files/" + relativePath);
                    productImage.setImage(image);
                    productImage.setFitWidth(150); // 设置图片宽度
                    productImage.setFitHeight(150); // 设置图片高度
                    productImage.setPreserveRatio(true); // 保持图片比例

                    // 创建商品详情
                    VBox productDetails = new VBox(5); // 间距为5
                    Label lblName = new Label("名称: " + product.getProductName());
                    Label lblRating = new Label("好评率: " + product.getProductCommentRate() * 100 + "%");
                    Label lblOriginalPrice = new Label("原价: ¥" + product.getProductOriginalPrice());
                    Label lblCurrentPrice = new Label("现价: ¥" + product.getProductCurrentPrice());
                    Label lblSeller = new Label("商家名称: " + product.getStoreName());
                    productDetails.getChildren().addAll(productImage, lblName, lblRating, lblOriginalPrice, lblCurrentPrice, lblSeller);

                    productItem.getChildren().addAll( productDetails);
                    items.add(productItem);

                    // 为每个商品栏设置双击事件
                    productItem.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            // 显示商品详情信息
                            try {
                                borderPane.setCenter(new VBox(showProductDetails(product)));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return shopLayout;
    }

    private VBox showOrders() throws IOException {

        // 创建搜索栏
        TextField searchField = new TextField();
        searchField.setPromptText("搜索订单...");
        Button searchButton = new Button("搜索");
        HBox searchBox = new HBox(10, searchField, searchButton);

        ListView<VBox> orderList = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();
        orderList.setItems(items);
        ScrollPane scrollPane = new ScrollPane(orderList);
        scrollPane.setFitToWidth(true);
        ShoppingOrder shoppingOrder=new ShoppingOrder();
        ShoppingOrder.oneOrder[] Orders=shoppingOrder.getAllOrdersByUser(user.getUsername());
        items.clear();
        ShoppingUser.oneUser currentUser = ShoppingUser.getUserDetails(user.getUsername());
        for (ShoppingOrder.oneOrder order:Orders)
        {
            VBox orderbox=new VBox();
            Label orderid=new Label("订单id: "+order.getOrderID());
            orderid.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
            Label productname=new Label("商品名称: "+order.productName());
            productname.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
            Label paidMoney=new Label("支付金额: "+order.getPaidMoney());
            paidMoney.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
            Button commentbutton=new Button("评论");
            commentbutton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
            Button paybutton=new Button("支付");
            paybutton.getStyleClass().add("main-button");
            // 应用CSS中的按钮样式
            Button logisticsButton=new Button("查看物流信息");
            logisticsButton.setOnAction(e->showlogistics());
            logisticsButton.getStyleClass().add("main-button");

            ComboBox<String> infoComboBox = new ComboBox<>();
            for (int i = 0; i < currentUser.getAddresses().length; i++) {
                String address = currentUser.getAddresses()[i];
                String telephone = currentUser.getTelephones()[i];
                infoComboBox.getItems().add("地址: " + address + " 电话: " + telephone);
            }
            infoComboBox.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
            infoComboBox.setPromptText("选择收货信息");
            // 默认选择第一个收货信息
            if (!infoComboBox.getItems().isEmpty()) {
                infoComboBox.getSelectionModel().selectFirst();
            }
            if(order.getpaidStatus())
            {
                paybutton.setText("已支付");
                paybutton.setDisable(true);
            }
            else
            {
                //未支付不可评论
                commentbutton.setDisable(true);
            }
            if(ShoppingOrder.getOrderCommentStatus(order.getOrderID()))
            {
                commentbutton.setText("已评论");
                commentbutton.setDisable(true);
            }
            orderbox.getChildren().addAll(orderid,productname,paidMoney,infoComboBox,commentbutton,paybutton,logisticsButton);
            items.add(orderbox);
            paybutton.setOnAction(e-> {
                List<String> orderIds = new ArrayList<>();
                orderIds.add(order.getOrderID());
                float totalAmount = order.getPaidMoney();
                boolean result = handlePayment(orderIds, totalAmount);
                if (result) {
                    ShoppingProduct.oneProduct oneProduct;
                    try {
                        oneProduct = ShoppingProduct.getProductDetails(order.getProductID());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String selectedInfo = infoComboBox.getSelectionModel().getSelectedItem();
                    String address = null;
                    if (selectedInfo != null) {
                        // 假设格式为 "地址: xxx 电话: xxx"
                        String[] parts = selectedInfo.split(" 电话: ");
                        address = parts[0].replace("地址: ", "");
                        System.out.println("选中的地址: " + address);
                    }
                    try {
                        ShoppingMap.addMapRecord(order.getProductID(), oneProduct.getProductAddress(), address);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            });
            commentbutton.setOnAction(e->{
                VBox commentbox = new VBox();
                // 创建下拉框用于选择评论态度
                ComboBox<String> attitudeComboBox = new ComboBox<>();
                attitudeComboBox.getItems().addAll("差评", "中评", "好评");
                attitudeComboBox.setPromptText("选择评论态度");

                // 创建输入框用于输入评论内容
                TextField commentField = new TextField();
                commentField.setPromptText("输入评论内容");

                // 创建确认和返回按钮
                Button confirmButton = new Button("确认");
                confirmButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                Button backButton = new Button("返回");
                backButton.setOnAction(event1->{
                    try {
                        borderPane.setCenter(showOrders());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                backButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                confirmButton.setOnAction(confirmEvent ->{
                    int commentAttitude = attitudeComboBox.getSelectionModel().getSelectedIndex()+1;
                    String commentContent = commentField.getText();
                    if (attitudeComboBox.getValue() == null || commentField.getText().isEmpty()) {
                        // 提示用户选择态度和输入评论内容
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("警告");
                        alert.setHeaderText(null);
                        alert.setContentText("请选择评论态度并输入评论内容！");
                        alert.showAndWait();
                    }
                    else{
                        boolean result = false;
                        try {
                            result = ShoppingComment.addComment(user.getUsername(), order.getProductID(), commentAttitude, commentContent, order.getOrderID());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (result) {
                            commentbutton.setText("已评论");
                            commentbutton.setDisable(true);
                            try {
                                ShoppingOrder.updateCommentStatus(order.getOrderID(),true);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("提示");
                            alert.setHeaderText(null);
                            alert.setContentText("评论成功！");
                            alert.showAndWait();
                            try {
                                borderPane.setCenter(new VBox(showOrders()));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("提示");
                            alert.setHeaderText(null);
                            alert.setContentText("评论失败！");
                            alert.showAndWait();
                            try {
                                borderPane.setCenter(new VBox(showOrders()));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });
                // 将所有控件添加到VBox中
                commentbox.getChildren().addAll(orderid, productname, paidMoney, attitudeComboBox, commentField, confirmButton, backButton);
                borderPane.setCenter(commentbox);
            });
        }

        searchButton.setOnAction(e -> {
            try {
                String searchText = searchField.getText();
                ShoppingOrder.oneOrder[] filteredOrders = shoppingOrder.searchOrdersByUser(user.getUsername(), searchText);
                ObservableList<VBox> filteredItems = FXCollections.observableArrayList();
                for (ShoppingOrder.oneOrder order : filteredOrders) {
                    VBox orderbox=new VBox();
                    Label orderid=new Label("订单id: "+order.getOrderID());
                    orderid.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
                    Label productname=new Label("商品名称: "+order.productName());
                    productname.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
                    Label paidMoney=new Label("支付金额: "+order.getPaidMoney());
                    paidMoney.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式
                    Button commentbutton=new Button("评论");
                    commentbutton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                    Button paybutton=new Button("支付");
                    paybutton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

                    ComboBox<String> infoComboBox = new ComboBox<>();
                    for (int i = 0; i < currentUser.getAddresses().length; i++) {
                        String address = currentUser.getAddresses()[i];
                        String telephone = currentUser.getTelephones()[i];
                        infoComboBox.getItems().add("地址: " + address + " 电话: " + telephone);
                    }
                    infoComboBox.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                    infoComboBox.setPromptText("选择收货信息");
                    // 默认选择第一个收货信息
                    if (!infoComboBox.getItems().isEmpty()) {
                        infoComboBox.getSelectionModel().selectFirst();
                    }
                    if(order.getpaidStatus())
                    {
                        paybutton.setText("已支付");
                        paybutton.setDisable(true);
                    }
                    if(ShoppingOrder.getOrderCommentStatus(order.getOrderID()))
                    {
                        commentbutton.setText("已评论");
                        commentbutton.setDisable(true);
                    }
                    orderbox.getChildren().addAll(orderid,productname,paidMoney,infoComboBox,commentbutton,paybutton);
                    filteredItems.add(orderbox);
                    paybutton.setOnAction(event-> {
                        List<String> orderIds = new ArrayList<>();
                        orderIds.add(order.getOrderID());
                        float totalAmount = order.getPaidMoney();
                        boolean result = handlePayment(orderIds, totalAmount);
                        if (result) {
                            ShoppingProduct.oneProduct oneProduct;
                            try {
                                oneProduct = ShoppingProduct.getProductDetails(order.getProductID());
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            String selectedInfo = infoComboBox.getSelectionModel().getSelectedItem();
                            String address = null;
                            if (selectedInfo != null) {
                                // 假设格式为 "地址: xxx 电话: xxx"
                                String[] parts = selectedInfo.split(" 电话: ");
                                address = parts[0].replace("地址: ", "");
                                System.out.println("选中的地址: " + address);
                            }
                            try {
                                ShoppingMap.addMapRecord(order.getProductID(), oneProduct.getProductAddress(), address);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    });
                    commentbutton.setOnAction(event->{
                        VBox commentbox = new VBox();
                        // 创建下拉框用于选择评论态度
                        ComboBox<String> attitudeComboBox = new ComboBox<>();
                        attitudeComboBox.getItems().addAll("差评", "中评", "好评");
                        attitudeComboBox.setPromptText("选择评论态度");

                        // 创建输入框用于输入评论内容
                        TextField commentField = new TextField();
                        commentField.setPromptText("输入评论内容");

                        // 创建确认和返回按钮
                        Button confirmButton = new Button("确认");
                        confirmButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                        Button backButton = new Button("返回");
                        backButton.setOnAction(event1->{
                            try {
                                borderPane.setCenter(showOrders());
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        backButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                        confirmButton.setOnAction(confirmEvent ->{
                            int commentAttitude = attitudeComboBox.getSelectionModel().getSelectedIndex()+1;
                            String commentContent = commentField.getText();
                            boolean result = false;
                            try {
                                result = ShoppingComment.addComment(user.getUsername(), order.getProductID(), commentAttitude, commentContent, order.getOrderID());
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (result) {
                                commentbutton.setText("已评论");
                                commentbutton.setDisable(true);
                                try {
                                    ShoppingOrder.updateCommentStatus(order.getOrderID(),true);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("提示");
                                alert.setHeaderText(null);
                                alert.setContentText("评论成功！");
                                alert.showAndWait();
                                try {
                                    borderPane.setCenter(new VBox(showOrders()));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("提示");
                                alert.setHeaderText(null);
                                alert.setContentText("评论失败！");
                                alert.showAndWait();
                                try {
                                    borderPane.setCenter(new VBox(showOrders()));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                        });
                        // 将所有控件添加到VBox中
                        commentbox.getChildren().addAll(orderid, productname, paidMoney, attitudeComboBox, commentField, confirmButton, backButton);
                        borderPane.setCenter(commentbox);
                    });
                }
                orderList.setItems(filteredItems);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        VBox ordersBox=new VBox();
        ordersBox.getChildren().addAll(scrollPane);
        Button backButton=new Button("返回");
        backButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        backButton.setOnAction(e->{
            try {
                borderPane.setCenter(new VBox(getShopLayout()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button deleteButton = new Button("删除收货信息");
        deleteButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        deleteButton.setOnAction(event -> {
            Stage popupStage = new Stage();
            popupStage.setTitle("删除收货信息");
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            Label delinfoLabel = new Label("新的收货信息:");
            delinfoLabel.getStyleClass().add("body-font");
            ComboBox<String> infoComboBox = new ComboBox<>();
            infoComboBox.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
            for (int i = 0; i < currentUser.getAddresses().length; i++) {
                String address = currentUser.getAddresses()[i];
                String telephone = currentUser.getTelephones()[i];
                infoComboBox.getItems().add("地址: " + address + " 电话: " + telephone);
            }
            Button confirmButton = new Button("确认");
            confirmButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
            confirmButton.setOnAction(e -> {
                int selectedIndex = infoComboBox.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    try {
                        boolean success = ShoppingUser.deleteUserContact(user.getUsername(), selectedIndex);
                        if (success) {
                            // 更新界面或显示成功消息
                            System.out.println("收货信息删除成功");
                            // 从下拉框中移除已删除的收货信息
                            infoComboBox.getItems().remove(selectedIndex);
                            popupStage.close();
                            borderPane.setCenter(showOrders());
                        } else {
                            // 显示错误消息
                            System.out.println("收货信息删除失败");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            layout.getChildren().addAll(delinfoLabel,infoComboBox, confirmButton);
            Scene scene = new Scene(layout, 300, 200);
            popupStage.setScene(scene);
            popupStage.show();
        });

        Button adduserButton = new Button("添加收货信息");
        adduserButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        adduserButton.setOnAction(e -> {
            Stage popupStage = new Stage();
            popupStage.setTitle("添加收货信息");
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            Label newinfoLabel = new Label("新的收货信息:");
            newinfoLabel.getStyleClass().add("body-font");
            TextField addressField = new TextField();
            addressField.setPromptText("输入新的收货地址");
            addressField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
            TextField telephoneField = new TextField();
            telephoneField.setPromptText("输入新的联系电话");
            telephoneField.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
            Button confirmButton = new Button("确认");
            confirmButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
            confirmButton.setOnAction(event -> {
                String newAddress = addressField.getText();
                String newTelephone = telephoneField.getText();
                try {
                    boolean success = ShoppingUser.addUserContact(user.getUsername(), newAddress, newTelephone);
                    if (success) {
                        // 更新界面或显示成功消息
                        System.out.println("收货信息添加成功");
                        // 关闭弹出窗口
                        popupStage.close();
                        borderPane.setCenter(showOrders());
                    } else {
                        // 显示错误消息
                        System.out.println("收货信息添加失败");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            // 将组件添加到布局中
            layout.getChildren().addAll(newinfoLabel, addressField, telephoneField, confirmButton);

            // 设置场景并显示弹出窗口
            Scene scene = new Scene(layout, 300, 200);
            popupStage.setScene(scene);
            popupStage.show();
        });

        ordersBox.getChildren().clear(); // 清除之前的子节点
        ordersBox.getChildren().addAll(searchBox,scrollPane, adduserButton, deleteButton, backButton);
        borderPane.setCenter(ordersBox);
        return ordersBox;

    }

    private void showlogistics() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // 捕获 JavaScript 的 Alert 用于调试
        webEngine.setOnAlert(event -> {
            System.out.println("JS Alert: " + event.getData());
        });

        // 加载 HTML 文件
        webEngine.load(getClass().getResource("/map_navigation.html").toExternalForm());

        // 页面加载完成时注入自定义的 console.log
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // 在 JavaScript 中重写 console.log 以输出到 Java 控制台
                webEngine.executeScript(
                        "console.log = (function(oldLog) {" +
                                "  return function(message) {" +
                                "    oldLog(message);" +
                                "    alert(message);" + // 使用 alert 输出到 Java 控制台
                                "  };" +
                                "})(console.log);"
                );


                // 调用 JavaScript 函数，传递两个地址而不是坐标
                String startAddress = "北京市朝阳区阜通东大街6号"; // 起始地址
                String endAddress = "上海市黄浦区人民大道"; // 终点地址
                webEngine.executeScript("displayRoute('" + startAddress + "', '" + endAddress + "');");
            }
        });


        VBox logisticsBox = new VBox(10);
        logisticsBox.setPadding(new Insets(10));

        Label logisticsStatusLabel = new Label("物流状态: 正在运输");
        Label estimatedArrivalLabel = new Label("预计到达时间: 2天后");

        logisticsBox.getChildren().addAll(webView, logisticsStatusLabel, estimatedArrivalLabel);

        Button backButton = new Button("返回");
        backButton.setOnAction(e -> {
            try {
                borderPane.setCenter(showOrders());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        logisticsBox.getChildren().add(backButton);
        borderPane.setCenter(logisticsBox);
    }



    //购物车页
    private void showShoppingCart(User user) throws IOException {
        ListView<VBox> cartelementList = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();
        cartelementList.setItems(items);
        ScrollPane scrollPane = new ScrollPane(cartelementList);
        scrollPane.setFitToWidth(true);
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCart.oneCartElement[] cartElements = shoppingCart.getShoppingCart(user.getUsername());
        items.clear();
        Label totalPriceLabel = new Label("总价格: 0.0");
        Button buybutton = new Button("购买");
        buybutton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        double[] totalPrice = {0.0};
        ComboBox<String> infoComboBox = null;
        if (cartElements == null) {
        } else {
            for (ShoppingCart.oneCartElement cartElement : cartElements) {

                VBox cart=new VBox();
                ShoppingProduct.oneProduct oneProduct=ShoppingProduct.getProductDetails(cartElement.getProductID());
                Label productname=new Label("商品名称: "+oneProduct.getProductName());
                Label productid=new Label("商品id: "+oneProduct.getProductID());
                Label productdetail=new Label("商品属性: "+oneProduct.getProductDetail());
                Label quantityLabel = new Label("数量: "+cartElement.getProductNumber());
                CheckBox selectBox = new CheckBox();
                selectBox.setOnAction(event -> {
                    if (selectBox.isSelected()) {
                        totalPrice[0] += oneProduct.getProductCurrentPrice() * Integer.parseInt(quantityLabel.getText().split(": ")[1]);
                    } else {
                        totalPrice[0] -= oneProduct.getProductCurrentPrice() * Integer.parseInt(quantityLabel.getText().split(": ")[1]);
                    }
                    totalPriceLabel.setText("总价格: " + totalPrice[0]);
                });

                Button decreaseButton = new Button("-");
                Button increaseButton = new Button("+");
                // 设置按钮样式为圆形
                decreaseButton.setStyle("-fx-background-radius: 50%; -fx-min-width: 15px; -fx-min-height: 15px;");
                increaseButton.setStyle("-fx-background-radius: 50%; -fx-min-width: 15px; -fx-min-height: 15px;");

                HBox quantityBox = new HBox(decreaseButton, quantityLabel, increaseButton);
                int quantity = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
                Label sumprice=new Label("商品总价: "+oneProduct.getProductCurrentPrice()*quantity);
                decreaseButton.setOnAction(event -> {
                    int quantityb = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
                    if (quantityb > 1) {
                        quantityb--;
                        quantityLabel.setText("数量: " + quantityb);
                        try {
                            ShoppingCart.updateCart(user.getUsername(),oneProduct.getProductID(), quantityb);
                        } catch (IOException e) {
                            System.out.println("error");
                            throw new RuntimeException(e);

                        }
                        sumprice.setText("商品总价: "+oneProduct.getProductCurrentPrice()*quantityb);
                        if (selectBox.isSelected()) {
                            totalPrice[0] -= oneProduct.getProductCurrentPrice();
                            totalPriceLabel.setText("总价格: " + totalPrice[0]);
                        }
                    }
                });

                increaseButton.setOnAction(event -> {
                    int quantityb = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
                    quantityb++;
                    quantityLabel.setText("数量: " + quantityb);
                    try {
                        ShoppingCart.updateCart(user.getUsername(),oneProduct.getProductID(), quantityb);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sumprice.setText("商品总价: "+oneProduct.getProductCurrentPrice()*quantityb);
                    if (selectBox.isSelected()) {
                        totalPrice[0] += oneProduct.getProductCurrentPrice();
                        totalPriceLabel.setText("总价格: " + totalPrice[0]);
                    }
                });
                Button deleteButton=new Button("删除");
                deleteButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                deleteButton.setOnAction(event -> {
                    items.remove(cart);
                    if (selectBox.isSelected()) {
                        totalPrice[0] -= oneProduct.getProductCurrentPrice() * cartElement.getProductNumber();
                        totalPriceLabel.setText("总价格: " + totalPrice[0]);
                    }
                    try {
                        ShoppingCart.removeFromCart(user.getUsername(),oneProduct.getProductID());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                ComboBox<String> finalInfoComboBox = new ComboBox<>();
                finalInfoComboBox.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                ShoppingUser.oneUser currentUser = ShoppingUser.getUserDetails(user.getUsername());
                for (int i = 0; i < currentUser.getAddresses().length; i++) {
                    String address = currentUser.getAddresses()[i];
                    String telephone = currentUser.getTelephones()[i];
                    finalInfoComboBox.getItems().add("地址: " + address + " 电话: " + telephone);
                }
                finalInfoComboBox.setPromptText("选择收货信息");
                // 默认选择第一个收货信息
                if (!finalInfoComboBox.getItems().isEmpty()) {
                    finalInfoComboBox.getSelectionModel().selectFirst();
                }

                buybutton.setOnAction(e -> {
                    List<String> orderIds = new ArrayList<>();
                    List<String> productIds = new ArrayList<>();
                    float totalAmount = 0;

                    for (VBox cartBox : items) {
                        CheckBox selectBoxb = null;
                        for (Node node : cartBox.getChildren()) {
                            if (node instanceof HBox) {
                                HBox hbox = (HBox) node;
                                for (Node hboxNode : hbox.getChildren()) {
                                    if (hboxNode instanceof CheckBox) {
                                        selectBoxb = (CheckBox) hboxNode;
                                        break;
                                    }
                                }
                            }
                            if (selectBoxb != null) {
                                break;
                            }
                        }

                        if (selectBoxb != null && selectBoxb.isSelected()) {
                            Label productnameb = (Label) ((HBox) cartBox.getChildren().get(0)).getChildren().get(1);
                            Label productIdd = (Label) ((HBox) cartBox.getChildren().get(0)).getChildren().get(3);
                            Label quantityLabelb = (Label) ((HBox) cartBox.getChildren().get(1)).getChildren().get(1);
                            Label paymoney = (Label) ((HBox) cartBox.getChildren().get(2)).getChildren().get(0);
                            String productName = productnameb.getText().replace("商品名称: ", "");
                            String productId = productIdd.getText().replace("商品id: ", "");
                            int quantityb = Integer.parseInt(quantityLabelb.getText().replace("数量: ", ""));
                            float paymoneyb = Float.parseFloat(paymoney.getText().replace("商品总价: ", ""));
                            totalAmount += paymoneyb;

                            // 调用创建订单的函数
                            try {
                                String orderid = createOrder(user.getUsername(), productId, productName, quantityb, paymoneyb);
                                orderIds.add(orderid);
                                productIds.add(productId);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }

                    // 合并结算
                    if (!orderIds.isEmpty()) {
                        boolean result = handlePayment(orderIds, totalAmount);
                        if (result) {
                            String selectedInfo = finalInfoComboBox.getSelectionModel().getSelectedItem();
                            String address = null;
                            if (selectedInfo != null) {
                                // 假设格式为 "地址: xxx 电话: xxx"
                                String[] parts = selectedInfo.split(" 电话: ");
                                address = parts[0].replace("地址: ", "");
                                System.out.println("选中的地址: " + address);
                            }

                            for (String productId : productIds) {
                                try {
                                    ShoppingProduct.oneProduct oneProduct1 = ShoppingProduct.getProductDetails(productId);
                                    ShoppingMap.addMapRecord(productId, oneProduct1.getProductAddress(), address);

                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                            try {
                                borderPane.setCenter(new VBox(showOrders()));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });

                HBox boxa = new HBox(selectBox, productname, productdetail, productid);
                HBox boxb = new HBox(sumprice, deleteButton);

                cart.getChildren().addAll(boxa, quantityBox, boxb);
                items.add(cart);

                Button backButton = new Button("返回");
                backButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
                backButton.setOnAction(e -> {
                    try {
                        borderPane.setCenter(getShopLayout());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                HBox bottom = new HBox(totalPriceLabel, buybutton, backButton);
                VBox layout = new VBox();
                layout.getChildren().addAll(scrollPane, finalInfoComboBox, bottom);
                borderPane.setCenter(layout);

    }}}
    //进入商品详情页
    private VBox showProductDetails(ShoppingProduct.oneProduct product) throws IOException {
        VBox detailLayout = new VBox(10); // 间距为10
        detailLayout.setPadding(new Insets(10)); // 内边距为10

        // 创建商品图片
        ImageView productImage = new ImageView();
        String relativePath = product.getProductImage().replace("uploads/", "");
        Image image = new Image("http://localhost:8082/files/" + relativePath);
        productImage.setImage(image);
        productImage.setFitWidth(200); // 设置图片宽度
        productImage.setFitHeight(200); // 设置图片高度
        productImage.setPreserveRatio(true); // 保持图片比例

        Label lblName = new Label("商品名称: " + product.getProductName());
        Label lblProductdetail = new Label("商品属性: " + product.getProductDetail());
        Label lblOriginalPrice = new Label("原价: ¥" + product.getProductOriginalPrice());
        Label lblCurrentPrice = new Label("现价: ¥" + product.getProductCurrentPrice());
        Label lblInventory = new Label("商品库存: " + product.getProductInventory());
        Button rateButton = new Button("商品评价");
        rateButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式

        rateButton.setOnAction(e -> {
            try {
                showComment(product);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Label lblAddress = new Label("商品发货地址: " + product.getProductAddress());
        ShoppingStore.oneStore oneStore = ShoppingStore.oneStore(product.getStoreID());
        VBox shopperBox = new VBox(5); // 间距为5
        Label lblSeller = new Label("商家名称: " + product.getStoreName());
        Label lblSellerrating = new Label("商家好评率: " + oneStore.getStoreRate() * 100 + "%");
        shopperBox.getChildren().addAll(lblSeller, lblSellerrating);
        shopperBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // 显示商店详情信息
                try {
                    borderPane.setCenter(new VBox(showShoppingStore(product)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        HBox buyBox = new HBox(10); // 间距为10
        Button buyButton = new Button("直接购买");
        buyButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        // 点击购买进入购买页
        buyButton.setOnAction(e -> {
            try {
                buyproduct(product);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // 加入购物车
        Button addButton = new Button("加入购物车");
        addButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        addButton.setOnAction(e -> {
            try {
                addproduct(product);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buyBox.getChildren().addAll(buyButton, addButton);
        Button backButton = new Button("返回");
        backButton.getStyleClass().add("main-button"); // 应用CSS中的按钮样式
        backButton.setOnAction(e -> {
            try {
                borderPane.setCenter(getShopLayout());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        detailLayout.getChildren().addAll(productImage, lblName, lblProductdetail, lblOriginalPrice, lblCurrentPrice, lblInventory, rateButton, lblAddress, shopperBox, buyBox, backButton);

        return detailLayout;
    }

    private void addproduct(ShoppingProduct.oneProduct product) throws IOException {
        VBox buyproductBox=new VBox();
        Label common=new Label("同类商品");
        ListView<VBox> commonproductList = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();
        commonproductList.setItems(items);
        ScrollPane scrollPane = new ScrollPane(commonproductList);
        scrollPane.setFitToWidth(true);
        ShoppingProduct shoppingProduct = new ShoppingProduct();
        ShoppingProduct.oneProduct[] commonproducts = shoppingProduct.getSameCategoryProducts(product.getProductID());
        items.clear();
        for (ShoppingProduct.oneProduct commonproduct : commonproducts)
        {
            VBox productItem=new VBox();
            Label productDetail=new Label(commonproduct.getProductDetail());
            productItem.getChildren().addAll(productDetail);
            items.add(productItem);
        }
        Label selectedProductOriginalPrice=new Label();
        Label selectedProductCurrentPrice=new Label();
        Label quantityLabel = new Label("数量: 1");
        Button decreaseButton = new Button("-");
        Button increaseButton = new Button("+");
        // 设置按钮样式为圆形
        decreaseButton.setStyle("-fx-background-radius: 50%; -fx-min-width: 15px; -fx-min-height: 15px;");
        increaseButton.setStyle("-fx-background-radius: 50%; -fx-min-width: 15px; -fx-min-height: 15px;");
        Button confirmButton = new Button("加入购物车");

        HBox quantityBox = new HBox(decreaseButton, quantityLabel, increaseButton);

        Button backButton=new Button("返回");
        backButton.setOnAction(e->{
            try {
                borderPane.setCenter(showProductDetails(product));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        buyproductBox.getChildren().addAll(common,selectedProductOriginalPrice,selectedProductCurrentPrice,scrollPane,quantityBox,confirmButton,backButton);

        commonproductList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = commonproductList.getSelectionModel().getSelectedIndex();
                ShoppingProduct.oneProduct selectedProduct = commonproducts[selectedIndex];
                selectedProductOriginalPrice.setText("商品原价: " + selectedProduct.getProductOriginalPrice());
                selectedProductCurrentPrice.setText("商品现价: " + selectedProduct.getProductCurrentPrice());
                quantityLabel.setText("数量: 1"); // 重置数量
            }
        });
        decreaseButton.setOnAction(event -> {
            int quantity = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
            if (quantity > 1) {
                quantity--;
                quantityLabel.setText("数量: " + quantity);
            }
        });

        increaseButton.setOnAction(event -> {
            int quantity = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
            quantity++;
            quantityLabel.setText("数量: " + quantity);
        });

        confirmButton.setOnAction(event -> {
            int selectedIndex = commonproductList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ShoppingProduct.oneProduct selectedProduct = commonproducts[selectedIndex];
                int quantity = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
                boolean bool;
                try {
                    bool = ShoppingCart.AddToCart(user.getUsername(), selectedProduct.getProductID(), quantity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (bool) {
                    // 添加成功，弹出提示框
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText(null);
                    alert.setContentText("添加成功！");
                    alert.showAndWait();
                    try {
                        borderPane.setCenter(new VBox(showProductDetails(product)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    // 添加失败，弹出提示框
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("提示");
                    alert.setHeaderText(null);
                    alert.setContentText("添加失败！");
                    alert.showAndWait();
                    try {
                        borderPane.setCenter(new VBox(showProductDetails(product)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        borderPane.setCenter(buyproductBox);
    }
    //商品购买页
    private void buyproduct(ShoppingProduct.oneProduct product) throws IOException {
        VBox buyproductBox=new VBox();
        Label common=new Label("同类商品");
        ListView<VBox> commonproductList = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();
        commonproductList.setItems(items);
        ScrollPane scrollPane = new ScrollPane(commonproductList);
        scrollPane.setFitToWidth(true);
        ShoppingProduct shoppingProduct = new ShoppingProduct();
        ShoppingProduct.oneProduct[] commonproducts = shoppingProduct.getSameCategoryProducts(product.getProductID());
        items.clear();
        for (ShoppingProduct.oneProduct commonproduct : commonproducts)
        {
            VBox productItem=new VBox();
            Label productDetail=new Label(commonproduct.getProductDetail());
            productItem.getChildren().addAll(productDetail);
            items.add(productItem);
        }
        Label selectedProductOriginalPrice=new Label();
        Label selectedProductCurrentPrice=new Label();
        Label quantityLabel = new Label("数量: 1");
        Button decreaseButton = new Button("-");
        Button increaseButton = new Button("+");
        // 设置按钮样式为圆形
        decreaseButton.setStyle("-fx-background-radius: 50%; -fx-min-width: 15px; -fx-min-height: 15px;");
        increaseButton.setStyle("-fx-background-radius: 50%; -fx-min-width: 15px; -fx-min-height: 15px;");
        Button confirmButton = new Button("确认购买");

        HBox quantityBox = new HBox(decreaseButton, quantityLabel, increaseButton);

        ShoppingUser.oneUser currentUser = ShoppingUser.getUserDetails(user.getUsername());
        ComboBox<String> infoComboBox = new ComboBox<>();
        for (int i = 0; i < currentUser.getAddresses().length; i++) {
            String address = currentUser.getAddresses()[i];
            String telephone = currentUser.getTelephones()[i];
            infoComboBox.getItems().add("地址: " + address + " 电话: " + telephone);
        }
        infoComboBox.setPromptText("选择收货信息");
        // 默认选择第一个收货信息
        if (!infoComboBox.getItems().isEmpty()) {
            infoComboBox.getSelectionModel().selectFirst();
        }
        Button backButton=new Button("返回");
        backButton.setOnAction(e->{
            try {
                borderPane.setCenter(showProductDetails(product));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buyproductBox.getChildren().addAll(common,selectedProductOriginalPrice,selectedProductCurrentPrice,scrollPane,quantityBox,infoComboBox,confirmButton,backButton);

        commonproductList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = commonproductList.getSelectionModel().getSelectedIndex();
                ShoppingProduct.oneProduct selectedProduct = commonproducts[selectedIndex];
                selectedProductOriginalPrice.setText("商品原价: " + selectedProduct.getProductOriginalPrice());
                selectedProductCurrentPrice.setText("商品现价: " + selectedProduct.getProductCurrentPrice());
                quantityLabel.setText("数量: 1"); // 重置数量
            }
        });
        decreaseButton.setOnAction(event -> {
            int quantity = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
            if (quantity > 1) {
                quantity--;
                quantityLabel.setText("数量: " + quantity);
            }
        });

        increaseButton.setOnAction(event -> {
            int quantity = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
            quantity++;
            quantityLabel.setText("数量: " + quantity);
        });

        confirmButton.setOnAction(event -> {
            int selectedIndex = commonproductList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ShoppingProduct.oneProduct selectedProduct = commonproducts[selectedIndex];
                int quantity = Integer.parseInt(quantityLabel.getText().split(": ")[1]);
                try {
                    String orderid = createOrder(user.getUsername(), product.getProductID(), product.getProductName(), quantity, quantity * product.getProductCurrentPrice());
                    List<String> orderIds = new ArrayList<>();
                    orderIds.add(orderid);
                    float totalAmount = quantity * product.getProductCurrentPrice();
                    boolean result = handlePayment(orderIds, totalAmount);
                    if (result)
                    {
                        String selectedInfo = infoComboBox.getSelectionModel().getSelectedItem();
                        String address = null;
                        if (selectedInfo != null) {
                            // 假设格式为 "地址: xxx 电话: xxx"
                            String[] parts = selectedInfo.split(" 电话: ");
                            address = parts[0].replace("地址: ", "");
                            System.out.println("选中的地址: " + address);
                        }
                        ShoppingMap.addMapRecord(product.getProductID(), product.getProductAddress(), address);
                        borderPane.setCenter(new VBox(getShopLayout()));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        borderPane.setCenter(buyproductBox);
    }
    //商店商品页
    private VBox showShoppingStore(ShoppingProduct.oneProduct product) throws IOException {
        VBox shoppingStore=new VBox();
        ShoppingStore.oneStore oneStore=ShoppingStore.oneStore(product.getStoreID());
        VBox store=new VBox();
        Label storename=new Label("商店名称: "+oneStore.getStoreName());
        Label storeRate=new Label("商店好评率: "+oneStore.getStoreRate());
        Label storePhone=new Label("联系电话: "+oneStore.getStorePhone());
        store.getChildren().addAll(storename,storeRate,storePhone);

//        ListView<VBox> commentList = new ListView<>();
//        ObservableList<VBox> items = FXCollections.observableArrayList();
//        commentList.setItems(items);
//        ScrollPane scrollPane = new ScrollPane(commentList);
//        scrollPane.setFitToWidth(true);

        return shoppingStore;


    }
    //评论页
    private void showComment(ShoppingProduct.oneProduct product) throws IOException {

        VBox commentBox=new VBox();

        HBox select=new HBox();
        Button backButton=new Button("返回");
        backButton.setOnAction(e->{
            try {
                borderPane.setCenter(new VBox(showProductDetails(product)));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ComboBox<String> selectByComboBox = new ComboBox<>();
        selectByComboBox.setItems(FXCollections.observableArrayList("查看全部评论","差评","中评","好评"));
        selectByComboBox.setPromptText("选择评论态度");
        Button selectButton=new Button("查看");
        select.getChildren().addAll(backButton,selectByComboBox,selectButton);
        ListView<VBox> commentList = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();
        commentList.setItems(items);
        ScrollPane scrollPane = new ScrollPane(commentList);
        scrollPane.setFitToWidth(true);

        selectButton.setOnAction(e->{
            ShoppingComment shoppingComment=new ShoppingComment();
            String attitude=null;
            String Attitude=selectByComboBox.getValue();
            if(Attitude==null)
                attitude="0";
            if(Attitude=="查看全部评论")
                attitude="0";
            if(Attitude=="差评")
                attitude="1";
            if(Attitude=="中评")
                attitude="2";
            if(Attitude=="好评")
                attitude="3";
            ShoppingComment.oneComment[]comments= null;
            try {
                comments = shoppingComment.getProductComments(product.getProductID(), Integer.parseInt(attitude));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            items.clear();
            for (ShoppingComment.oneComment comment : comments)
            {
                VBox commentItem=new VBox();
                Label username=new Label("用户账号: "+comment.getUsername());
                Label commentAttitude=new Label("评论态度: "+comment.getCommentAttitude());
                Label commentContent=new Label("评论内容: "+comment.getCommentContent());
                commentItem.getChildren().addAll(username,commentAttitude,commentContent);
                items.add(commentItem);
            }
        });
        commentBox.getChildren().addAll(select,scrollPane);
        borderPane.setCenter(commentBox);
    }
    private boolean handlePayment(List<String> orderIds, float totalAmount) {
        // 弹出窗口输入用户名和密码
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("支付");
        dialog.setHeaderText("请输入用户名和密码");

        ButtonType loginButtonType = new ButtonType("确认", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Label usernameLabel = new Label("用户名:");
        usernameLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式

        Label passwordLabel = new Label("密码:");
        passwordLabel.getStyleClass().add("body-font"); // 应用CSS中的正文字体样式

        TextField username = new TextField();
        username.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        username.setPromptText("用户名");
        PasswordField password = new PasswordField();
        password.getStyleClass().add("input-field"); // 应用CSS中的输入框样式
        password.setPromptText("密码");

        grid.add(usernameLabel, 0, 0);
        grid.add(username, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        // 加载外部CSS
        dialog.getDialogPane().getStylesheets().add("main-styles.css");

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()) {
            final String[] credentials = {result.get().getKey(), result.get().getValue()};
            CountDownLatch latch = new CountDownLatch(1);
            final boolean[] paymentSuccess = {false};

            // 线程1：调用 payOrder 方法
            new Thread(() -> {
                try {
                    ShoppingOrder.payOrder(orderIds.toArray(new String[0]), totalAmount);
                    latch.await(); // 等待 payment 线程完成
                    // 继续处理 payOrder
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("提示");
                        alert.setHeaderText(null);
                        alert.setContentText("订单处理完成！");
                        alert.showAndWait();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("提示");
                        alert.setHeaderText(null);
                        alert.setContentText("订单请求失败。");
                        alert.showAndWait();
                    });
                }
            }).start();

            // 延迟一段时间后启动 payment 线程
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // 延迟1秒
                    boolean paymentResult = payment(credentials[0], credentials[1], orderIds.get(0), totalAmount);
                    if (paymentResult) {
                        paymentSuccess[0] = true;
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("提示");
                            alert.setHeaderText(null);
                            alert.setContentText("支付成功！");
                            alert.showAndWait();
                        });
                    } else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("提示");
                            alert.setHeaderText(null);
                            alert.setContentText("支付失败");
                            alert.showAndWait();
                        });
                    }
                    latch.countDown(); // 通知 payOrder 线程
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // 等待所有线程完成
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return paymentSuccess[0];
        }
        return false;
    }
}
