package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.SupplierBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.RoleDao;
import edu.icet.crm.dao.custom.SupplierDao;
import edu.icet.crm.dto.Product;
import edu.icet.crm.dto.Supplier;
import edu.icet.crm.entity.ProductEntity;
import edu.icet.crm.entity.SupplierEntity;
import edu.icet.crm.util.DaoType;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SupplierBoImpl implements SupplierBo {

    private final ModelMapper modelMapper;

    private final SupplierDao supplierDao;

    public SupplierBoImpl() throws SQLException, ClassNotFoundException {
        this.supplierDao = DaoFactory.getInstance().getDao(DaoType.SUPPLIER);

        this.modelMapper = new ModelMapper();
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        List<SupplierEntity> supplierEntities = supplierDao.getAll();
        return supplierEntities.stream()
                .map(entity -> modelMapper.map(entity, Supplier.class))
                .collect(Collectors.toList());
    }

    @Override
    public Supplier getSupplierById(Integer supplierId) {
        SupplierEntity supplierEntity = supplierDao.getById(supplierId);
        if (supplierEntity == null) {
            return null;
        }
        return modelMapper.map(supplierEntity, Supplier.class);
    }

    @Override
    public boolean addSupplier(Supplier supplier) {
        SupplierEntity supplierEntity = modelMapper.map(supplier, SupplierEntity.class);
        return supplierDao.add(supplierEntity);
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        SupplierEntity supplierEntity = modelMapper.map(supplier, SupplierEntity.class);
        return supplierDao.update(supplierEntity);
    }

    @Override
    public boolean deleteSupplier(Integer supplierId) throws SQLException {
        return supplierDao.delete(supplierId);
    }
}
