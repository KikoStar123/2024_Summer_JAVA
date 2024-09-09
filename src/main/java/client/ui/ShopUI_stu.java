package client.ui;

import client.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.IOException;

import static client.service.ShoppingOrder.createOrder;
import static client.ui.MainUI.borderPane;

public class ShopUI_stu {
    User user;
    ShopUI_stu(User user)
    {
        this.user=user;
    }
    public VBox getShopLayout() {
        VBox shopLayout = new VBox();

        // 创建顶部按钮
        HBox topMenu = new HBox();
        Button btnProducts = new Button("商品");
        Button btnCart = new Button("购物车");
        Button btnOrders = new Button("订单");
        topMenu.getChildren().addAll(btnProducts, btnCart, btnOrders);
        btnCart.setOnAction(e-> {
            try {
                showShoppingCart(user);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnOrders.setOnAction(e-> {
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
        ComboBox<String> sortByComboBox = new ComboBox<>();
        sortByComboBox.setItems(FXCollections.observableArrayList("price", "rate"));
        sortByComboBox.setPromptText("排序方式");
        ComboBox<String> sortOrderComboBox = new ComboBox<>();
        sortOrderComboBox.setItems(FXCollections.observableArrayList("ASC", "DESC"));
        sortOrderComboBox.setPromptText("排序顺序");
        Button searchButton = new Button("搜索");
        searchBox.getChildren().addAll(searchField, sortByComboBox, sortOrderComboBox, searchButton);

        // 创建商品列表
        ListView<HBox> productList = new ListView<>();
        ObservableList<HBox> items = FXCollections.observableArrayList();
        productList.setItems(items);

        // 将商品列表放入滚动面板
        ScrollPane scrollPane = new ScrollPane(productList);
        scrollPane.setFitToWidth(true);

        shopLayout.getChildren().addAll(topMenu, searchBox, scrollPane);

        // 搜索按钮点击事件
        searchButton.setOnAction(e -> {
            String searchTerm = searchField.getText();
            String sortBy = sortByComboBox.getValue();
            String sortOrder = sortOrderComboBox.getValue();

            try {
                ShoppingProduct shoppingProduct = new ShoppingProduct();
                ShoppingProduct.oneProduct[] products = shoppingProduct.searchProducts(searchTerm, sortBy, sortOrder);
                items.clear();
                for (ShoppingProduct.oneProduct product : products) {
                    HBox productItem = new HBox();
                    //ImageView productImage = new ImageView(new Image(product.productImage));
                    //productImage.setFitWidth(100);
                    //productImage.setFitHeight(100);
                    VBox productDetails = new VBox();
                    Label lblName = new Label("名称: " + product.getProductName());
                    Label lblRating = new Label("好评率: " + product.getProductCommentRate()*100 + "%");
                    Label lblOriginalPrice = new Label("原价: ¥" + product.getProductOriginalPrice());
                    Label lblCurrentPrice = new Label("现价: ¥" + product.getProductCurrentPrice());
                    Label lblSeller = new Label("商家名称: " + product.getStoreName());
                    productDetails.getChildren().addAll(lblName, lblRating, lblOriginalPrice, lblCurrentPrice, lblSeller);
                    productItem.getChildren().addAll(productDetails);
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
                            ;
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
        ListView<VBox> orderList = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();
        orderList.setItems(items);
        ScrollPane scrollPane = new ScrollPane(orderList);
        scrollPane.setFitToWidth(true);
        ShoppingOrder shoppingOrder=new ShoppingOrder();
        ShoppingOrder.oneOrder[] Orders=shoppingOrder.getAllOrdersByUser(user.getUsername());
        items.clear();
        for (ShoppingOrder.oneOrder order:Orders)
        {
            VBox orderbox=new VBox();
            Label orderid=new Label("订单id: "+order.getOrderID());
            Label productname=new Label("商品名称: "+order.productName());
            Label paidMoney=new Label("支付金额: "+order.getPaidMoney());
            Button commentbutton=new Button("评论");
            Button paybutton=new Button("支付");
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
            orderbox.getChildren().addAll(orderid,productname,paidMoney,commentbutton,paybutton);
            items.add(orderbox);
            paybutton.setOnAction(e-> {
                boolean result;
                try {
                    result = ShoppingOrder.payOrder(order.getOrderID(), order.getPaidMoney());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (result)
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText(null);
                    alert.setContentText("支付成功！");
                    alert.showAndWait();
                    try {
                        borderPane.setCenter(new VBox(showOrders()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText(null);
                    alert.setContentText("支付失败！");
                    alert.showAndWait();
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
                Button backButton = new Button("返回");
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
            });
        }
        VBox ordersBox=new VBox();
        ordersBox.getChildren().addAll(scrollPane);
        Button backButton=new Button("返回");
        backButton.setOnAction(e->{
            borderPane.setCenter(new VBox(getShopLayout()));
        });
        borderPane.setCenter(ordersBox);
        return ordersBox;

    }
    //购物车页
    private void showShoppingCart(User user) throws IOException {
        ListView<VBox> cartelementList = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();
        cartelementList.setItems(items);
        ScrollPane scrollPane = new ScrollPane(cartelementList);
        scrollPane.setFitToWidth(true);
        ShoppingCart shoppingCart=new ShoppingCart();
        ShoppingCart.oneCartElement[] cartElements=shoppingCart.getShoppingCart(user.getUsername());
        items.clear();
        Label totalPriceLabel = new Label("总价格: 0.0");
        Button buybutton=new Button("购买");
        double[] totalPrice = {0.0};
        if(cartElements==null)
        {
        }
        else
        {
            for (ShoppingCart.oneCartElement cartElement:cartElements)
            {

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
                buybutton.setOnAction(e->{
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
                            Label quantityLabelb = (Label)((HBox) cartBox.getChildren().get(1)).getChildren().get(1);
                            Label paymoney=(Label) ((HBox) cartBox.getChildren().get(2)).getChildren().get(0);
                            String productName = productnameb.getText().replace("商品名称: ", "");
                            String productId = productIdd.getText().replace("商品id: ", "");
                            int quantityb = Integer.parseInt(quantityLabelb.getText().replace("数量: ", ""));
                            float paymoneyb= Float.parseFloat(paymoney.getText().replace("商品总价: ",""));
                            // 调用创建订单的函数
                            try {
                                String orderid=createOrder(user.getUsername(), productId,productName, quantityb,paymoneyb);
                                boolean result = ShoppingOrder.payOrder(orderid,paymoneyb);
                                if (result)
                                {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("提示");
                                    alert.setHeaderText(null);
                                    alert.setContentText("商品"+orderid+"支付成功！");
                                    alert.showAndWait();
                                }
                                else{
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("提示");
                                    alert.setHeaderText(null);
                                    alert.setContentText("商品"+orderid+"支付失败！");
                                    alert.showAndWait();
                                }

                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });
                HBox boxa=new HBox(selectBox,productname,productdetail,productid);
                HBox boxb=new HBox(sumprice,deleteButton);

                cart.getChildren().addAll(boxa, quantityBox, boxb);
                items.add(cart);
            }
        }

        HBox bottom=new HBox(totalPriceLabel,buybutton);
        VBox layout = new VBox();
        layout.getChildren().addAll(scrollPane, bottom);
        borderPane.setCenter(layout);
    }
    //进入商品详情页
    private VBox showProductDetails(ShoppingProduct.oneProduct product) throws IOException {
        VBox detailLayout = new VBox();
        Label lblName = new Label("商品名称: " + product.getProductName());
        Label lblProductdetail=new Label("商品属性: "+product.getProductDetail());
        Label lblOriginalPrice = new Label("原价: ¥" + product.getProductOriginalPrice());
        Label lblCurrentPrice = new Label("现价: ¥" + product.getProductCurrentPrice());
        Label lblInventory=new Label("商品库存: "+product.getProductInventory());
        Button rateButton=new Button("商品评价");
        rateButton.setOnAction(e-> {
            try {
                showComment(product);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Label lblAddress=new Label("商品发货地址: "+product.getProductAddress());
        ShoppingStore.oneStore oneStore=ShoppingStore.oneStore(product.getStoreID());
        VBox shopperBox=new VBox();
        Label lblSeller = new Label("商家名称: " + product.getStoreName());
        Label lblSellerrating=new Label("商家好评率: "+oneStore.getStoreRate()*100+"%");
        shopperBox.getChildren().addAll(lblSeller,lblSellerrating);
        shopperBox.setOnMouseClicked(event->{
            if (event.getClickCount() == 2) {
                // 显示商店详情信息
                try {
                    borderPane.setCenter(new VBox(showShoppingStore(product)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        HBox buyBox=new HBox();
        Button buyButton=new Button("直接购买");
        //点击购买进入购买页
        buyButton.setOnAction(e-> {
            try {
                buyproduct(product);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        //加入购物车
        Button addButton=new Button("加入购物车");
        addButton.setOnAction(e-> {
            try {
                addproduct(product);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buyBox.getChildren().addAll(buyButton,addButton);
        Button backButton=new Button("返回");
        backButton.setOnAction(e->{
            borderPane.setCenter(new VBox(getShopLayout()));
        });
        detailLayout.getChildren().addAll(lblName, lblProductdetail, lblOriginalPrice, lblCurrentPrice,lblInventory,rateButton,lblAddress, shopperBox,buyBox,backButton);

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
            Label productDetail=new Label("商品属性"+commonproduct.getProductDetail());
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

        buyproductBox.getChildren().addAll(common,selectedProductOriginalPrice,selectedProductCurrentPrice,scrollPane,quantityBox,confirmButton);

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
            Label productDetail=new Label("商品属性"+commonproduct.getProductDetail());
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

        buyproductBox.getChildren().addAll(common,selectedProductOriginalPrice,selectedProductCurrentPrice,scrollPane,quantityBox,confirmButton);

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
                    String orderid= createOrder(user.getUsername(), product.getProductID(), product.getProductName(), quantity,quantity* product.getProductCurrentPrice());
                    boolean result = ShoppingOrder.payOrder(orderid,quantity* product.getProductCurrentPrice());
                    if (result)
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("提示");
                        alert.setHeaderText(null);
                        alert.setContentText("支付成功！");
                        alert.showAndWait();
                        borderPane.setCenter(new VBox(getShopLayout()));
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("提示");
                        alert.setHeaderText(null);
                        alert.setContentText("支付失败！");
                        alert.showAndWait();
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
        selectByComboBox.setItems(FXCollections.observableArrayList("0","1","2","3"));
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
            String attitude = selectByComboBox.getValue();
            if(attitude == null)
            {
                attitude="0";
            }
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
}