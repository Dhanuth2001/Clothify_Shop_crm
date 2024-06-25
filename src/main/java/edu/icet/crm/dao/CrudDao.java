package edu.icet.crm.dao;

import edu.icet.crm.dto.Employee;
import edu.icet.crm.entity.EmployeeEntity;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao <T> extends SuperDao{
    List<T> getAll();

    T getById(Integer id);

    boolean add(T entity);

    boolean update(T entity);

    boolean delete(Integer id) throws SQLException;
}
