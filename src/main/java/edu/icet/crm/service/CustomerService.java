package edu.icet.crm.service;

import edu.icet.crm.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private Connection connection= DbConnection.getInstance().getConnection();

    public CustomerService() throws SQLException, ClassNotFoundException {
    }

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
    }
}
