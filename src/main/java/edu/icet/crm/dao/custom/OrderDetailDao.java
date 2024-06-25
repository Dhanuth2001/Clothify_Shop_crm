package edu.icet.crm.dao.custom;

import edu.icet.crm.dao.CrudDao;
import edu.icet.crm.dto.OrderDetails;
import edu.icet.crm.entity.OrderDetailsEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OrderDetailDao extends CrudDao<OrderDetailsEntity> {

    List<OrderDetailsEntity> getOrderDetailsByOrderId(int orderID);
    public List<Integer> getProductIDsByOrderID(Integer orderId);
    public OrderDetailsEntity getOrderDetailsByOrderIDandProductID(Integer orderId, Integer productId);


    boolean addOrderDetails(List<OrderDetailsEntity> orderDetailsEntities);

    public Map<String, Integer> getTopSellingProductsByCategory() throws SQLException;
}
