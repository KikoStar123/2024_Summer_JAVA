package client.ui;

import client.service.StudentInformation;
import client.service.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StuUI {
    private JFrame StuFrame;
    private JTextField name;
    private JTextField id;
    private JTextField gender;
    private JTextField origin;
    private JTextField birthday;
    private JTextField academy;

    private User user;

    public StuUI(User user){
        this.user=user;
    }

    public void displaystu(){

        StudentInformation student=new StudentInformation();
        //StudentInformation.oneStudentInformation onestudent=student.viewStudentInfo(user.getRole(),user.getId());

        StuFrame=new JFrame("学生学籍管理");
        StuFrame.setSize(300, 350);
        StuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StuFrame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        StuFrame.add(panel);
        stuplaceComponents(panel);
        StuFrame.setVisible(true);
    }

    public void displaytea(){
        //接收一个students数组
        JFrame frame = new JFrame("JTable 示例");
        frame.setSize(2000, 1500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] columnNames = {"列1", "列2"};
        Object[][] data = {
                {"数据1-1", "数据1-2"},
                {"数据2-1", "数据2-2"},
                {"数据3-1", "数据3-2"}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
//    public ArrayTableDisplay(String[] data) {
//        frame = new JFrame("数组展示");
//        frame.setSize(300, 200);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
//
//        String[] columnNames = {"元素"};
//        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
//        for (String element : data) {
//            model.addRow(new Object[]{element});
//        }
//
//        table = new JTable(model);
//        JScrollPane scrollPane = new JScrollPane(table);
//        frame.add(scrollPane);
//
//        frame.setVisible(true);
//    }
    private void stuplaceComponents(JPanel panel){
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("name:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        JLabel idLabel = new JLabel("id:");
        idLabel.setBounds(10, 60, 80, 25);
        panel.add(idLabel);

        JLabel genderLabel = new JLabel("gender:");
        genderLabel.setBounds(10, 100, 80, 25);
        panel.add(genderLabel);

        JLabel originLabel = new JLabel("origin:");
        originLabel.setBounds(10, 140, 80, 25);
        panel.add(originLabel);

        JLabel birthdayLabel = new JLabel("birthday:");
        birthdayLabel.setBounds(10, 180, 80, 25);
        panel.add(birthdayLabel);

        JLabel academyLabel = new JLabel("academy:");
        academyLabel.setBounds(10, 220, 80, 25);
        panel.add(academyLabel);

        JButton backButton =new JButton("返回");
        backButton.setBounds(10,260,80,25);
        panel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StuFrame.dispose(); // 关闭当前界面
                MainUI mainUI = new MainUI(user); // 传入用户名和密码
                mainUI.display(); // 显示主界面
            }
        });
    }
}
