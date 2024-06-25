package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.UserBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.SupplierDao;
import edu.icet.crm.dao.custom.UserDao;
import edu.icet.crm.dto.User;
import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.entity.UserEntity;
import edu.icet.crm.util.DaoType;
import edu.icet.crm.util.EncryptionUtil;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;

public class UserBoImpl implements UserBo {
    private final ModelMapper modelMapper;

    private final UserDao userDao;

    public UserBoImpl() throws SQLException, ClassNotFoundException {
        this.userDao = DaoFactory.getInstance().getDao(DaoType.USER);

        this.modelMapper = new ModelMapper();
    }
    @Override
    public boolean isEmployeeIDInUserTable(String employeeID) {
        try {
            return userDao.isEmployeeIDInUserTable(employeeID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createUser(User user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        return userDao.add(userEntity);
    }

    @Override
    public boolean isEmployeeEmailInUserTable(String employeeEmail) {
        return userDao.isEmployeeEmailInUserTable(employeeEmail);
    }

    @Override
    public boolean changePassword(String trim, String newPassword) {

        return userDao.changePassword(trim,newPassword);
    }

    @Override
    public int getEmployeeIdByEmail(String employeeEmail) {
        System.out.println(userDao.getEmployeeIDByEmail(employeeEmail));
        return userDao.getEmployeeIDByEmail(employeeEmail);
    }

    public boolean authenticateUser(String email, String password) {
        try {
            User user = modelMapper.map(userDao.getUserByEmail(email), User.class);
            if (user != null) {
                String decryptedPassword = EncryptionUtil.decrypt(user.getPassword());
                System.out.println(decryptedPassword);
                return decryptedPassword.equals(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getUserRole(String loggedEmail) {
        return userDao.getUserRole(loggedEmail);
    }
}
