package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.OrderBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.OrderDao;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.dto.Order;
import edu.icet.crm.dto.Sales;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.entity.OrderEntity;
import edu.icet.crm.util.DaoType;
import net.sf.jasperreports.engine.JRException;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderBoImpl implements OrderBo {
    private final ModelMapper modelMapper;
    private final OrderDao orderDao;



    public OrderBoImpl() throws SQLException, ClassNotFoundException {
        this.orderDao = DaoFactory.getInstance().getDao(DaoType.ORDER);
        this.modelMapper = new ModelMapper();
    }
    @Override
    public Order getOrderById(Integer orderId) {
        OrderEntity orderEntity = orderDao.getById(orderId);
        if (orderEntity == null) {
            return null;
        }
        return modelMapper.map(orderEntity, Order.class);
    }

    @Override
    public List<Order> getAllOrders() {
        List<OrderEntity> orderEntities = orderDao.getAll();
        return orderEntities.stream()
                .map(entity -> modelMapper.map(entity, Order.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteOrder(Integer orderId) throws SQLException {
        return orderDao.delete(orderId);
    }

    @Override
    public boolean updateOrder(Order updatedOrder) {
        OrderEntity orderEntity = modelMapper.map(updatedOrder, OrderEntity.class);
        return orderDao.update(orderEntity);
    }

    @Override
    public String generateOrderId() {
        return orderDao.generateOrderId();
    }

    @Override
    public boolean placeOrder(Order order) {
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        return orderDao.add(orderEntity);
    }

    @Override
    public Map<String, Double> getMonthlySalesData() {
        try {
            return orderDao.getMonthlySalesData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public double getOrderTotalForMonth(int employeeId, int month) throws SQLException {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        List<OrderEntity> orders = orderDao.getOrdersByEmployeeAndDateRange(employeeId, startDate, endDate);
        return calculateTotalAmount(orders);
    }
    @Override
    public double getOrderTotalForDay(int employeeId, LocalDate date) throws SQLException {
        List<OrderEntity> orders = orderDao.getOrdersByEmployeeAndDate(employeeId, date);
        return calculateTotalAmount(orders);
    }

    @Override
    public List<Order> getDailyReportData() {
        return null;
    }

    @Override
    public List<Order> getMonthlyReportData() {
        return null;
    }

    @Override
    public List<Order> getAnnualReportData() {
        return null;
    }

    @Override
    public byte[] generateOrderBill(Order order) throws JRException, SQLException, ClassNotFoundException {
        return orderDao.generateOrderBill(order);
    }

    @Override
    public List<Sales> getSalesData() {
        return orderDao.getSalesData();
    }


    private double calculateTotalAmount(List<OrderEntity> orders) {
        double total = 0.0;
        for (OrderEntity order : orders) {
            total += order.getTotalCost();
        }
        return total;
    }
}
