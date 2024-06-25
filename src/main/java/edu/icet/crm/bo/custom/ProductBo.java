package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.OrderDetails;
import edu.icet.crm.dto.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductBo extends SuperBo {
    List<Product> getAllProducts();

    List<String> getProductCategories();

    Product getProductById(Integer productId);

    boolean addProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProduct(Integer productId) throws SQLException;

    List<String> getSizesByProductId(int productId);

    int getAvailableQuantityByProductId(int productId);

    boolean restoreProductQuantities(List<OrderDetails> deletedOrderDetailList);

    boolean updateProductQuantities(List<OrderDetails> orderDetailList);

    double getUnitPriceByProductId(int productID);

    List<Integer> getAllProductIDs();
}
