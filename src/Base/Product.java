package Base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
    private int id;
    private String productName;
    private String manufacturer;
    private long productionDate;
    private String model;
    private double purchasePrice;
    private double retailPrice;
    private int quantity;
    private int maxStockQuantity; // 最大库存数量属性

    public Product(int id, String productName, String manufacturer, long productionDate, String model,
                   double purchasePrice, double retailPrice, int quantity) {
        this.id = id;
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.productionDate = productionDate;
        this.model = model;
        this.purchasePrice = purchasePrice;
        this.retailPrice = retailPrice;
        this.quantity = quantity;
    }
    public Product(int id, String productName, double retailPrice, int quantity, int maxStockQuantity) {
        this.id = id;
        this.productName = productName;
        this.retailPrice = retailPrice;
        this.quantity = quantity;
        this.maxStockQuantity = maxStockQuantity;
    }


    public Object[] toTableRow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedProductionDate = dateFormat.format(new Date(productionDate));

        return new Object[]{id, productName, manufacturer, formattedProductionDate, model, purchasePrice, retailPrice, quantity};
    }


    public int getMaxStockQuantity() {
        return maxStockQuantity;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public long getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(long productionDate) {
        this.productionDate = productionDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
