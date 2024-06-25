package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dao.SuperDao;
import edu.icet.crm.dto.Role;
import edu.icet.crm.entity.RoleEntity;

import java.util.List;

public interface RoleBo extends SuperBo {
    public List<Role> getAllRoles();

    Role getRoleByName(String role);

    int getRoleIdByName(String role);

    String getRoleNameById(int id);

    RoleEntity getRoleById(int roleID);
}
