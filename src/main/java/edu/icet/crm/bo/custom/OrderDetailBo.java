package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.OrderDetails;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OrderDetailBo extends SuperBo {


    List<OrderDetails> getOrderDetailsByOrderId(int orderID);

    List<Integer> getProductIDsByOrderID(Integer orderId);

    OrderDetails getOrderDetailsByOrderIDandProductID(Integer orderId, Integer productId);

    boolean deleteOrderDetails(Integer orderID) throws SQLException;

    boolean addOrderDetails(List<OrderDetails> orderDetailList);

    Map<String, Integer> getTopSellingProductsByCategory();
}
