package user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

class login extends JFrame implements ActionListener {
    private JPanel jp;
    private JLabel lblUserName;
    private JLabel lblUserPwd;
    private JTextField txtUserName;
    private JPasswordField txtUserPwd;
    private JButton btnLogin;
    private JButton btnResign;

    public login() {
        jp = new JPanel(null);
        lblUserName = new JLabel("用户名：");
        lblUserPwd = new JLabel("用户密码：");
        txtUserName = new JTextField();
        txtUserPwd = new JPasswordField();
        btnLogin = new JButton("登录");
        btnResign = new JButton("注册");
        init();
        btnLogin.addActionListener(this);
        btnResign.addActionListener(this);
    }

    public void init() {
        this.setTitle("登录窗口");
        this.setLocation(500, 400);
        this.setSize(300, 200);
        this.setResizable(false);
        lblUserName.setBounds(30, 30, 75, 25);
        lblUserPwd.setBounds(30, 70, 75, 25);
        txtUserName.setBounds(110, 30, 120, 25);
        txtUserPwd.setBounds(110, 70, 120, 25);
        btnLogin.setBounds(50, 120, 75, 25);
        btnResign.setBounds(150, 120, 75, 25);
        this.add(jp);
        jp.add(lblUserName);
        jp.add(lblUserPwd);
        jp.add(txtUserName);
        jp.add(txtUserPwd);
        jp.add(btnLogin);
        jp.add(btnResign);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("登录")) {
            if (resign_login.login_DB_operation(txtUserName.getText(), String.valueOf(txtUserPwd.getPassword()))) {
                this.setVisible(false);
                new success();
            } else {
                this.setVisible(false);
                new failure();
            }
        } else {
            this.setVisible(false);
            new resign();
        }
    }
}

class resign extends JFrame implements ActionListener {
    private JPanel jp;
    private JLabel lblUserName;
    private JLabel lblUserPwd;
    private JLabel lblUserSex;
    private JLabel lblUserTel;
    private JTextField txtUserName;
    private JPasswordField txtUserPwd;
    private JTextField txtUserSex;
    private JTextField txtUserTel;
    private JButton btnResign;

    public resign() {
        jp = new JPanel(null);
        lblUserName = new JLabel("您的姓名：");
        lblUserPwd = new JLabel("您的密码：");
        lblUserSex = new JLabel("您的性别：");
        lblUserTel = new JLabel("您的电话：");
        txtUserName = new JTextField();
        txtUserPwd = new JPasswordField();
        txtUserSex = new JTextField();
        txtUserTel = new JTextField();
        btnResign = new JButton("注册");
        init();
        btnResign.addActionListener(this);
    }

    public void init() {
        this.setTitle("登录窗口");
        this.setLocation(500, 400);
        this.setSize(300, 300);
        this.setResizable(false);
        lblUserName.setBounds(30, 30, 75, 25);
        lblUserPwd.setBounds(30, 70, 75, 25);
        txtUserName.setBounds(110, 30, 120, 25);
        txtUserPwd.setBounds(110, 70, 120, 25);
        lblUserSex.setBounds(30, 110, 75, 25);
        lblUserTel.setBounds(30, 150, 75, 25);
        txtUserSex.setBounds(110, 110, 120, 25);
        txtUserTel.setBounds(110, 150, 120, 25);
        btnResign.setBounds(110, 190, 75, 25);
        this.add(jp);
        jp.add(lblUserName);
        jp.add(lblUserPwd);
        jp.add(txtUserName);
        jp.add(txtUserPwd);
        jp.add(lblUserSex);
        jp.add(lblUserTel);
        jp.add(txtUserSex);
        jp.add(txtUserTel);
        jp.add(btnResign);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resign_login.resign_DB_operation(txtUserName.getText(), String.valueOf(txtUserPwd.getPassword()),
                txtUserSex.getText(), txtUserTel.getText());
        this.setVisible(false);
        new login();
    }
}

class success extends JFrame {
    private JPanel jp;
    private JLabel lblsuccess;

    public success() {
        jp = new JPanel(null);
        lblsuccess = new JLabel("登陆成功！");
        this.setTitle("登陆成功");
        this.setLocation(500, 400);
        this.setSize(300, 200);
        this.setResizable(false);
        lblsuccess.setBounds(110, 60, 75, 25);
        jp.add(lblsuccess);
        this.add(jp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

class failure extends JFrame implements ActionListener {
    private JPanel jp;
    private JLabel lblsuccess;
    private JButton jbtn;

    public failure() {
        jp = new JPanel(null);
        lblsuccess = new JLabel("登陆失败！");
        jbtn = new JButton("重新登录");
        this.setTitle("登陆失败");
        this.setLocation(500, 400);
        this.setSize(300, 200);
        this.setResizable(false);
        lblsuccess.setBounds(110, 60, 75, 25);
        jbtn.setBounds(70, 100, 150, 25);
        jp.add(lblsuccess);
        jp.add(jbtn);
        this.add(jp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        jbtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
        new login();
    }
}

public class resign_login {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/java?characterEncoding=utf-8";
            conn = DriverManager.getConnection(url, "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static boolean login_DB_operation(String username, String userPwd) {
        boolean b = false;
        try {
            Connection conn = resign_login.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM userInfor WHERE userName=" + "\"" + username + "\""
                    + "AND userPwd=" + "\"" + userPwd + "\"");
            b = rs.next();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public static void resign_DB_operation(String username, String userPwd, String sex, String tel) {
        try {
            Connection conn = resign_login.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO userInfor(userName,userPwd,userSex,userTel) VALUES('" + username + "','"
                    + userPwd + "','" + sex + "','" + tel + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new login();
    }
}
