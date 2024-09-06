package testclient.shop;

import client.service.ShoppingComment;
import client.service.ShoppingOrder;

import java.io.IOException;
import java.util.Scanner;

public class ShoppingCommentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShoppingComment shoppingComment = new ShoppingComment();

        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 查看所有商品评论");
            System.out.println("2. 查看特定商品的评论");
            System.out.println("3. 查询某个用户对某个商品的评论");
            System.out.println("4. 添加评论");
            System.out.println("5. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 处理换行符

            try {
                switch (choice) {
                    case 1:
                        // 获取所有商品的评论
                        ShoppingComment.oneComment[] allComments = shoppingComment.getAllProductComments();
                        if (allComments != null) {
                            for (ShoppingComment.oneComment comment : allComments) {
                                System.out.println("用户名: " + comment.getUsername() + ", 商品ID: " + comment.getProductID() +
                                        ", 评论ID: " + comment.getCommentID() + ", 评论态度: " + comment.getCommentAttitude() +
                                        ", 评论内容: " + comment.getCommentContent());
                            }
                        } else {
                            System.out.println("获取评论失败");
                        }
                        break;

                    case 2:
                        // 根据商品ID和评论态度获取评论
                        System.out.println("请输入商品ID:");
                        String productID = scanner.nextLine();
                        System.out.println("请输入评论态度（差评=1、中评=2、好评=3，查看所有评论=0）:");
                        int commentAttitude = scanner.nextInt();

                        ShoppingComment.oneComment[] productComments = shoppingComment.getProductComments(productID, commentAttitude);
                        if (productComments != null) {
                            for (ShoppingComment.oneComment comment : productComments) {
                                System.out.println("用户名: " + comment.getUsername() + ", 商品ID: " + comment.getProductID() +
                                        ", 评论ID: " + comment.getCommentID() + ", 评论态度: " + comment.getCommentAttitude() +
                                        ", 评论内容: " + comment.getCommentContent());
                            }
                        } else {
                            System.out.println("获取评论失败");
                        }
                        break;

                    case 3:
                        // 查询某个用户对某个商品的评论
                        System.out.println("请输入用户名:");
                        String username = scanner.nextLine();
                        System.out.println("请输入商品ID:");
                        String searchProductID = scanner.nextLine();

                        ShoppingComment.oneComment[] userProductComments = shoppingComment.searchProductComments(username, searchProductID);
                        if (userProductComments != null) {
                            for (ShoppingComment.oneComment comment : userProductComments) {
                                System.out.println("用户名: " + comment.getUsername() + ", 商品ID: " + comment.getProductID() +
                                        ", 评论ID: " + comment.getCommentID() + ", 评论态度: " + comment.getCommentAttitude() +
                                        ", 评论内容: " + comment.getCommentContent());
                            }
                        } else {
                            System.out.println("获取评论失败");
                        }
                        break;

                    case 4:
                        // 添加评论
                        System.out.println("请输入用户名:");
                        String addUsername = scanner.nextLine();
                        System.out.println("请输入商品ID:");
                        String addProductID = scanner.nextLine();
                        System.out.println("请输入评论态度（差评=1、中评=2、好评=3）:");
                        int addCommentAttitude = scanner.nextInt();
                        scanner.nextLine(); // 处理换行符
                        System.out.println("请输入评论内容:");
                        String commentContent = scanner.nextLine();
                        System.out.println("请输入订单ID:");
                        String orderID = scanner.nextLine();

                        boolean addCommentSuccess = shoppingComment.addComment(addUsername, addProductID, addCommentAttitude, commentContent, orderID);
                        System.out.println("添加评论" + (addCommentSuccess ? "成功" : "失败"));
                        break;

                    case 5:
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
