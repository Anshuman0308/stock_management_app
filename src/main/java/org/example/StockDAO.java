package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO implements AutoCloseable {
    private final Connection connection;

    public StockDAO(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public void create(Stock stock) throws SQLException {
        String sql = "INSERT INTO stocks (product_name, price) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, stock.getProductName());
            stmt.setDouble(2, stock.getPrice());
            stmt.executeUpdate();
        }
    }

    public Stock read(int id) throws SQLException {
        String sql = "SELECT * FROM stocks WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Stock(rs.getInt("id"), rs.getString("product_name"), 
                               rs.getDouble("price"), null);
            }
        }
        return null;
    }

    public List<Stock> readAll() throws SQLException {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM stocks";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stocks.add(new Stock(rs.getInt("id"), rs.getString("product_name"), 
                                   rs.getDouble("price"), null));
            }
        }
        return stocks;
    }

    public void update(Stock stock) throws SQLException {
        String sql = "UPDATE stocks SET product_name = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, stock.getProductName());
            stmt.setDouble(2, stock.getPrice());
            stmt.setInt(3, stock.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM stocks WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) connection.close();
    }
}
