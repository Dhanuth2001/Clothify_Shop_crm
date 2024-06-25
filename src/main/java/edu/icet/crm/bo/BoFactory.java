package edu.icet.crm.bo;

import edu.icet.crm.bo.custom.impl.*;
import edu.icet.crm.util.BoType;

import java.sql.SQLException;

public class BoFactory {
    private static BoFactory instance;

    private BoFactory() {}

    public static BoFactory getInstance() {
        if (instance == null) {
            instance = new BoFactory();
        }
        return instance;
    }

    public <T extends SuperBo> T getBo(BoType type) throws SQLException, ClassNotFoundException {
        switch (type) {
            case EMPLOYEE:
                return (T) new EmployeeBoImpl();
            case CUSTOMER:
                return (T) new CustomerBoImpl();
            case ROLE:
                return (T) new RoleBoImpl();
            case USER:
                return (T) new UserBoImpl();
            case PRODUCT:
                return (T) new ProductBoImpl();
            case SUPPLIER:
                return (T) new SupplierBoImpl();
            case ORDER:
                return (T) new OrderBoImpl();
            case ORDERDETAILS:
                return (T) new OrderDetailBoImpl();
            default:
                return null;
        }
    }
}
