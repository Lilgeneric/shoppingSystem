package StartMenu;

import Base.Customer;
import Base.DatabaseManager;
import Base.LoginBaseView;
import Base.UserType;
import CustomerMenu.CustomerInfoForm;
import CustomerMenu.CustomerMainView;
import CustomerMenu.ForgotPasswordView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerLoginView extends LoginBaseView {
    private JButton forgotPasswordButton;
    public CustomerLoginView(StartView startView) {
        super("客户登录", startView, 600, 400);
        // 设置特定于客户的内容...
        nameLabel.setText("客户登陆");
        // 其他特定于客户的初始化...
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        forgotPasswordButton = new JButton("忘记密码");
        Font centerFont = new Font("仿宋", Font.PLAIN, 20);
        registerBtn.setFont(centerFont);
        centerPanel.add(registerBtn);
        forgotPasswordButton.setFont(centerFont);
        centerPanel.add(forgotPasswordButton);

        loginBtn.addActionListener((ActionEvent e) -> {
            String username = userTxt.getText();
            String password = new String(pwdField.getPassword());

            if (DatabaseManager.loginUser(username, password, UserType.CUSTOMER)) {
                // 登录成功
                new CustomerMainView(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "登录失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerBtn.addActionListener((ActionEvent e) -> {
            String username = userTxt.getText();
            String password = new String(pwdField.getPassword());

            // 验证用户名和密码是否满足要求
            if (!Customer.isValidUsername(username)) {
                JOptionPane.showMessageDialog(this, "用户名不满足要求，请重新输入。", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (!Customer.isValidPassword(password)) {
                JOptionPane.showMessageDialog(this, "密码不满足要求，请重新输入。", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (DatabaseManager.userExists(username, UserType.CUSTOMER)) {
                JOptionPane.showMessageDialog(this, "该账号已经注册过", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                // 如果用户名尚未被使用，则注册用户
                DatabaseManager.registerUser(username, password, UserType.CUSTOMER);
                JOptionPane.showMessageDialog(this, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);

                // 打开信息填写界面
                CustomerInfoForm infoForm = new CustomerInfoForm(username,this);
                infoForm.setVisible(true);
                dispose();
            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ForgotPasswordView ForgotPasswordView = new ForgotPasswordView();
                ForgotPasswordView.setVisible(true);
                dispose();
            }
        });

    }

    protected void layoutComponents() {
        // 调用父类的布局方法
        super.layoutComponents();

        // registerBtn
        springLayout.putConstraint(SpringLayout.WEST, registerBtn, 50, SpringLayout.EAST, loginBtn);
        springLayout.putConstraint(SpringLayout.NORTH, registerBtn, 0, SpringLayout.NORTH, loginBtn);

        // forgotPasswordButton
        springLayout.putConstraint(SpringLayout.WEST, forgotPasswordButton, 10, SpringLayout.EAST, registerBtn); // 设置水平偏移
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, forgotPasswordButton, 0, SpringLayout.VERTICAL_CENTER, registerBtn);
    }

}
