package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StorehouseTypeDto;
import com.gestion.almacenes.entities.StorehouseType;
import java.util.List;


public interface StorehouseTypeService {

  List<StorehouseType> getAll();

  StorehouseType create(StorehouseTypeDto dto);

  StorehouseType update(Integer id, StorehouseTypeDto dto);

  StorehouseType getById(Integer id);

  StorehouseType getByCode(String code);

  void delete(Integer id);

  List<StorehouseType> getFiltered(String code, String name);

  PagePojo<StorehouseType> getByPageAndFilters(Integer page, Integer size, String sortField,
      String sortOrder, String code, String name);
}
