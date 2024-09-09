package testclient.shop;

import client.service.ShoppingOrder;

import java.io.IOException;

public class TestShopPay {
    public static void main(String[] args) throws IOException {
        ShoppingOrder orderService = new ShoppingOrder();

        String orderID1 = orderService.createOrder("200000001", "123456", "laptop", 2, 1200.5f);
        System.out.println("orderID: " + orderID1);

        String orderID2 = orderService.createOrder("200000001", "123457", "laptop1", 2, 1200.5f);
        System.out.println("orderID: " + orderID2);

        // 创建订单ID数组
        String[] orderIDs = {orderID1, orderID2};

        // 支付订单
        System.out.println("pay status: " + (orderService.payOrder(orderIDs, 2401.0f) ? "支付成功" : "支付失败"));
    }

}
