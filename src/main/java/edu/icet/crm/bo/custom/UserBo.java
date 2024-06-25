package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.User;

public interface UserBo extends SuperBo {


    

    boolean isEmployeeIDInUserTable(String employeeID);

    boolean createUser(User user);

    boolean isEmployeeEmailInUserTable(String employeeEmail);

    boolean changePassword(String trim, String newPassword);

    int getEmployeeIdByEmail(String employeeEmail);

    boolean authenticateUser(String loggedEmail, String loggedPassword);

    String getUserRole(String loggedEmail);
}
