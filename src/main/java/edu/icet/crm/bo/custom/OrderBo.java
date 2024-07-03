package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.Order;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderBo extends SuperBo {
    Order getOrderById(Integer orderId);

    List<Order> getAllOrders();

    boolean deleteOrder(Integer orderId) throws SQLException;

    boolean updateOrder(Order updatedOrder);

    String generateOrderId();

    boolean placeOrder(Order order);

    Map<String, Double> getMonthlySalesData();

    double getOrderTotalForMonth(int employeeID, int monthValue) throws SQLException;

    double getOrderTotalForDay(int employeeID, LocalDate now) throws SQLException;

    List<Order> getDailyReportData();

    List<Order> getMonthlyReportData();

    List<Order> getAnnualReportData();
}
