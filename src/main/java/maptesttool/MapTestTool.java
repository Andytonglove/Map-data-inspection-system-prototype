package maptesttool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
// 下面是geotools依赖
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

public class MapTestTool {
    // int inputFlag = 0; // 输入上报区域的节流阀
    int[] cntRecord = { 0, 0 }; // 记录错误条序数，与记录总值
    String selectString = "";
    public final static JTextArea inputPositionArea = new JTextArea(); // 这里位置放到外面为了监听字符串改变

    public MapTestTool() {
        JFrame jf = new JFrame("地图数据检查工具");
        jf.setSize(860, 600);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // windowsUI
        } catch (Exception e) {
            System.out.println("工具外观生成出现错误！");
            System.exit(0);
        }

        String dir = System.getProperty("user.dir"); // 获取相对路径，这里是项目主文件夹下地址
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

        JPanel p = new JPanel();
        jf.setContentPane(p); // 面板
        p.setLayout(null); // 自由布局
        p.setOpaque(false);

        // 菜单棒
        JMenuBar menubar1 = new JMenuBar();
        jf.setJMenuBar(menubar1);

        // 菜单项
        final ImageIcon imageIcon_menu = new ImageIcon(dir + "\\src\\main\\resources\\images\\intersmall.png"); // 菜单图标
        JMenu menu1 = new JMenu("文件");
        JMenu menu2 = new JMenu("错误标注");
        JMenu menu3 = new JMenu("错误查询");
        JMenu menu4 = new JMenu("帮助");
        menubar1.add(menu1);
        menubar1.add(menu2);
        menubar1.add(menu3);
        menubar1.add(menu4);

        // 建立子菜单项
        // 1.地图数据显示：可视化显示：地图（栅格/矢量）形式；文本显示：直接显示数据内容。
        // 2.错误记录管理：位置标识，错误记录保存，错误记录查询。
        final JMenuItem item1 = new JMenuItem("打开数据文件", imageIcon_menu);
        final JMenuItem item2 = new JMenuItem("查看栅格地图文件", imageIcon_menu);
        final JMenuItem item3 = new JMenuItem("查看矢量地图文件", imageIcon_menu);
        final JMenuItem item4 = new JMenuItem("查看文本数据文件", imageIcon_menu); // menu1：文件
        final JMenuItem item5 = new JMenuItem("错误位置标识上报", imageIcon_menu);
        final JMenuItem item6 = new JMenuItem("错误位置输入上报", imageIcon_menu);
        final JMenuItem item7 = new JMenuItem("错误标注文件查看", imageIcon_menu); // menu2：错误标注
        final JMenuItem item8 = new JMenuItem("错误记录输入查询", imageIcon_menu);
        final JMenuItem item9 = new JMenuItem("错误记录标识查询", imageIcon_menu);
        final JMenuItem item10 = new JMenuItem("错误查询历史", imageIcon_menu);
        final JMenuItem item11 = new JMenuItem("错误查询文件查看", imageIcon_menu); // menu3：错误查询
        final JMenuItem item12 = new JMenuItem("残忍再见", imageIcon_menu);
        final JMenuItem item13 = new JMenuItem("项目主页", imageIcon_menu);
        final JMenuItem item14 = new JMenuItem("关于项目", imageIcon_menu); // menu4：帮助

        menu1.add(item1);
        menu1.addSeparator();
        menu1.add(item2);
        menu1.addSeparator();
        menu1.add(item3);
        menu1.addSeparator();
        menu1.add(item4); // 1
        menu2.add(item5);
        menu2.addSeparator();
        menu2.add(item6);
        menu2.addSeparator();
        menu2.add(item7); // 2
        menu3.add(item8);
        menu3.addSeparator();
        menu3.add(item9);
        menu3.addSeparator();
        menu3.add(item10);
        menu3.addSeparator();
        menu3.add(item11); // 3
        menu4.add(item12);
        menu4.addSeparator();
        menu4.add(item13);
        menu4.addSeparator();
        menu4.add(item14); // 4

        // 一个行动提示文本框
        final JTextArea textArea = new JTextArea();
        p.add(textArea);
        textArea.setSize(330, 450);
        textArea.setBounds(0, 510, 360, 450);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setForeground(Color.BLUE);
        textArea.setFont(new Font("楷体", Font.PLAIN, 16));
        textArea.setText("欢迎使用地图数据质检工具！");

