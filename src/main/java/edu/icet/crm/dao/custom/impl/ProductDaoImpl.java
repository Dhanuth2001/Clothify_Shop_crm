package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.dao.custom.ProductDao;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.entity.OrderDetailsEntity;
import edu.icet.crm.entity.ProductEntity;
import edu.icet.crm.util.CategoryType;
import edu.icet.crm.util.HibernateUtil;
import edu.icet.crm.util.SizeType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    private final Connection connection;


    public ProductDaoImpl() throws SQLException, ClassNotFoundException {

        this.connection = DbConnection.getInstance().getConnection();

    }
    @Override
    public List<ProductEntity> getAll() {
//       List<ProductEntity> productEntities = new ArrayList<>();
//        String query = "SELECT productID, name, category, size, unitPrice, quantity, supplierID,dateAdded FROM Product";
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query);
//                ResultSet resultSet = preparedStatement.executeQuery()) {
//            while (resultSet.next()) {
//                ProductEntity productEntity = extractProducts(resultSet);
//                productEntities.add(productEntity);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return productEntities;
        Session session = HibernateUtil.getSession();
        List<ProductEntity> productEntities = new ArrayList<>();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            productEntities = session.createQuery("FROM ProductEntity", ProductEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return productEntities;
    }

    @Override
    public ProductEntity getById(Integer id) {
        /*String query = "SELECT productID, name, category, size, unitPrice, quantity, supplierID, dateAdded FROM Product WHERE productID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractProducts(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        ProductEntity productEntity = null;
        try  {
            transaction = session.beginTransaction();
            productEntity = session.get(ProductEntity.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return productEntity;
    }

    private ProductEntity extractProducts(ResultSet resultSet) throws SQLException {
        return new ProductEntity(
                resultSet.getInt("productID"),
                resultSet.getDate("dateAdded").toLocalDate(),
                resultSet.getString("name"),
                CategoryType.valueOf(resultSet.getString("category")),
                SizeType.valueOf(resultSet.getString("size")),
                resultSet.getDouble("unitPrice"),
                resultSet.getInt("quantity"),
                resultSet.getInt("supplierID")
        );
    }

    @Override
    public boolean add(ProductEntity entity) {
        /*String query = "INSERT INTO Product (productID, name, category, size, unitPrice, quantity, supplierID, dateAdded) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, entity.getProductID());

            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getCategory());
            preparedStatement.setString(4, entity.getSize());
            preparedStatement.setDouble(5, entity.getUnitPrice());
            preparedStatement.setInt(6, entity.getQuantity());
            preparedStatement.setInt(7, entity.getSupplierID());
            preparedStatement.setDate(8, Date.valueOf(entity.getDate()));

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        /*Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(entity);
        session.getTransaction().commit();
        session.close();
        return false;*/
        //return false;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean update(ProductEntity entity) {
//        String query = "UPDATE Product SET name = ?, category = ?, size = ?, unitPrice = ?, quantity = ?, supplierID = ? ,dateAdded = ? WHERE productID = ?";
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setString(1, entity.getName());
//            preparedStatement.setString(2, entity.getCategoryType().toString());
//            preparedStatement.setString(3, entity.getSizeType().toString());
//            preparedStatement.setDouble(4, entity.getUnitPrice());
//            preparedStatement.setInt(5, entity.getQuantity());
//            preparedStatement.setInt(6, entity.getSupplierID());
//            preparedStatement.setDate(7, Date.valueOf(entity.getDate()));
//            preparedStatement.setInt(8, entity.getProductID());
//
//            return preparedStatement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Integer id) {
//        String query = "DELETE FROM Product WHERE productID = ?";
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, id);
//            return preparedStatement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            ProductEntity productEntity = session.get(ProductEntity.class, id);
            if (productEntity != null) {
                session.delete(productEntity);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public List<String> getProductCategories() {
//        List<String> categories = new ArrayList<>();
//        try (
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("SELECT DISTINCT category FROM product")) {
//
//            while (resultSet.next()) {
//                String category = resultSet.getString("category");
//                categories.add(category);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return categories;

        List<String> categories = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            categories = session.createQuery("SELECT DISTINCT p.category FROM ProductEntity p", String.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return categories;
    }

    @Override
    public List<SizeType> getSizesByProductId(int productId) {
//        List<String> sizes = new ArrayList<>();
//        String query = "SELECT size FROM product WHERE productID = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, productId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                sizes.add(resultSet.getString("size"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return sizes;

        List<SizeType> sizes = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            sizes = session.createQuery("SELECT p.sizeType FROM ProductEntity p WHERE p.productID = :productId", SizeType.class)
                    .setParameter("productId", productId)
                    .list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return sizes;
    }

    @Override
    public int getAvailableQuantityByProductId(int productId) {
//        String query = "SELECT quantity FROM product WHERE productID = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, productId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getInt("quantity");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;

        Integer quantity = null;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            quantity = session.createQuery("SELECT p.quantity FROM ProductEntity p WHERE p.productID = :productId", Integer.class)
                    .setParameter("productId", productId)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return quantity != null ? quantity : 0;
    }

    @Override
    public boolean restoreProductQuantities(List<OrderDetailsEntity> deletedOrderDetailList) {
       /* String updateProductQuantityQuery = "UPDATE product SET quantity = quantity + ? WHERE productID = ?";

        try (PreparedStatement updateProductStatement = connection.prepareStatement(updateProductQuantityQuery)) {
            for (OrderDetailsEntity orderDetailsEntity : deletedOrderDetailList) {
                updateProductStatement.setInt(1, orderDetailsEntity.getQuantity());
                updateProductStatement.setInt(2, orderDetailsEntity.getProductID());

                updateProductStatement.addBatch(); // Add to batch for execution
            }

            // Execute batch update
            int[] batchResult = updateProductStatement.executeBatch();
            return batchResult.length == deletedOrderDetailList.size(); // Check if all updates were successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }*/

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            for (OrderDetailsEntity orderDetailsEntity : deletedOrderDetailList) {
                ProductEntity productEntity = session.get(ProductEntity.class, orderDetailsEntity.getProductID());
                if (productEntity != null) {
                    productEntity.setQuantity(productEntity.getQuantity() + orderDetailsEntity.getQuantity());
                    session.update(productEntity);
                }
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean updateProductQuantities(List<OrderDetailsEntity> orderDetailsEntities) {
        /*String updateProductQuantityQuery = "UPDATE product SET quantity = quantity - ? WHERE productID = ?";

        try (PreparedStatement updateProductStatement = connection.prepareStatement(updateProductQuantityQuery)) {
            for (OrderDetailsEntity orderDetailsEntity : orderDetailsEntities) {
                updateProductStatement.setInt(1, orderDetailsEntity.getQuantity());
                updateProductStatement.setInt(2, orderDetailsEntity.getProductID());

                updateProductStatement.addBatch(); // Add to batch for execution
            }

            // Execute batch update
            int[] batchResult = updateProductStatement.executeBatch();
            return batchResult.length == orderDetailsEntities.size(); // Check if all updates were successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }*/

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (OrderDetailsEntity orderDetailsEntity : orderDetailsEntities) {
                ProductEntity productEntity = session.get(ProductEntity.class, orderDetailsEntity.getProductID());
                if (productEntity != null) {
                    productEntity.setQuantity(productEntity.getQuantity() - orderDetailsEntity.getQuantity());
                    session.update(productEntity);
                }
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }
    }
    @Override
    public List<Integer> getAllProductIDs() {
//        List<Integer> productIDs = new ArrayList<>();
//        String query = "SELECT productID FROM product";
//
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    int productId = resultSet.getInt("productId");
//                    productIDs.add(productId);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return productIDs;

        List<Integer> productIDs = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            productIDs = session.createQuery("SELECT p.productID FROM ProductEntity p", Integer.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return productIDs;
    }

    @Override
    public double getUnitPriceByProductId(int productID) {
//        String query = "SELECT unitPrice FROM product WHERE productID = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, productID);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getDouble("unitPrice");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;


        Double unitPrice = null;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            unitPrice = session.createQuery("SELECT p.unitPrice FROM ProductEntity p WHERE p.productID = :productID", Double.class)
                    .setParameter("productID", productID)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return unitPrice != null ? unitPrice : 0;
    }
}
