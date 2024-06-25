package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBo extends SuperBo {
    List<Integer> getCustomerIDs();

    String getCustomerEmailById(Integer customerID);

    boolean addCustomer(Customer customer);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Integer customerId);

    boolean updateCustomer(Customer customer);

    boolean deleteCustomer(Integer customerId) throws SQLException;
}