        // 下面是主体首页页面部分

        // 左侧上部切换窗口
        final JTextArea textAreaTopArea = new JTextArea();
        p.add(textAreaTopArea);
        textAreaTopArea.setSize(330, 450);
        textAreaTopArea.setBounds(10, 10, 450, 30);
        textAreaTopArea.setOpaque(false);
        textAreaTopArea.setEditable(false);
        textAreaTopArea.setForeground(Color.black);
        textAreaTopArea.setFont(new Font("楷体", Font.PLAIN, 16));
        textAreaTopArea.setText("数据文件名：");

        // 左侧地图上部复选框
        JButton ChooseButton1 = new JButton("地图类型");
        p.add(ChooseButton1);
        ChooseButton1.setBounds(35, 40, 100, 25);
        JButton ChooseButton2 = new JButton("文档类型");
        p.add(ChooseButton2);
        ChooseButton2.setBounds(135, 40, 100, 25);

        // 左侧地图部分
        ImageIcon imageIcon_button = new ImageIcon(dir + "\\src\\main\\resources\\images\\photoforbutton_1.png");
        JButton jb1 = new JButton("地图显示", imageIcon_button);
        p.add(jb1);
        jb1.setBounds(35, 65, 400, 365);
        jb1.setFont(new Font("楷体", Font.PLAIN, 20));
        jb1.setForeground(Color.black);
        jb1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 添加鼠标样式
        jb1.setContentAreaFilled(false); // 透明
        jb1.setBackground(new Color(207, 226, 243)); // 颜色，需要和透明遮挡一起内容，和透明在一起只有边框
        jb1.setOpaque(true); // 遮挡
        jb1.setBorder(BorderFactory.createRaisedBevelBorder()); // 凸起按钮
        jb1.setVerticalTextPosition(SwingConstants.BOTTOM); // 文本相对位置设置
        jb1.setHorizontalTextPosition(SwingConstants.CENTER);
        jb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 打开地图文件显示 这里使用geotools

                Object[] fileType = { "矢量地图文件", "栅格地图文件", "取消" };
                int cho = JOptionPane.showOptionDialog(null,
                        "即将选择并打开地图文件，请选择您想打开的地图数据文件格式。\n"
                                + "本按钮目前演示的支持数据格式为：.shp矢量文件、.tif栅格文件（暂不）。\n"
                                + "查看结束后关闭地图窗口将自动退出程序，如需标注上报请点击标记位置获取信息填写上报。",
                        "地图显示按钮提示",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, fileType, fileType[0]);

