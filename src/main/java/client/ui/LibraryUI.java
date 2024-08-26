package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryUI {

    private JFrame LibFrame;
    private JTextField name;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel resultPanel;

    private String username;
    private String password;

    public LibraryUI(String username,String password){
        this.username=username;
        this.password=password;
    }

    public void display(){
        LibFrame=new JFrame("图书馆");
        LibFrame.setSize(300, 350);
        LibFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LibFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel searchPanel = new JPanel();
        placeComponents(searchPanel);

        resultPanel = new JPanel();
        resultPanel.setLayout(null);

        mainPanel.add(searchPanel, "Search Panel");
        mainPanel.add(resultPanel, "Result Panel");

        LibFrame.add(mainPanel);
        LibFrame.setVisible(true);
    }
    private void placeComponents(JPanel panel){
        panel.setLayout(null);

        JLabel userLabel=new JLabel("用户名:");
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        JLabel nameLabel = new JLabel("查询:");
        nameLabel.setBounds(10, 50, 80, 25);
        panel.add(nameLabel);

        name = new JTextField(20);
        name.setBounds(100, 50, 165, 25);
        panel.add(name);

        JButton searchButton = new JButton("查询");
        searchButton.setBounds(10, 90, 80, 25);
        panel.add(searchButton);

        JButton backButton =new JButton("返回");
        backButton.setBounds(10,120,80,25);
        panel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LibFrame.dispose(); // 关闭当前界面
                MainUI mainUI = new MainUI(username, password); // 传入用户名和密码
                mainUI.display(); // 显示主界面
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 调用查询方法，返回查询结果
                String bookName = name.getText();
                displayBookInfo(bookName);
                cardLayout.show(mainPanel, "Result Panel");
            }
        });
    }
    private void displayBookInfo(String bookName) {
        resultPanel.removeAll(); // 清除之前的查询结果

        // 假设查询到的图书信息
        JLabel titleLabel = new JLabel("书名: " + bookName);
        titleLabel.setBounds(10, 20, 200, 25);
        resultPanel.add(titleLabel);

        JLabel idLabel = new JLabel("编号: " );
        idLabel.setBounds(10, 50, 200, 25);
        resultPanel.add(idLabel);

        JLabel authorLabel = new JLabel("作者: " );
        authorLabel.setBounds(10, 80, 200, 25);
        resultPanel.add(authorLabel);

        JLabel numLabel = new JLabel("剩余数量: " );
        numLabel.setBounds(10, 110, 200, 25);
        resultPanel.add(numLabel);

        JButton borrowButton= new JButton("借阅");
        borrowButton.setBounds(10, 150, 80, 25);
        resultPanel.add(borrowButton);

        JButton returnButton=new JButton("归还");
        returnButton.setBounds(100,150,80,25);
        resultPanel.add(returnButton);

        JButton backButton = new JButton("返回");
        backButton.setBounds(190,150,80,25);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Search Panel");
            }
        });
        resultPanel.add(backButton);

        // 重新绘制结果面板
        resultPanel.revalidate();
        resultPanel.repaint();
    }
};