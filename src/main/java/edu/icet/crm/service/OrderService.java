package edu.icet.crm.service;

import edu.icet.crm.db.DbConnection;
import edu.icet.crm.model.Order;
import edu.icet.crm.model.OrderDetails;
import edu.icet.crm.model.Supplier;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private OrderDetailsService orderDetailsService;
    private Connection connection= DbConnection.getInstance().getConnection();
    public OrderService() throws SQLException, ClassNotFoundException {
        this.orderDetailsService = new OrderDetailsService();
    }

    public boolean deleteOrder(Integer orderId) {
        String query = "DELETE FROM Order WHERE orderId = ?";
        try (PreparedStatement preparedStatement= connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTotalCost(Integer orderId, Double totalCost) {
        String query = "UPDATE orders SET totalCost = ? WHERE orderID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, totalCost);
            preparedStatement.setInt(2, orderId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Order getOrderById(Integer orderId) {
        String query = "SELECT orderID, employeeID,customerID,discount, totalCost, paymentType, datePlaced FROM orders WHERE orderID = ? ";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order order = extractOrder(resultSet);
                List<OrderDetails> orderDetailsList = orderDetailsService.getOrderDetailsByOrderId(orderId);
                order.setOrderDetailList(orderDetailsList);
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Order extractOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderID(resultSet.getInt("orderID"));
        order.setEmployeeID(resultSet.getInt("employeeID"));
        order.setCustomerID(resultSet.getInt("customerID"));
        order.setTotalCost(resultSet.getDouble("totalCost"));
        order.setPaymentType(resultSet.getString("paymentType"));
        order.setDatePlaced(resultSet.getDate("datePlaced").toLocalDate());
        order.setDiscount(resultSet.getDouble("discount"));
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT orderID, employeeID, customerID,discount, totalCost, paymentType, datePlaced FROM orders";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {



                Order order = extractOrder(resultSet);
                List<OrderDetails> orderDetailsList = orderDetailsService.getOrderDetailsByOrderId(resultSet.getInt("orderID"));
                order.setOrderDetailList(orderDetailsList);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("this is order"+orders);
        return orders;
    }

    public boolean updateOrder(Order order) {

        String query = "UPDATE orders SET employeeID = ?, customerID = ?, discount = ?, totalCost = ?, paymentType = ?, datePlaced = ? WHERE orderID = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, order.getEmployeeID());
            preparedStatement.setInt(2, order.getCustomerID());
            preparedStatement.setDouble(3, order.getDiscount());
            preparedStatement.setDouble(4, order.getTotalCost());
            preparedStatement.setString(5, order.getPaymentType());
            preparedStatement.setDate(6, Date.valueOf(order.getDatePlaced()));
            preparedStatement.setInt(7, order.getOrderID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Double getTotalCostByOrderID(Integer orderID) {

        String query = "SELECT totalCost FROM orders WHERE orderID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("totalCost");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getDiscountByOrderID(Integer orderID) {
        String query = "SELECT discount FROM orders WHERE orderID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("discount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
