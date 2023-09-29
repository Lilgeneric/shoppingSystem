package CustomerMenu;

import Base.BaseWindow;
import Base.DatabaseManager;
import Base.Product;
import Base.PurchaseHistory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Base.DatabaseManager.getAllProducts;

public class ShoppingManagementView extends BaseWindow {
    private JTable productTable;
    private JTable cartTable;
    private JButton addToCartButton;
    private JButton removeFromCartButton;
    private JButton updateQuantityButton;
    private JButton checkoutButton;
    private JButton historyButton;
    private JButton backButton;
    private List<PurchaseHistory> purchaseHistoryList = new ArrayList<>(); // 新增购物历史记录列表

    private DefaultTableModel productTableModel;
    private DefaultTableModel cartTableModel;
    private List<Product> productsInCart = new ArrayList<>();


    public ShoppingManagementView(String username) {
        super("购物管理", 1000, 600);
        initComponents(username);
        layoutComponents();
        loadProductData();
        loadCartData(username);
        setVisible(true);
    }

    private void initComponents(String username) {
        productTable = new JTable();
        cartTable = new JTable();
        addToCartButton = new JButton("加入购物车");
        removeFromCartButton = new JButton("移除商品");
        updateQuantityButton = new JButton("修改数量");
        checkoutButton = new JButton("结账");
        historyButton = new JButton("查看购物历史");
        backButton = new JButton("返回");

        addToCartButton.addActionListener(e -> addToCart(username));
        removeFromCartButton.addActionListener(e -> removeFromCart(username));
        updateQuantityButton.addActionListener(e -> updateQuantity(username));
        checkoutButton.addActionListener(e -> checkout(username));
        historyButton.addActionListener(e -> viewHistory(username));
        backButton.addActionListener(e -> goBackToMainMenu(username));
    }

    private void layoutComponents() {
        JPanel productPanel = new JPanel(new BorderLayout());
        productTableModel = new DefaultTableModel(new String[]{"商品编号", "商品名称", "售卖价格", "数量"}, 0);
        productTable.setModel(productTableModel);
        productPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        JPanel productButtonPanel = new JPanel();
        // TODO: 添加到购物车
        productPanel.add(productButtonPanel, BorderLayout.SOUTH);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartTableModel = new DefaultTableModel(new String[]{"商品编号", "商品名称", "售卖价格", "数量"}, 0);
        cartTable.setModel(cartTableModel);
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        JPanel cartButtonPanel = new JPanel();
        // TODO: 撤销与更新
        cartPanel.add(cartButtonPanel, BorderLayout.SOUTH);

        JPanel controlPanel = new JPanel();
        controlPanel.add(addToCartButton);
        controlPanel.add(removeFromCartButton);
        controlPanel.add(updateQuantityButton);
        controlPanel.add(checkoutButton);
        controlPanel.add(historyButton);
        controlPanel.add(backButton);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productPanel, cartPanel);
        splitPane.setDividerLocation(500);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadProductData() {
        productTableModel.setRowCount(0); // Clear existing data

        List<Product> allProducts = DatabaseManager.getAllProducts();
        for (Product product : allProducts) {
            productTableModel.addRow(new Object[]{
                    product.getId(),
                    product.getProductName(),
                    product.getRetailPrice(),
                    product.getQuantity()
            });
        }
    }

    private void loadCartData(String username) {
        cartTableModel.setRowCount(0);

        productsInCart = DatabaseManager.getCartItems(username);
        for (Product product : productsInCart) {
            cartTableModel.addRow(new Object[]{
                    product.getId(),
                    product.getProductName(),
                    product.getRetailPrice(),
                    product.getQuantity()
            });
        }
    }


    private void addToCart(String username) {
        int selectedRowIndex = productTable.getSelectedRow();
        if (selectedRowIndex >= 0) {
            Product selectedProduct = getProductFromRow(selectedRowIndex, productTableModel);

            if (selectedProduct != null) {
                int availableStock = selectedProduct.getQuantity();
                int desiredQuantity = 1;

                // Check if the selected product is already in the cart
                boolean productInCart = false;
                Product cartProduct = null;
                for (Product product : productsInCart) {
                    if (product.getId() == selectedProduct.getId()) {
                        productInCart = true;
                        cartProduct = product;
                        desiredQuantity = product.getQuantity() + 1;
                        break;
                    }
                }

                if (desiredQuantity > availableStock) {
                    desiredQuantity = availableStock;
                }

                if (productInCart) {
                    DatabaseManager.updateCartItemQuantity(username, selectedProduct.getId(), desiredQuantity);
                    if (cartProduct != null) {
                        cartProduct.setQuantity(desiredQuantity); // Update quantity in productsInCart list
                    }
                } else {
                    DatabaseManager.addToCart(username, selectedProduct.getId(), desiredQuantity);
                    selectedProduct.setQuantity(desiredQuantity);
                    productsInCart.add(selectedProduct);
                }

                loadCartData(username);
            }
        }
    }

    private void removeFromCart(String username) {
        int selectedRowIndex = cartTable.getSelectedRow();
        if (selectedRowIndex >= 0) {
            Product selectedProduct = getProductFromCartRow(selectedRowIndex, cartTableModel, productsInCart);
            if (selectedProduct != null) {
                int productId = selectedProduct.getId();
                DatabaseManager.removeFromCart(username, productId);
                loadCartData(username);
            }
        }
    }



