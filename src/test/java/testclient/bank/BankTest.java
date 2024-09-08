package testclient.bank;

import client.service.Bank;

import java.io.IOException;
import java.util.Scanner;

public class BankTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 存款");
            System.out.println("2. 取款");
            System.out.println("3. 模拟过月结算");
            System.out.println("4. 模拟过年结算");
            System.out.println("5. 修改利率");
            System.out.println("6. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 处理换行符

            switch (choice) {
                case 1:
                    System.out.println("请输入用户账号:");
                    String depositUsername = scanner.nextLine();
                    System.out.println("请输入存款金额:");
                    float depositAmount = scanner.nextFloat();
                    System.out.println("请输入存款类型 (活期/定期):");
                    String depositType = scanner.next();
                    int term = 0;
                    if (depositType.equals("定期")) {
                        System.out.println("请输入存款期限 (月):");
                        term = scanner.nextInt();
                    }

                    boolean depositSuccess = bank.deposit(depositUsername, depositAmount, depositType, term);
                    System.out.println("存款" + (depositSuccess ? "成功" : "失败"));
                    break;

                case 2:
                    System.out.println("请输入用户账号:");
                    String withdrawUsername = scanner.nextLine();
                    System.out.println("请输入取款金额:");
                    float withdrawAmount = scanner.nextFloat();

                    boolean withdrawSuccess = bank.withdraw(withdrawUsername, withdrawAmount);
                    System.out.println("取款" + (withdrawSuccess ? "成功" : "失败"));
                    break;


                case 3:
                    boolean monthEndSuccess = bank.simulateMonthEnd();
                    System.out.println("模拟过月结算" + (monthEndSuccess ? "成功" : "失败"));
                    break;

                case 4:
                    boolean yearEndSuccess = bank.simulateYearEnd();
                    System.out.println("模拟过年结算" + (yearEndSuccess ? "成功" : "失败"));
                    break;

                case 5:
                    System.out.println("请输入利率类型 (活期/定期):");
                    String rateType = scanner.next();
                    System.out.println("请输入新的利率:");
                    double newRate = scanner.nextDouble();

                    boolean updateRateSuccess = bank.updateInterestRate(rateType, newRate);
                    System.out.println("利率修改" + (updateRateSuccess ? "成功" : "失败"));
                    break;

                case 6:
                    System.out.println("退出程序");
                    System.exit(0);

                default:
                    System.out.println("无效的选择，请重新输入");
            }
        }
    }
}
