package CustomerMenu;

import Base.Customer;
import Base.BaseWindow;
import Base.DatabaseManager;
import StartMenu.StartView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMainView extends BaseWindow {
    private JPanel centerPanel = new JPanel();

    public CustomerMainView(String username) {
        super("购物管理系统客户界面", 600, 600);
        initComponents(username);
        layoutComponents();
        setVisible(true);
    }

    private void initComponents(final String username) {
        centerPanel.setLayout(new GridLayout(2, 2, 20, 20));

        JButton viewInfoBtn = new JButton("信息查看");
        JButton passwordManagementBtn = new JButton("密码管理");
        JButton shoppingBtn = new JButton("购物");
        JButton logoutBtn = new JButton("退出登录");

        Font centerFont = new Font("仿宋", Font.PLAIN, 30);
        viewInfoBtn.setFont(centerFont);
        passwordManagementBtn.setFont(centerFont);
        shoppingBtn.setFont(centerFont);
        logoutBtn.setFont(centerFont);

        viewInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 使用传入的 username 获取当前登录的用户
                Customer loggedInCustomer = DatabaseManager.getCustomerByUsername(username);

                if (loggedInCustomer != null) {
                    // 创建并显示信息查看界面
                    InformationManagementView infoView = new InformationManagementView(loggedInCustomer);
                    infoView.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(CustomerMainView.this, "无法获取当前登录用户信息。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        passwordManagementBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 打开密码管理界面
                new CustomerPasswordManagementView(username);
                dispose();
            }
        });

        shoppingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 打开购物界面
                new ShoppingManagementView(username);
                dispose();
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 返回到登录界面
                dispose();
                StartView newStartView = new StartView();
                newStartView.setVisible(true);
                dispose();
            }
        });

        centerPanel.add(viewInfoBtn);
        centerPanel.add(passwordManagementBtn);
        centerPanel.add(shoppingBtn);
        centerPanel.add(logoutBtn);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(20, 20, 20, 20);

        constraints.gridx = 0;
        constraints.gridy = 0;
        centerPanel.add(viewInfoBtn, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        centerPanel.add(passwordManagementBtn, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        centerPanel.add(shoppingBtn, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        centerPanel.add(logoutBtn, constraints);
    }

    private void layoutComponents() {
        add(centerPanel, BorderLayout.CENTER);
    }

}
