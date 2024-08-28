package client.ui;

import javax.swing.*;
import client.ui.LibraryUI;
import client.ui.StuUI;
import client.service.User;

public class MainUI {
    private JFrame mainFrame;
    private User user;

    public MainUI(User user){
       this.user=user;
    }


    public void display() {


        mainFrame = new JFrame("主界面");
        mainFrame.setSize(500, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); // 使用绝对布局

        // 添加标签
        JLabel welcomeLabel = new JLabel("用户名:"+'\t'+user.getUsername()+'\t'+"身份:"+'\t'+"年龄:"+'\t');
        welcomeLabel.setBounds(10, 20, 200, 25);
        mainPanel.add(welcomeLabel);

        // 添加按钮
        JButton libButton = new JButton("图书馆");
        libButton.setBounds(10, 60, 100, 25);
        mainPanel.add(libButton);

        libButton.addActionListener(e -> handleLibrary());

        JButton button2 = new JButton("选课系统");
        button2.setBounds(120, 60, 100, 25);
        mainPanel.add(button2);

        JButton stubutton = new JButton("学籍管理");
        stubutton.setBounds(10, 100, 100, 25);
        mainPanel.add(stubutton);

        stubutton.addActionListener(e -> handleStudent(user.getUsername()));

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

    private void handleLibrary(){
        LibraryUI libUI=new LibraryUI(user);
        libUI.display();
        mainFrame.dispose();
    }

    private void handleStudent(String username){

        char identity = username.charAt(0);
        int identityValue = Character.getNumericValue(identity);

        if (identityValue == 0) {
            StuUI stuUI = new StuUI(user);
            stuUI.displaystu();
        } else if (identityValue == 1) {
            StuUI teaUI = new StuUI(user);
            teaUI.displaytea();
        }
        mainFrame.dispose();
    }
}