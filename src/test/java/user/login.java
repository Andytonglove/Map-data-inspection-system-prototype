package user;

import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
// import com.mysql.jdbc.Statement;

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

        // loginButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // if (e.getSource() == loginButton) {
        // try {
        // conn = jdbcUtil.getConnection();// 获取数据库连接
        // stam = (Statement) conn.createStatement(); // 创建sql语句执行对象
        // // 编写sql语句
        // String sql = "select * from user where username='" + username.getText() + "'
        // and password='"
        // + password.getPassword() + "' ";
        // // 执行sql语句
        // ResultSet rs = stam.executeQuery(sql);
        // if (rs.next()) {
        // dispose(); // 关闭当前窗口
        // JOptionPane.showMessageDialog(null, "用户登陆成功！", "登录",
        // JOptionPane.PLAIN_MESSAGE);
        // }
        // } catch (Exception e0) {
        // e0.printStackTrace();
        // } finally {
        // jdbcUtil.result(conn, stam);
        // }
        // }
        // }
        // });

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
