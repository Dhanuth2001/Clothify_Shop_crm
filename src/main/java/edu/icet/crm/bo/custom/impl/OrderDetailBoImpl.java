package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.OrderDetailBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.OrderDao;
import edu.icet.crm.dao.custom.OrderDetailDao;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.dto.OrderDetails;
import edu.icet.crm.dto.Product;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.entity.OrderDetailsEntity;
import edu.icet.crm.entity.ProductEntity;
import edu.icet.crm.util.DaoType;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderDetailBoImpl implements OrderDetailBo {
    private final ModelMapper modelMapper;
    private final OrderDetailDao orderDetailDao;

    public OrderDetailBoImpl() throws SQLException, ClassNotFoundException {
        this.orderDetailDao = DaoFactory.getInstance().getDao(DaoType.ORDERDETAILS);
        this.modelMapper = new ModelMapper();
    }
    @Override
    public List<OrderDetails> getOrderDetailsByOrderId(int orderID) {
        List<OrderDetailsEntity> orderDetailsEntities = orderDetailDao.getOrderDetailsByOrderId(orderID);
        return orderDetailsEntities.stream()
                .map(entity -> modelMapper.map(entity, OrderDetails.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getProductIDsByOrderID(Integer orderId) {
        return orderDetailDao.getProductIDsByOrderID(orderId);
    }

    @Override
    public OrderDetails getOrderDetailsByOrderIDandProductID(Integer orderId, Integer productId) {
        OrderDetailsEntity orderDetailsEntity = orderDetailDao.getOrderDetailsByOrderIDandProductID(orderId,productId);
        if (orderDetailsEntity == null) {
            return null;
        }
        return modelMapper.map(orderDetailsEntity, OrderDetails.class);
    }

    @Override
    public boolean deleteOrderDetails(Integer orderID) throws SQLException {
        return orderDetailDao.delete(orderID);
    }

    @Override
    public boolean addOrderDetails(List<OrderDetails> orderDetailList) {
        List<OrderDetailsEntity> orderDetailsEntities = orderDetailList.stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailsEntity.class))
                .collect(Collectors.toList());

        // Call the addOrderDetails method with the list of OrderDetailsEntity objects
        return orderDetailDao.addOrderDetails(orderDetailsEntities);
    }

    @Override
    public Map<String, Integer> getTopSellingProductsByCategory() {
        try {
            return orderDetailDao.getTopSellingProductsByCategory();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
