package CustomerMenu;

import Base.Customer;
import Base.BaseWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InformationManagementView extends BaseWindow {
    private JPanel centerPanel = new JPanel();

    public InformationManagementView(Customer customer) {
        super("信息查看", 400, 300);
        initComponents(customer);
        layoutComponents();
        setVisible(true);
    }

    private void initComponents(Customer customer) {
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        addLabelValuePair("客户ID:", String.valueOf(customer.getId()), gbc, 0, 0);
        addLabelValuePair("用户名:", customer.getUsername(), gbc, 0, 1);
        addLabelValuePair("用户级别:", customer.getUserLevel(), gbc, 0, 2);
        addLabelValuePair("注册时间:", customer.getFormattedRegistrationTime(), gbc, 0, 3);
        addLabelValuePair("累计消费总金额:", String.valueOf(customer.getTotalSpending()), gbc, 0, 4);
        addLabelValuePair("手机号:", customer.getPhoneNumber(), gbc, 0, 5);
        addLabelValuePair("邮箱:", customer.getEmail(), gbc, 0, 6);

        // 添加返回按钮
        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 返回到CustomerMainView
                new CustomerMainView(customer.getUsername());
                dispose(); // 关闭当前窗口
            }
        });
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        centerPanel.add(backButton, gbc);
    }
    private void addLabelValuePair(String label, String value, GridBagConstraints gbc, int gridx, int gridy) {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        centerPanel.add(new JLabel(label), gbc);

        gbc.gridx = gridx + 1;
        gbc.weightx = 1.0;
        centerPanel.add(new JLabel(value), gbc);
    }


    private void layoutComponents() {
        add(centerPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Customer dummyCustomer = new Customer();
            dummyCustomer.setId(1);
            dummyCustomer.setUsername("John Doe");
            dummyCustomer.setUserLevel("金牌客户");
            dummyCustomer.setRegistrationTime("1679030400"); // A sample Unix timestamp for "2023-03-16"
            dummyCustomer.setTotalSpending(10000.0);
            dummyCustomer.setPhoneNumber("123-456-7890");
            dummyCustomer.setEmail("john@example.com");

            InformationManagementView infoView = new InformationManagementView(dummyCustomer);
        });
    }
}
