package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeBo extends SuperBo {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Integer id);
    boolean addEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(Integer id) throws SQLException;

    String getUsernameByEmail(String loggedUserEmail);

    boolean isEmployeeIDValid(String employeeID);

    Integer getRoleIDByEmployeeID(String employeeID);

    String getEmailByEmployeeID(String employeeID);

    boolean isEmployeeEmailValid(String employeeEmail);
}
