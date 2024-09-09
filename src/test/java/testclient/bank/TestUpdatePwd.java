package testclient.bank;

import client.service.Bank;

public class TestUpdatePwd {
    public static void main(String[] args) {
        Bank client = new Bank();
        boolean success = client.updatePwd("200000001", "newPassword123", "password123"); // 假设用户名为200000001，旧密码为oldPassword123，新密码为newPassword123

        if (success) {
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Failed to update password.");
        }
    }
}
