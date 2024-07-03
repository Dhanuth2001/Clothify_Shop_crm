package edu.icet.crm.dao.custom;

import edu.icet.crm.dao.CrudDao;
import edu.icet.crm.entity.OrderDetailsEntity;
import edu.icet.crm.entity.ProductEntity;
import edu.icet.crm.util.SizeType;

import java.util.List;

public interface ProductDao extends CrudDao<ProductEntity> {
    public List<String> getProductCategories();
    public List<SizeType> getSizesByProductId(int productId) ;


    public int getAvailableQuantityByProductId(int productId);

    public boolean restoreProductQuantities(List<OrderDetailsEntity> deletedOrderDetailList) ;

    public boolean updateProductQuantities(List<OrderDetailsEntity> orderDetailEntityList);

    public List<Integer> getAllProductIDs();

    public double getUnitPriceByProductId(int productID);
}
