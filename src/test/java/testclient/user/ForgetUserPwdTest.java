package testclient.user;

import client.service.ClientService;

public class ForgetUserPwdTest {

    public static void main(String[] args) {
        ClientService client = new ClientService();
        boolean success = client.forgetUserPwd("201", "pwd201");

        if (success) {
            System.out.println("Password reset successfully.");
        } else {
            System.out.println("Failed to reset password.");
        }
    }
}
