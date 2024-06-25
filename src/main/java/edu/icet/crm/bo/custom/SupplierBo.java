package edu.icet.crm.bo.custom;

import edu.icet.crm.bo.SuperBo;
import edu.icet.crm.dto.Supplier;

import java.sql.SQLException;
import java.util.List;

public interface SupplierBo extends SuperBo {
    List<Supplier> getAllSuppliers();

    Supplier getSupplierById(Integer supplierId);

    boolean addSupplier(Supplier supplier);

    boolean updateSupplier(Supplier supplier);

    boolean deleteSupplier(Integer supplierId) throws SQLException;
}
