package testclient.bank;

import client.service.Bank;
public class TestWithdraw {

    public static void main(String[] args) {
        Bank client = new Bank();
        boolean success = client.withdraw("200000001", 50.0f); // 假设用户名为testuser，取款金额为50.0

        if (success) {
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Failed to withdraw.");
        }
    }

}