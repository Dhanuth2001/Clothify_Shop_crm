package edu.icet.crm.service;

import edu.icet.crm.db.DbConnection;
import edu.icet.crm.model.Employee;
import edu.icet.crm.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RoleService {

    private Connection connection = DbConnection.getInstance().getConnection();

    public RoleService() throws SQLException, ClassNotFoundException {
    }

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT * FROM Role";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Role role = new Role(
                        resultSet.getInt("roleId"),
                        resultSet.getString("roleName")
                );
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public Role getRoleById(int id) {
        String query = "SELECT * FROM Role WHERE roleId = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Role(
                        resultSet.getInt("roleId"),
                        resultSet.getString("roleName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Role getRoleByName(String role) {
        String query = "SELECT * FROM role WHERE roleName = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, role);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractRole(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Role extractRole(ResultSet resultSet) throws SQLException {
        return new Role(
                resultSet.getInt("roleId"),
                resultSet.getString("roleName")
        );
    }
}
