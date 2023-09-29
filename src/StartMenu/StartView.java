package StartMenu;

import Base.BaseWindow;
import javax.swing.*;
import java.awt.*;


public class StartView extends BaseWindow {
    // 标题标签
    JLabel nameLabel = new JLabel("购物管理系统", JLabel.CENTER);
    JPanel centerPanel = new JPanel(new GridLayout(1, 2)); // 使用GridLayout布局管理器，1行2列
    // 按钮标签
    JButton adminBtn = new JButton("管理员登录");
    JButton customerBtn = new JButton("客户登录");

    public StartView() {
        super("购物管理系统", 1000, 700);
        initComponents();
        layoutComponents();
        adminBtn.setBackground(Color.pink);
        adminBtn.setForeground(Color.orange);
        customerBtn.setBackground(Color.pink);
        customerBtn.setForeground(Color.orange);
        setVisible(true);
    }

    private void initComponents() {
        //字体设置
        nameLabel.setFont(new Font("华文琥珀", Font.PLAIN, 40));
        nameLabel.setPreferredSize(new Dimension(0, 80));
        Font centerFont = new Font("华文楷体", Font.PLAIN, 50);
        adminBtn.setFont(centerFont);
        customerBtn.setFont(centerFont);

        // 将按钮添加到面板
        centerPanel.add(adminBtn);
        centerPanel.add(customerBtn);

        //设置按钮动作
        adminBtn.addActionListener(e -> {
            // 创建管理员登录窗口
            AdminLoginView adminLoginView = new AdminLoginView(this); // 创建AdminLoginView实例
            setVisible(false); // 隐藏当前的StartView窗口
        });

        customerBtn.addActionListener(e -> {
            // 创建客户登录窗口
            CustomerLoginView customerLoginView = new CustomerLoginView(this); // 创建AdminLoginView实例
            setVisible(false); // 隐藏当前的StartView窗口
        });
    }
    private void layoutComponents() {
        Container contentPane = getContentPane();
        // 将标题标签添加到内容面板的北部
        contentPane.add(nameLabel, BorderLayout.NORTH);
        // 将按钮面板添加到内容面板的中部
        contentPane.add(centerPanel, BorderLayout.CENTER);
    }

}

