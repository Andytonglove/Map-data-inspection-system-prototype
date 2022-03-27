package view;

import Toaster.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import util.*;
import dao.UserDao;
import model.User;

import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

public class loginui extends JFrame {

    private final Toaster toaster;

    private String dir = System.getProperty("user.dir");
    private ImageIcon ig = new ImageIcon(dir + "\\src\\main\\resources\\images\\login_placeholder.png");
    private ImageIcon ig2Icon = new ImageIcon(dir + "\\src\\main\\resources\\images\\inter.png");

    public JPanel mainJPanel = getMainJPanel();
    private TextFieldUsername usernameField = new TextFieldUsername();
    private TextFieldPassword passwordField = new TextFieldPassword();

    public static int loginFlag = 0; // 登录节流阀

    /**
     * new loginui();
     */

    public loginui() {

        addLogo(mainJPanel);
        addSeparator(mainJPanel);
        addUsernameTextField(mainJPanel);
        addPasswordTextField(mainJPanel);
        addLoginButton(mainJPanel);
        addForgotPasswordButton(mainJPanel);
        addRegisterButton(mainJPanel);

        this.add(mainJPanel);
        this.pack();
        this.setVisible(true);
        this.toFront();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

        toaster = new Toaster(mainJPanel);
    }

    private JPanel getMainJPanel() {
        // this.setUndecorated(true);
        this.setIconImage(ig2Icon.getImage());
        this.setTitle("用户登录注册");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension size = new Dimension(800, 400);

        JPanel panel1 = new JPanel();
        panel1.setSize(size);
        panel1.setPreferredSize(size);
        panel1.setBackground(UIUtils.COLOR_BACKGROUND);
        panel1.setLayout(null);

        MouseAdapter ma = new MouseAdapter() {
            int lastX, lastY;

            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getXOnScreen();
                lastY = e.getYOnScreen();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(getLocationOnScreen().x + x - lastX, getLocationOnScreen().y + y - lastY);
                lastX = x;
                lastY = y;
            }
        };

        panel1.addMouseListener(ma);
        panel1.addMouseMotionListener(ma);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        return panel1;
    }

    private void addSeparator(JPanel panel1) {
        JSeparator separator1 = new JSeparator();
        separator1.setOrientation(SwingConstants.VERTICAL);
        separator1.setForeground(UIUtils.COLOR_OUTLINE);
        panel1.add(separator1);
        separator1.setBounds(310, 80, 1, 240);
    }

    private void addLogo(JPanel panel1) {
        JLabel label1 = new JLabel();
        label1.setFocusable(false);
        label1.setIcon(ig);
        panel1.add(label1);
        label1.setBounds(55, 146, 200, 110);
    }

    private void addUsernameTextField(JPanel panel1) {

        usernameField.setBounds(423, 109, 250, 44);
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals(UIUtils.PLACEHOLDER_TEXT_USERNAME)) {
                    usernameField.setText("");
                }
                usernameField.setForeground(Color.white);
                usernameField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText(UIUtils.PLACEHOLDER_TEXT_USERNAME);
                }
                usernameField.setForeground(UIUtils.COLOR_OUTLINE);
                usernameField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });

        panel1.add(usernameField);
    }

    private void addPasswordTextField(JPanel panel1) {

        passwordField.setBounds(423, 168, 250, 44);
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setForeground(Color.white);
                passwordField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setForeground(UIUtils.COLOR_OUTLINE);
                passwordField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    loginEventHandler();
            }
        });

        panel1.add(passwordField);
    }

    private void addLoginButton(JPanel panel1) {
        final Color[] loginButtonColors = { UIUtils.COLOR_INTERACTIVE, Color.white };

        JLabel loginButton = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                super.paintComponent(g2);

                Insets insets = getInsets();
                int w = getWidth() - insets.left - insets.right;
                int h = getHeight() - insets.top - insets.bottom;
                g2.setColor(loginButtonColors[0]);
                g2.fillRoundRect(insets.left, insets.top, w, h, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                FontMetrics metrics = g2.getFontMetrics(UIUtils.FONT_GENERAL_UI);
                int x2 = (getWidth() - metrics.stringWidth(UIUtils.BUTTON_TEXT_LOGIN)) / 2;
                int y2 = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                g2.setColor(loginButtonColors[1]);
                g2.drawString(UIUtils.BUTTON_TEXT_LOGIN, x2, y2);
            }
        };

        loginButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                loginEventHandler();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginButtonColors[0] = UIUtils.COLOR_INTERACTIVE_DARKER;
                loginButtonColors[1] = UIUtils.OFFWHITE;
                loginButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButtonColors[0] = UIUtils.COLOR_INTERACTIVE;
                loginButtonColors[1] = Color.white;
                loginButton.repaint();
            }
        });

        loginButton.setBackground(UIUtils.COLOR_BACKGROUND);
        loginButton.setBounds(423, 247, 250, 44);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel1.add(loginButton);
    }

    private void addForgotPasswordButton(JPanel panel1) {
        panel1.add(new HyperlinkText(UIUtils.BUTTON_TEXT_FORGOT_PASS, 423, 300, () -> {
            toaster.error("忘记密码~您可以注册新账号或找回!");
        }));
    }

    private void addRegisterButton(JPanel panel1) {
        HyperlinkText registerButton = new HyperlinkText(UIUtils.BUTTON_TEXT_REGISTER, 631, 300, () -> {
            toaster.warn("正在注册!");
        });
        panel1.add(registerButton);

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                registerEventHandler();
            }
        });
    }

    /**
     * 登录与注册事件，存储进数据库与读取
     */

    private void loginEventHandler() {
        toaster.warn("正在登录!");

        // 登录事件，已重写
        String userName = usernameField.getText();
        String passWord = new String(passwordField.getPassword());
        if (StringUtil.isEmpty(userName)) {
            toaster.warn("用户名不能为空");
            return;
        }
        if (StringUtil.isEmpty(passWord)) {
            toaster.warn("密码不能为空");
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
                toaster.success("登录成功!登陆界面将在3s后关闭~");
                loginFlag = 1;
                // 延时3s关闭窗口
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        dispose();
                    }
                }, 3000);
            } else {
                toaster.error("用户名或密码错误!如果您还没有注册,您需要先进行注册!");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void registerEventHandler() {
        // 注册事件，已重写
        String userName = usernameField.getText();
        String passWord = new String(passwordField.getPassword());
        if (StringUtil.isEmpty(userName)) {
            toaster.warn("用户名不能为空");
            return;
        }
        if (StringUtil.isEmpty(passWord)) {
            toaster.warn("密码不能为空");
            return;
        }
        User user = new User(userName, passWord);
        Connection connection = null;
        DbUtil dbUtil = new DbUtil();
        UserDao userDao = new UserDao();
        try {
            connection = dbUtil.getConnection();
            int cnt = userDao.register(connection, user);
            if (cnt > 0) {
                toaster.success("注册成功!接下来您可以使用此账号登录了!");
            } else if (cnt < 0) {
                toaster.error("用户名重复!请您更换用户名重新注册!");
            } else {
                toaster.error("注册失败!请您尝试重新注册!");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
