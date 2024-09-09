package testclient.shop;

import client.service.ShoppingMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class ShoppingMapTest {

    public static void main(String[] args) {
        ShoppingMap shoppingMap = new ShoppingMap();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ShoppingMap 测试程序 =====");
            System.out.println("1. 添加地图记录");
            System.out.println("2. 删除地图记录");
            System.out.println("3. 更新地图记录");
            System.out.println("4. 查看所有地图记录");
            System.out.println("5. 退出");
            System.out.print("请选择操作: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 处理换行符

            if (choice == 5) {
                break;
            }

            switch (choice) {
                case 1:  // 添加地图记录
                    System.out.print("请输入商品ID: ");
                    String productID = scanner.nextLine();
                    System.out.print("请输入起始位置: ");
                    String mapStart = scanner.nextLine();
                    System.out.print("请输入终止位置: ");
                    String mapEnd = scanner.nextLine();
                    try {
                        boolean success = shoppingMap.addMapRecord(productID, mapStart, mapEnd);
                        System.out.println(success ? "记录添加成功！" : "记录添加失败！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:  // 删除地图记录
                    System.out.print("请输入商品ID: ");
                    String deleteProductID = scanner.nextLine();
                    try {
                        boolean success = shoppingMap.deleteMapRecord(deleteProductID);
                        System.out.println(success ? "记录删除成功！" : "记录删除失败！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:  // 更新地图记录
                    System.out.print("请输入商品ID: ");
                    String updateProductID = scanner.nextLine();
                    System.out.print("请输入新的起始位置: ");
                    String newMapStart = scanner.nextLine();
                    System.out.print("请输入新的终止位置: ");
                    String newMapEnd = scanner.nextLine();
                    try {
                        boolean success = shoppingMap.updateMapRecord(updateProductID, newMapStart, newMapEnd);
                        System.out.println(success ? "记录更新成功！" : "记录更新失败！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:  // 查看所有地图记录
                    try {
                        JSONArray records = shoppingMap.getAllMapRecords();
                        System.out.println("服务端返回的数据: " + records.toString()); // 输出完整的JSON数据用于调试
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject record = records.getJSONObject(i);
                            System.out.println("商品ID: " + record.getString("productID"));
                            System.out.println("起始位置: " + record.getString("mapStart"));
                            System.out.println("终止位置: " + record.getString("mapEnd"));
                            System.out.println("------------");
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
        scanner.close();
    }
}
