package testclient.bank;

import client.service.Bank;
public class TestBankLogin {
    public static void main(String[] args) {
        Bank client = new Bank();
        boolean success = client.bankLogin("200000001", "password123"); // 假设用户名为testuser，密码为testpassword

        if (success) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Failed to login.");
        }
    }
}
