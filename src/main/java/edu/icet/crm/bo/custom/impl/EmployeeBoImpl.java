package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.bo.custom.CustomerBo;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.EmployeeDao;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.util.DaoType;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeBoImpl implements EmployeeBo {
    private final EmployeeDao employeeDao;
    private final ModelMapper modelMapper;

    public EmployeeBoImpl() throws SQLException, ClassNotFoundException {
        this.employeeDao = DaoFactory.getInstance().getDao(DaoType.EMPLOYEE);
        this.modelMapper = new ModelMapper();
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<EmployeeEntity> employeeEntities = employeeDao.getAll();
        return employeeEntities.stream()
                .map(entity -> modelMapper.map(entity, Employee.class))
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        EmployeeEntity employeeEntity = employeeDao.getById(id);
        if (employeeEntity == null) {
            return null;
        }
        return modelMapper.map(employeeEntity, Employee.class);
    }

    @Override
    public boolean addEmployee(Employee employee) {
        EmployeeEntity employeeEntity = modelMapper.map(employee, EmployeeEntity.class);
        return employeeDao.add(employeeEntity);
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        EmployeeEntity employeeEntity = modelMapper.map(employee, EmployeeEntity.class);
        return employeeDao.update(employeeEntity);
    }

    @Override
    public boolean deleteEmployee(Integer id) throws SQLException {
        return employeeDao.delete(id);
    }

    @Override
    public String getUsernameByEmail(String loggedUserEmail) {
        return employeeDao.getUsernameByEmail(loggedUserEmail);
    }

    @Override
    public boolean isEmployeeIDValid(String employeeID) {
        try {
            return employeeDao.isEmployeeIDValid(employeeID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getRoleIDByEmployeeID(String employeeID) {
        try {
            return employeeDao.getRoleIDByEmployeeID(employeeID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getEmailByEmployeeID(String employeeID) {
        try {
            return employeeDao.getEmailByEmployeeID(employeeID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEmployeeEmailValid(String employeeEmail) {
        return employeeDao.isEmployeeEmailValid(employeeEmail);
    }
}
