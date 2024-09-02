package client.ui;

import client.service.ClientService;
import client.service.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import client.service.Gender;

public class RegisterUI {
    private JFrame regFrame;
    private JTextField name;
    private JTextField id;
    private JComboBox<String> genderComboBox;
    private JTextField origin;
    private JTextField birthday;
    private JTextField academy;
    private JButton registerButton;
    private JPasswordField passwordField;

    public void display() {
        regFrame = new JFrame("学籍注册");
        regFrame.setSize(300, 400);
        regFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        regFrame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        regFrame.add(panel);
        placeComponents(panel);
        regFrame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        name = new JTextField(20);
        name.setBounds(100, 20, 165, 25);
        panel.add(name);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(10, 60, 80, 25);
        panel.add(idLabel);

        id = new JTextField(20);
        id.setBounds(100, 60, 165, 25);
        panel.add(id);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(10, 100, 80, 25); // 调整 y 坐标到 100
        panel.add(genderLabel);

// 创建下拉框并添加选项
        String[] genders = new String[Gender.values().length];
        int i = 0;
        for (Gender gender : Gender.values()) {
            genders[i++] = gender.toString().toLowerCase();
        }
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(100, 100, 165, 25); // 调整 y 坐标到 140
        panel.add(genderComboBox);

        JLabel originLabel = new JLabel("Origin:");
        originLabel.setBounds(10, 140, 80, 25); // 调整 y 坐标到 180
        panel.add(originLabel);

        origin = new JTextField(20);
        origin.setBounds(100, 140, 165, 25); // 调整 y 坐标到 180
        panel.add(origin);

        JLabel birthdayLabel = new JLabel("Birthday:");
        birthdayLabel.setBounds(10, 180, 80, 25);
        panel.add(birthdayLabel);

        birthday = new JTextField(20);
        birthday.setBounds(100, 180, 165, 25);
        panel.add(birthday);

        JLabel academyLabel = new JLabel("Academy:");
        academyLabel.setBounds(10, 220, 80, 25);
        panel.add(academyLabel);

        academy = new JTextField(20);
        academy.setBounds(100, 220, 165, 25);
        panel.add(academy);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 260, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 260, 165, 25);
        panel.add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(100, 300, 80, 25);
        panel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
    }

    private void handleRegister() {
        String nameText = name.getText();
        String idText = id.getText();
        String genderText = (String) genderComboBox.getSelectedItem();

        // 将字符串转换为 Gender 枚举
        Gender genderEnum;
        if ("male".equalsIgnoreCase(genderText)) {
            genderEnum = Gender.male;
        } else if ("female".equalsIgnoreCase(genderText)) {
            genderEnum = Gender.female;
        } else {
            // 处理未知性别或提供默认值
            genderEnum = Gender.male; // 例如，可以设置为 male 作为默认值
        }
        String originText = origin.getText();
        String birthdayText = birthday.getText();
        String academyText = academy.getText();
        String passwordText = new String(passwordField.getPassword());
//
//        User user = new User();
//        user.setTruename(nameText);
//        user.setId(idText);
//        user.setGender(Gender.valueOf(genderText));
//        user.setOrigin(originText);
//        user.setBirthday(birthdayText);
//        user.setAcademy(academyText);
//        user.setPwd(passwordText);
//        user.setRole(Role.Student); // 默认角色为学生
//
        ClientService clientService = new ClientService();
        boolean success = clientService.register(nameText,genderEnum,idText,originText,birthdayText,academyText,passwordText);
//
        if (success) {
            JOptionPane.showMessageDialog(regFrame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            regFrame.dispose();
            // 可以启动主界面或其他逻辑
        } else {
            JOptionPane.showMessageDialog(regFrame, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}