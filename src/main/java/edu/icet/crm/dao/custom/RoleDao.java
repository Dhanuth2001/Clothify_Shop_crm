package edu.icet.crm.dao.custom;

import edu.icet.crm.dao.CrudDao;
import edu.icet.crm.dao.SuperDao;
import edu.icet.crm.entity.RoleEntity;

public interface RoleDao extends CrudDao<RoleEntity>  {
    public RoleEntity getByName(String name);

    int getRoleIdByName(String role);

    String getRoleNameById(int id);
}
