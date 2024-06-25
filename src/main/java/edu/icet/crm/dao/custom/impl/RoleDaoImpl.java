package edu.icet.crm.dao.custom.impl;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.RoleBo;
import edu.icet.crm.dao.custom.RoleDao;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.dto.Role;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.entity.RoleEntity;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    private final Connection connection;


    public RoleDaoImpl() throws SQLException, ClassNotFoundException {

        this.connection = DbConnection.getInstance().getConnection();

    }

    @Override
    public List<RoleEntity> getAll() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        String query = "SELECT roleId, roleName FROM Role";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                RoleEntity roleEntity = extractRole(resultSet);
                roleEntities.add(roleEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roleEntities;

        /*List<RoleEntity> roleEntities = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            roleEntities = session.createQuery("FROM Role", RoleEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return roleEntities;*/
    }

    private RoleEntity extractRole(ResultSet resultSet) throws SQLException {
        return new RoleEntity(
                resultSet.getInt("roleID"),
                resultSet.getString("roleName")
        );
    }

    @Override
    public RoleEntity getById(Integer id) {
        String query = "SELECT * FROM Role WHERE roleID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new RoleEntity(
                        resultSet.getInt("roleId"),
                        resultSet.getString("roleName")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

        /*Transaction transaction = null;
        RoleEntity roleEntity = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            roleEntity = session.get(RoleEntity.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return roleEntity;*/
    }

    public RoleEntity getByName(String name) {
        String query = "SELECT * FROM Role WHERE roleName = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new RoleEntity(
                        resultSet.getInt("roleId"),
                        resultSet.getString("roleName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

        /*Transaction transaction = null;
        RoleEntity roleEntity = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            roleEntity = session.createQuery("FROM RoleEntity WHERE roleName = :roleName", RoleEntity.class)
                    .setParameter("roleName", name)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return roleEntity;*/
    }

    @Override
    public int getRoleIdByName(String role) {
        String query = "SELECT roleID FROM Role WHERE roleName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, role);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) { // Check if there is at least one row
                return resultSet.getInt("roleID"); // Correct column name to "roleID"
            } else {
                throw new RuntimeException("No role found with name: " + role);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching roleID for role: " + role, e);
        }
       /* Transaction transaction = null;
        Integer roleId = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            roleId = session.createQuery("SELECT r.roleId FROM RoleEntity r WHERE r.roleName = :roleName", Integer.class)
                    .setParameter("roleName", role)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return roleId != null ? roleId : -1;*/
    }

    @Override
    public String getRoleNameById(int id) {
        String query = "SELECT roleName FROM Role WHERE roleID = ?";
        try (

                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("roleName");
                } else {
                    throw new RuntimeException("No role found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving role name for ID: " + id, e);
        }
        /*Transaction transaction = null;
        String roleName = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            roleName = session.createQuery("SELECT r.roleName FROM RoleEntity r WHERE r.roleId = :roleId", String.class)
                    .setParameter("roleId", id)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return roleName;*/
    }

    @Override
    public boolean add(RoleEntity entity) {
        return false;
    }

    @Override
    public boolean update(RoleEntity entity) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
