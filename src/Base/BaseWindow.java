package Base;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;


public class BaseWindow extends JFrame {
    public BaseWindow(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        getContentPane().setBackground(Color.orange); // 设置窗口背景颜色为红色
    }
}
