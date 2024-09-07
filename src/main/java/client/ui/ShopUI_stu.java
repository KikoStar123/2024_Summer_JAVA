package client.ui;

import client.service.ShoppingComment;
import client.service.ShoppingProduct;
import client.service.ShoppingStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.IOException;

import static client.ui.MainUI.borderPane;

public class ShopUI_stu {

    public VBox getShopLayout() {
        VBox shopLayout = new VBox();

        // 创建顶部按钮
        HBox topMenu = new HBox();
        Button btnProducts = new Button("商品");
        Button btnCart = new Button("购物车");
        Button btnOrders = new Button("订单");
        topMenu.getChildren().addAll(btnProducts, btnCart, btnOrders);

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
        HBox buyBox=new HBox();
        Button buyButton=new Button("直接购买");
        Button addButton=new Button("加入购物车");
        buyBox.getChildren().addAll(buyButton,addButton);
        Button backButton=new Button("返回");
        backButton.setOnAction(e->{
            borderPane.setCenter(new VBox(getShopLayout()));
        });
        detailLayout.getChildren().addAll(lblName, lblProductdetail, lblOriginalPrice, lblCurrentPrice,lblInventory,rateButton,lblAddress, shopperBox,buyBox,backButton);

        return detailLayout;

    }

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
