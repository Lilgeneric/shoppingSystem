package Base;

import StartMenu.StartView;

import javax.swing.*;
import java.awt.*;

public class LoginBaseView extends BaseWindow {
    protected StartView startView; // 保存StartView实例

    //标题标签
    protected JLabel nameLabel = new JLabel("", JLabel.CENTER);
    protected SpringLayout springLayout = new SpringLayout();
    protected JPanel centerPanel = new JPanel(springLayout);

    //用户名标签和文本框
    protected JLabel userNameLabel = new JLabel("用户名：");
    protected JTextField userTxt = new JTextField();
    // 密码标签和密码框
    protected JLabel pwdLabel = new JLabel("密码：");
    protected JPasswordField pwdField = new JPasswordField();

    // 登录和返回按钮
    protected JButton loginBtn = new JButton("登录");
    protected JButton registerBtn = new JButton("注册");
    protected JButton backBtn = new JButton("返回");

    public LoginBaseView(String title, StartView startView, int width, int height) {
        super(title, width, height);
        this.startView = startView; // 保存StartView实例
        initComponents();
        layoutComponents();
        getRootPane().setDefaultButton(loginBtn);
//        loginBtn.setBackground(Color.cyan);
//        loginBtn.setForeground(Color.WHITE);
        setVisible(true);
    }

    protected void initComponents() {
        // ... 初始化组件 ...
        //字体设置
        nameLabel.setFont(new Font("宋体", Font.PLAIN, 40));
        nameLabel.setPreferredSize(new Dimension(0, 80));
        Font centerFont = new Font("仿宋", Font.PLAIN, 20);
        userNameLabel.setFont(centerFont);
        userTxt.setPreferredSize(new Dimension(200, 30));
        pwdLabel.setFont(centerFont);
        pwdField.setPreferredSize(new Dimension(200, 30));
        loginBtn.setFont(centerFont);
        backBtn.setFont(centerFont);

        // 将组件添加到面板
        centerPanel.add(userNameLabel);
        centerPanel.add(userTxt);
        centerPanel.add(pwdLabel);
        centerPanel.add(pwdField);
        centerPanel.add(loginBtn);
        centerPanel.add(backBtn);

        //按钮动作
        //返回
        backBtn.addActionListener(e -> {
            startView.setVisible(true); // 显示之前的StartView窗口
            dispose(); // 关闭当前的AdminLoginView窗口
        });
    }

    protected void layoutComponents() {
        // ... 布局组件 ...
        // 设置组件位置、大小等...
        Container contentPane = getContentPane();
        Spring childWidth = Spring.sum(Spring.sum(Spring.width(userNameLabel), Spring.width(userTxt)), Spring.constant(20));
        int offsetX = childWidth.getValue() / 2;

        springLayout.putConstraint(SpringLayout.WEST, userNameLabel, -offsetX, SpringLayout.HORIZONTAL_CENTER, centerPanel);
        springLayout.putConstraint(SpringLayout.NORTH, userNameLabel, 20, SpringLayout.NORTH, centerPanel);
        contentPane.add(nameLabel, BorderLayout.NORTH);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // userTxt
        springLayout.putConstraint(SpringLayout.WEST, userTxt, 20, SpringLayout.EAST, userNameLabel);
        springLayout.putConstraint(SpringLayout.NORTH, userTxt, 0, SpringLayout.NORTH, userNameLabel);

        // pwdLabel
        springLayout.putConstraint(SpringLayout.EAST, pwdLabel, 0, SpringLayout.EAST, userNameLabel);
        springLayout.putConstraint(SpringLayout.NORTH, pwdLabel, 20, SpringLayout.SOUTH, userNameLabel);

        // pwdField
        springLayout.putConstraint(SpringLayout.WEST, pwdField, 20, SpringLayout.EAST, pwdLabel);
        springLayout.putConstraint(SpringLayout.NORTH, pwdField, 0, SpringLayout.NORTH, pwdLabel);

        // loginBtn
        springLayout.putConstraint(SpringLayout.WEST, loginBtn, 50, SpringLayout.WEST, pwdLabel);
        springLayout.putConstraint(SpringLayout.NORTH, loginBtn, 20, SpringLayout.SOUTH, pwdLabel);

        // backBtn
        springLayout.putConstraint(SpringLayout.WEST, backBtn, 20, SpringLayout.WEST, centerPanel);
        springLayout.putConstraint(SpringLayout.NORTH, backBtn, 20, SpringLayout.NORTH, centerPanel);
    }

}