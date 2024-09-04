package testclient.shop;

import client.service.ShoppingOrder;
import client.service.ShoppingOrder.oneOrder;

import java.io.IOException;
import java.util.Scanner;

public class ShoppingOrderTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShoppingOrder shoppingOrder = new ShoppingOrder();

        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 创建订单");
            System.out.println("2. 查看单个订单详情");
            System.out.println("3. 获取特定用户的所有订单");
            System.out.println("4. 搜索特定用户的订单");
            System.out.println("5. 搜索所有订单（管理员功能）");
            System.out.println("6. 获取所有订单");
            System.out.println("7. 获取订单评论状态");
            System.out.println("8. 更新订单评论状态");
            System.out.println("9. 支付订单");
            System.out.println("10. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 处理换行符

            try {
                switch (choice) {
                    case 1:
                        System.out.println("请输入用户名:");
                        String username = scanner.nextLine();
                        System.out.println("请输入商品ID:");
                        String productID = scanner.nextLine();
                        System.out.println("请输入商品数量:");
                        int productNumber = scanner.nextInt();
                        System.out.println("请输入支付金额:");
                        float paidMoney = scanner.nextFloat();

                        String orderID = shoppingOrder.createOrder(username, productID, productNumber, paidMoney);
                        System.out.println("订单创建成功，订单号为: " + orderID);
                        break;

                    case 2:
                        System.out.println("请输入订单ID:");
                        String viewOrderID = scanner.nextLine();
                        oneOrder orderDetails = shoppingOrder.getOrderDetails(viewOrderID);
                        if (orderDetails != null) {
                            System.out.println("订单ID: " + orderDetails.getOrderID());
                            System.out.println("用户名: " + orderDetails.getUsername());
                            System.out.println("商品ID: " + orderDetails.getProductID());
                            System.out.println("商品数量: " + orderDetails.getProductNumber());
                            System.out.println("是否评价: " + (orderDetails.isWhetherComment() ? "是" : "否"));
                            System.out.println("支付金额: " + orderDetails.getPaidMoney());
                            System.out.println("支付状态: " + (orderDetails.getpaidStatus() ? "已支付" : "未支付"));
                        } else {
                            System.out.println("订单详情获取失败");
                        }
                        break;

                    case 3:
                        System.out.println("请输入用户名:");
                        String userToViewOrders = scanner.nextLine();
                        oneOrder[] allOrders = shoppingOrder.getAllOrdersByUser(userToViewOrders);
                        if (allOrders != null) {
                            for (oneOrder order : allOrders) {
                                System.out.println("订单ID: " + order.getOrderID() + " 商品ID: " + order.getProductID() +
                                        " 商品数量: " + order.getProductNumber() + " 支付金额: " + order.getPaidMoney());
                            }
                        } else {
                            System.out.println("获取用户订单失败");
                        }
                        break;

                    case 4:
                        System.out.println("请输入用户名:");
                        String userToSearchOrders = scanner.nextLine();
                        System.out.println("请输入搜索关键词:");
                        String searchTerm = scanner.nextLine();
                        oneOrder[] searchedOrders = shoppingOrder.searchOrdersByUser(userToSearchOrders, searchTerm);
                        if (searchedOrders != null) {
                            for (oneOrder order : searchedOrders) {
                                System.out.println("订单ID: " + order.getOrderID() + " 商品ID: " + order.getProductID() +
                                        " 商品数量: " + order.getProductNumber() + " 支付金额: " + order.getPaidMoney());
                            }
                        } else {
                            System.out.println("搜索用户订单失败");
                        }
                        break;

                    case 5:
                        System.out.println("请输入搜索关键词:");
                        String globalSearchTerm = scanner.nextLine();
                        oneOrder[] globalSearchedOrders = shoppingOrder.searchOrdersByKeyword(globalSearchTerm);
                        if (globalSearchedOrders != null) {
                            for (oneOrder order : globalSearchedOrders) {
                                System.out.println("订单ID: " + order.getOrderID() + " 用户名: " + order.getUsername() +
                                        " 商品ID: " + order.getProductID() + " 商品数量: " + order.getProductNumber());
                            }
                        } else {
                            System.out.println("搜索所有订单失败");
                        }
                        break;

                    case 6:
                        oneOrder[] allOrdersList = shoppingOrder.getAllOrders();
                        if (allOrdersList != null) {
                            for (oneOrder order : allOrdersList) {
                                System.out.println("订单ID: " + order.getOrderID() + " 用户名: " + order.getUsername() +
                                        " 商品ID: " + order.getProductID() + " 支付金额: " + order.getPaidMoney());
                            }
                        } else {
                            System.out.println("获取所有订单失败");
                        }
                        break;

                    case 7:
                        System.out.println("请输入订单ID:");
                        String commentOrderID = scanner.nextLine();
                        boolean commentStatus = shoppingOrder.getOrderCommentStatus(commentOrderID);
                        System.out.println("订单是否评价: " + (commentStatus ? "是" : "否"));
                        break;

                    case 8:
                        System.out.println("请输入订单ID:");
                        String updateCommentOrderID = scanner.nextLine();
                        System.out.println("请输入评论状态 (true: 已评价, false: 未评价):");
                        boolean updateCommentStatus = scanner.nextBoolean();
                        boolean updateSuccess = shoppingOrder.updateCommentStatus(updateCommentOrderID, updateCommentStatus);
                        System.out.println("更新订单评论状态" + (updateSuccess ? "成功" : "失败"));
                        break;

                    case 9:
                        System.out.println("请输入订单ID:");
                        String payOrderID = scanner.nextLine();
                        System.out.println("请输入支付金额:");
                        float payAmount = scanner.nextFloat();
                        boolean paySuccess = shoppingOrder.payOrder(payOrderID, payAmount);
                        System.out.println("订单支付" + (paySuccess ? "成功" : "失败"));
                        break;

                    case 10:
                        System.out.println("退出程序");
                        System.exit(0);
                        break;

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
