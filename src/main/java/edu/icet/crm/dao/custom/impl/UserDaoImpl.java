package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.bo.custom.RoleBo;
import edu.icet.crm.dao.custom.UserDao;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.dto.User;
import edu.icet.crm.entity.UserEntity;
import edu.icet.crm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final Connection connection;

private RoleBo roleBo;

private EmployeeBo employeeBo;
    public UserDaoImpl() throws SQLException, ClassNotFoundException {

        this.connection = DbConnection.getInstance().getConnection();

    }
    @Override
    public List<UserEntity> getAll() {
        /*try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM UserEntity", UserEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
        return null;
    }

    @Override
    public UserEntity getById(Integer id) {
       /* try (Session session = HibernateUtil.getSession()) {
            return session.get(UserEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
        return null;
    }

    @Override
    public boolean add(UserEntity entity) {
       /* String query = "INSERT INTO user (email, password, roleId, employeeId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getEmail());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setInt(3, entity.getRoleID());
            preparedStatement.setInt(4, entity.getEmployeeID());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
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
    public boolean update(UserEntity entity) {

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
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        /*Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            UserEntity user = session.get(UserEntity.class, id);
            if (user != null) {
                session.delete(user);
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
        return false;
    }


    @Override
    public int getEmployeeIDByEmail(String employeeEmail) {
        /*String query = "SELECT employeeID FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employeeEmail);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("employeeID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;*/

        Integer employeeId = null;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            employeeId = session.createQuery("SELECT employeeID FROM user WHERE email = :Email", Integer.class)
                    .setParameter("Email", employeeEmail)
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
        return employeeId != null ? employeeId : 0;
    }
    @Override
    public boolean isEmployeeIDInUserTable(String employeeID) throws SQLException {
        /*String query = "SELECT COUNT(*) FROM user WHERE employeeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(employeeID));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;*/

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = "SELECT COUNT(*) FROM UserEntity WHERE employeeID = :employeeId";
            Long result = (Long) session.createQuery(hql)
                    .setParameter("employeeId", employeeID)
                    .uniqueResult();
            transaction.commit();
            return result > 0;
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
    public boolean isEmployeeEmailInUserTable(String email) {
        /*String query = "SELECT COUNT(*) FROM user WHERE email = ?";
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

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = "SELECT COUNT(*) FROM UserEntity WHERE email = :Email";
            Long result = (Long) session.createQuery(hql)
                    .setParameter("Email", email)
                    .uniqueResult();
            transaction.commit();
            System.out.println(result>0);
            return result > 0;
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
    public boolean changePassword(String email, String newPassword) {
        /*String query = "UPDATE User SET password = ? WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = "UPDATE UserEntity SET password = :newPassword WHERE email = :email";
            int updatedEntities = session.createQuery(hql)
                    .setParameter("newPassword", newPassword)
                    .setParameter("email", email)
                    .executeUpdate();
            transaction.commit();
            return updatedEntities > 0;
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
    public UserEntity getUserByEmail(String email) {
        UserEntity user = null;
        /*String sql = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new UserEntity();
                    user.setUserID(resultSet.getInt("userID"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRoleID(resultSet.getInt("roleID"));
                    user.setEmployeeID(resultSet.getInt("employeeID"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = "FROM UserEntity WHERE email = :email";
            user = session.createQuery(hql, UserEntity.class)
                    .setParameter("email", email)
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

        return user;
    }

    @Override
    public String getUserRole(String loggedEmail) {
        /*String query = "SELECT roleID " +
                "FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, loggedEmail);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return String.valueOf(resultSet.getInt("roleID"));
            } else {
                // If no role is found for the email, you might want to handle this case
                return null; // or throw an exception, depending on your application logic
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        String userRole = null;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = "SELECT roleID FROM UserEntity WHERE email = :Email";
            userRole = String.valueOf(session.createQuery(hql, Integer.class)
                    .setParameter("Email", loggedEmail)
                    .uniqueResult());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return userRole;
    }


}
