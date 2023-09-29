package AdminMenu;

import Base.DatabaseManager;
import Base.BaseWindow;
import Base.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import static Base.DatabaseManager.hashPassword;

public class PasswordManagementView extends BaseWindow {
    private JTextField oldPasswordField = new JTextField(20);
    private JTextField newPasswordField = new JTextField(20);
    private JTextField confirmPasswordField = new JPasswordField(20);
    private JButton updatePasswordBtn = new JButton("更新密码");
    private JButton returnBtn = new JButton("返回");
    private JButton manageCustomerPasswordBtn = new JButton("修改客户密码");

    public PasswordManagementView() {
        super("密码管理", 400, 300);

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
                returnToAdminMainView();
            }
        });
        manageCustomerPasswordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageCustomerPassword();
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updatePasswordBtn);
        buttonPanel.add(manageCustomerPasswordBtn);
        buttonPanel.add(returnBtn);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(containerPanel);
    }

    private void manageCustomerPassword() {
        String username = JOptionPane.showInputDialog(this, "请输入客户用户名:", "修改客户密码", JOptionPane.PLAIN_MESSAGE);
        if (username != null && !username.trim().isEmpty()) {
            UserType userType = UserType.CUSTOMER;
            String oldPassword = getOldPasswordFromDatabase(username, userType);
            if (oldPassword != null) {
                String newPassword = JOptionPane.showInputDialog(this, "请输入新密码:", "修改客户密码", JOptionPane.PLAIN_MESSAGE);
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                    boolean passwordUpdated = updatePasswordInDatabase(username, newPassword, userType);
                    if (passwordUpdated) {
                        JOptionPane.showMessageDialog(this, "客户密码已成功更新。", "成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "客户密码更新失败，请稍后再试。", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "客户不存在，请输入有效的客户用户名。", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updatePassword() {
        // 获取旧密码、新密码、确认新密码的值
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 从数据库中获取管理员的旧密码
        String savedOldPassword = getOldPasswordFromDatabase("admin",UserType.ADMIN);

        System.out.println("输入的旧密码: '" + oldPassword + "', 长度: " + oldPassword.length());
        System.out.println("数据库中的密码: '" + savedOldPassword + "', 长度: " + savedOldPassword.length());

        // 验证旧密码是否正确
        if (!hashPassword(oldPassword).equals(savedOldPassword)) {
            JOptionPane.showMessageDialog(this, "旧密码输入错误，请重新输入。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }



        // 如果新密码和确认密码一致，更新密码
        if (newPassword.equals(confirmPassword)) {
            boolean passwordUpdated = updatePasswordInDatabase("admin", newPassword, UserType.ADMIN);
            if (passwordUpdated) {
                JOptionPane.showMessageDialog(this, "密码已成功更新。", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "密码更新失败，请稍后再试。", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "新密码和确认密码不一致，请重新输入。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getOldPasswordFromDatabase(String Username, UserType userType) {
        // 从数据库获取旧密码的逻辑
        String oldPassword = DatabaseManager.getPassword(Username,userType);
        return oldPassword;
    }

    private boolean updatePasswordInDatabase(String Username, String newPassword, UserType userType) {
        // 更新密码到数据库的逻辑
        return DatabaseManager.updatePassword(Username, newPassword,userType);
    }
    private void returnToAdminMainView() {
        // 返回到管理员主界面的逻辑
        // 创建一个新的 AdminMainView 实例并显示
        new AdminMainView();
        dispose(); // 关闭当前的 PasswordManagementView 窗口
    }
    private void clearFields() {
        oldPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.noddraw", "true");

        new PasswordManagementView();
    }
}
