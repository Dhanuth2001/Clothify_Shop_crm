package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.RoleBo;
import edu.icet.crm.dao.custom.EmployeeDao;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.dto.Role;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.entity.ProductEntity;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    private final RoleBo roleBo;
    private final Connection connection;


    public EmployeeDaoImpl() throws SQLException, ClassNotFoundException {
        this.roleBo = BoFactory.getInstance().getBo(BoType.ROLE);
        this.connection = DbConnection.getInstance().getConnection();

    }

    @Override
    public List<EmployeeEntity> getAll() {
        /*List<EmployeeEntity> employeeEntitylist = new ArrayList<>();
        String query = "SELECT * FROM Employee ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                EmployeeEntity employeeEntity = extractEmployee(resultSet);
                employeeEntitylist.add(employeeEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeEntitylist;*/

        List<EmployeeEntity> employeeEntityList = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            employeeEntityList = session.createQuery("FROM EmployeeEntity", EmployeeEntity.class).list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeEntityList;
    }

    @Override
    public EmployeeEntity getById(Integer id) {
        /*String query = "SELECT * FROM Employee WHERE employeeID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                EmployeeEntity employeeEntity = extractEmployee(resultSet);
                return employeeEntity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/

        EmployeeEntity employeeEntity = null;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            employeeEntity = session.get(EmployeeEntity.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeEntity;
    }

    @Override
    public boolean add(EmployeeEntity employeeEntity) {
        //Role role = roleBo.getRoleByName(employeeEntity.getRole());
       // System.out.println();
        //if (role == null) {
          //  return false;
       // }
        /*String query = "INSERT INTO Employee (employeeID, roleId, name, dob, doJoin, address, contactEmail, contactNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeEntity.getEmployeeId());
            preparedStatement.setInt(2, employeeEntity.getRoleId());
            preparedStatement.setString(3, employeeEntity.getName());
            preparedStatement.setDate(4, Date.valueOf(employeeEntity.getDob()));
            preparedStatement.setDate(5, Date.valueOf(employeeEntity.getDoJoin()));
            preparedStatement.setString(6, employeeEntity.getAddress());
            preparedStatement.setString(7, employeeEntity.getEmail());
            preparedStatement.setString(8, employeeEntity.getContactNo());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        /*Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(employeeEntity);
        session.getTransaction().commit();
        session.close();
        return false;*/
//        System.out.println("Adding employee: " + employeeEntity);
//        try (Session session = HibernateUtil.getSession()) {
//            Transaction transaction = session.beginTransaction();
//            try {
//                session.persist(employeeEntity);
//                System.out.println("Employee persisted: " + employeeEntity);
//                transaction.commit();
//                System.out.println("Transaction committed");
//                return true;
//            } catch (Exception e) {
//                if (transaction != null) {
//                    transaction.rollback();
//                }
//                e.printStackTrace();
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(employeeEntity);
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
    public boolean update(EmployeeEntity employeeEntity) {
        /*Role role = roleBo.getRoleByName(employeeEntity.getRole());
        if (role == null) {
            return false;
//        }*/
//       String query = "UPDATE Employee SET name = ?, dob = ?, roleId = ?, address = ?, contactEmail = ?, contactNumber = ? WHERE employeeId = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1, employeeEntity.getName());
//            preparedStatement.setDate(2, Date.valueOf(employeeEntity.getDob()));
//            preparedStatement.setInt(3, employeeEntity.getRoleId());
//            preparedStatement.setString(4, employeeEntity.getAddress());
//            preparedStatement.setString(5, employeeEntity.getEmail());
//            preparedStatement.setString(6, employeeEntity.getContactNo());
//            preparedStatement.setInt(7, employeeEntity.getId());
//            return preparedStatement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;

        /*Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.update(employeeEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }*/
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(employeeEntity);
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
//       String query = "DELETE FROM Employee WHERE employeeId = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, id);
//            return preparedStatement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;

        /*Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            EmployeeEntity employeeEntity = session.get(EmployeeEntity.class, id);
            if (employeeEntity != null) {
                session.delete(employeeEntity);
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

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            EmployeeEntity employeeEntity = session.get(EmployeeEntity.class, id);
            if (employeeEntity != null) {
                session.delete(employeeEntity);
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

    private EmployeeEntity extractEmployee(ResultSet resultSet) throws SQLException {
        return new EmployeeEntity(
                resultSet.getInt("employeeID"),
                resultSet.getInt("roleID"),
                resultSet.getString("name"),
                resultSet.getDate("dob").toLocalDate(),
                resultSet.getDate("doJoin").toLocalDate(),
                resultSet.getString("address"),
                resultSet.getString("contactEmail"),
                resultSet.getString("contactNumber")
        );
    }

    @Override
    public String getUsernameByEmail(String loggedUserEmail) {

/*        String query = "SELECT e.name " +
                "FROM Employee e " +
                "JOIN User u ON e.employeeID = u.employeeID " +
                "WHERE u.email = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, loggedUserEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/



        String username = null;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // Step 1: Fetch employeeId from UserEntity based on email
            Integer employeeId = (Integer) session.createQuery(
                            "SELECT employeeID FROM UserEntity WHERE email = :email")
                    .setParameter("email", loggedUserEmail)
                    .uniqueResult();

            // Step 2: Fetch name from EmployeeEntity based on employeeId
            if (employeeId != null) {
                username = (String) session.createQuery(
                                "SELECT name FROM EmployeeEntity WHERE employeeId = :employeeId")
                        .setParameter("employeeId", employeeId)
                        .uniqueResult();
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
        return username;
    }
    @Override
    public String getEmailByEmployeeID(String employeeID) throws SQLException {
//        String query = "SELECT contactEmail FROM employee WHERE employeeId = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, Integer.parseInt(employeeID));
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getString("contactEmail");
//            }
//        }
//        return null;

        Transaction transaction = null;
        String email = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            email = (String) session.createQuery("SELECT e.email FROM EmployeeEntity e WHERE e.id = :employeeId")
                    .setParameter("employeeId", Integer.parseInt(employeeID))
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return email;
    }
    @Override
    public boolean isEmployeeIDValid(String employeeID) throws SQLException {
        /*String query = "SELECT COUNT(*) FROM employee WHERE employeeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(employeeID));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;*/


        Long count = 0L;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            count = (Long) session.createQuery("SELECT COUNT(e) FROM EmployeeEntity e WHERE e.id = :employeeId")
                    .setParameter("employeeId", Integer.parseInt(employeeID))
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return count > 0;
    }
    @Override
    public Integer getRoleIDByEmployeeID(String employeeID) throws SQLException {
        /*String query = "SELECT roleId FROM employee WHERE employeeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(employeeID));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("roleId");
            }
        }
        return null;*/

        Integer roleId = null;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            roleId = (Integer) session.createQuery("SELECT roleId FROM EmployeeEntity e WHERE e.employeeId = :employeeId")
                    .setParameter("employeeId", Integer.parseInt(employeeID))
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return roleId;
    }

    @Override
    public boolean isEmployeeEmailValid(String email) {
        /*String query = "SELECT COUNT(*) FROM employee WHERE contactEmail = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;*/

        Long count = 0L;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            count = (Long) session.createQuery("SELECT COUNT(e) FROM EmployeeEntity e WHERE e.contactEmail = :email")
                    .setParameter("email", email)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return count > 0;

    }
}