    private void updateQuantity(String username) {
        int selectedRowIndex = cartTable.getSelectedRow();
        if (selectedRowIndex >= 0) {
            Product selectedProduct = getProductFromCartRow(selectedRowIndex, cartTableModel, productsInCart);
            int maxStockQuantity = selectedProduct.getMaxStockQuantity();

            int newQuantity = Integer.parseInt(JOptionPane.showInputDialog(this, "请输入新数量:"));

            if (newQuantity <= 0) {
                // 将商品从购物车中移除
                DatabaseManager.removeFromCart(username, selectedProduct.getId());
            } else if (newQuantity > maxStockQuantity) {
                JOptionPane.showMessageDialog(this, "超过最大库存数量。");
            } else {
                DatabaseManager.updateCartItemQuantity(username, selectedProduct.getId(), newQuantity);
            }

            // 重新加载购物车数据
            loadCartData(username);
        }
    }



    private void checkout(String username) {
        double totalAmount = 0;

        // 从购物车商品中计算总金额，并创建购物历史记录对象
        for (Product cartItem : productsInCart) {
            totalAmount += cartItem.getRetailPrice() * cartItem.getQuantity();
            PurchaseHistory purchase = new PurchaseHistory(
                    0, // id 自增，数据库会自动生成
                    username,
                    cartItem.getId(),
                    new Date(), // 使用当前时间作为购买日期
                    cartItem.getRetailPrice() * cartItem.getQuantity(),
                    cartItem.getQuantity()
            );
            purchaseHistoryList.add(purchase); // 将购物历史记录对象添加到列表中
        }

        // 查询当前用户的总消费金额
        double currentTotalSpending = DatabaseManager.getCustomerTotalSpending(username);

        // 将本次消费金额加上之前的总消费金额
        double newTotalSpending = currentTotalSpending + totalAmount;

        // 更新商品的库存数量
        for (Product cartItem : productsInCart) {
            int productId = cartItem.getId();
            int purchasedQuantity = cartItem.getQuantity();

            // 获取原商品的库存数量
            int maxStockQuantity = cartItem.getMaxStockQuantity();

            // 更新商品的库存数量
            int newStockQuantity = maxStockQuantity - purchasedQuantity;
            DatabaseManager.updateProductStockQuantity(productId, newStockQuantity);
        }

        // 将购物信息插入到 purchase_history 表中
        for (PurchaseHistory purchase : purchaseHistoryList) {
            DatabaseManager.insertPurchaseHistory(purchase);
        }

        // 更新客户的消费金额
        DatabaseManager.updateCustomerConsumption(username, newTotalSpending);

        // 更新客户的等级
        DatabaseManager.updateCustomerLevel(username, newTotalSpending);

        // 清空购物车
        DatabaseManager.clearCart(username);

        // 重新加载购物车数据
        loadCartData(username);

        // 显示消息，指示结账成功
        JOptionPane.showMessageDialog(this, "结账成功！总金额：" + totalAmount);

        dispose();
        new ShoppingManagementView(username);
    }

    private void viewHistory(String username) {
        // 获取当前用户的购物历史记录
        List<PurchaseHistory> purchaseHistoryList = DatabaseManager.getPurchaseHistoryForUser(username);

        // 创建一个用于显示购物历史的表格模型
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("购买日期");
        model.addColumn("商品名称");
        model.addColumn("购买数量");
        model.addColumn("总金额");

        // 将购物历史记录添加到表格模型中
        for (PurchaseHistory purchase : purchaseHistoryList) {
            String purchaseDate = purchase.getPurchaseDate().toString();
            String productName = purchase.getProductName();
            int quantity = purchase.getQuantity();
            double totalAmount = purchase.getTotalAmount();

            model.addRow(new Object[]{purchaseDate, productName, quantity, totalAmount});
        }

        // 创建一个表格并将表格模型设置为其数据源
        JTable historyTable = new JTable(model);

        // 创建一个滚动面板，将表格添加到滚动面板中
        JScrollPane scrollPane = new JScrollPane(historyTable);

        // 创建一个对话框，将滚动面板添加到对话框中
        JDialog historyDialog = new JDialog();
        historyDialog.setTitle("购物历史记录");
        historyDialog.getContentPane().add(scrollPane);
        historyDialog.pack();
        historyDialog.setLocationRelativeTo(this); // 使对话框居中显示
        historyDialog.setVisible(true);
    }


    private void goBackToMainMenu(String username) {
        dispose();
        new CustomerMainView(username);
    }

    private Product getProductFromRow(int rowIndex, DefaultTableModel model) {
        int productId = (int) model.getValueAt(rowIndex, 0);
        for (Product product : getAllProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }
    private Product getProductFromCartRow(int rowIndex, DefaultTableModel model, List<Product> cartProducts) {
        int productId = (int) model.getValueAt(rowIndex, 0);
        for (Product product : cartProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String dummyUsername = "testuser"; // Replace with actual username
            ShoppingManagementView shoppingView = new ShoppingManagementView(dummyUsername);
        });
    }
}