                // TODO 同一页面内部展示地图，重构包括tiff和shp，可用JSplitPane
                if (cho == 0) {
                    // 矢量地图文件：shp文件展示，利用geotools

                    // display a data store file chooser dialog for shapefiles
                    File file = JFileDataStoreChooser.showOpenFile("shp", null);
                    if (file == null) {
                        return;
                    }
                    String shpName = file.getName();
                    textAreaTopArea.setText("数据文件名：" + shpName);
                    FileDataStore store;
                    try {
                        store = FileDataStoreFinder.getDataStore(file);
                        SimpleFeatureSource featureSource;
                        featureSource = store.getFeatureSource();

                        // Create a map content and add our shapefile to it
                        MapContent map = new MapContent();
                        map.setTitle("矢量地图文件查看");

                        Style style = SLD.createSimpleStyle(featureSource.getSchema());
                        Layer layer = new FeatureLayer(featureSource, style);
                        map.addLayer(layer);

                        // Now display the map
                        // JMapFrame.showMap(map); // 这里下面用MapContent去代替静态方法的使用

                        // Create a JMapFrame with a menu to choose the display style for the
                        JMapFrame frame = new JMapFrame(map);
                        frame.setSize(800, 600);
                        frame.enableStatusBar(true);
                        frame.enableToolBar(true);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 这里加上这一句以销毁窗口！
                        frame.setVisible(true);

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                } else if (cho == 1) {
                    // 栅格地图文件：tif文件展示，利用geotools

                    GeoTiffLab me = new GeoTiffLab();
                    try {
                        String tifPath = me.getTiffLayersAndDisplay();
                        textAreaTopArea.setText("数据文件名：" + tifPath);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }

            }
        });

        // 右侧输入框-错误位置、错误类型、错误描述
        int widthR = 50;
        int heightR = 30;
        int deltaHr = 30;
        int heightBox = 75;

        // final JTextArea inputPositionArea = new JTextArea(); // TODO 修改合理性
        p.add(inputPositionArea);
        inputPositionArea.setBounds(450 + widthR, heightR, 320, heightBox);
        inputPositionArea.setText("请在此处输入地图错误位置：");
        inputPositionArea.setForeground(Color.gray);
        inputPositionArea.setLineWrap(true); // 激活自动换行功能
        inputPositionArea.setWrapStyleWord(true); // 激活断行不断字功能
        // 文本框焦点与提示相关处理
        inputPositionArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // 得到焦点时，当前文本框的提示文字和创建该对象时的提示文字一样，说明用户正要键入内容
                if (inputPositionArea.getText().equals("请在此处输入地图错误位置：")) {
                    inputPositionArea.setText("");
                    inputPositionArea.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 失去焦点时，用户尚未在文本框内输入任何内容，所以依旧显示提示文字
                if (inputPositionArea.getText().equals("")) {
                    inputPositionArea.setForeground(Color.gray);
                    inputPositionArea.setText("请在此处输入地图错误位置：");
                }
            }
        });

        final JTextArea inputTypeArea = new JTextArea();
        p.add(inputTypeArea);
        inputTypeArea.setBounds(450 + widthR, heightR + deltaHr + heightBox, 320, heightBox);
        inputTypeArea.setText("请在此处输入地图错误类型：");
        inputTypeArea.setForeground(Color.gray);
        inputTypeArea.setLineWrap(true);
        inputTypeArea.setWrapStyleWord(true);
        inputTypeArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputTypeArea.getText().equals("请在此处输入地图错误类型：")) {
                    inputTypeArea.setText("");
                    inputTypeArea.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (inputTypeArea.getText().equals("")) {
                    inputTypeArea.setForeground(Color.gray);
                    inputTypeArea.setText("请在此处输入地图错误类型：");
                }
            }
        });

        final JTextArea inputDescriArea = new JTextArea();
        p.add(inputDescriArea);
        inputDescriArea.setBounds(450 + widthR, heightR + deltaHr * 2 + heightBox * 2, 320, heightBox);
        inputDescriArea.setText("请在此处输入地图错误描述：");
        inputDescriArea.setForeground(Color.gray);
        inputDescriArea.setLineWrap(true);
        inputDescriArea.setWrapStyleWord(true);
        inputDescriArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputDescriArea.getText().equals("请在此处输入地图错误描述：")) {
                    inputDescriArea.setText("");
                    inputDescriArea.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (inputDescriArea.getText().equals("")) {
                    inputDescriArea.setForeground(Color.gray);
                    inputDescriArea.setText("请在此处输入地图错误描述：");
                }
            }
        });

        // 下方查找模块
        final JTextField inputSearchField = new JTextField(); // 搜索输入框
        p.add(inputSearchField);
        inputSearchField.setBounds(450 + widthR, heightR + deltaHr * 2 + heightBox * 3 + 45, 225, 25);
        inputSearchField.setText("请在此处输入搜索以查找：");
        inputSearchField.setForeground(Color.gray);
        // 文本框焦点与提示相关处理
        inputSearchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // 得到焦点时，当前文本框的提示文字和创建该对象时的提示文字一样，说明用户正要键入内容
                if (inputSearchField.getText().equals("请在此处输入搜索以查找：")) {
                    inputSearchField.setText("");
                    inputSearchField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 得到焦点时，当前文本框的提示文字和创建该对象时的提示文字一样，说明用户正要键入内容
                if (inputSearchField.getText().equals("")) {
                    inputSearchField.setForeground(Color.gray);
                    inputSearchField.setText("请在此处输入搜索以查找：");
                }
            }
        });

        // 若干查找按钮
        JButton searchButton = new JButton("搜索");
        p.add(searchButton);
        searchButton.setBounds(450 + widthR + 225 + 25, heightR + deltaHr * 2 + heightBox * 3 + 45, 70, 25);
        searchButton.setName("搜索");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchContent = inputSearchField.getText();
                System.out.println(searchContent);
                // DONE 查找文件
                int[] ErrorsCnt = { 0, 0 }; // 错误上报数量的计数值，第二个是一个找到个数的计数值
                String itemSearchString[] = new String[100]; // 这里暂时先写死，查100条
                String dir = System.getProperty("user.dir"); // 获取当前路径，打开文件，这里相对路径是项目主文件夹下地址，jar也可
                String filepath = dir + "/Errors.txt";
                MyreadFileItems(filepath, itemSearchString, ErrorsCnt);
                System.out.println(ErrorsCnt[0]);
                cntRecord[1] = ErrorsCnt[1];
                for (int i = 1; i <= ErrorsCnt[0]; i++) {
                    if (itemSearchString[i].contains(searchContent)) {
                        System.out.println(itemSearchString[i]);
                        System.out.println(i);
                        ErrorsCnt[1]++;
                        JOptionPane.showMessageDialog(null,
                                "您的搜索存在于第" + i + "条搜索记录中。\n" + "相关错误提交结果是：\n"
                                        + "错误位置：" + itemSearchString[i].split("&")[0] + "\n"
                                        + "错误类型：" + itemSearchString[i].split("&")[1] + "\n"
                                        + "错误描述：" + itemSearchString[i].split("&")[2],
                                "相关搜索结果",
                                JOptionPane.PLAIN_MESSAGE,
                                imageIcon_menu);
                        cntRecord[0] = i; // 把其赋给全局变量来记录
                    }
                }
                if (ErrorsCnt[1] == 0) {
                    JOptionPane.showMessageDialog(null, "您的搜索结果不存在于记录中，\n您可以自己提交错误记录！", "相关搜索结果统计",
                            JOptionPane.PLAIN_MESSAGE, imageIcon_menu);
                } else {
                    JOptionPane.showMessageDialog(null, "您的相关搜索结果一共有" + ErrorsCnt[1] + "条，您可以继续添加！", "相关搜索结果统计",
                            JOptionPane.PLAIN_MESSAGE, imageIcon_menu);
                }
            }
        });

        final JButton firstsearchButton = new JButton("首条");
        p.add(firstsearchButton);
        firstsearchButton.setBounds(450 + widthR, heightR + deltaHr * 2 + heightBox * 3 + 90, 60, 25);

        final JButton presearchButton = new JButton("前一条");
        p.add(presearchButton);
        presearchButton.setBounds(450 + widthR + 80, heightR + deltaHr * 2 + heightBox * 3 + 90, 70, 25);

        final JButton nextsearchButton = new JButton("后一条");
        p.add(nextsearchButton);
        nextsearchButton.setBounds(450 + widthR + 170, heightR + deltaHr * 2 + heightBox * 3 + 90, 70, 25);

        final JButton lastsearchButton = new JButton("末条");
        p.add(lastsearchButton);
        lastsearchButton.setBounds(450 + widthR + 260, heightR + deltaHr * 2 + heightBox * 3 + 90, 60, 25);

        ActionListener searchButtonSListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == firstsearchButton) {
                    cntRecord[0] = 1;
                }
                if (e.getSource() == presearchButton) {
                    cntRecord[0]--;
                }
                if (e.getSource() == nextsearchButton) {
                    cntRecord[0]++;
                }
                if (e.getSource() == lastsearchButton) {
                    cntRecord[0] = cntRecord[0];
                }

                int[] ErrorsCnt = { 0, 0 }; // 错误上报数量的计数值，第二个是一个找到个数的计数值
                String itemSearchString[] = new String[100]; // 这里暂时先写死，查100条
                String dir = System.getProperty("user.dir"); // 获取当前路径，打开文件
                String filepath = dir + "/Errors.txt";
                MyreadFileItems(filepath, itemSearchString, ErrorsCnt);

                if (cntRecord[0] < 1 || cntRecord[0] > ErrorsCnt[0]) {
                    JOptionPane.showMessageDialog(null, "您的搜索不合法，请先进行或重新搜索！", "查找结果", JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "您的搜索跳转至第" + cntRecord[0] + "条搜索记录。\n" + "该条相关错误提交结果是：" + itemSearchString[cntRecord[0]],
                            "查找结果",
                            JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                }
            }
        };
        firstsearchButton.addActionListener(searchButtonSListener);
        presearchButton.addActionListener(searchButtonSListener);
        nextsearchButton.addActionListener(searchButtonSListener);
        lastsearchButton.addActionListener(searchButtonSListener);

        // 保存与放弃 860*600
        JButton saveButton = new JButton("保存上报");
        p.add(saveButton);
        saveButton.setBounds(860 / 2 - 100 - 45, 460, 100, 50);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputPositionArea.getText().contains("请在此处输入地图错误位置：")
                        || inputTypeArea.getText().contains("请在此处输入地图错误类型：")
                        || inputDescriArea.getText().contains("请在此处输入地图错误描述：")) {
                    JOptionPane.showMessageDialog(null, "请输入合法正确的地图错误相关信息！", "输入错误", JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                } else if (inputPositionArea.getText().matches("^\\s*$")
                        || inputTypeArea.getText().matches("^\\s*$")) {
                    // 这里用正则表达式判断是否输入为空，上面是判断是否没变
                    JOptionPane.showMessageDialog(null, "请输入地图错误相关信息！", "输入错误", JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                } else {
                    String commitContent = inputPositionArea.getText() + "&" + inputTypeArea.getText() + "&"
                            + inputDescriArea.getText();
                    // ErrorsCnt++;
                    System.out.println(commitContent);
                    // 保存到文件，之后可能考虑用JSON形式储存上报的错误，目前先用txt
                    MyWriteFileItems("Errors.txt", commitContent);

                    // 完成后清空输入框
                    try {
                        TimeUnit.SECONDS.sleep(1); // 休眠1s
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "保存成功！即将清空输入框", "保存成功", JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                    inputPositionArea.setText("");
                    inputTypeArea.setText("");
                    inputDescriArea.setText("");
                }
            }
        });

        JButton giveupButton = new JButton("放弃修改");
        p.add(giveupButton);
        giveupButton.setBounds(860 / 2 + 45, 460, 100, 50);
        giveupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputPositionArea.setText("");
                inputTypeArea.setText("");
                inputDescriArea.setText("");
            }
        });

        /*
         * ////////////////////////////
         * 下面是菜单栏按钮的部分功能实现
         * ////////////////////////////
         */

        // 1-4.打开数据地图文件
        ActionListener listenOpenFile = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // DONE 打开地图文件
                if (e.getSource() == item1) {
                    // 这里借用基于geotools的ImageLab示例（这里可以是多个）
                    ImageLab me = new ImageLab();
                    try {
                        me.getLayersAndDisplay();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                if (e.getSource() == item2) {
                    JOptionPane.showMessageDialog(null, "请通过下方地图显示按钮选择您想要打开的文件形式，并打开文件可视化查看！", "打开栅格地图文件提示",
                            JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                }
                if (e.getSource() == item3) {
                    JOptionPane.showMessageDialog(null, "请通过下方地图显示按钮选择您想要打开的文件形式，并打开文件可视化查看！", "打开矢量地图文件提示",
                            JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                }
                if (e.getSource() == item4) {
                    JFileChooser openFileChooser = new JFileChooser(".");
                    openFileChooser.setDialogTitle("打开文本数据文件");
                    openFileChooser.showOpenDialog(null);
                    File file = openFileChooser.getSelectedFile();
                    String fileName = file.getName();
                    textAreaTopArea.setText("数据文件名：" + fileName);

                    // 读取文本类型文件
                    try {
                        ReadText tmrReadText = new ReadText(fileName);
                        tmrReadText.setVisible(true);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        // 如果无法打开则启动已在本机桌面上注册的关联应用程序，打开文件
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (Exception e2) {
                            // 异常处理
                            JOptionPane.showMessageDialog(null, "文件不合法，请检查您的文件！", "错误", JOptionPane.PLAIN_MESSAGE,
                                    imageIcon_menu);
                            System.err.println(e2);
                        }
                    }

                }
            }
        };
        item1.addActionListener(listenOpenFile);
        item2.addActionListener(listenOpenFile);
        item3.addActionListener(listenOpenFile);
        item4.addActionListener(listenOpenFile);

        // 5-7.错误标注
        ActionListener listenUploadListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == item5) {
                    JOptionPane.showMessageDialog(null, "请通过在界面中标识选择，通过SELECT模式点击选择地址，\n"
                            + "工具将会自动自动获取标识位置并填入；或者点击查看坐标进行输入上报。",
                            "错误标识上报",
                            JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                    // DONE 利用geotools的SelectionLab示例，支持shp
                    SelectionLab me = new SelectionLab();
                    File file = JFileDataStoreChooser.showOpenFile("shp", null);
                    if (file == null) {
                        return;
                    }
                    try {
                        me.displayShapefile(file);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                if (e.getSource() == item6) {
                    JOptionPane.showMessageDialog(null, "请按照右侧提示，在文本框内输入相关错误信息，\n"
                            + "包括错误位置、错误类型和错误描述，并保存提交。",
                            "错误输入上报", JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                }
                if (e.getSource() == item7) {
                    String dir = System.getProperty("user.dir"); // 获取当前路径，打开文件
                    String filepath = dir + "/Errors.txt";
                    try {
                        File file = new File(filepath);
                        Desktop.getDesktop().open(file); // 启动已在本机桌面上注册的关联应用程序，打开文件.
                    } catch (Exception e2) {
                        // 异常处理
                        JOptionPane.showMessageDialog(null, "错误文件未生成或未找到，请重新生成！", "错误", JOptionPane.PLAIN_MESSAGE,
                                imageIcon_menu);
                        System.err.println(e2);
                    }
                }
            }
        };
        item5.addActionListener(listenUploadListener);
        item6.addActionListener(listenUploadListener);
        item7.addActionListener(listenUploadListener);

        // 8-11.错误查询
        ActionListener listenSearchListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == item8) {
                    JOptionPane.showMessageDialog(null, "请按照右侧提示，在文本框内输入相关错误信息，\n"
                            + "包括错误位置、错误类型和错误描述，并保存提交。",
                            "错误记录输入查询", JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                }
                if (e.getSource() == item9) {
                    JOptionPane.showMessageDialog(null, "请在【地图查看】界面标识查询，点击查看坐标", "错误记录标识查询", JOptionPane.PLAIN_MESSAGE,
                            imageIcon_menu);
                }
                if (e.getSource() == item10 || e.getSource() == item11) {
                    String dir = System.getProperty("user.dir");
                    String filepath = dir + "/Errors.txt";
                    try {
                        File file = new File(filepath);
                        Desktop.getDesktop().open(file);
                    } catch (Exception e2) {
                        // 异常处理
                        JOptionPane.showMessageDialog(null, "错误记录文件未生成或未找到，请重新生成！", "错误", JOptionPane.PLAIN_MESSAGE,
                                imageIcon_menu);
                        System.err.println(e2);
                    }
                }
            }
        };
        item8.addActionListener(listenSearchListener);
        item9.addActionListener(listenSearchListener);
        item10.addActionListener(listenSearchListener);
        item11.addActionListener(listenSearchListener);

        // 12.残忍再见
        item12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // 13.项目主页
        item13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // 我的github项目首页
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler https://github.com/Andytonglove");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // 14.关于项目
        item14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame fr6 = new JFrame();
                fr6.setTitle("关于项目");
                fr6.setSize(400, 300);
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
                p6.setLayout(new BorderLayout());

                JTextArea textArea6 = new JTextArea();
                textArea6.setLineWrap(true);
                textArea6.setSize(250, 220);
                p6.add("Center", textArea6);
                textArea6.setOpaque(false);
                textArea6.setForeground(Color.RED);
                textArea6.setFont(new Font("仿宋", Font.BOLD, 18));

                textArea6.setEditable(false);
                textArea6.append("本Java程序开发信息：本桌面程序为地图数据检查工具。\n\n"
                        + "包括主要内容：\n"
                        + "1、地图数据显示：地图(栅格/矢量)可视化与文本显示；\n"
                        + "2、错误记录管理：位置标识、错误记录保存、错误记录查询。\n"
                        + "软件版本：Version 1.0\n"
                        + "开发设计：关子安\n"
                        + "开发基于：Java GUI、GeoTools");
                textArea6.setEditable(false);

            }
        });

        // 左下角文本提示框
        ActionListener listen = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setEditable(false);
                if (e.getSource() == item1)
                    textArea.setText("正在打开数据文件……\n");
                if (e.getSource() == item2)
                    textArea.setText("即将查看栅格地图文件……\n");
                if (e.getSource() == item3)
                    textArea.setText("即将查看矢量地图文件……\n");
                if (e.getSource() == item4)
                    textArea.setText("即将查看文本数据文件……\n");
                if (e.getSource() == item5)
                    textArea.setText("正在进行错误位置标识上报！\n");
                if (e.getSource() == item6)
                    textArea.setText("正在进行错误位置输入上报！\n");
                if (e.getSource() == item7)
                    textArea.setText("即将查看错误标注文件……");
                if (e.getSource() == item8)
                    textArea.setText("正在进行错误记录输入查询！\n");
                if (e.getSource() == item9)
                    textArea.setText("正在进行错误记录标识查询！\n");
                if (e.getSource() == item10)
                    textArea.setText("即将查看错误查询历史……");
                if (e.getSource() == item11)
                    textArea.setText("即将查看错误查询文件……\n");
                if (e.getSource() == item12)
                    textArea.setText("886！\n");
                if (e.getSource() == item13)
                    textArea.setText("即将访问本项目GitHub项目主页……\n");
                if (e.getSource() == item14)
                    textArea.setText("即将访问本项目相关信息！\n");

            }
        };
        item1.addActionListener(listen);
        item2.addActionListener(listen);
        item3.addActionListener(listen);
        item4.addActionListener(listen);
        item5.addActionListener(listen);
        item6.addActionListener(listen);
        item7.addActionListener(listen);
        item8.addActionListener(listen);
        item9.addActionListener(listen);
        item10.addActionListener(listen);
        item11.addActionListener(listen);
        item12.addActionListener(listen);
        item13.addActionListener(listen);
        item14.addActionListener(listen);
    }

    /*
     * ///////////////////////////////
     * 这里以下是一些其他函数的定义与使用
     * ///////////////////////////////
     */

    // 以换行为分隔符依次读取文件，调用next
    public static void MyreadFileItems(String fileName, String items[], int cnt[]) {
        try {
            File file = new File(fileName);
            Scanner s = new Scanner(file, "UTF-8").useDelimiter("\r\n");
            // System.out.println(s.hasNext());
            // System.out.println(s);
            // SOLVED 这里hasnextline是false原因是读取编码问题
            while (s.hasNext()) {
                cnt[0]++; // 这里存在一个函数改变实参的问题，这里要用数组地址传参才能修改
                items[cnt[0]] = s.next();
            }
            s.close();
        } catch (Exception ee) {
            System.out.println("数据文件未找到，错误是：" + ee);
            JOptionPane.showMessageDialog(null, "需读取的数据文件未找到，\n"
                    + "请您检查数据文件的完整性！或提交您的错误上报！");
        }
    }

    // filewriter写入文件
    public static void MyWriteFileItems(String fileName, String item) {
        try {
            BufferedWriter fwriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8")); // true追加写入
            fwriter.write(item + "\r\n");
            fwriter.flush();
            fwriter.close();
            // FileWriter fw = new FileWriter(fileName, true);
            // 这里的true代表追加写入，FileWriter存在编码问题，故使用BufferedWriter，使读取成功
            // fw.write(item + "\r\n");
            // fw.flush();
            // fw.close();
        } catch (Exception eee) {
            eee.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new MapTestTool();

        String fozu = "                   _ooOoo_" + "\n" +
                "                  o8888888o" + "\n" +
                "                  88\" . \"88" + "\n" +
                "                  (| -_- |)" + "\n" +
                "                  O\\  =  /O" + "\n" +
                "               ____/`---'\\____" + "\n" +
                "             .'  \\\\|     |//  `." + "\n" +
                "            /  \\\\|||  :  |||//  \\" + "\n" +
                "           /  _||||| -:- |||||-  \\" + "\n" +
                "           |   | \\\\\\  -  /// |   |" + "\n" +
                "           | \\_|  ''\\---/''  |   |" + "\n" +
                "           \\  .-\\__  `-`  ___/-. /" + "\n" +
                "         ___`. .'  /--.--\\  `. . __" + "\n" +
                "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\"." + "\n" +
                "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |" + "\n" +
                "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /" + "\n" +
                "======`-.____`-.___\\_____/___.-`____.-'======" + "\n" +
                "                   `=---='" + "\n" +
                "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + "\n" +
                "                 佛祖保佑       永无BUG";
        System.out.println(fozu);

    }
}
