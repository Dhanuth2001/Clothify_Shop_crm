package edu.icet.crm.dao.custom;

import edu.icet.crm.dao.CrudDao;
import edu.icet.crm.dto.Order;
import edu.icet.crm.dto.Sales;
import edu.icet.crm.entity.OrderEntity;
import net.sf.jasperreports.engine.JRException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDao extends CrudDao<OrderEntity> {
    public Map<String, Double> getMonthlySalesData() throws SQLException;
    public String generateOrderId();
    List<OrderEntity> getOrdersByEmployeeAndDateRange(int employeeId, LocalDate startDate, LocalDate endDate) throws SQLException;

    List<OrderEntity> getOrdersByEmployeeAndDate(int employeeId, LocalDate date) throws SQLException;

    byte[] generateOrderBill(Order order) throws JRException, SQLException, ClassNotFoundException;

    List<Sales> getSalesData();
}

