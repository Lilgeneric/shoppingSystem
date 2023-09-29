package CustomerMenu;

import Base.BaseWindow;
import Base.DatabaseManager;
import Base.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ForgotPasswordView extends BaseWindow {
    private JTextField usernameField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JButton resetButton = new JButton("重置密码");
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-";


    public ForgotPasswordView() {
        super("密码重置", 400, 200);
        initComponents();
        layoutComponents();
        setVisible(true);
    }

    private void initComponents() {
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String enteredEmail = emailField.getText(); // 获取用户输入的邮箱地址

                // TODO: 从数据库中获取对应的邮箱地址
                String databaseEmail = DatabaseManager.getEmailByUsername(username);

                if (enteredEmail.equals(databaseEmail)) {
                    // 邮箱地址一致，继续执行重置密码操作
                    String newPassword = generateRandomPassword();

                    // 创建一个文本区域用于显示新密码，并设置其为只读
                    JTextArea newPasswordArea = new JTextArea(newPassword);
                    newPasswordArea.setEditable(false);

                    // 创建滚动面板以便容纳文本区域
                    JScrollPane scrollPane = new JScrollPane(newPasswordArea);
                    scrollPane.setPreferredSize(new Dimension(300, 100));

                    // 弹出一个带有文本区域的消息对话框
                    JOptionPane.showMessageDialog(ForgotPasswordView.this, scrollPane, "重置密码成功，新密码已发送至邮箱", JOptionPane.INFORMATION_MESSAGE);

                    // 更新数据库中的密码
                    boolean updated = DatabaseManager.updatePassword(username, newPassword, UserType.CUSTOMER);
                    if (updated) {
                        JOptionPane.showMessageDialog(ForgotPasswordView.this, "密码已更新至数据库。", "密码更新成功", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // 关闭当前窗口
                        new CustomerMainView(username);
                    } else {
                        JOptionPane.showMessageDialog(ForgotPasswordView.this, "密码更新失败，请稍后再试。", "密码更新失败", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    // 邮箱地址不一致，弹出提示框要求用户重新输入
                    JOptionPane.showMessageDialog(ForgotPasswordView.this, "输入的邮箱地址与预留的邮箱地址不一致，请重新输入。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("请输入用户名:"));
        mainPanel.add(usernameField);

        mainPanel.add(new JLabel("请输入预留邮箱:"));
        mainPanel.add(emailField);

        mainPanel.add(new JLabel()); // 空标签用于占位
        mainPanel.add(resetButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(mainPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }


    private String generateRandomPassword() {
        // TODO: 生成随机密码的逻辑
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        // 添加至少一个小写字母
        password.append(LOWERCASE_CHARS.charAt(random.nextInt(LOWERCASE_CHARS.length())));

        // 添加至少一个大写字母
        password.append(UPPERCASE_CHARS.charAt(random.nextInt(UPPERCASE_CHARS.length())));

        // 添加至少一个数字
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        // 添加至少一个特殊字符
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // 添加剩余的字符，保证总长度大于等于8
        int remainingLength = 8 - password.length();
        for (int i = 0; i < remainingLength; i++) {
            String characterSet = LOWERCASE_CHARS + UPPERCASE_CHARS + DIGITS + SPECIAL_CHARS;
            password.append(characterSet.charAt(random.nextInt(characterSet.length())));
        }

        // 打乱密码中的字符顺序
        for (int i = 0; i < password.length() * 2; i++) {
            int index1 = random.nextInt(password.length());
            int index2 = random.nextInt(password.length());
            char temp = password.charAt(index1);
            password.setCharAt(index1, password.charAt(index2));
            password.setCharAt(index2, temp);
        }

        return password.toString();
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.noddraw", "true");
        SwingUtilities.invokeLater(ForgotPasswordView::new);
    }
}
