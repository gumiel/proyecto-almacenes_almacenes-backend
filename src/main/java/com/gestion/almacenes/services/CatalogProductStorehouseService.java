package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.CatalogProductStorehouseDto;
import com.gestion.almacenes.entities.CatalogProductStorehouse;
import java.util.List;


public interface CatalogProductStorehouseService {

  List<CatalogProductStorehouse> getAll();

  CatalogProductStorehouse create(CatalogProductStorehouseDto dto);

  CatalogProductStorehouse update(Integer id, CatalogProductStorehouseDto dto);

  CatalogProductStorehouse getById(Integer id);

  void delete(Integer id);

  void deleteByProductAndStorehouse(CatalogProductStorehouseDto dto);

  List<CatalogProductStorehouse> getFiltered();

  PagePojo<CatalogProductStorehouse> getByPageAndFilters(Integer page, Integer size,
      String sortField, String sortOrder);

  void addAllProductsToStorehouse(Integer storehouseId);
}
