package edu.icet.crm.dao.custom;

import edu.icet.crm.dao.CrudDao;
import edu.icet.crm.dto.User;
import edu.icet.crm.entity.UserEntity;

import java.sql.SQLException;

public interface UserDao extends CrudDao<UserEntity> {


    int getEmployeeIDByEmail(String employeeEmail);

    public boolean isEmployeeIDInUserTable(String employeeID) throws SQLException;
    boolean isEmployeeEmailInUserTable(String employeeEmail);

    boolean changePassword(String trim, String newPassword);

    UserEntity getUserByEmail(String email);

    String getUserRole(String loggedEmail);
}
