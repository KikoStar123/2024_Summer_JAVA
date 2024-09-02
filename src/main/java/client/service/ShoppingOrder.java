package client.service;

public class ShoppingOrder {
    private final String SERVER_ADDRESS = "localhost";//服务器的地址 即本地服务器
    private final int SERVER_PORT = 8080;//定义服务器的端口号

    public class oneProduct
    {
        String productID;//商品id
        String productName;//商品名称
        String productDetail;//商品属性（string）
        String productImage;//商品图片
        float productOriginalPrice;//商品原价
        float productCurrentPrice;//商品现价
        int productInventory;//商品库存
        String productAddress;//商品发货地址
        float productCommentRate;//商品好评率
        boolean productStatus;//商品状态
    }
}
