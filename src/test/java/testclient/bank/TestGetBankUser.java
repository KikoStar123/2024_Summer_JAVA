package testclient.bank;

import client.service.Bank;
import client.service.BankUser;

public class TestGetBankUser {
    public static void main(String[] args) {
        Bank client = new Bank();
        BankUser user = client.getBankUser("200000001", "password123"); // 假设用户名为200000001，密码为password123

        if (user != null) {
            System.out.println("User found:");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Balance: " + user.getBalance());
            System.out.println("Bank Password: " + user.getBankpwd());
        } else {
            System.out.println("User not found or invalid credentials.");
        }
    }
}
