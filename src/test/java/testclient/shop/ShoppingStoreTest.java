package testclient.shop;

import client.service.ShoppingStore;
import client.service.ShoppingStore.oneStore;

import java.io.IOException;
import java.util.Scanner;

public class ShoppingStoreTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShoppingStore shoppingStore = new ShoppingStore();

        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 添加商店");
            System.out.println("2. 更新商店信息");
            System.out.println("3. 删除商店");
            System.out.println("4. 获取商店详情");
            System.out.println("5. 获取所有商店信息");
            System.out.println("6. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 处理换行符

            try {
                switch (choice) {
                    case 1: // 添加商店
                        System.out.println("请输入商店ID:");
                        String addStoreID = scanner.nextLine();
                        System.out.println("请输入商店名称:");
                        String addStoreName = scanner.nextLine();
                        System.out.println("请输入商店电话:");
                        String addStorePhone = scanner.nextLine();
                        System.out.println("请输入商店好评率:");
                        float addStoreRate = scanner.nextFloat();
                        System.out.println("请输入商店状态 (true/false):");
                        boolean addStoreStatus = scanner.nextBoolean();
                        boolean addStoreSuccess = shoppingStore.addStore(addStoreID, addStoreName, addStorePhone, addStoreRate, addStoreStatus);
                        System.out.println("添加商店" + (addStoreSuccess ? "成功" : "失败"));
                        break;

                    case 2: // 更新商店信息
                        System.out.println("请输入商店ID:");
                        String updateStoreID = scanner.nextLine();
                        System.out.println("请输入商店名称:");
                        String updateStoreName = scanner.nextLine();
                        System.out.println("请输入商店电话:");
                        String updateStorePhone = scanner.nextLine();
                        System.out.println("请输入商店好评率:");
                        float updateStoreRate = scanner.nextFloat();
                        System.out.println("请输入商店状态 (true/false):");
                        boolean updateStoreStatus = scanner.nextBoolean();
                        boolean updateStoreSuccess = shoppingStore.updateStore(updateStoreID, updateStoreName, updateStorePhone, updateStoreRate, updateStoreStatus);
                        System.out.println("更新商店信息" + (updateStoreSuccess ? "成功" : "失败"));
                        break;

                    case 3: // 删除商店
                        System.out.println("请输入商店ID:");
                        String deleteStoreID = scanner.nextLine();
                        boolean deleteStoreSuccess = shoppingStore.deleteStore(deleteStoreID);
                        System.out.println("删除商店" + (deleteStoreSuccess ? "成功" : "失败"));
                        break;

                    case 4: // 获取商店详情
                        System.out.println("请输入商店ID:");
                        String getStoreID = scanner.nextLine();
                        oneStore store = shoppingStore.oneStore(getStoreID);
                        if (store != null) {
                            System.out.println("商店详情:");
                            System.out.println("商店ID: " + store.getStoreID());
                            System.out.println("商店名称: " + store.getStoreName());
                            System.out.println("商店电话: " + store.getStorePhone());
                            System.out.println("商店好评率: " + store.getStoreRate());
                            System.out.println("商店状态: " + store.getStoreStatus());
                        } else {
                            System.out.println("无法获取商店详情");
                        }
                        break;

                    case 5: // 获取所有商店信息
                        oneStore[] stores = shoppingStore.getAllStores();
                        if (stores != null) {
                            System.out.println("所有商店信息:");
                            for (oneStore s : stores) {
                                System.out.println("商店ID: " + s.getStoreID() + " 名称: " + s.getStoreName() + " 电话: " + s.getStorePhone() + " 好评率: " + s.getStoreRate() + " 状态: " + s.getStoreStatus());
                            }
                        } else {
                            System.out.println("无法获取所有商店信息");
                        }
                        break;

                    case 6: // 退出程序
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
