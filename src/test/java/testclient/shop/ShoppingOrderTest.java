package testclient.shop;

import client.service.ShoppingOrder;
import client.service.ShoppingProduct;

import java.io.IOException;
import java.util.Scanner;

public class ShoppingOrderTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShoppingOrder shoppingOrder = new ShoppingOrder();

        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 创建订单");
            System.out.println("2. 查看订单详情");
            System.out.println("3. 查看用户所有订单");
            System.out.println("4. 按关键词搜索用户订单");
            System.out.println("5. 按关键词搜索所有订单");
            System.out.println("6. 获取订单是否评论状态");
            System.out.println("7. 更新订单评论状态");
            System.out.println("8. 支付订单");
            System.out.println("9. 查看商店所有订单");
            System.out.println("10. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 处理换行符

            try {
                switch (choice) {
                    case 1:
                        System.out.println("请输入用户账号:");
                        String username = scanner.nextLine();
                        System.out.println("请输入商品ID:");
                        String productID = scanner.nextLine();
                        System.out.println("请输入商品名称:");
                        String productName = scanner.nextLine();
                        System.out.println("请输入商品数量:");
                        int productNumber = scanner.nextInt();
                        System.out.println("请输入支付金额:");
                        float paidMoney = scanner.nextFloat();

                        String orderID = shoppingOrder.createOrder(username, productID, productName, productNumber, paidMoney);
                        System.out.println("创建订单成功，订单ID为: " + orderID);
                        break;

                    case 2:
                        System.out.println("请输入订单ID:");
                        String orderDetailsID = scanner.nextLine();
                        ShoppingOrder.oneOrder orderDetails = shoppingOrder.getOrderDetails(orderDetailsID);

                        System.out.println("订单详情:");
                        System.out.println("订单ID: " + orderDetails.getOrderID());
                        System.out.println("用户名: " + orderDetails.getUsername());
                        System.out.println("商品ID: " + orderDetails.getProductID());
                        System.out.println("商品名称: " + orderDetails.productName());
                        System.out.println("商品数量: " + orderDetails.getProductNumber());
                        System.out.println("支付金额: " + orderDetails.getPaidMoney());
                        System.out.println("是否评论: " + orderDetails.isWhetherComment());
                        System.out.println("支付状态: " + (orderDetails.getpaidStatus() ? "已支付" : "未支付"));
                        break;

                    case 3:
                        System.out.println("请输入用户账号:");
                        String allOrdersUsername = scanner.nextLine();
                        ShoppingOrder.oneOrder[] allOrders = shoppingOrder.getAllOrdersByUser(allOrdersUsername);

                        System.out.println("用户订单:");
                        for (ShoppingOrder.oneOrder order : allOrders) {
                            System.out.println("订单ID: " + order.getOrderID() + ", 商品名称: " + order.productName() + ", 支付金额: " + order.getPaidMoney());
                        }
                        break;

                    case 4:
                        System.out.println("请输入用户账号:");
                        String searchUsername = scanner.nextLine();
                        System.out.println("请输入搜索关键词:");
                        String searchTerm = scanner.nextLine();
                        ShoppingOrder.oneOrder[] searchedOrdersByUser = shoppingOrder.searchOrdersByUser(searchUsername, searchTerm);

                        System.out.println("搜索结果:");
                        for (ShoppingOrder.oneOrder order : searchedOrdersByUser) {
                            System.out.println("订单ID: " + order.getOrderID() + ", 商品名称: " + order.productName() + ", 支付金额: " + order.getPaidMoney());
                        }
                        break;

                    case 5:
                        System.out.println("请输入搜索关键词:");
                        String keyword = scanner.nextLine();
                        ShoppingOrder.oneOrder[] searchedOrders = shoppingOrder.searchOrdersByKeyword(keyword);

                        System.out.println("搜索结果:");
                        for (ShoppingOrder.oneOrder order : searchedOrders) {
                            System.out.println("订单ID: " + order.getOrderID() + ", 商品名称: " + order.productName() + ", 支付金额: " + order.getPaidMoney());
                        }
                        break;

                    case 6:
                        System.out.println("请输入订单ID:");
                        String commentStatusOrderID = scanner.nextLine();
                        boolean commentStatus = shoppingOrder.getOrderCommentStatus(commentStatusOrderID);
                        System.out.println("订单评论状态: " + (commentStatus ? "已评论" : "未评论"));
                        break;

                    case 7:
                        System.out.println("请输入订单ID:");
                        String updateCommentOrderID = scanner.nextLine();
                        System.out.println("请输入评论状态 (1: 已评论, 0: 未评论):");
                        boolean whetherComment = scanner.nextInt() == 1;
                        boolean updateCommentStatus = shoppingOrder.updateCommentStatus(updateCommentOrderID, whetherComment);
                        System.out.println("评论状态更新" + (updateCommentStatus ? "成功" : "失败"));
                        break;

                    case 8:
                        System.out.println("请输入订单ID:");
                        String payOrderID = scanner.nextLine();
                        System.out.println("请输入支付金额:");
                        float amount = scanner.nextFloat();
                        boolean payStatus = shoppingOrder.payOrder(payOrderID, amount);
                        System.out.println("支付状态: " + (payStatus ? "支付成功" : "支付失败"));
                        break;
                    case 9:
                        System.out.println("请输入商店ID:");
                        String storeID = scanner.nextLine();
                        ShoppingOrder.oneOrder[] allOrdersInStore = shoppingOrder.getAllOrdersByStore(storeID);
                        if (allOrdersInStore != null) {
                            for (ShoppingOrder.oneOrder o : allOrdersInStore) {
                                System.out.println("订单ID: " + o.getOrderID() + ", 商品名称: " + o.productName());
                            }
                        }
                        break;
                    case 10:
                        System.out.println("退出程序");
                        System.exit(0);

                    default:
                        System.out.println("无效的选择，请重新输入");
                }
            } catch (IOException e) {
                System.out.println("操作失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
