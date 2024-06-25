package edu.icet.crm.dao;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.bo.custom.impl.CustomerBoImpl;
import edu.icet.crm.dao.custom.impl.*;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.DaoType;

import java.sql.SQLException;

public class DaoFactory {
    private static DaoFactory instance;

    private DaoFactory() {}

    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public <T extends SuperDao> T getDao(DaoType type) throws SQLException, ClassNotFoundException {
        switch (type) {
            case EMPLOYEE:
                return (T) new EmployeeDaoImpl();
            case CUSTOMER:
                return (T) new CustomerDaoImpl();

            case ROLE:
                return (T) new RoleDaoImpl();
            case USER:
                return (T) new UserDaoImpl();

            case PRODUCT:
                return (T) new ProductDaoImpl();
            case SUPPLIER:
                return (T) new SupplierDaoImpl();
            case ORDER:
                return (T) new OrderDaoImpl();

            case ORDERDETAILS:
                return (T) new OrderDetailsDaoImpl();
            default:
                return null;
        }
    }
}
