package testclient.shop;

import client.service.ShoppingOrder;

import java.io.IOException;

public class TestShopProduct {
    public static void main(String[] args) throws IOException {
        ShoppingOrder orderService = new ShoppingOrder();

        String orderID = orderService.createOrder("200000001", "123456", "laptop", 2,1200.5f);

        System.out.println("orderID: " + orderID);

        System.out.println("pay status: " + orderService.payOrder(orderID, 1200.5f));
    }
}
