package edu.icet.crm.dao.custom;

import edu.icet.crm.dao.CrudDao;
import edu.icet.crm.dao.SuperDao;
import edu.icet.crm.dto.OrderDetails;
import edu.icet.crm.entity.OrderDetailsEntity;
import edu.icet.crm.entity.ProductEntity;

import java.util.List;

public interface ProductDao extends CrudDao<ProductEntity> {
    public List<String> getProductCategories();
    public List<String> getSizesByProductId(int productId) ;


    public int getAvailableQuantityByProductId(int productId);

    public boolean restoreProductQuantities(List<OrderDetailsEntity> deletedOrderDetailList) ;

    public boolean updateProductQuantities(List<OrderDetailsEntity> orderDetailEntityList);

    public List<Integer> getAllProductIDs();

    public double getUnitPriceByProductId(int productID);
}
