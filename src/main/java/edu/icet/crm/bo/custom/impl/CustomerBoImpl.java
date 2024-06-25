package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.CustomerBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.CustomerDao;
import edu.icet.crm.dao.custom.EmployeeDao;
import edu.icet.crm.dto.Customer;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.entity.CustomerEntity;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.util.DaoType;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerBoImpl implements CustomerBo {
    private final CustomerDao customerDao;
    private final ModelMapper modelMapper;

    public CustomerBoImpl() throws SQLException, ClassNotFoundException {
        this.customerDao = DaoFactory.getInstance().getDao(DaoType.CUSTOMER);
        this.modelMapper = new ModelMapper();
    }
    @Override
    public List<Integer> getCustomerIDs() {
        return customerDao.getCustomerIDs();
    }

    @Override
    public String getCustomerEmailById(Integer customerID) {
        return customerDao.getCustomerEmailById(customerID);
    }

    @Override
    public boolean addCustomer(Customer customer) {
        CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);
        return customerDao.add(customerEntity);
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<CustomerEntity> customerEntities = customerDao.getAll();
        return customerEntities.stream()
                .map(entity -> modelMapper.map(entity, Customer.class))
                .collect(Collectors.toList());
    }

    @Override
    public Customer getCustomerById(Integer customerId) {

        CustomerEntity customerEntity = customerDao.getById(customerId);
        if (customerEntity == null) {
            return null;
        }
        return modelMapper.map(customerEntity, Customer.class);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);
        return customerDao.update(customerEntity);
    }

    @Override
    public boolean deleteCustomer(Integer customerId) throws SQLException {
        return customerDao.delete(customerId);
    }
}
