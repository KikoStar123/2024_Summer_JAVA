package client.ui;
import client.service.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LibraryUI {

    private JFrame LibFrame;
    private JTextField searchField;
    private JTextArea resultArea;
    private JPanel mainPanel; // 声明 mainPanel
    private CardLayout cardLayout;
    private JPanel searchPanel, resultPanel;

    private User user;

    public LibraryUI(User user){
        this.user = user;
    }

    public void display(){
        LibFrame = new JFrame("图书馆");
        LibFrame.setSize(400, 500);
        LibFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LibFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout); // 初始化 mainPanel

        searchPanel = new JPanel();
        placeComponents(searchPanel);

        resultPanel = new JPanel(new BorderLayout());
        resultArea = new JTextArea(10, 30);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        mainPanel.add(searchPanel, "Search Panel");
        mainPanel.add(resultPanel, "Result Panel");

        LibFrame.add(mainPanel);
        LibFrame.setVisible(true);
    }

    private void placeComponents(JPanel panel){
        panel.setLayout(null);

        JLabel userLabel = new JLabel("用户名: " + user.getUsername());
        userLabel.setBounds(10, 20, 100, 25);
        panel.add(userLabel);

        JLabel roleLabel = new JLabel("身份: " + user.getRole());
        roleLabel.setBounds(10, 50, 100, 25);
        panel.add(roleLabel);

        searchField = new JTextField(20);
        searchField.setBounds(10, 80, 165, 25);
        panel.add(searchField);

        JButton searchButton = new JButton("查询");
        searchButton.setBounds(10, 110, 100, 25);
        panel.add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBookInfo();
                cardLayout.show(mainPanel, "Result Panel");
            }
        });

        JButton myinfoButton = new JButton("我的借阅");
        myinfoButton.setBounds(130, 110, 100, 25);
        panel.add(myinfoButton);
        myinfoButton.addActionListener(e -> {
            // 显示借阅历史
            displayBorrowHistory();
        });

        JButton backButton = new JButton("返回");
        backButton.setBounds(250, 110, 100, 25);
        panel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LibFrame.dispose();
                MainUI mainUI = new MainUI(user);
                mainUI.display();
            }
        });
    }

    private void displayBookInfo() {
        resultPanel.removeAll();
        resultArea = new JTextArea(10, 30);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // 假设查询到的图书信息
        String bookInfo = "书名: 示例书籍\n作者: 作者A\n出版社: 出版社B\n出版日期: 2024年\nISBN: 1234567890";
        resultArea.setText(bookInfo);

        JButton borrowButton = new JButton("借阅");
        borrowButton.setBounds(10, 10, 80, 25);
        resultPanel.add(borrowButton, BorderLayout.NORTH);
        borrowButton.addActionListener(e -> {
            // 调用借阅方法
            borrowBook();
        });

        JButton returnButton = new JButton("归还");
        returnButton.setBounds(100, 10, 80, 25);
        resultPanel.add(returnButton, BorderLayout.NORTH);
        returnButton.addActionListener(e -> {
            // 调用归还方法
            returnBook();
        });

        JButton backButton = new JButton("返回");
        backButton.setBounds(290, 10, 80, 25);
        resultPanel.add(backButton, BorderLayout.NORTH);
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Search Panel");
        });

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void displayBorrowHistory() {
        // 显示借阅历史记录
    }

    private void borrowBook() {
        // 调用服务层方法借阅图书
    }

    private void returnBook() {
        // 调用服务层方法归还图书
    }
}