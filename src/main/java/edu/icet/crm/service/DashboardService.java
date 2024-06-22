package edu.icet.crm.service;

import edu.icet.crm.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardService {
    private Connection connection = DbConnection.getInstance().getConnection();

    public DashboardService() throws SQLException, ClassNotFoundException {
    }

    public String getUsernameByEmail(String email) {
        String query = "SELECT e.name " +
                "FROM Employee e " +
                "JOIN User u ON e.employeeID = u.employeeID " +
                "WHERE u.email = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractEmployee(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String extractEmployee(ResultSet resultSet) throws SQLException {

        return resultSet.getString("name");
    }
}
