package edu.icet.crm.service;
import edu.icet.crm.db.DbConnection;
import edu.icet.crm.model.Supplier;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SupplierService {
    private Connection connection = DbConnection.getInstance().getConnection();
    private static SupplierService instance;
    public SupplierService() throws SQLException, ClassNotFoundException {
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT supplierID, company, address, contactNumber, email ,dateAdded FROM Supplier";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Supplier supplier = extractSupplier(resultSet);
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    private Supplier extractSupplier(ResultSet resultSet) throws SQLException {
        return new Supplier(
                resultSet.getInt("supplierID"),
                resultSet.getString("company"),
                resultSet.getString("address"),
                resultSet.getString("contactNumber"),
                resultSet.getString("email"),
                resultSet.getDate("dateAdded").toLocalDate()
        );
    }

    public Supplier getSupplierById(Integer id) {
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
    }

    public boolean addSupplier(Supplier supplier) {
        String query = "INSERT INTO Supplier (supplierID, company, address, contactNumber, email ,dateAdded ) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, supplier.getSupplierID());
            preparedStatement.setString(2, supplier.getCompany());
            preparedStatement.setString(3, supplier.getAddress());
            preparedStatement.setString(4, supplier.getContactNumber());
            preparedStatement.setString(5, supplier.getEmail());
            preparedStatement.setDate(6, Date.valueOf(supplier.getDate()));

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSupplier(Supplier supplier) {
        String query = "UPDATE Supplier SET company = ?, address = ?, contactNumber = ?, email = ? ,dateAdded = ? WHERE supplierID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, supplier.getCompany());
            preparedStatement.setString(2, supplier.getAddress());
            preparedStatement.setString(3, supplier.getContactNumber());
            preparedStatement.setString(4, supplier.getEmail());
            preparedStatement.setDate(5, Date.valueOf(supplier.getDate()));
            preparedStatement.setInt(6, supplier.getSupplierID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSupplier(Integer id) {
        String query = "DELETE FROM Supplier WHERE supplierID = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
