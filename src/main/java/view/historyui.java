package view;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.HistoryDao;
import model.History;
import util.DbUtil;

public class HistoryUI {
    // 这里使用swing：历史记录和用户管理界面；后续可转为Java Web
    private JFrame jf = new JFrame("用户远端管理历史");
    JPanel p = new JPanel();
    private JMenuBar menubar = new JMenuBar();
    private String dir = System.getProperty("user.dir");
    private final ImageIcon imageIcon_menu = new ImageIcon(dir + "\\src\\main\\resources\\images\\intersmall.png");

    public HistoryUI() {
        jf.setSize(800, 540);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("工具外观生成出现错误！");
        }

        // 设置图标
        ImageIcon ig = new ImageIcon(dir + "\\src\\main\\resources\\images\\inter.png");
        final Image im = ig.getImage();
        jf.setIconImage(im);
        // 设置背景
        final ImageIcon img = new ImageIcon(dir + "\\src\\main\\resources\\images\\back.jpg");
        JLabel imgLabel = new JLabel(img); // 将背景图放在标签里
        jf.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));
        imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        imgLabel.setOpaque(true);

        jf.setContentPane(p);
        // p.setLayout(null); // 特别注意：显示JTable不能是null风格！
        p.setOpaque(false);

        // 菜单棒
        jf.setJMenuBar(menubar);
        // 菜单项
        final JMenuItem menu1 = new JMenuItem("用户管理:" + MapTestTool.loginUserName);
        final JMenuItem menu2 = new JMenuItem("用户操作记录");
        final JMenuItem menu3 = new JMenuItem("记录模糊查找");
        menubar.add(menu1);
        menubar.add(menu2);
        menubar.add(menu3);

        // 表格初始化函数，转为内部方便调用成员，相当于initTable()

        // 定义二维数组作为表格数据，这里可以设置自动刷新
        ArrayList<String> histories = searchHistoryfromDBAll();
        Object[][] tableData = new String[histories.size()][6];
        for (int i = 0; i < histories.size(); i++) {
            tableData[i][5] = histories.get(i).split("&")[0]; // id
            tableData[i][1] = histories.get(i).split("&")[1]; // 地图名
            tableData[i][2] = histories.get(i).split("&")[2]; // 位置
            tableData[i][3] = histories.get(i).split("&")[3]; // 类型
            tableData[i][4] = histories.get(i).split("&")[4]; // 描述
            tableData[i][0] = histories.get(i).split("&")[5]; // 用户名
        }
        // 定义一维数据作为列标题
        Object[] columnTitle = { "用户名", "地图名称", "错误位置", "错误类型", "错误描述", "上报序号" };
        JTable hisTable = new JTable();
        hisTable.setModel(new DefaultTableModel(tableData, columnTitle));
        p.add(new JScrollPane(hisTable));

        // 设置一下居中对齐
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        cr.setHorizontalAlignment(JLabel.CENTER);
        hisTable.setDefaultRenderer(Object.class, cr);
        hisTable.setEnabled(false); // 不可随意编辑

        // 设置表格内容颜色
        hisTable.setForeground(Color.BLACK); // 字体颜色
        hisTable.setFont(new Font("宋体", Font.PLAIN, 14)); // 字体样式
        hisTable.setSelectionForeground(Color.DARK_GRAY); // 选中后字体颜色
        hisTable.setSelectionBackground(Color.LIGHT_GRAY); // 选中后字体背景
        hisTable.setGridColor(Color.GRAY); // 网格颜色

        // 设置表头
        hisTable.getTableHeader().setFont(new Font("宋体", Font.BOLD, 14)); // 设置表头名称字体样式
        hisTable.getTableHeader().setForeground(Color.RED); // 设置表头名称字体颜色
        hisTable.getTableHeader().setResizingAllowed(true); // 设置不允许手动改变列宽
        hisTable.getTableHeader().setReorderingAllowed(true); // 设置不允许拖动重新排序各列

        // 设置行高
        hisTable.setRowHeight(30);
        // 第一列列宽设置为40
        hisTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        hisTable.setPreferredScrollableViewportSize(new Dimension(600, 300));

        // 1、用户管理
        menu1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame fr6 = new JFrame();
                fr6.setTitle("用户信息管理");
                fr6.setSize(400, 330);
                fr6.setLocationRelativeTo(null);
                fr6.setResizable(false);
                fr6.setVisible(true);
                fr6.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fr6.setIconImage(im);
                JLabel imgLabel = new JLabel(img); // 将背景图放在标签里。
                fr6.getLayeredPane().add(imgLabel, Integer.valueOf(Integer.MIN_VALUE));
                imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
                imgLabel.setOpaque(true);

                JPanel p6 = new JPanel();
                fr6.setContentPane(p6); // 面板
                p6.setOpaque(false);

                JTextArea textArea6 = new JTextArea();
                textArea6.setLineWrap(true);
                textArea6.setSize(250, 220);
                p6.add("Center", textArea6);
                textArea6.setOpaque(false);
                textArea6.setForeground(Color.RED);
                textArea6.setFont(new Font("仿宋", Font.BOLD, 18));

                textArea6.setEditable(false);
                textArea6.append("用户信息管理\n\n"
                        + "用户名: " + MapTestTool.loginUserName + "\n"
                        + "您好！欢迎使用地图数据质检系统！");
                textArea6.setEditable(false);

            }
        });

        // 2、用户名错误上报信息
        menu2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (MapTestTool.loginFlag == 1) {
                    // 在数据库中通过用户名搜索用户操作记录，这里注意join的用法，用户管理界面可以渲染更漂亮一些
                    ArrayList<String> his4dbString = MapTestTool.searchHistoryfromDBbyName(MapTestTool.loginUserName);
                    String searchHistorysString = "用户 " + MapTestTool.loginUserName + " 的远端数据库相关记录:\n\n";

                    for (int i = 0; i < his4dbString.size(); i++) {
                        System.out.println(his4dbString.get(i));
                        // 字符串格式优化一下
                        String tempHistorysString = String.join("\n",
                                "用户记录编号:" + his4dbString.get(i).split("&")[0],
                                "操作地图名称:" + his4dbString.get(i).split("&")[1],
                                "上报错误位置:" + his4dbString.get(i).split("&")[2],
                                "上报错误类型:" + his4dbString.get(i).split("&")[3],
                                "上报错误描述:" + his4dbString.get(i).split("&")[4] + "\n\n");

                        // 可以用vector存数据
                        System.out.println(tempHistorysString);
                        searchHistorysString += tempHistorysString;
                    }
                    JOptionPane.showMessageDialog(null, searchHistorysString, "用户远端数据库相关记录",
                            JOptionPane.PLAIN_MESSAGE, imageIcon_menu);
                } else {
                    JOptionPane.showMessageDialog(null, "请您先登陆账号，再查看用户操作记录！\n或者您可以直接搜索查看!", "未登录",
                            JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                }
            }
        });

        // 3、模糊搜索查找
        menu3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchContent = JOptionPane.showInputDialog(jf, "模糊搜索关键词", "在此输入");
                if (searchContent.isEmpty() || searchContent.equals("在此键入")) {
                    JOptionPane.showMessageDialog(null, "输入不合法，请输入搜索关键词！", "输入错误",
                            JOptionPane.PLAIN_MESSAGE, imageIcon_menu);
                    return;
                } else {
                    // 在数据库中通过关键词搜索用户操作记录，模糊查询，TODO 这里最好渲染到表格里
                    ArrayList<String> his4dbkeywordString = MapTestTool.searchHistoryfromDBbyKeyWord(searchContent);
                    String searchHistoryString = "用户 " + MapTestTool.loginUserName + " 的远端数据库相关记录:\n\n";

                    for (int i = 0; i < his4dbkeywordString.size(); i++) {
                        System.out.println(his4dbkeywordString.get(i));
                        // 字符串格式优化一下
                        String tempHistorysString = String.join("\n",
                                "用户记录编号:" + his4dbkeywordString.get(i).split("&")[0],
                                "操作地图名称:" + his4dbkeywordString.get(i).split("&")[1],
                                "上报错误位置:" + his4dbkeywordString.get(i).split("&")[2],
                                "上报错误类型:" + his4dbkeywordString.get(i).split("&")[3],
                                "上报错误描述:" + his4dbkeywordString.get(i).split("&")[4] + "\n\n");

                        System.out.println(tempHistorysString);
                        searchHistoryString += tempHistorysString;
                    }
                    JOptionPane.showMessageDialog(null, searchHistoryString, "用户远端数据库相关记录",
                            JOptionPane.PLAIN_MESSAGE, imageIcon_menu);
                }
            }
        });

        jf.setVisible(true); // 最后再可视化

    }

    public JTable initTable() {
        // 表格初始化函数

        // 定义二维数组作为表格数据，这里可以设置自动刷新
        ArrayList<String> histories = searchHistoryfromDBAll();
        Object[][] tableData = new String[histories.size()][6];
        for (int i = 0; i < histories.size(); i++) {
            tableData[i][5] = histories.get(i).split("&")[0]; // id
            tableData[i][1] = histories.get(i).split("&")[1]; // 地图名
            tableData[i][2] = histories.get(i).split("&")[2]; // 位置
            tableData[i][3] = histories.get(i).split("&")[3]; // 类型
            tableData[i][4] = histories.get(i).split("&")[4]; // 描述
            tableData[i][0] = histories.get(i).split("&")[5]; // 用户名
        }
        // 定义一维数据作为列标题
        Object[] columnTitle = { "用户名", "地图名称", "错误位置", "错误类型", "错误描述", "上报序号" };
        JTable hisTable = new JTable();
        hisTable.setModel(new DefaultTableModel(tableData, columnTitle));
        p.add(new JScrollPane(hisTable));

        // 设置一下居中对齐
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        cr.setHorizontalAlignment(JLabel.CENTER);
        hisTable.setDefaultRenderer(Object.class, cr);
        hisTable.setEnabled(false); // 不可随意编辑

        // 设置表格内容颜色
        hisTable.setForeground(Color.BLACK); // 字体颜色
        hisTable.setFont(new Font("宋体", Font.PLAIN, 14)); // 字体样式
        hisTable.setSelectionForeground(Color.DARK_GRAY); // 选中后字体颜色
        hisTable.setSelectionBackground(Color.LIGHT_GRAY); // 选中后字体背景
        hisTable.setGridColor(Color.GRAY); // 网格颜色

        // 设置表头
        hisTable.getTableHeader().setFont(new Font("宋体", Font.BOLD, 14)); // 设置表头名称字体样式
        hisTable.getTableHeader().setForeground(Color.RED); // 设置表头名称字体颜色
        hisTable.getTableHeader().setResizingAllowed(true); // 设置不允许手动改变列宽
        hisTable.getTableHeader().setReorderingAllowed(true); // 设置不允许拖动重新排序各列

        // 设置行高
        hisTable.setRowHeight(30);
        // 第一列列宽设置为40
        hisTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        hisTable.setPreferredScrollableViewportSize(new Dimension(600, 300));

        return hisTable;
    }

    /**
     * 历史记录搜索
     * 错误上报通过名字在数据库中完成搜索
     * 
     * @param historysNameString 历史记录用户名字符串
     * @return searchHistoryString 结果字符串
     * @throws Exception
     */
    public static ArrayList<String> searchHistoryfromDBAll() {
        if (MapTestTool.loginFlag == 1) {
            Connection connection = null;
            DbUtil dbUtil = new DbUtil();
            HistoryDao historyDao = new HistoryDao();
            ArrayList<History> his4db = new ArrayList<History>();
            ArrayList<String> searchHistoryString = new ArrayList<String>();
            try {
                connection = dbUtil.getConnection();
                his4db = historyDao.searchHistoriesFromAll(connection);
                // 对得到的对象进行处理，返回字符串
                for (int i = 0; i < his4db.size(); i++) {
                    String tempHistorysString = String.join("&", Integer.toString(his4db.get(i).getId()),
                            his4db.get(i).getmapname(), his4db.get(i).getposition(),
                            his4db.get(i).gettype(), his4db.get(i).getdiscription(), his4db.get(i).getUserName());
                    searchHistoryString.add(tempHistorysString);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return searchHistoryString;
        }
        return null;
    }
}
