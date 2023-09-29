package Base;

import Base.UserType;


import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

public class DatabaseManager {


    private static final String DB_URL = "jdbc:mariadb://localhost:3360/data";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    //注册
    public static void registerUser(String username, String password, UserType userType) {
        String tableName = (userType == UserType.ADMIN) ? "admin_users" : "customer_users";
        String insertSQL = "INSERT INTO " + tableName + " (username, password, register_time, user_level) VALUES (?, ?, ?, ?)";

        // 使用 MD5 哈希密码（仅用于示例目的）
        String hashedPassword = hashPassword(password);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);

            // 获取当前时间戳（以秒为单位）
            long currentTimeInSeconds = System.currentTimeMillis() / 1000;
            statement.setLong(3, currentTimeInSeconds);

            // 根据累计消费金额确定用户级别
            double totalSpending = 0; // 设置默认值
            String userLevel = "铜牌客户"; // 设置默认级别
            statement.setDouble(4, totalSpending);
            statement.setString(5, userLevel);
            statement.executeUpdate();
            System.out.println("用户注册成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    //注册信息补全
    public static boolean saveCustomerInfo(String username, String phone, String email) {
        String updateSQL = "UPDATE customer_users SET phone_number = ?, email = ? WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, phone);
            statement.setString(2, email);
            statement.setString(3, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //用户重复判断
    public static boolean userExists(String username, UserType userType) {
        String tableName = (userType == UserType.ADMIN) ? "admin_users" : "customer_users";
        String selectSQL = "SELECT * FROM " + tableName + " WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Return true if the user exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //登陆
    public static boolean loginUser(String username, String password, UserType userType) {
        String tableName = (userType == UserType.ADMIN) ? "admin_users" : "customer_users";
        String selectSQL = "SELECT password, password_attempts, locked FROM " + tableName + " WHERE username = ?";

        // 使用 MD5 哈希密码
        String hashedPassword = hashPassword(password);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                int passwordAttempts = resultSet.getInt("password_attempts");
                boolean locked = resultSet.getBoolean("locked");

                if (locked) {
                    // 账户已锁定，无法登录
                    return false;
                }

                if (hashedPassword.equals(storedPassword)) {
                    // 密码正确，重置密码错误次数并登录成功
                    updateLoginStatus(username, 0, false);
                    return true;
                } else {
                    // 密码错误，增加密码错误次数
                    passwordAttempts++;

                    if (passwordAttempts >= 5) {
                        // 超过五次密码错误，锁定账户
                        updateLoginStatus(username, passwordAttempts, true);
                    } else {
                        // 更新密码错误次数
                        updateLoginStatus(username, passwordAttempts, false);
                    }

                    // 密码错误，但仍有尝试次数剩余
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //更新登陆锁定状态
    public static void updateLoginStatus(String username, int passwordAttempts, boolean locked) {
        String updateSQL = "UPDATE customer_users SET password_attempts = ?, locked = ? WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setInt(1, passwordAttempts);
            statement.setBoolean(2, locked);
            statement.setString(3, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //获取密码
    public static String getPassword(String username, UserType userType) {
        String tableName = (userType == UserType.ADMIN) ? "admin_users" : "customer_users";
        String selectSQL = "SELECT password FROM " + tableName + " WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //更新密码
    public static boolean updatePassword(String username, String newPassword, UserType userType) {
        String tableName = (userType == UserType.ADMIN) ? "admin_users" : "customer_users";
        String updateSQL = "UPDATE " + tableName + " SET password = ? WHERE username = ?";

        // 使用 MD5 哈希新密码
        String hashedNewPassword = hashPassword(newPassword);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, hashedNewPassword);
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // 返回是否成功更新密码
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "密码更新失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

        }
        return false;
    }


    // 获取所有客户的信息并更新用户级别
    public static List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();

        String selectSQL = "SELECT * FROM customer_users"; // 表名为 customer_users

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("uid"));
                customer.setUsername(resultSet.getString("username"));
                customer.setUserLevel(resultSet.getString("user_level"));
                customer.setRegistrationTime(resultSet.getString("register_time"));
                customer.setTotalSpending(resultSet.getDouble("total_spending"));
                customer.setPhoneNumber(resultSet.getString("phone_number"));
                customer.setEmail(resultSet.getString("email"));

                // 获取客户当前的用户级别
                String currentUserLevel = customer.getUserLevel();

                // 获取客户当前的累计消费金额
                double currentTotalSpending = customer.getTotalSpending();

                // 判断用户级别是否需要更新，并且不是金牌客户
                if (!currentUserLevel.equals("金牌客户")) {
                    // 更新用户级别并返回是否成功
                    boolean updateSuccess = updateCustomerLevel(customer.getUsername(), currentTotalSpending);

                    if (updateSuccess) {
                        // 如果更新成功，则更新客户对象的用户级别
                        customer.setUserLevel(getNewUserLevel(currentTotalSpending));
                    }
                }

                customerList.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }
    //查找当前用户消费
    public static double getCustomerTotalSpending(String username) {
        String selectSQL = "SELECT total_spending FROM customer_users WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("total_spending");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // 如果没有找到对应用户，返回默认值
    }

    // 更新用户级别并返回是否成功
    public static boolean updateCustomerLevel(String username, double totalSpending) {
        String updateSQL = "UPDATE customer_users SET user_level = ? WHERE username = ?";

        String newUserLevel = getNewUserLevel(totalSpending);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, newUserLevel);
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 根据累计消费金额获取用户级别
    private static String getNewUserLevel(double totalSpending) {
        if (totalSpending >= 10000) {
            return "金牌客户";
        } else if (totalSpending >= 1000) {
            return "银牌客户";
        } else  {
            return "铜牌客户";
        }
    }

    //删除客户信息
    public static boolean deleteCustomerById(int customerId) {
        String deleteSQL = "DELETE FROM customer_users WHERE uid = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, customerId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //通过username获取客户信息
    public static Customer getCustomerByUsername(String username) {
        String selectSQL = "SELECT * FROM customer_users WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("uid"));
                customer.setUsername(resultSet.getString("username"));
                customer.setUserLevel(resultSet.getString("user_level"));
                customer.setRegistrationTime(resultSet.getString("register_time"));
                customer.setTotalSpending(resultSet.getDouble("total_spending"));
                customer.setPhoneNumber(resultSet.getString("phone_number"));
                customer.setEmail(resultSet.getString("email"));

                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String getEmailByUsername(String username) {
        String email = null;

        String query = "SELECT email FROM customer_users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;
    }
    // 在商品列表中查询商品
    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();

        String selectSQL = "SELECT * FROM products"; // 假设表名为 products

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String productName = resultSet.getString("product_name");
                String manufacturer = resultSet.getString("manufacturer");
                long productionDate = resultSet.getLong("production_date");
                String model = resultSet.getString("model");
                double purchasePrice = resultSet.getDouble("purchase_price");
                double retailPrice = resultSet.getDouble("retail_price");
                int quantity = resultSet.getInt("quantity");

                Product product = new Product(id, productName, manufacturer, productionDate, model,
                        purchasePrice, retailPrice, quantity);
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }
    public static String fetchProductName(int productId) {
        String productName = "";
        String selectSQL = "SELECT product_name FROM products WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                productName = resultSet.getString("product_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productName;
    }
    // 添加商品信息
    public static boolean addProduct(Product product) {
        String insertSQL = "INSERT INTO products (product_name, manufacturer, production_date, model, " +
                "purchase_price, retail_price, quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getManufacturer());
            statement.setLong(3, product.getProductionDate());
            statement.setString(4, product.getModel());
            statement.setDouble(5, product.getPurchasePrice());
            statement.setDouble(6, product.getRetailPrice());
            statement.setInt(7, product.getQuantity());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 更新商品信息
    public static boolean updateProduct(Product product) {
        String updateSQL = "UPDATE products SET product_name = ?, manufacturer = ?, production_date = ?, model = ?, " +
                "purchase_price = ?, retail_price = ?, quantity = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getManufacturer());
            statement.setLong(3, product.getProductionDate());
            statement.setString(4, product.getModel());
            statement.setDouble(5, product.getPurchasePrice());
            statement.setDouble(6, product.getRetailPrice());
            statement.setInt(7, product.getQuantity());
            statement.setInt(8, product.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 删除商品信息
    public static boolean deleteProduct(int productId) {
        String deleteSQL = "DELETE FROM products WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, productId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 搜索商品
    public static List<Product> searchProducts(String keyword) {
        List<Product> matchedProducts = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection(); // 获取数据库连接

            // 使用 LIKE 运算符来进行模糊匹配查询
            String query = "SELECT * FROM products WHERE product_name LIKE ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, "%" + keyword + "%");

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String productName = resultSet.getString("product_name");
                String manufacturer = resultSet.getString("manufacturer");
                long productionDate = resultSet.getLong("production_date");
                String model = resultSet.getString("model");
                double purchasePrice = resultSet.getDouble("purchase_price");
                double retailPrice = resultSet.getDouble("retail_price");
                int quantity = resultSet.getInt("quantity");

                // 创建 Product 对象并添加到匹配列表中
                Product product = new Product(productId, productName, manufacturer, productionDate,
                        model, purchasePrice, retailPrice, quantity);
                matchedProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return matchedProducts;
    }
    private static void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //更新商品数量
    public static boolean updateProductStockQuantity(int productId, int newStockQuantity) {
        String updateSQL = "UPDATE products SET quantity = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setInt(1, newStockQuantity);
            statement.setInt(2, productId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // 获取购物车内商品
    public static List<Product> getCartItems(String username) {
        List<Product> cartItems = new ArrayList<>();

        String selectSQL = "SELECT products.*, cart_items.quantity AS cart_quantity, products.quantity AS max_stock_quantity " +
                "FROM cart_items " +
                "JOIN products ON cart_items.product_id = products.id " +
                "WHERE cart_items.username = ?";


        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String productName = resultSet.getString("product_name");
                double retailPrice = resultSet.getDouble("retail_price");
                int cartQuantity = resultSet.getInt("cart_quantity");

                // 获取商品的最大库存数量，假设有一个方法获取
                int maxStockQuantity = resultSet.getInt("quantity");

                Product product = new Product(id, productName, retailPrice, cartQuantity, maxStockQuantity);
                cartItems.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }
    // 添加商品到购物车
    public static boolean addToCart(String username, int productId, int quantity) {
        String insertSQL = "INSERT INTO cart_items (username, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, username);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 从购物车移除商品
    public static boolean removeFromCart(String username, int productId) {
        String deleteSQL = "DELETE FROM cart_items WHERE username = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, username);
            statement.setInt(2, productId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新购物车内商品数量
    public static boolean updateCartItemQuantity(String username, int productId, int newQuantity) {
        String updateSQL = "UPDATE cart_items SET quantity = ? WHERE username = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setInt(1, newQuantity);
            statement.setString(2, username);
            statement.setInt(3, productId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //更新消费金额
    public static boolean updateCustomerConsumption(String username, double newConsumption) {
        String updateSQL = "UPDATE customer_users SET total_spending = ? WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setDouble(1, newConsumption);
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 清空购物车
    public static boolean clearCart(String username) {
        String deleteSQL = "DELETE FROM cart_items WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<PurchaseHistory> getPurchaseHistoryForUser(String username) {
        List<PurchaseHistory> purchaseHistoryList = new ArrayList<>();

        String selectSQL = "SELECT * FROM purchase_history WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productId = resultSet.getInt("product_id");
                Date purchaseDate = resultSet.getTimestamp("purchase_date");
                double totalAmount = resultSet.getDouble("total_amount");
                int quantity = resultSet.getInt("quantity");

                PurchaseHistory purchaseHistory = new PurchaseHistory(id, username, productId, purchaseDate, totalAmount, quantity);
                purchaseHistoryList.add(purchaseHistory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchaseHistoryList;
    }
    //输入进历史记录中
    public static boolean insertPurchaseHistory(PurchaseHistory purchaseHistory) {
        String insertSQL = "INSERT INTO purchase_history (username, product_id, purchase_date, total_amount, quantity) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, purchaseHistory.getUsername());
            statement.setInt(2, purchaseHistory.getProductId());
            statement.setTimestamp(3, new Timestamp(purchaseHistory.getPurchaseDate().getTime()));
            statement.setDouble(4, purchaseHistory.getTotalAmount());
            statement.setInt(5, purchaseHistory.getQuantity());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
