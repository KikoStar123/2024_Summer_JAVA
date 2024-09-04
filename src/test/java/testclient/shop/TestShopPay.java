package testclient.shop;

import client.service.ShoppingOrder;

import java.io.IOException;

public class TestShopPay {
    public static void main(String[] args) throws IOException {
        ShoppingOrder orderService = new ShoppingOrder();

        String orderID = orderService.createOrder("200000001", "123456", 2, 120.5f);

        System.out.println("orderID: " + orderID);

        order.payOrder(orderID);
    }
}
