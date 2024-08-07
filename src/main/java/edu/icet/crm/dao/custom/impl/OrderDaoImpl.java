package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.CustomerBo;
import edu.icet.crm.bo.custom.OrderDetailBo;
import edu.icet.crm.bo.custom.ProductBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.OrderDao;

import edu.icet.crm.dao.custom.OrderDetailDao;
import edu.icet.crm.dao.custom.ProductDao;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.dto.Customer;
import edu.icet.crm.dto.Order;
import edu.icet.crm.dto.OrderDetails;
import edu.icet.crm.dto.Sales;
import edu.icet.crm.entity.OrderDetailsEntity;
import edu.icet.crm.entity.OrderEntity;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.CategoryType;
import edu.icet.crm.util.DaoType;
import edu.icet.crm.util.HibernateUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoImpl implements OrderDao {
    private final OrderDetailBo orderDetailsBo;
    private final ProductBo productBo;
    private final CustomerBo customerBo;
    public OrderDaoImpl() throws SQLException, ClassNotFoundException {
        this.orderDetailsBo = BoFactory.getInstance().getBo(BoType.ORDERDETAILS);
        this.productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
        this.customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    }
    @Override
    public List<OrderEntity> getAll() {
//        List<OrderEntity> orderEntities = new ArrayList<>();
//        String query = "SELECT orderID, employeeID, customerID,discount, totalCost, paymentType, datePlaced FROM orders";
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query);
//                ResultSet resultSet = preparedStatement.executeQuery()) {
//            while (resultSet.next()) {
//
//
//
//                OrderEntity orderEntity = extractOrder(resultSet);
//                List<OrderDetails> orderDetailsList = orderDetailsBo.getOrderDetailsByOrderId(resultSet.getInt("orderID"));
//                orderEntity.setOrderDetailList(orderDetailsList);
//                orderEntities.add(orderEntity);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        //System.out.println("this is order"+orderEntities);
//        return orderEntities;

        List<OrderEntity> orderEntities = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            orderEntities = session.createQuery("FROM OrderEntity", OrderEntity.class).list();
            for (OrderEntity order : orderEntities) {
                order.setOrderDetailList(orderDetailsBo.getOrderDetailsByOrderId(order.getOrderID()));
            }
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return orderEntities;
    }

    private OrderEntity extractOrder(ResultSet resultSet) throws SQLException {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderID(resultSet.getInt("orderID"));
        orderEntity.setEmployeeID(resultSet.getInt("employeeID"));
        orderEntity.setCustomerID(resultSet.getInt("customerID"));
        orderEntity.setTotalCost(resultSet.getDouble("totalCost"));
        orderEntity.setPaymentType(resultSet.getString("paymentType"));
        orderEntity.setDatePlaced(resultSet.getDate("datePlaced").toLocalDate());
        orderEntity.setDiscount(resultSet.getDouble("discount"));
        return orderEntity;
    }

    @Override
    public OrderEntity getById(Integer id) {
//        String query = "SELECT orderID, employeeID,customerID,discount, totalCost, paymentType, datePlaced FROM orders WHERE orderID = ? ";
//        try (
//                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                OrderEntity orderEntity = extractOrder(resultSet);
//                List<OrderDetails> orderDetailsList = orderDetailsBo.getOrderDetailsByOrderId(id);
//                orderEntity.setOrderDetailList(orderDetailsList);
//                return orderEntity;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        OrderEntity order = null;
        try {
            transaction = session.beginTransaction();
            order = session.get(OrderEntity.class, id);
            if (order != null) {
                order.setOrderDetailList(orderDetailsBo.getOrderDetailsByOrderId(id));
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return order;
    }

    @Override
    public boolean add(OrderEntity entity) {
        /*System.out.println(entity.getEmployeeID()+""+ entity.getOrderID());
        String insertOrderQuery = "INSERT INTO orders (orderID, employeeID, customerID, discount, totalCost, paymentType, datePlaced) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Insert into orders table
            try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery)) {
                orderStatement.setInt(1, entity.getOrderID());
                orderStatement.setInt(2, entity.getEmployeeID());
                orderStatement.setInt(3, entity.getCustomerID());
                orderStatement.setDouble(4, entity.getDiscount());
                orderStatement.setDouble(5, entity.getTotalCost());
                orderStatement.setString(6, entity.getPaymentType());
                orderStatement.setDate(7, Date.valueOf(entity.getDatePlaced()));

                orderStatement.executeUpdate();
            }

            // Insert order details using OrderDetailsService
            boolean orderDetailsInserted = orderDetailsBo.addOrderDetails(entity.getOrderDetailList());

            // Update product quantities using ProductService
            boolean productsUpdated = productBo.updateProductQuantities(entity.getOrderDetailList());

            if (orderDetailsInserted && productsUpdated) {
                // Commit transaction if all operations succeed
                connection.commit();
                return true;
            } else {
                // Rollback transaction if any operation fails
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            // Rollback transaction on error
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Reset auto-commit mode
            } catch (SQLException resetException) {
                resetException.printStackTrace();
            }
        }*/

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Save the order entity
            session.persist(entity);

            // Insert order details using OrderDetailsService
            boolean orderDetailsInserted = orderDetailsBo.addOrderDetails(entity.getOrderDetailList());

            // Update product quantities using ProductService
            boolean productsUpdated = productBo.updateProductQuantities(entity.getOrderDetailList());

            // Commit transaction if all operations succeed
            if (orderDetailsInserted && productsUpdated) {
                transaction.commit();
                return true;
            } else {
                // Rollback transaction if any operation fails
                transaction.rollback();
                return false;
            }
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
    public boolean update(OrderEntity entity) {
//        String updateOrderQuery = "UPDATE orders SET employeeID = ?, customerID = ?, discount = ?, totalCost = ?, paymentType = ?, datePlaced = ? WHERE orderID = ?";
//        try {
//            // Start transaction
//            connection.setAutoCommit(false);
//
//            // Update order in orders table
//            try (PreparedStatement orderStatement = connection.prepareStatement(updateOrderQuery)) {
//                orderStatement.setInt(1, entity.getEmployeeID());
//                orderStatement.setInt(2, entity.getCustomerID());
//                orderStatement.setDouble(3, entity.getDiscount());
//                orderStatement.setDouble(4, entity.getTotalCost());
//                orderStatement.setString(5, entity.getPaymentType());
//                orderStatement.setDate(6, Date.valueOf(entity.getDatePlaced()));
//                orderStatement.setInt(7, entity.getOrderID());
//
//                orderStatement.executeUpdate();
//            }
//            List<OrderDetails> deletedOrderDetailList = orderDetailsBo.getOrderDetailsByOrderId(entity.getOrderID());
//            // Delete existing order details
//            boolean orderDetailsDeleted = orderDetailsBo.deleteOrderDetails(entity.getOrderID());
//
//            boolean deletedProductsUpdated = productBo.restoreProductQuantities(deletedOrderDetailList);
//
//            // Insert new order details
//            boolean orderDetailsInserted = orderDetailsBo.addOrderDetails(entity.getOrderDetailList());
//
//            // Update product quantities
//            boolean addedProductsUpdated = productBo.updateProductQuantities(entity.getOrderDetailList());
//
//            if (orderDetailsDeleted && orderDetailsInserted && addedProductsUpdated && deletedProductsUpdated) {
//                // Commit transaction if all operations succeed
//                connection.commit();
//                return true;
//            } else {
//                // Rollback transaction if any operation fails
//                connection.rollback();
//                return false;
//            }
//
//        } catch (SQLException e) {
//            // Rollback transaction on error
//            try {
//                connection.rollback();
//            } catch (SQLException rollbackException) {
//                rollbackException.printStackTrace();
//            }
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                connection.setAutoCommit(true); // Reset auto-commit mode
//            } catch (SQLException resetException) {
//                resetException.printStackTrace();
//            }
//        }

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Update order in orders table
            session.update(entity);

            // Get existing order details
            List<OrderDetails> deletedOrderDetailList = orderDetailsBo.getOrderDetailsByOrderId(entity.getOrderID());

            // Delete existing order details
            boolean orderDetailsDeleted = orderDetailsBo.deleteOrderDetails(entity.getOrderID());

            // Restore product quantities for deleted order details
            boolean deletedProductsUpdated = productBo.restoreProductQuantities(deletedOrderDetailList);

            // Insert new order details
            boolean orderDetailsInserted = orderDetailsBo.addOrderDetails(entity.getOrderDetailList());

            // Update product quantities for new order details
            boolean addedProductsUpdated = productBo.updateProductQuantities(entity.getOrderDetailList());

            // Commit transaction if all operations succeed
            if (orderDetailsDeleted && orderDetailsInserted && addedProductsUpdated && deletedProductsUpdated) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
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
//        List<OrderDetails> deletedOrderDetailList = orderDetailsBo.getOrderDetailsByOrderId(id);
//        String deleteOrderQuery = "DELETE FROM orders WHERE orderID = ?";
//
//        try {
//            // Start transaction
//            connection.setAutoCommit(false);
//
//            // Restore product quantities for the deleted order details
//            boolean productQuantitiesRestored = productBo.restoreProductQuantities(deletedOrderDetailList);
//
//            if (!productQuantitiesRestored) {
//                connection.rollback();
//                return false;
//            }
//
//            // Delete order details
//            boolean orderDetailsDeleted = orderDetailsBo.deleteOrderDetails(id);
//
//            if (!orderDetailsDeleted) {
//                connection.rollback();
//                return false;
//            }
//
//            // Delete the order from orders table
//            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteOrderQuery)) {
//                preparedStatement.setInt(1, id);
//                int rowsAffected = preparedStatement.executeUpdate();
//                if (rowsAffected == 0) {
//                    connection.rollback();
//                    return false;
//                }
//            }
//
//            // Commit transaction if all operations succeed
//            connection.commit();
//            return true;
//        } catch (SQLException e) {
//            // Rollback transaction on error
//            try {
//                connection.rollback();
//            } catch (SQLException rollbackException) {
//                rollbackException.printStackTrace();
//            }
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                connection.setAutoCommit(true); // Reset auto-commit mode
//            } catch (SQLException resetException) {
//                resetException.printStackTrace();
//            }
//        }

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Retrieve the order details for the specified order ID
            List<OrderDetails> deletedOrderDetailList = orderDetailsBo.getOrderDetailsByOrderId(id);

            // Restore product quantities for the deleted order details
            boolean productQuantitiesRestored = productBo.restoreProductQuantities(deletedOrderDetailList);

            if (!productQuantitiesRestored) {
                transaction.rollback();
                return false;
            }

            // Delete the order details
            boolean orderDetailsDeleted = orderDetailsBo.deleteOrderDetails(id);

            if (!orderDetailsDeleted) {
                transaction.rollback();
                return false;
            }

            // Delete the order itself
            OrderEntity order = session.get(OrderEntity.class, id);
            if (order != null) {
                session.delete(order);
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
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

    public Map<String, Double> getMonthlySalesData() throws SQLException {
        Map<String, Double> salesData = new HashMap<>();

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            String hql = "SELECT MONTHNAME(o.datePlaced), SUM(o.totalCost) " +
                    "FROM OrderEntity o " +
                    "GROUP BY MONTHNAME(o.datePlaced)";
            List<Object[]> result = session.createQuery(hql, Object[].class).getResultList();

            for (Object[] row : result) {
                String month = (String) row[0];
                Double totalSales = (Double) row[1];
                salesData.put(month, totalSales);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();  // Consider using a logger here
        } finally {
            session.close();
        }
        return salesData;
    }

    public String generateOrderId() {
       /* String newOrderId = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM orders");
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                return "4001";
            }

            ResultSet resultSet1 = connection.createStatement().executeQuery(
                    "SELECT OrderID FROM orders ORDER BY OrderID DESC LIMIT 1"
            );
            if (resultSet1.next()) {
                String lastOrderId = resultSet1.getString(1);
                int number = Integer.parseInt(lastOrderId);
                number++;
                newOrderId = String.valueOf(number);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newOrderId;*/



        String newOrderId = "";
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Query to count orders
            Long count = (Long) session.createQuery("SELECT COUNT(*) FROM OrderEntity").uniqueResult();
            if (count == 0) {
                return "4001";
            }

            // Query to get the last OrderID
            String lastOrderId = String.valueOf(session.createQuery("SELECT o.orderID FROM OrderEntity o ORDER BY o.orderID DESC")
                    .setMaxResults(1)
                    .uniqueResult());
            if (lastOrderId != null) {
                int number = Integer.parseInt(lastOrderId);
                number++;
                newOrderId = String.valueOf(number);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            session.close();
        }

        return newOrderId;

    }

    @Override
    public List<OrderEntity> getOrdersByEmployeeAndDateRange(int employeeId, LocalDate startDate, LocalDate endDate) throws SQLException {
        return null;
    }

    @Override
    public List<OrderEntity> getOrdersByEmployeeAndDate(int employeeId, LocalDate date) throws SQLException {
        return null;
    }

    @Override
    public byte[] generateOrderBill(Order order) throws JRException, SQLException, ClassNotFoundException {
       /* // Retrieve order details
        List<OrderDetails> orderDetails = order.getOrderDetailList();

        // Retrieve customer information
        Customer customer = customerBo.getCustomerById(order.getCustomerID());
        java.util.Date datePlaced = Date.valueOf(order.getDatePlaced());

        // Prepare parameters for JasperReports
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderID", order.getOrderID());
        parameters.put("customerName", customer.getName());
        parameters.put("customerEmail", customer.getContactEmail());
        parameters.put("date", datePlaced);
        parameters.put("paymentMethod", order.getPaymentType());
        parameters.put("totalAmount", order.getTotalCost());
        parameters.put("discount", order.getDiscount());

        // Calculate subtotal
        double subtotal = orderDetails.stream()
                .mapToDouble(od -> od.getQuantity() * od.getUnitPrice())
                .sum();
        parameters.put("subTotal", subtotal);




        // Prepare statement for fields query with product name
        String fieldsQuery = "SELECT " +
                "p.name AS description, " + // Product name as description
                "od.quantity, " +
                "od.unitPrice, " +
                "od.cost AS amount " +
                "FROM orders o " +
                "JOIN orderDetails od ON o.orderID = od.id " +
                "JOIN product p ON od.productID = p.productID " +
                "WHERE o.orderID = ?";
        PreparedStatement fieldsStatement = connection.prepareStatement(fieldsQuery);
        fieldsStatement.setInt(1, order.getOrderID()); // Use orderID from the Order object
        ResultSet fieldsResultSet = fieldsStatement.executeQuery();

        // Create a JRDataSource for fieldsResultSet
        JRResultSetDataSource jrFieldsDataSource = new JRResultSetDataSource(fieldsResultSet);

        // Load and compile JasperReports template
        String reportPath = "src/main/resources/reports/Clothify_Bill.jrxml"; // Replace with your actual JRXML file path
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

        // Fill report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrFieldsDataSource);

        JasperViewer.viewReport(jasperPrint, false);

        // Export JasperPrint to PDF (byte array)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }*/

        List<OrderDetails> orderDetails = order.getOrderDetailList();

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Customer customer = customerBo.getCustomerById(order.getCustomerID());
            java.util.Date datePlaced = Date.valueOf(order.getDatePlaced());

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderID", order.getOrderID());
            parameters.put("customerName", customer.getName());
            parameters.put("customerEmail", customer.getContactEmail());
            parameters.put("date", datePlaced);
            parameters.put("paymentMethod", order.getPaymentType());
            parameters.put("totalAmount", order.getTotalCost());
            parameters.put("discount", order.getDiscount());

            double subtotal = orderDetails.stream()
                    .mapToDouble(OrderDetails::getCost)
                    .sum();
            System.out.println(subtotal);
            parameters.put("subTotal", subtotal);

            String hql = "SELECT new map(p.name as description, od.quantity as quantity, od.unitPrice as unitPrice, od.cost as amount) " +
                    "FROM OrderEntity o " +
                    "JOIN OrderDetailsEntity od ON o.orderID = od.id " +
                    "JOIN ProductEntity p ON od.productID = p.productID " +
                    "WHERE o.orderID = :orderID";
            Query<Map<String, Object>> query = session.createQuery(hql);
            query.setParameter("orderID", order.getOrderID());
            List<Map<String, Object>> fieldsData = query.list();

            JRBeanCollectionDataSource jrFieldsDataSource = new JRBeanCollectionDataSource(fieldsData);

            String reportPath = "src/main/resources/reports/Clothify_Bill.jrxml"; // Replace with your actual JRXML file path
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrFieldsDataSource);

            JasperViewer.viewReport(jasperPrint, false);

            transaction.commit();

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Sales> getSalesData() {
//        String sql = "SELECT p.productID, p.name, p.category, SUM(od.quantity) AS totalQuantity, SUM(od.cost) AS totalCost " +
//                "FROM orderdetails od " +
//                "JOIN product p ON od.productID = p.productID " +
//                "GROUP BY p.productID, p.name, p.category";
//
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        ResultSet resultSet = preparedStatement.executeQuery() ;
//
//        List<Object[]> resultList = query.getResultList();
//        List<Sales> salesList = new ArrayList<>();
//
//        for (Object[] result : resultList) {
//            Sales sales = new Sales();
//            sales.setProductID((Integer) result[0]);
//            sales.setName((String) result[1]);
//            sales.setCategory(CategoryType.valueOf((String) result[2]));  // Assuming CategoryType is an enum
//            sales.setQuantity(((Number) result[3]).intValue());
//            sales.setCost(((Number) result[4]).doubleValue());
//            salesList.add(sales);
//        }
//
//        return salesList;

        List<Sales> salesList = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            String sql = "SELECT p.productID, p.name, p.categoryType, SUM(od.quantity) AS totalQuantity, SUM(od.cost) AS totalCost " +
                    "FROM OrderDetails od " +
                    "JOIN Product p ON od.productID = p.productID " +
                    "GROUP BY p.productID, p.name, p.categoryType";

            List<Object[]> resultList = session.createNativeQuery(sql).getResultList();

            for (Object[] result : resultList) {
                Sales sales = new Sales();
                sales.setProductID(((Number) result[0]).intValue());
                sales.setName((String) result[1]);
                sales.setCategory(CategoryType.valueOf((String) result[2])); // Assuming CategoryType is an enum
                sales.setQuantity(((Number) result[3]).intValue());
                sales.setCost(((Number) result[4]).doubleValue());
                salesList.add(sales);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return salesList;
    }
}
