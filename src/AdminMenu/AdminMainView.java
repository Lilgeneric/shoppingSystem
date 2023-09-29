package AdminMenu;

import Base.BaseWindow;
import StartMenu.StartView;
import StartMenu.AdminLoginView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMainView extends BaseWindow {
    private JPanel centerPanel = new JPanel(); // 这里可以根据需要选择适当的布局管理器

    public AdminMainView() {
        super("购物管理系统管理员界面", 600, 600);
        //setExtendedState(JFrame.MAXIMIZED_BOTH); // 设置全屏显示

        initComponents();
        layoutComponents();
        setVisible(true);
    }

    private void initComponents() {
        centerPanel.setLayout(new GridLayout(2, 2, 20, 20));
        // 创建功能按钮
        JButton passwordManagementBtn = new JButton("密码管理");
        JButton customerManagementBtn = new JButton("客户管理");
        JButton productManagementBtn = new JButton("商品管理");
        JButton logoutBtn = new JButton("退出登录");
        //字体设置
        Font centerFont = new Font("仿宋", Font.PLAIN, 30);
        passwordManagementBtn.setFont(centerFont);
        customerManagementBtn.setFont(centerFont);
        productManagementBtn.setFont(centerFont);
        logoutBtn.setFont(centerFont);

        // 添加按钮事件监听器
        passwordManagementBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 打开密码管理界面
                new PasswordManagementView();
                dispose();
            }
        });

        customerManagementBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 打开客户管理界面
                new CustomerManagementView();
                dispose();
            }
        });

        productManagementBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 打开商品管理界面
                new ProductManagementView();
                dispose();
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 返回到登录界面
                StartView newStartView = new StartView(); // 创建一个新的StartView实例
                newStartView.setVisible(true); // 显示新的StartView窗口
                dispose(); // 关闭当前的AdminLoginView窗口
            }
        });

        // 将按钮添加到面板
        centerPanel.add(passwordManagementBtn);
        centerPanel.add(customerManagementBtn);
        centerPanel.add(productManagementBtn);
        centerPanel.add(logoutBtn);
        // 使用GridBagLayout布局管理器，设置按钮均匀填充并居中对齐
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(20, 20, 20, 20); // 设置按钮之间的间距

        constraints.gridx = 0;
        constraints.gridy = 0;
        centerPanel.add(passwordManagementBtn, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        centerPanel.add(customerManagementBtn, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        centerPanel.add(productManagementBtn, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        centerPanel.add(logoutBtn, constraints);
    }

    private void layoutComponents() {
        // 使用合适的布局管理器布局按钮
        // 设置中心面板在BorderLayout的CENTER位置
        add(centerPanel, BorderLayout.CENTER);
    }
}
