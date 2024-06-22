package edu.icet.crm.service;

import edu.icet.crm.db.DbConnection;
import edu.icet.crm.model.Employee;
import edu.icet.crm.model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private RoleService roleService;
    private Connection connection = DbConnection.getInstance().getConnection();
    public EmployeeService() throws SQLException, ClassNotFoundException {
        this.roleService = new RoleService();
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT e.employeeID, e.name, e.dob,e.doJoin, r.roleName as role, e.address, e.contactEmail, e.contactNumber FROM Employee e JOIN Role r ON e.roleId = r.roleId";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = extractEmployee(resultSet);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private Employee extractEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getInt("employeeID"),
                resultSet.getString("role"),
                resultSet.getString("name"),
                resultSet.getDate("dob").toLocalDate(),
                resultSet.getDate("doJoin").toLocalDate(),
                resultSet.getString("address"),
                resultSet.getString("contactEmail"),
                resultSet.getString("contactNumber")
        );
    }

    public Employee getEmployeeById(Integer id) {
        String query = "SELECT e.employeeID, e.name, e.dob,e.doJoin, r.roleName as role, e.address, e.contactEmail, e.contactNumber FROM Employee e JOIN Role r ON e.roleId = r.roleId WHERE e.employeeID = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractEmployee(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addEmployee(Employee employee) {
        System.out.println(employee.getRole());
        Role role = roleService.getRoleByName(employee.getRole());
        System.out.println(role);
        if (role == null) {
            return false;
        }
        String query = "INSERT INTO Employee (employeeID, roleId, name, dob, doJoin, address, contactEmail, contactNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.setInt(2, role.getRoleId());
            preparedStatement.setString(3, employee.getName());
            preparedStatement.setDate(4, Date.valueOf(employee.getDob()));
            preparedStatement.setDate(5, Date.valueOf(employee.getDoJoin()));
            preparedStatement.setString(6, employee.getAddress());
            preparedStatement.setString(7, employee.getEmail());
            preparedStatement.setString(8, employee.getContactNo());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployee(Employee employee) {
        Role role = roleService.getRoleByName(employee.getRole());
        if (role == null) {
            return false;
        }
        String query = "UPDATE Employee SET name = ?, dob = ?, roleId = ?, address = ?, contactEmail = ?, contactNumber = ? WHERE employeeId = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setDate(2, Date.valueOf(employee.getDob()));
            preparedStatement.setInt(3, role.getRoleId());
            preparedStatement.setString(4, employee.getAddress());
            preparedStatement.setString(5, employee.getEmail());
            preparedStatement.setString(6, employee.getContactNo());
            preparedStatement.setInt(7, employee.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(Integer id) {
        String query = "DELETE FROM Employee WHERE employeeId = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getEmployeeIDs() {
        List<Integer> employeeIds = new ArrayList<>();
        String query = "SELECT employeeID FROM employee";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int employeeId = resultSet.getInt("employeeId");
                    employeeIds.add(employeeId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeIds;
    }
}
