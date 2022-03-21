package maptesttool;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadText extends JFrame {

    private JTextArea textAreaOutput;
    private JScrollPane jsp;

    public ReadText(String filepath) throws IOException {
        super("文本类型文件读取");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.FlowLayout());
        textAreaOutput = new JTextArea("666", 45, 80);
        textAreaOutput.setSelectedTextColor(Color.RED);
        textAreaOutput.setLineWrap(true);
        textAreaOutput.setWrapStyleWord(true);

        // 设置图标背景
        String dir = System.getProperty("user.dir");
        ImageIcon ig = new ImageIcon(dir + "\\src\\main\\resources\\images\\inter.png");
        final Image im = ig.getImage();
        setIconImage(im);

        // 读文件
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filepath), "UTF-8");
        BufferedReader reader = new BufferedReader(isr);

        String str = reader.readLine();
        String end = "";
        while ((str != null)) {
            end += str + "\n";
            str = reader.readLine();
        }
        textAreaOutput.setText(end);

        getContentPane().add(textAreaOutput);
        setSize(650, 540);
        setLocationRelativeTo(null); // 需要放在size后面

        jsp = new JScrollPane(textAreaOutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(jsp);

        reader.close();

    }

}