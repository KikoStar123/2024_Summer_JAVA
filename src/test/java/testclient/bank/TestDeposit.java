package testclient.bank;

import client.service.Bank;
public class TestDeposit {

    public static void main(String[] args) {
        Bank client = new Bank();
        boolean success = client.deposit("200000001", 100.0f); // 假设用户名为testuser，存款金额为100.0

        if (success) {
            System.out.println("Deposit successful.");
        } else {
            System.out.println("Failed to deposit.");
        }
    }
}
