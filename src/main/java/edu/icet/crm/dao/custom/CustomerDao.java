package edu.icet.crm.dao.custom;

import edu.icet.crm.dao.CrudDao;
import edu.icet.crm.dao.SuperDao;
import edu.icet.crm.entity.CustomerEntity;

import java.util.List;

public interface CustomerDao extends CrudDao<CustomerEntity> {
    public List<Integer> getCustomerIDs();
    public String getCustomerEmailById(Integer customerID);


}
