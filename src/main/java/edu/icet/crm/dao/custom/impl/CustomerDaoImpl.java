package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.dao.custom.CustomerDao;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.entity.CustomerEntity;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    private final Connection connection;



    public CustomerDaoImpl() throws SQLException, ClassNotFoundException {


        this.connection = DbConnection.getInstance().getConnection();

    }
    @Override
    public List<CustomerEntity> getAll() {
        List<CustomerEntity> customerEntities = new ArrayList<>();
        String query = "SELECT customerID, name, dob,  contactEmail, contactNumber FROM Customer";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                CustomerEntity customerEntity = extractCustomer(resultSet);
                customerEntities.add(customerEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerEntities;
       /* List<CustomerEntity> customerEntities = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            customerEntities = session.createQuery("FROM CustomerEntity", CustomerEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerEntities;*/
    }

    private CustomerEntity extractCustomer(ResultSet resultSet) throws SQLException {
        return new CustomerEntity(
                resultSet.getInt("customerID"),
                resultSet.getString("name"),
                resultSet.getDate("dob").toLocalDate(),
                resultSet.getString("contactEmail"),
                resultSet.getString("contactNumber")
        );
    }

    @Override
    public CustomerEntity getById(Integer id) {
        String query = "SELECT customerID, name, dob,  contactEmail, contactNumber FROM Customer WHERE CustomerId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                CustomerEntity customerEntity = extractCustomer(resultSet);
                return customerEntity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        /*try (Session session = HibernateUtil.getSession()) {
            return session.get(CustomerEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;*/
    }

    @Override
    public boolean add(CustomerEntity entity) {

       /*Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(entity);
        session.getTransaction().commit();
        session.close();*/

        /*Session session = HibernateUtil.getSession();
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
        }*/


        String query = "INSERT INTO customer (customerID, name, dob, contactEmail, contactNumber) VALUES (?, ?, ?, ?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, entity.getCustomerID());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setDate(3, Date.valueOf(entity.getDob())); // Assuming dob is of type LocalDate

            preparedStatement.setString(4, entity.getContactEmail());
            preparedStatement.setString(5, entity.getContactNumber());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(CustomerEntity entity) {
        String query = "UPDATE customer SET name = ?, dob = ?, contactEmail = ?, contactNumber = ? WHERE CustomerID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDate(2, Date.valueOf(entity.getDob()));
            preparedStatement.setString(3, entity.getContactEmail());
            preparedStatement.setString(4, entity.getContactNumber());
            preparedStatement.setInt(5, entity.getCustomerID());


            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        /*Session session = HibernateUtil.getSession();
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
        }*/
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String query = "DELETE FROM Customer WHERE customerID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Cannot delete customer with ID " + id + " because they are referenced in other records.", e);
        }
        /*Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            CustomerEntity customer = session.get(CustomerEntity.class, id);
            if (customer != null) {
                session.delete(customer);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new SQLException("Error deleting customer with ID " + id, e);
        } finally {
            session.close();
        }*/

    }

    @Override
    public List<Integer> getCustomerIDs() {
        List<Integer> customerIDs = new ArrayList<>();
        String query = "SELECT customerID FROM customer";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int customerId = resultSet.getInt("customerID");
                    customerIDs.add(customerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerIDs;
        /*List<Integer> customerIDs = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            List<CustomerEntity> customers = session.createQuery("SELECT c FROM Customer c", CustomerEntity.class).list();
            for (CustomerEntity customer : customers) {
                customerIDs.add(customer.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerIDs;*/

    }

    @Override
    public String getCustomerEmailById(Integer customerID) {
        String query = "SELECT contactEmail FROM Customer WHERE customerID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("contactEmail");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*try (Session session = HibernateUtil.getSession()) {
            CustomerEntity customer = session.get(CustomerEntity.class, customerID);
            if (customer != null) {
                return customer.getContactEmail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }
}
