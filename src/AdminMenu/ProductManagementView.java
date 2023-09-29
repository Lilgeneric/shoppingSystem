package AdminMenu;

import Base.BaseWindow;
import Base.DatabaseManager;
import Base.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class ProductManagementView extends BaseWindow {
    private JTable productTable;
    private JButton refreshButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField searchField;
    private JButton searchButton;
    private JButton backButton;

    public ProductManagementView() {
        super("商品管理", 800, 600);
        initComponents();
        layoutComponents();
        loadProductData();
        setVisible(true);
    }

    private void initComponents() {
        productTable = new JTable();
        refreshButton = new JButton("刷新");
        addButton = new JButton("添加");
        editButton = new JButton("修改");
        deleteButton = new JButton("删除");
        searchField = new JTextField(20);
        searchButton = new JButton("查询");
        backButton = new JButton("返回");

        refreshButton.addActionListener(e -> loadProductData());
        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        searchButton.addActionListener(e -> searchProduct());
        backButton.addActionListener(e -> goBackToMainMenu());
    }

    private void layoutComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JLabel("查询商品信息:"));
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        buttonPanel.add(backButton);

        add(new JScrollPane(productTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadProductData() {
        // 定义表格的列名
        String[] columnNames = {"商品编号", "商品名称", "生产厂家", "生产日期", "型号", "进货价", "零售价格", "数量"};

        // 获取商品数据列表
        List<Product> productList = DatabaseManager.getAllProducts();

        // 创建一个 DefaultTableModel，并设置列名和初始数据
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // 将商品数据添加到表格模型中
        for (Product product : productList) {
            model.addRow(product.toTableRow());
        }

        // 设置表格的模型
        productTable.setModel(model);
    }

    // 添加商品信息
    private void addProduct() {
        // Create a custom dialog for adding product information
        JDialog addProductDialog = new JDialog(this, "添加商品信息", true);
        addProductDialog.setLayout(new GridLayout(8, 2));

        JTextField nameField = new JTextField();
        JTextField manufacturerField = new JTextField();
        JTextField productionDateField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField purchasePriceField = new JTextField();
        JTextField retailPriceField = new JTextField();
        JTextField quantityField = new JTextField();

        addProductDialog.add(new JLabel("商品名称:"));
        addProductDialog.add(nameField);
        addProductDialog.add(new JLabel("生产厂家:"));
        addProductDialog.add(manufacturerField);
        addProductDialog.add(new JLabel("生产日期 (yyyy-MM-dd):"));
        addProductDialog.add(productionDateField);
        addProductDialog.add(new JLabel("型号:"));
        addProductDialog.add(modelField);
        addProductDialog.add(new JLabel("进货价:"));
        addProductDialog.add(purchasePriceField);
        addProductDialog.add(new JLabel("零售价格:"));
        addProductDialog.add(retailPriceField);
        addProductDialog.add(new JLabel("数量:"));
        addProductDialog.add(quantityField);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                // 从输入字段中获取值
                String productName = nameField.getText();
                String manufacturer = manufacturerField.getText();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = dateFormat.parse(productionDateField.getText());
                long productionDate = parsedDate.getTime(); // 将解析后的日期转换为毫秒数

                String model = modelField.getText();
                double purchasePrice = Double.parseDouble(purchasePriceField.getText());
                double retailPrice = Double.parseDouble(retailPriceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                // 创建一个新的 Product 对象
                Product newProduct = new Product(0, productName, manufacturer, productionDate, model,
                        purchasePrice, retailPrice, quantity);

                // 将新商品添加到数据库
                boolean insertionSuccessful = DatabaseManager.addProduct(newProduct);
                if (insertionSuccessful) {
                    JOptionPane.showMessageDialog(this, "商品信息已添加。", "提示", JOptionPane.INFORMATION_MESSAGE);
                    addProductDialog.dispose();
                    loadProductData(); // 刷新商品表格
                } else {
                    JOptionPane.showMessageDialog(this, "添加商品信息失败，请稍后再试。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "输入格式不正确，请检查输入内容。", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (java.text.ParseException ex) {
                JOptionPane.showMessageDialog(this, "日期输入格式不正确，请使用'yyyy-MM-dd'格式。", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        addProductDialog.add(saveButton);


        addProductDialog.pack();
        addProductDialog.setLocationRelativeTo(this); // 将窗口设置在屏幕中心
        addProductDialog.setVisible(true);
    }

    // 修改商品信息
    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product selectedProduct = getProductFromSelectedRow(selectedRow);

            JDialog editProductDialog = new JDialog(this, "编辑商品信息", true);
            editProductDialog.setLayout(new GridLayout(8, 2));

            JTextField nameField = new JTextField(selectedProduct.getProductName());
            JTextField manufacturerField = new JTextField(selectedProduct.getManufacturer());
            JTextField productionDateField = new JTextField(getFormattedDate(selectedProduct.getProductionDate()));
            JTextField modelField = new JTextField(selectedProduct.getModel());
            JTextField purchasePriceField = new JTextField(String.valueOf(selectedProduct.getPurchasePrice()));
            JTextField retailPriceField = new JTextField(String.valueOf(selectedProduct.getRetailPrice()));
            JTextField quantityField = new JTextField(String.valueOf(selectedProduct.getQuantity()));

            editProductDialog.add(new JLabel("商品名称:"));
            editProductDialog.add(nameField);
            editProductDialog.add(new JLabel("生产厂家:"));
            editProductDialog.add(manufacturerField);
            editProductDialog.add(new JLabel("生产日期:"));
            editProductDialog.add(productionDateField);
            editProductDialog.add(new JLabel("型号:"));
            editProductDialog.add(modelField);
            editProductDialog.add(new JLabel("进货价:"));
            editProductDialog.add(purchasePriceField);
            editProductDialog.add(new JLabel("零售价格:"));
            editProductDialog.add(retailPriceField);
            editProductDialog.add(new JLabel("数量:"));
            editProductDialog.add(quantityField);

            JButton saveButton = new JButton("保存");
            saveButton.addActionListener(e -> {
                try {
                    String productName = nameField.getText();
                    String manufacturer = manufacturerField.getText();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date parsedDate = dateFormat.parse(productionDateField.getText());
                    long productionDate = parsedDate.getTime();

                    String model = modelField.getText();
                    double purchasePrice = Double.parseDouble(purchasePriceField.getText());
                    double retailPrice = Double.parseDouble(retailPriceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());

                    selectedProduct.setProductName(productName);
                    selectedProduct.setManufacturer(manufacturer);
                    selectedProduct.setProductionDate(productionDate);
                    selectedProduct.setModel(model);
                    selectedProduct.setPurchasePrice(purchasePrice);
                    selectedProduct.setRetailPrice(retailPrice);
                    selectedProduct.setQuantity(quantity);

                    boolean updateSuccessful = DatabaseManager.updateProduct(selectedProduct);
                    if (updateSuccessful) {
                        JOptionPane.showMessageDialog(this, "商品信息已更新。", "提示", JOptionPane.INFORMATION_MESSAGE);
                        editProductDialog.dispose();
                        loadProductData();
                    } else {
                        JOptionPane.showMessageDialog(this, "更新商品信息失败，请稍后再试。", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "输入格式不正确，请检查输入内容。", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (java.text.ParseException ex) {
                    JOptionPane.showMessageDialog(this, "日期输入格式不正确，请使用'yyyy-MM-dd'格式。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            editProductDialog.add(saveButton);
            editProductDialog.pack();
            editProductDialog.setLocationRelativeTo(this); // 将窗口设置在屏幕中心
            editProductDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "请先选择要修改的商品。", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private Product getProductFromSelectedRow(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        int idColumnIndex = model.findColumn("商品编号");
        int nameColumnIndex = model.findColumn("商品名称");
        int manufacturerColumnIndex = model.findColumn("生产厂家");
        int productionDateColumnIndex = model.findColumn("生产日期");
        int modelColumnIndex = model.findColumn("型号");
        int purchasePriceColumnIndex = model.findColumn("进货价");
        int retailPriceColumnIndex = model.findColumn("零售价格");
        int quantityColumnIndex = model.findColumn("数量");

        int productId = (int) model.getValueAt(selectedRow, idColumnIndex);
        String productName = (String) model.getValueAt(selectedRow, nameColumnIndex);
        String manufacturer = (String) model.getValueAt(selectedRow, manufacturerColumnIndex);
        long productionDate = parseDateStringToTimestamp((String) model.getValueAt(selectedRow, productionDateColumnIndex));
        String modelValue = (String) model.getValueAt(selectedRow, modelColumnIndex);
        double purchasePrice = (double) model.getValueAt(selectedRow, purchasePriceColumnIndex);
        double retailPrice = (double) model.getValueAt(selectedRow, retailPriceColumnIndex);
        int quantity = (int) model.getValueAt(selectedRow, quantityColumnIndex);

        return new Product(productId, productName, manufacturer, productionDate, modelValue, purchasePrice, retailPrice, quantity);
    }
    private long parseDateStringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // 返回默认值或者适当的错误值
        }
    }
    private String getFormattedDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(timestamp));
    }

    // 删除商品信息
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "确定要删除选中的商品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                Product selectedProduct = getProductFromSelectedRow(selectedRow);

                boolean deletionSuccessful = DatabaseManager.deleteProduct(selectedProduct.getId());
                if (deletionSuccessful) {
                    JOptionPane.showMessageDialog(this, "商品信息已删除。", "提示", JOptionPane.INFORMATION_MESSAGE);
                    loadProductData(); // 刷新商品表格
                } else {
                    JOptionPane.showMessageDialog(this, "删除商品信息失败，请稍后再试。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "请先选择要删除的商品。", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    // 查询商品信息
    private void searchProduct() {
        String keyword = searchField.getText();
        if (!keyword.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0); // 清空表格数据

            // 从数据库或缓存中获取与关键字匹配的商品数据
            List<Product> matchedProducts = DatabaseManager.searchProducts(keyword);

            for (Product product : matchedProducts) {
                Object[] rowData = {
                        product.getId(),
                        product.getProductName(),
                        product.getManufacturer(),
                        getFormattedDate(product.getProductionDate()),
                        product.getModel(),
                        product.getPurchasePrice(),
                        product.getRetailPrice(),
                        product.getQuantity()
                };
                model.addRow(rowData);
            }
        } else {
            // 如果关键字为空，恢复显示所有商品数据
            loadProductData();
        }
    }


    private void goBackToMainMenu() {
        dispose(); // 关闭当前窗口
        new AdminMainView(); // 返回到主菜单窗口
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.noddraw", "true");

        SwingUtilities.invokeLater(ProductManagementView::new);
    }
}
