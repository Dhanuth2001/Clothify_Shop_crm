package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.dao.custom.SupplierDao;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.dto.Supplier;
import edu.icet.crm.entity.SupplierEntity;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoImpl implements SupplierDao {

    private final Connection connection;


    public SupplierDaoImpl() throws SQLException, ClassNotFoundException {

        this.connection = DbConnection.getInstance().getConnection();

    }
    @Override
    public List<SupplierEntity> getAll() {
        List<SupplierEntity> supplierEntities = new ArrayList<>();
        String query = "SELECT supplierID, company, address, contactNumber, email ,dateAdded FROM Supplier";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                SupplierEntity supplierEntity = extractSupplier(resultSet);
                supplierEntities.add(supplierEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplierEntities;

       /* List<SupplierEntity> supplierEntities = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            supplierEntities = session.createQuery("FROM Supplier", SupplierEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return supplierEntities;*/
    }

    private SupplierEntity extractSupplier(ResultSet resultSet) throws SQLException {
        return new SupplierEntity(
                resultSet.getInt("supplierID"),
                resultSet.getString("company"),
                resultSet.getString("address"),
                resultSet.getString("contactNumber"),
                resultSet.getString("email"),
                resultSet.getDate("dateAdded").toLocalDate()
        );
    }

    @Override
    public SupplierEntity getById(Integer id) {
        String query = "SELECT supplierID, company, address, contactNumber, email ,dateAdded FROM Supplier WHERE supplierID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractSupplier(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        /*Transaction transaction = null;
        SupplierEntity supplierEntity = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            supplierEntity = session.get(SupplierEntity.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return supplierEntity;*/
    }

    @Override
    public boolean add(SupplierEntity entity) {
        String query = "INSERT INTO Supplier (supplierID, company, address, contactNumber, email ,dateAdded ) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, entity.getSupplierID());
            preparedStatement.setString(2, entity.getCompany());
            preparedStatement.setString(3, entity.getAddress());
            preparedStatement.setString(4, entity.getContactNumber());
            preparedStatement.setString(5, entity.getEmail());
            preparedStatement.setDate(6, Date.valueOf(entity.getDate()));

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        /*Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
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
        }*/
    }

    @Override
    public boolean update(SupplierEntity entity) {
        String query = "UPDATE Supplier SET company = ?, address = ?, contactNumber = ?, email = ? ,dateAdded = ? WHERE supplierID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, entity.getCompany());
            preparedStatement.setString(2, entity.getAddress());
            preparedStatement.setString(3, entity.getContactNumber());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setDate(5, Date.valueOf(entity.getDate()));
            preparedStatement.setInt(6, entity.getSupplierID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        /*Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
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
        }*/
    }

    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM Supplier WHERE supplierID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

        /*Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            SupplierEntity supplierEntity = session.get(SupplierEntity.class, id);
            if (supplierEntity != null) {
                session.delete(supplierEntity);
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
        }*/
    }
}
