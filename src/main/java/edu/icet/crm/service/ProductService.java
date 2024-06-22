package edu.icet.crm.service;

import edu.icet.crm.db.DbConnection;

import edu.icet.crm.model.Product;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private Connection connection = DbConnection.getInstance().getConnection();

    public ProductService() throws SQLException, ClassNotFoundException {
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT productID, name, category, size, unitPrice, quantity, supplierID,dateAdded FROM Product";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Product product = extractProduct(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product extractProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("productID"),
                resultSet.getDate("dateAdded").toLocalDate(),
                resultSet.getString("name"),
                resultSet.getString("category"),
                resultSet.getString("size"),
                resultSet.getDouble("unitPrice"),
                resultSet.getInt("quantity"),
                resultSet.getInt("supplierID")
        );
    }

    public List<String> getProductCategories() {
        List<String> categories = new ArrayList<>();
        try (
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT category FROM product")) {

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public Product getProductById(Integer id) {
        String query = "SELECT productID, name, category, size, unitPrice, quantity, supplierID, dateAdded FROM Product WHERE productID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractProduct(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addProduct(Product product) {
        String query = "INSERT INTO Product (productID, name, category, size, unitPrice, quantity, supplierID, dateAdded) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, product.getProductID());

            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getCategory());
            preparedStatement.setString(4, product.getSize());
            preparedStatement.setDouble(5, product.getUnitPrice());
            preparedStatement.setInt(6, product.getQuantity());
            preparedStatement.setInt(7, product.getSupplierID());
            preparedStatement.setDate(8, Date.valueOf(product.getDate()));

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        String query = "UPDATE Product SET name = ?, category = ?, size = ?, unitPrice = ?, quantity = ?, supplierID = ? ,dateAdded = ? WHERE productID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getCategory());
            preparedStatement.setString(3, product.getSize());
            preparedStatement.setDouble(4, product.getUnitPrice());
            preparedStatement.setInt(5, product.getQuantity());
            preparedStatement.setInt(6, product.getSupplierID());
            preparedStatement.setDate(7, Date.valueOf(product.getDate()));
            preparedStatement.setInt(8, product.getProductID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(Integer id) {
        String query = "DELETE FROM Product WHERE productID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProductQuantity(Integer productId, int quantityDifference) {
        String query = "UPDATE product SET quantity = quantity - ? WHERE productID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, quantityDifference);
            preparedStatement.setInt(2, productId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getProductQuantityById(Integer productId) {
        String query = "SELECT quantity FROM product WHERE productID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // or throw an exception
    }
}