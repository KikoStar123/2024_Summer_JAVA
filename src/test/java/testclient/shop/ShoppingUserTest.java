package testclient.shop;

import client.service.ShoppingUser;
import java.io.IOException;
import java.util.Scanner;

public class ShoppingUserTest {
    public static void main(String[] args) {
        ShoppingUser shoppingUser = new ShoppingUser();
        Scanner scanner = new Scanner(System.in);

        // 菜单选项
        while (true) {
            System.out.println("\n===== ShoppingUser 测试程序 =====");
            System.out.println("1. 查看用户地址和电话");
            System.out.println("2. 更新用户某个地址和电话（指定索引）");
            System.out.println("3. 更新用户所有地址和电话");
            System.out.println("4. 添加新地址和电话");
            System.out.println("5. 删除某个地址和电话（指定索引）");
            System.out.println("6. 退出");
            System.out.print("请选择操作: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 处理换行符

            if (choice == 6) {
                break;
            }

            System.out.print("请输入用户名: ");
            String username = scanner.nextLine();

            switch (choice) {
                case 1:  // 查看用户地址和电话
                    try {
                        ShoppingUser.oneUser user = shoppingUser.getUserDetails(username);
                        if (user != null) {
                            System.out.println("用户的地址:");
                            for (String address : user.getAddresses()) {
                                System.out.println(address);
                            }
                            System.out.println("用户的电话:");
                            for (String phone : user.getTelephones()) {
                                System.out.println(phone);
                            }
                        } else {
                            System.out.println("未找到该用户。");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:  // 更新用户某个地址和电话（指定索引）
                    System.out.print("请输入要更新的地址索引: ");
                    int updateIndex = scanner.nextInt();
                    scanner.nextLine();  // 处理换行符
                    System.out.print("请输入新地址: ");
                    String newAddress = scanner.nextLine();
                    System.out.print("请输入新电话: ");
                    String newTelephone = scanner.nextLine();
                    try {
                        boolean success = shoppingUser.updateUserContactAtIndex(username, updateIndex, newAddress, newTelephone);
                        if (success) {
                            System.out.println("地址和电话更新成功！");
                        } else {
                            System.out.println("地址和电话更新失败。");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:  // 更新用户所有地址和电话
                    System.out.print("请输入要更新的地址数量: ");
                    int addressCount = scanner.nextInt();
                    scanner.nextLine();  // 处理换行符
                    String[] addresses = new String[addressCount];
                    String[] telephones = new String[addressCount];

                    for (int i = 0; i < addressCount; i++) {
                        System.out.print("请输入地址 " + (i + 1) + ": ");
                        addresses[i] = scanner.nextLine();
                        System.out.print("请输入电话 " + (i + 1) + ": ");
                        telephones[i] = scanner.nextLine();
                    }

                    try {
                        boolean success = shoppingUser.updateUserContacts(username, addresses, telephones);
                        if (success) {
                            System.out.println("地址和电话更新成功！");
                        } else {
                            System.out.println("地址和电话更新失败。");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:  // 添加新地址和电话
                    System.out.print("请输入新地址: ");
                    String newAddr = scanner.nextLine();
                    System.out.print("请输入新电话: ");
                    String newTel = scanner.nextLine();
                    try {
                        boolean success = shoppingUser.addUserContact(username, newAddr, newTel);
                        if (success) {
                            System.out.println("新地址和电话添加成功！");
                        } else {
                            System.out.println("添加新地址和电话失败。");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 5:  // 删除某个地址和电话（指定索引）
                    System.out.print("请输入要删除的地址索引: ");
                    int deleteIndex = scanner.nextInt();
                    scanner.nextLine();  // 处理换行符
                    try {
                        boolean success = shoppingUser.deleteUserContact(username, deleteIndex);
                        if (success) {
                            System.out.println("地址和电话删除成功！");
                        } else {
                            System.out.println("删除失败，索引无效或操作失败。");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    System.out.println("无效选项，请重试。");
                    break;
            }
        }

        System.out.println("退出程序。");
        scanner.close();
    }
}
