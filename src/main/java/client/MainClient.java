package client;

import client.ui.Bankui_Manager;
import client.ui.LoginUI;
import client.ui.ShopUI_Manager;
import client.ui.UpdatePwdUI;


public class MainClient {
    public static void main(String[] args) {
        if (args.length > 0) {
            LoginUI.setInstanceName(args[0]);
        }
        //LoginUI.main(args);
        Bankui_Manager.main(args);
    }
}
