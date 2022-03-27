package util;

import java.awt.*;
import java.util.HashMap;

public class UIUtils {
    public static final Font FONT_GENERAL_UI = new Font("宋体", Font.PLAIN, 20);
    public static final Font FONT_FORGOT_PASSWORD = new Font("宋体", Font.PLAIN, 14);

    public static final Color COLOR_OUTLINE = new Color(103, 112, 120);
    public static final Color COLOR_BACKGROUND = new Color(37, 51, 61);
    public static final Color COLOR_INTERACTIVE = new Color(108, 216, 158);
    public static final Color COLOR_INTERACTIVE_DARKER = new Color(87, 171, 127);
    public static final Color OFFWHITE = new Color(229, 229, 229);

    public static final String BUTTON_TEXT_LOGIN = "登录账号";
    public static final String BUTTON_TEXT_FORGOT_PASS = "忘记密码?";
    public static final String BUTTON_TEXT_REGISTER = "注册账号";

    public static final String PLACEHOLDER_TEXT_USERNAME = "用户名";

    public static final int ROUNDNESS = 8;

    public static Graphics2D get2dGraphics(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.addRenderingHints(new HashMap<RenderingHints.Key, Object>() {
            {
                put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            }
        });
        return g2;
    }
}