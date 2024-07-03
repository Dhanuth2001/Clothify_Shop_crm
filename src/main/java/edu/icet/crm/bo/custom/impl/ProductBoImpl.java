package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.ProductBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.ProductDao;
import edu.icet.crm.dto.OrderDetails;
import edu.icet.crm.dto.Product;
import edu.icet.crm.entity.OrderDetailsEntity;
import edu.icet.crm.entity.ProductEntity;
import edu.icet.crm.util.DaoType;
import edu.icet.crm.util.SizeType;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductBoImpl implements ProductBo {
    private final ProductDao productDao;
    private final ModelMapper modelMapper;

    public ProductBoImpl() throws SQLException, ClassNotFoundException {
        this.productDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
        this.modelMapper = new ModelMapper();
    }
    @Override
    public List<Product> getAllProducts() {
        List<ProductEntity> productEntities = productDao.getAll();
        return productEntities.stream()
                .map(entity -> modelMapper.map(entity, Product.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getProductCategories() {
        return productDao.getProductCategories();
    }

    @Override
    public Product getProductById(Integer productId) {
        ProductEntity productEntity = productDao.getById(productId);
        if (productEntity == null) {
            return null;
        }
        return modelMapper.map(productEntity, Product.class);
    }

    @Override
    public boolean addProduct(Product product) {
        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        return productDao.add(productEntity);
    }

    @Override
    public boolean updateProduct(Product product) {
        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        return productDao.update(productEntity);
    }

    @Override
    public boolean deleteProduct(Integer productId) throws SQLException {
        return productDao.delete(productId);
    }

    @Override
    public List<SizeType> getSizesByProductId(int productId) {
        return productDao.getSizesByProductId(productId);
    }

    @Override
    public int getAvailableQuantityByProductId(int productId) {
        return productDao.getAvailableQuantityByProductId(productId);
    }

    @Override
    public boolean restoreProductQuantities(List<OrderDetails> deletedOrderDetailList) {
        List<OrderDetailsEntity> orderDetailsEntities = deletedOrderDetailList.stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailsEntity.class))
                .collect(Collectors.toList());

        // Call the addOrderDetails method with the list of OrderDetailsEntity objects
        return productDao.restoreProductQuantities(orderDetailsEntities);
    }

    @Override
    public boolean updateProductQuantities(List<OrderDetails> orderDetailList) {
        List<OrderDetailsEntity> orderDetailsEntities = orderDetailList.stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailsEntity.class))
                .collect(Collectors.toList());

        // Call the addOrderDetails method with the list of OrderDetailsEntity objects
        return productDao.updateProductQuantities(orderDetailsEntities);
    }

    @Override
    public double getUnitPriceByProductId(int productID) {
        return productDao.getUnitPriceByProductId(productID);
    }

    @Override
    public List<Integer> getAllProductIDs() {
        return productDao.getAllProductIDs();
    }
}
