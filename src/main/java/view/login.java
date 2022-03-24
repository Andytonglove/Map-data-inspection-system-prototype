package view;

import javax.swing.*;

import dao.UserDao;
import model.User;
import util.DbUtil;
import util.StringUtil;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class login extends JFrame {

    private JFrame frame = new JFrame("用户登录-地图数据质检系统");
    private JPanel panel = new JPanel();
    private JLabel userLabel = new JLabel("用户名:"); // 创建UserJLabel
    private JTextField username = new JTextField(); // 获取登录名
    private JLabel passLabel = new JLabel("密码:"); // 创建PassJLabel
    private JPasswordField password = new JPasswordField(20); // 密码框隐藏
    private JButton loginButton = new JButton("登录账户"); // 创建登录按钮
    private JButton registerButton = new JButton("用户注册"); // 创建注册按钮

    Connection conn;
    // Statement stam;

    public login() {
        // 设置窗体的位置及大小
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel); // 添加面板
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        placeComponents(panel); // 往窗体里放其他控件
        frame.setVisible(true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // windowsUI
        } catch (Exception e) {
            System.out.println("工具外观生成出现错误！");
            System.exit(0);
        }

        String dir = System.getProperty("user.dir");
        ImageIcon ig = new ImageIcon(dir + "\\src\\main\\resources\\images\\inter.png");
        final Image im = ig.getImage();
        frame.setIconImage(im);
    }

    /**
     * 面板具体布局
     * 
     * @param panel
     */
    private void placeComponents(JPanel panel) {

        panel.setLayout(null); // 设置布局为 null

        // 创建 UserJLabel
        userLabel.setBounds(30, 25, 80, 25);
        panel.add(userLabel);
        // 创建文本域用于用户输入
        username.setBounds(105, 25, 165, 25);
        panel.add(username);

        // 创建PassJLabel
        passLabel.setBounds(30, 65, 80, 25);
        panel.add(passLabel);
        // 密码输入框 隐藏
        password.setBounds(105, 65, 165, 25);
        panel.add(password);

        // 创建登录按钮
        loginButton.setBounds(25, 110, 100, 25);
        panel.add(loginButton);
        registerButton.setBounds(170, 110, 100, 25);
        panel.add(registerButton);

        // 按钮-登录
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = username.getText();
                String passWord = new String(password.getPassword());
                if (StringUtil.isEmpty(userName)) {
                    JOptionPane.showMessageDialog(null, "用户名不能为空");
                    return;
                }
                if (StringUtil.isEmpty(passWord)) {
                    JOptionPane.showMessageDialog(null, "密码不能为空");
                    return;
                }
                User user = new User(userName, passWord);
                Connection connection = null;
                DbUtil dbUtil = new DbUtil(); // 不是静态就得new出来
                UserDao userDao = new UserDao();
                try {
                    connection = dbUtil.getConnection();
                    User currentUser = userDao.login(connection, user);
                    if (currentUser != null) {
                        JOptionPane.showMessageDialog(null, "登录成功!");
                    } else {
                        JOptionPane.showMessageDialog(null, "用户名或密码错误!你可以选择先进行注册!");
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        // 按钮-注册
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭登录窗体
                // new Register().addMan(); // 打开注册窗体
            }
        });
    }

}
