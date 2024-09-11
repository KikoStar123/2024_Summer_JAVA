package testclient.user;

import client.service.ClientService;

public class GetEmailByUsernameTest {

    public static void main(String[] args) {
        ClientService client = new ClientService();
        String email = client.getEmailByUsername("201");

        if (email != null) {
            System.out.println("Email retrieved successfully: " + email);
        } else {
            System.out.println("Failed to retrieve email.");
        }
    }
}
