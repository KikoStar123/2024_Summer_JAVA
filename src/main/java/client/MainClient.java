package client;

import client.ui.LoginUI;

public class MainClient {
    public static void main(String[] args) {
        if (args.length > 0) {
            LoginUI.setInstanceName(args[0]);
        }
        LoginUI.main(args);
    }
}
