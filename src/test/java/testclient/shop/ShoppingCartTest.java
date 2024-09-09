package testclient.shop;

import client.service.ShoppingCart;
import client.service.ShoppingCart.oneCartElement;

import java.io.IOException;
import java.util.Scanner;

public class ShoppingCartTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShoppingCart shoppingCart = new ShoppingCart();

        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 添加商品到购物车");
            System.out.println("2. 从购物车删除商品");
            System.out.println("3. 更新购物车商品数量");
            System.out.println("4. 查看购物车");
            System.out.println("5. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 处理换行符

            try {
                switch (choice) {
                    case 1: // 添加商品到购物车
                        System.out.println("请输入用户名:");
                        String addUsername = scanner.nextLine();
                        System.out.println("请输入商品ID:");
                        String addProductID = scanner.nextLine();
                        System.out.println("请输入商品数量:");
                        int addQuantity = scanner.nextInt();
                        boolean addToCartSuccess = shoppingCart.AddToCart(addUsername, addProductID, addQuantity);
                        System.out.println("添加到购物车" + (addToCartSuccess ? "成功" : "失败"));
                        break;

                    case 2: // 从购物车删除商品
                        System.out.println("请输入用户名:");
                        String removeUsername = scanner.nextLine();
                        System.out.println("请输入商品ID:");
                        String removeProductID = scanner.nextLine();
                        boolean removeFromCartSuccess = shoppingCart.removeFromCart(removeUsername, removeProductID);
                        System.out.println("从购物车删除商品" + (removeFromCartSuccess ? "成功" : "失败"));
                        break;

                    case 3: // 更新购物车商品数量
                        System.out.println("请输入用户名:");
                        String updateUsername = scanner.nextLine();
                        System.out.println("请输入商品ID:");
                        String updateProductID = scanner.nextLine();
                        System.out.println("请输入新数量:");
                        int updateQuantity = scanner.nextInt();
                        boolean updateCartSuccess = shoppingCart.updateCart(updateUsername, updateProductID, updateQuantity);
                        System.out.println("更新购物车" + (updateCartSuccess ? "成功" : "失败"));
                        break;

                    case 4: // 查看购物车
                        System.out.println("请输入用户名:");
                        String viewUsername = scanner.nextLine();
                        oneCartElement[] cartItems = shoppingCart.getShoppingCart(viewUsername);
                        if (cartItems != null) {
                            System.out.println("购物车内容:");
                            for (oneCartElement item : cartItems) {
                                System.out.println("商品ID: " + item.getProductID() + " 数量: " + item.getProductNumber());
                            }
                        } else {
                            System.out.println("无法获取购物车内容");
                        }
                        break;

                    case 5: // 退出程序
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
