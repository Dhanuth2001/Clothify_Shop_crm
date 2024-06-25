package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.RoleBo;
import edu.icet.crm.dao.DaoFactory;

import edu.icet.crm.dao.custom.RoleDao;

import edu.icet.crm.dto.Role;

import edu.icet.crm.entity.RoleEntity;
import edu.icet.crm.util.DaoType;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RoleBoImpl implements RoleBo {
    private final RoleDao roleDao;
    private final ModelMapper modelMapper;

    public RoleBoImpl() throws SQLException, ClassNotFoundException {
        this.roleDao = DaoFactory.getInstance().getDao(DaoType.ROLE);
        this.modelMapper = new ModelMapper();
    }
    @Override
    public List<Role> getAllRoles() {
        List<RoleEntity> roleEntities = roleDao.getAll();

        return roleEntities.stream()
                .map(entity -> modelMapper.map(entity, Role.class))
                .collect(Collectors.toList());
    }

    @Override
    public Role getRoleByName(String role) {
        return modelMapper.map(roleDao.getByName(role),Role.class);
    }

    @Override
    public int getRoleIdByName(String role) {
        return roleDao.getRoleIdByName(role);
    }

    @Override
    public String getRoleNameById(int id) {
        return roleDao.getRoleNameById(id);
    }

    @Override
    public RoleEntity getRoleById(int roleID) {
        return roleDao.getById(roleID);
    }
}
