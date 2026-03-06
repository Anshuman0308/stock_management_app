package org.example;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "product_name")
    private String productName;
    
    private double price;
    
    @Column(name = "stock_left")
    private int stockLeft;
    
    private int sell;
    
    private double high;

    public Stock() {}

    public Stock(int id, String productName, double price, Date lastUpdated) {
        this.id = id;
        this.productName = productName;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStockLeft() { return stockLeft; }
    public void setStockLeft(int stockLeft) { this.stockLeft = stockLeft; }
    public int getSell() { return sell; }
    public void setSell(int sell) { this.sell = sell; }
    public double getHigh() { return high; }
    public void setHigh(double high) { this.high = high; }

    @Override
    public String toString() {
        return "Stock{id=" + id + ", productName='" + productName + "', price=" + price + ", stockLeft=" + stockLeft + ", sell=" + sell + ", high=" + high + '}';
    }
}
