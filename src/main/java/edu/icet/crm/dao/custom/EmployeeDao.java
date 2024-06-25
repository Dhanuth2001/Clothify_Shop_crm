package edu.icet.crm.dao.custom;

import edu.icet.crm.dao.CrudDao;
import edu.icet.crm.dao.SuperDao;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.entity.EmployeeEntity;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao extends CrudDao<EmployeeEntity> {
    public String getUsernameByEmail(String loggedUserEmail);
    public String getEmailByEmployeeID(String employeeID) throws SQLException ;
    public boolean isEmployeeIDValid(String employeeID) throws SQLException;

    Integer getRoleIDByEmployeeID(String employeeID) throws SQLException;


    boolean isEmployeeEmailValid(String employeeEmail);
}
