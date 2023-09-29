package CustomerMenu;

import Base.BaseWindow;
import Base.DatabaseManager;
import StartMenu.CustomerLoginView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerInfoForm extends BaseWindow {
    private JTextField phoneField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JButton saveButton = new JButton("保存信息");
    private String username;
    private CustomerLoginView customerLoginView;

    public CustomerInfoForm(String username, CustomerLoginView customerLoginView) {
        super("注册信息补全", 400, 300);
        this.username = username;
        this.customerLoginView = customerLoginView; // 保存CustomerLoginView实例

        initComponents();
        layoutComponents();

        setVisible(true);
    }


    private void initComponents() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCustomerInfo();
                // 关闭当前的CustomerLoginView界面
                customerLoginView.dispose();
                //直接登陆
                new CustomerMainView(username);
                dispose();
            }
        });
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("手机号:"));
        mainPanel.add(phoneField);

        mainPanel.add(new JLabel("邮箱:"));
        mainPanel.add(emailField);

        mainPanel.add(new JLabel()); // 空标签用于占位
        mainPanel.add(saveButton);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void saveCustomerInfo() {
        // 在这里将填写的客户信息保存到数据库中
        String phone = phoneField.getText();
        String email = emailField.getText();

        boolean saved = DatabaseManager.saveCustomerInfo(username, phone, email);

        if (saved) {
            JOptionPane.showMessageDialog(this, "信息保存成功。", "成功", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // 关闭信息填写窗口
        } else {
            JOptionPane.showMessageDialog(this, "信息保存失败，请稍后再试。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

}
