package edu.icet.crm.service;

import edu.icet.crm.db.DbConnection;
import edu.icet.crm.model.OrderDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsService {
    private Connection connection= DbConnection.getInstance().getConnection();
    public OrderDetailsService() throws SQLException, ClassNotFoundException {

    }
    public List<OrderDetails> getOrderDetailsByOrderId(Integer orderId) {
        String query = "SELECT orderID, productID, quantity, unitPrice,size FROM orderdetails WHERE orderID = ?";
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderID(resultSet.getInt("orderID"));
                orderDetails.setProductID(resultSet.getInt("productID"));
                orderDetails.setQuantity(resultSet.getInt("quantity"));
                orderDetails.setUnitPrice(resultSet.getDouble("unitPrice"));
                orderDetails.setSizes(resultSet.getString("size"));
                orderDetailsList.add(orderDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(orderDetailsList);
        return orderDetailsList;
    }

    public OrderDetails getOrderDetailsByOrderIDandProductID(Integer orderId,Integer productId) {
        String query = "SELECT orderID, productID, quantity, unitPrice,size FROM orderdetails WHERE orderID = ? AND productId = ?";
        OrderDetails orderDetails = new OrderDetails();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                orderDetails.setOrderID(resultSet.getInt("orderID"));
                orderDetails.setProductID(resultSet.getInt("productID"));
                orderDetails.setQuantity(resultSet.getInt("quantity"));
                orderDetails.setUnitPrice(resultSet.getDouble("unitPrice"));
                orderDetails.setSizes(resultSet.getString("size"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }

    public List<Integer> getProductIDsByOrderID(int orderID) {
        List<Integer> productIDs = new ArrayList<>();
        String query = "SELECT productID FROM orderDetails WHERE orderID = ?";

        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int productID = resultSet.getInt("productID");
                    productIDs.add(productID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productIDs;
    }

    public boolean deleteOrderDetail(Integer orderId, Integer productId) {
        String query = "DELETE FROM orderDetails WHERE orderId = ? AND productID = ?";
        try (PreparedStatement preparedStatement= connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, productId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateOrderDetail(OrderDetails orderDetails) {
        String query = "UPDATE orderDetails SET quantity = ?, unitPrice = ?, size = ? WHERE orderID = ? AND productID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, orderDetails.getQuantity());
            preparedStatement.setDouble(2, orderDetails.getUnitPrice());
            preparedStatement.setString(3, orderDetails.getSizes());
            preparedStatement.setInt(4, orderDetails.getOrderID());
            preparedStatement.setInt(5, orderDetails.getProductID());


            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
