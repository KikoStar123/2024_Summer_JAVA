package client.ui;

import javax.swing.*;

public class MainUI {
    private JFrame mainFrame;

    public void display(String username, String password) {
        mainFrame = new JFrame("主界面");
        mainFrame.setSize(500, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); // 使用绝对布局

        // 添加标签
        JLabel welcomeLabel = new JLabel("用户名:"+'\t'+username+'\t'+"身份:"+'\t'+"年龄:"+'\t');
        welcomeLabel.setBounds(10, 20, 200, 25);
        mainPanel.add(welcomeLabel);

        // 添加按钮
        JButton button1 = new JButton("图书馆");
        button1.setBounds(10, 60, 100, 25);
        mainPanel.add(button1);

        JButton button2 = new JButton("选课系统");
        button2.setBounds(120, 60, 100, 25);
        mainPanel.add(button2);

        JButton button3 = new JButton("学籍管理");
        button3.setBounds(10, 100, 100, 25);
        mainPanel.add(button3);

        JButton button4 = new JButton("银行");
        button4.setBounds(120, 100, 100, 25);
        mainPanel.add(button4);

        JButton button5 = new JButton("商店");
        button5.setBounds(230, 60, 100, 25);
        mainPanel.add(button5);

        JButton button6 = new JButton("医院");
        button6.setBounds(230, 100, 100, 25);
        mainPanel.add(button6);

        JButton button7 = new JButton("注销");
        button7.setBounds(30, 150, 60, 25);
        mainPanel.add(button7);

        JButton button8 = new JButton("登出");
        button8.setBounds(100, 150, 60, 25);
        mainPanel.add(button8);

        JButton button9 = new JButton("更新");
        button9.setBounds(170, 150, 60, 25);
        mainPanel.add(button9);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

}
