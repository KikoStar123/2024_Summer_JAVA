package client.ui;

import javax.swing.*;

public class RegisterUI {
    private JFrame regFrame;
    private JTextField name;
    private JTextField id;
    private JTextField gender;
    private JTextField origin;
    private JTextField birthday;
    private JTextField academy;
    public void display(){
        regFrame=new JFrame("学籍注册");
        regFrame.setSize(300, 350);
        regFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        regFrame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        regFrame.add(panel);
        placeComponents(panel);
        regFrame.setVisible(true);
    }
    private void placeComponents(JPanel panel){
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("name:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        name = new JTextField(20);
        name.setBounds(100, 20, 165, 25);
        panel.add(name);

        JLabel idLabel = new JLabel("id:");
        idLabel.setBounds(10, 60, 80, 25);
        panel.add(idLabel);

        id = new JTextField(20);
        id.setBounds(100, 60, 165, 25);
        panel.add(id);

        JLabel genderLabel = new JLabel("gender:");
        genderLabel.setBounds(10, 100, 80, 25);
        panel.add(genderLabel);

        gender = new JTextField(20);
        gender.setBounds(100, 100, 165, 25);
        panel.add(gender);

        JLabel originLabel = new JLabel("origin:");
        originLabel.setBounds(10, 140, 80, 25);
        panel.add(originLabel);

        origin = new JTextField(20);
        origin.setBounds(100, 140, 165, 25);
        panel.add(origin);

        JLabel birthdayLabel = new JLabel("birthday:");
        birthdayLabel.setBounds(10, 180, 80, 25);
        panel.add(birthdayLabel);

        birthday = new JTextField(20);
        birthday.setBounds(100, 180, 165, 25);
        panel.add(birthday);

        JLabel academyLabel = new JLabel("academy:");
        academyLabel.setBounds(10, 220, 80, 25);
        panel.add(academyLabel);

        academy = new JTextField(20);
        academy.setBounds(100, 220, 165, 25);
        panel.add(academy);

        JButton verifyButton = new JButton("verify");
        verifyButton.setBounds(100, 260, 80, 25);
        panel.add(verifyButton);
    }
}
