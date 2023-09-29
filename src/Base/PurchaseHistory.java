package Base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseHistory {
    private int id;
    private String username;
    private int productId;
    private Date purchaseDate;
    private double totalAmount;
    private int quantity;
    private String productName; // 添加商品名称属性

    public PurchaseHistory(int id, String username, int productId, Date purchaseDate, double totalAmount, int quantity) {
        this.id = id;
        this.username = username;
        this.productId = productId;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
        this.quantity = quantity;
        this.productName = DatabaseManager.fetchProductName(productId); // 获取商品名称
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getProductId() {
        return productId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }



    // Add any other methods or getters/setters as needed
}
