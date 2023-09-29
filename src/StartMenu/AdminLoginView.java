package StartMenu;

import AdminMenu.AdminMainView;
import Base.DatabaseManager;
import Base.UserType;
import Base.LoginBaseView;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.ActionListener;

public class AdminLoginView extends LoginBaseView {

    public AdminLoginView(StartView startView) {
        super("管理员登录", startView, 1000, 700);
        // 设置特定于管理员的内容...
        nameLabel.setText("管理员登陆");
        // 其他特定于管理员的初始化...
        // nameLabel.setForeground(Color.RED);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        // 添加管理员独有的组件...
        loginBtn.addActionListener((ActionEvent e) -> {
            String username = userTxt.getText();
            String password = new String(pwdField.getPassword());

            if (DatabaseManager.loginUser(username, password, UserType.ADMIN)) {
                // 登录成功
                new AdminMainView();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "登录失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    @Override
    protected void layoutComponents() {
        super.layoutComponents();
        // 设置管理员独有的布局...
    }
}
