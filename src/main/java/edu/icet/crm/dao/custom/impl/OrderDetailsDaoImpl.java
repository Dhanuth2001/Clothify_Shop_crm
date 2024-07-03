package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.dao.custom.OrderDetailDao;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.entity.OrderDetailsEntity;
import edu.icet.crm.entity.ProductEntity;
import edu.icet.crm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsDaoImpl implements OrderDetailDao {
    private final Connection connection;



    public OrderDetailsDaoImpl() throws SQLException, ClassNotFoundException {


        this.connection = DbConnection.getInstance().getConnection();

    }

    @Override
    public List<OrderDetailsEntity> getAll() {
        /*try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM OrderDetails", OrderDetailsEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/ return null;
    }

    @Override
    public OrderDetailsEntity getById(Integer id) {
       /* try (Session session = HibernateUtil.getSession()) {
            return session.get(OrderDetailsEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/ return null;
    }

    @Override
    public boolean add(OrderDetailsEntity entity) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(OrderDetailsEntity entity) {
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
        } finally {
            session.close();
        }
    }


    @Override
    public boolean delete(Integer id) {
//        String deleteOrderDetailsQuery = "DELETE FROM orderdetails WHERE orderID = ?";
//        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteOrderDetailsQuery)) {
//            deleteStatement.setInt(1, id);
//            deleteStatement.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            OrderDetailsEntity orderDetails = session.get(OrderDetailsEntity.class, id);
            if (orderDetails != null) {
                session.delete(orderDetails);
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
        } finally {
            session.close();
        }
    }

    @Override
    public List<OrderDetailsEntity> getOrderDetailsByOrderId(int orderID) {
//        String query = "SELECT orderID, productID, quantity, unitPrice,size FROM orderdetails WHERE orderID = ?";
//        List<OrderDetailsEntity> orderDetailsEntities = new ArrayList<>();
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, orderID);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
//                orderDetailsEntity.setOrderID(resultSet.getInt("orderID"));
//                orderDetailsEntity.setProductID(resultSet.getInt("productID"));
//                orderDetailsEntity.setQuantity(resultSet.getInt("quantity"));
//                orderDetailsEntity.setUnitPrice(resultSet.getDouble("unitPrice"));
//                orderDetailsEntity.setSizes(resultSet.getString("size"));
//                orderDetailsEntities.add(orderDetailsEntity);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.out.println(orderDetailsEntities);
//        return orderDetailsEntities;

        List<OrderDetailsEntity> orderDetailsList = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            orderDetailsList = session.createQuery("FROM OrderDetailsEntity WHERE orderID = :orderId", OrderDetailsEntity.class)
                    .setParameter("orderId", orderID)
                    .list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return orderDetailsList;
    }

    @Override
    public List<Integer> getProductIDsByOrderID(Integer orderId) {
//        List<Integer> productIDs = new ArrayList<>();
//        String query = "SELECT productID FROM orderDetails WHERE orderID = ?";
//
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, orderId);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    int productID = resultSet.getInt("productID");
//                    productIDs.add(productID);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return productIDs;

       /* try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT productID FROM OrderDetails WHERE orderID = :orderId", Integer.class)
                    .setParameter("orderId", orderId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/

        List<Integer> productIDs = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            productIDs = session.createQuery("SELECT productID FROM OrderDetailsEntity WHERE orderID = :orderId", Integer.class)
                    .setParameter("orderId", orderId)
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
        return productIDs;
    }

    @Override
    public OrderDetailsEntity getOrderDetailsByOrderIDandProductID(Integer orderId, Integer productId) {
        /*String query = "SELECT orderID, productID, quantity, unitPrice,size FROM orderdetails WHERE orderID = ? AND productId = ?";
        OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                orderDetailsEntity.setOrderID(resultSet.getInt("orderID"));
                orderDetailsEntity.setProductID(resultSet.getInt("productID"));
                orderDetailsEntity.setQuantity(resultSet.getInt("quantity"));
                orderDetailsEntity.setUnitPrice(resultSet.getDouble("unitPrice"));
                orderDetailsEntity.setSizes(resultSet.getString("size"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetailsEntity;*/

        /*try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM OrderDetails WHERE orderID = :orderId AND productID = :productId", OrderDetailsEntity.class)
                    .setParameter("orderId", orderId)
                    .setParameter("productId", productId)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        OrderDetailsEntity orderDetailsEntity = null;
        try  {
            transaction = session.beginTransaction();
            orderDetailsEntity = session.createQuery("FROM OrderDetailsEntity WHERE orderID = :orderId AND productID = :productId", OrderDetailsEntity.class)
                    .setParameter("orderId", orderId)
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
        return orderDetailsEntity;
    }

    @Override
    public boolean addOrderDetails(List<OrderDetailsEntity> orderDetailsEntities) {
        /*String insertOrderDetailQuery = "INSERT INTO orderdetails (orderID, productID, quantity, size, unitPrice) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement orderDetailStatement = connection.prepareStatement(insertOrderDetailQuery)) {
            for (OrderDetailsEntity orderDetailsEntity : orderDetailsEntities) {
                orderDetailStatement.setInt(1, orderDetailsEntity.getOrderID());
                orderDetailStatement.setInt(2, orderDetailsEntity.getProductID());
                orderDetailStatement.setInt(3, orderDetailsEntity.getQuantity());
                orderDetailStatement.setString(4, orderDetailsEntity.getSizes());
                orderDetailStatement.setDouble(5, orderDetailsEntity.getUnitPrice());


                orderDetailStatement.addBatch(); // Add to batch for execution
            }

            // Execute batch insert
            int[] batchResult = orderDetailStatement.executeBatch();
            return batchResult.length == orderDetailsEntities.size(); // Check if all inserts were successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }*/

//        Transaction transaction = null;
//        try (Session session = HibernateUtil.getSession()) {
//            transaction = session.beginTransaction();
//            for (OrderDetailsEntity orderDetailsEntity : orderDetailsEntities) {
//                session.save(orderDetailsEntity);
//            }
//            transaction.commit();
//            return true;
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//            return false;
//        }

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (OrderDetailsEntity orderDetailsEntity : orderDetailsEntities) {
                session.persist(orderDetailsEntity);
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
    public Map<String, Integer> getTopSellingProductsByCategory() throws SQLException {
        String query = "SELECT p.category, SUM(od.quantity) AS totalQuantity " +
                "FROM orderDetails od " +
                "JOIN product p ON od.productID = p.productID " +
                "JOIN orders o ON od.orderID = o.orderID " +
                "GROUP BY p.category " +
                "ORDER BY totalQuantity DESC " +
                "LIMIT 5";

        Map<String, Integer> topSellingProducts = new HashMap<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString("category");
                int totalQuantity = rs.getInt("totalQuantity");
                topSellingProducts.put(category, totalQuantity);
            }
        }

        return topSellingProducts;

        /*try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT p.category, SUM(od.quantity) AS totalQuantity " +
                    "FROM OrderDetails od " +
                    "JOIN od.product p " +
                    "JOIN od.order o " +
                    "GROUP BY p.category " +
                    "ORDER BY totalQuantity DESC";
            List<Object[]> result = session.createQuery(hql, Object[].class).list();
            Map<String, Integer> topSellingProducts = new HashMap<>();
            for (Object[] row : result) {
                String category = (String) row[0];
                Integer totalQuantity = ((Number) row[1]).intValue(); // Cast to Integer
                topSellingProducts.put(category, totalQuantity);
            }
            return topSellingProducts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
    }
}
