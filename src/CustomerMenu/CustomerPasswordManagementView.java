package CustomerMenu;

import Base.BaseWindow;
import Base.DatabaseManager;
import Base.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerPasswordManagementView extends BaseWindow {
    private JTextField oldPasswordField = new JTextField(20);
    private JTextField newPasswordField = new JTextField(20);
    private JTextField confirmPasswordField = new JPasswordField(20);
    private JButton updatePasswordBtn = new JButton("更新密码");
    private JButton returnBtn = new JButton("返回");
    private String loggedInUsername; // 保存登录的用户名

    public CustomerPasswordManagementView(String username) {
        super("密码管理", 400, 300);
        this.loggedInUsername = username;

        initComponents();
        layoutComponents();

        setVisible(true);
    }

    private void initComponents() {
        updatePasswordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePassword();
            }
        });
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToCustomerMainView();
            }
        });
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("旧密码:"));
        mainPanel.add(oldPasswordField);

        mainPanel.add(new JLabel("新密码:"));
        mainPanel.add(newPasswordField);

        mainPanel.add(new JLabel("确认新密码:"));
        mainPanel.add(confirmPasswordField);

        mainPanel.add(new JLabel()); // 空标签用于占位
        mainPanel.add(updatePasswordBtn);

        mainPanel.add(new JLabel()); // 空标签用于占位
        mainPanel.add(returnBtn);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void updatePassword() {
        // 获取旧密码、新密码、确认新密码的值
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 从数据库中获取客户的旧密码
        String savedOldPassword = getOldPasswordFromDatabase(loggedInUsername, UserType.CUSTOMER);

        System.out.println("输入的旧密码: '" + oldPassword + "', 长度: " + oldPassword.length());
        System.out.println("数据库中的密码: '" + savedOldPassword + "', 长度: " + savedOldPassword.length());
        // 验证旧密码是否正确
        if (!DatabaseManager.hashPassword(oldPassword).equals(savedOldPassword)) {
            JOptionPane.showMessageDialog(this, "旧密码输入错误，请重新输入。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 如果新密码和确认密码一致，更新密码
        if (newPassword.equals(confirmPassword)) {
            boolean passwordUpdated = updatePasswordInDatabase(loggedInUsername, newPassword, UserType.CUSTOMER);
            if (passwordUpdated) {
                JOptionPane.showMessageDialog(this, "密码已成功更新。", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "密码更新失败，请稍后再试。", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "新密码和确认密码不一致，请重新输入。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getOldPasswordFromDatabase(String username, UserType userType) {
        // 从数据库获取旧密码的逻辑
        String oldPassword = DatabaseManager.getPassword(username, userType);
        return oldPassword;
    }

    private boolean updatePasswordInDatabase(String username, String newPassword, UserType userType) {
        // 更新密码到数据库的逻辑
        return DatabaseManager.updatePassword(username, newPassword, userType);
    }

    private void returnToCustomerMainView() {
        // 返回到客户主界面的逻辑
        // 创建一个新的 CustomerMainView 实例并显示
        new CustomerMainView(loggedInUsername);
        dispose(); // 关闭当前的 CustomerPasswordManagementView 窗口
    }

}
