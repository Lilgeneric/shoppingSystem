package AdminMenu;

import Base.BaseWindow;
import Base.Customer;
import Base.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;


public class CustomerManagementView extends BaseWindow {
    private JTable customerTable;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton backButton;
    private JTextField searchField;
    private JButton searchButton;

    public CustomerManagementView() {
        super("客户管理", 800, 600);
        initComponents();
        layoutComponents();
        loadCustomerData();
        setVisible(true);
    }

    private void initComponents() {
        customerTable = new JTable();
        refreshButton = new JButton("刷新");
        deleteButton = new JButton("删除");
        searchField = new JTextField(20);
        searchButton = new JButton("查询");
        backButton = new JButton("返回");

        refreshButton.addActionListener(e -> loadCustomerData());
        deleteButton.addActionListener(e -> deleteSelectedCustomer());
        searchButton.addActionListener(e -> searchCustomer());
        backButton.addActionListener(e -> goBackToMainMenu());

    }

    private void layoutComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JLabel("查询用户名或ID:"));
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);

        add(new JScrollPane(customerTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCustomerData() {
        // 定义表格的列名
        String[] columnNames = {"客户ID", "用户名", "用户级别", "用户注册时间", "累计消费总金额", "手机号", "邮箱"};

        // 获取客户数据列表
        List<Customer> customerList = DatabaseManager.getAllCustomers();

        // 创建一个 DefaultTableModel，并设置列名和初始数据
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // 将客户数据添加到表格模型中
        for (Customer customer : customerList) {
            model.addRow(customer.toTableRow());
        }

        // 设置表格的模型
        customerTable.setModel(model);
    }

    private void deleteSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "确定要删除选中的客户吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // 获取选中行的客户对象
                DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
                int uidColumnIndex = model.findColumn("客户ID"); // 此处根据表格中 "客户ID" 列的标题找到列索引
                int customerId = (int) model.getValueAt(selectedRow, uidColumnIndex);

                // 在数据库中执行删除操作，示例中假设有一个方法 deleteCustomerById 进行删除
                boolean deletionSuccessful = DatabaseManager.deleteCustomerById(customerId);

                if (deletionSuccessful) {
                    JOptionPane.showMessageDialog(this, "删除成功。", "提示", JOptionPane.INFORMATION_MESSAGE);

                    // 刷新 customerTable
                    loadCustomerData();
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败，请稍后再试。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "请先选择要删除的客户。", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }




    private void searchCustomer() {
        String keyword = searchField.getText();
        if (!keyword.isEmpty()) {
            List<Customer> customerList = DatabaseManager.getAllCustomers();
            List<Customer> searchResults = new ArrayList<>();

            for (Customer customer : customerList) {
                if (customer.getUsername().contains(keyword) || Integer.toString(customer.getId()).equals(keyword)) {
                    searchResults.add(customer);
                }
            }

            populateTableWithSearchResults(searchResults);
        }
    }

    private void populateTableWithSearchResults(List<Customer> searchResults) {
        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0); // Clear existing rows in the table

        for (Customer customer : searchResults) {
            model.addRow(customer.toTableRow());
        }
    }
    //返回
    private void goBackToMainMenu() {
        dispose(); // 关闭当前窗口
        new AdminMainView(); // 打开 AdminMainView 窗口
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerManagementView::new);
    }
}
