package testclient.bank;

import client.service.Bank;
public class TestBankRegister {
    public static void main(String[] args) {
        Bank client = new Bank();
        boolean success = client.bankRegister("200000001", "password123"); // 假设用户名为newuser，密码为newpassword

        if (success) {
            System.out.println("Registration successful.");
        } else {
            System.out.println("Failed to register.");
        }
    }
}
