package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.SupplierDto;
import com.gestion.almacenes.entities.Supplier;

import java.util.List;


public interface SupplierService{

    List<Supplier> getAll();

    Supplier create(SupplierDto dto);

    Supplier update(Integer id, SupplierDto dto);

    Supplier getById(Integer id);

    Supplier getBySupplierCode(String supplierCode);

    void delete(Integer id);

    List<Supplier> getFiltered(String code, String name);

    PagePojo<Supplier> getByPageAndFilters(Integer page, Integer size, String sortField, String sortOrder, String code, String name);
}
