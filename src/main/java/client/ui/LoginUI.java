package client.ui;

import client.service.ClientService;
import client.service.Role;
import client.service.User;
import client.ui.MainUI;
import client.ui.RegisterUI;
import javax.swing.*;

import client.service.Gender;

public class LoginUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public void display() {
        frame = new JFrame("Login");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JButton RegisterButton = new JButton("注册");
        RegisterButton.setBounds(100, 80, 80, 25);
        panel.add(RegisterButton);

        loginButton.addActionListener(e -> handleLogin());
        RegisterButton.addActionListener(e -> handleRegister());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        ClientService clientService = new ClientService();
        boolean success = clientService.login(username, password);


        if (success) {
            JOptionPane.showMessageDialog(frame, "Login successful!");
            //User user=clientService.login_return(username,password);
            //User user=new User("xiix", Role.student,12,Gender.male,"123");
//            MainUI mainUI = new MainUI(user);
//            mainUI.display();
//            frame.dispose();

        } else {
            JOptionPane.showMessageDialog(frame, "Login failed. Please try again.");


            User user=new User("xiix", Role.student,12,Gender.male,"123");
            MainUI mainUI = new MainUI(user);
            mainUI.display();
            frame.dispose();
        }
    }
    private void handleRegister(){
        RegisterUI regUI=new RegisterUI();
        regUI.display();
    }

}
