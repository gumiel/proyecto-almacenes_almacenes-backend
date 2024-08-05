package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StoreHouseDto;
import com.gestion.almacenes.dtos.StorehouseProductDto;
import com.gestion.almacenes.entities.Storehouse;
import java.util.List;


public interface StorehouseService {

  List<Storehouse> getAll();

  Storehouse create(StoreHouseDto dto);

  Storehouse update(Integer id, StoreHouseDto dto);

  Storehouse getById(Integer id);

  Storehouse getByCode(String code);

  void delete(Integer id);

  List<Storehouse> search(String code, String name);

  PagePojo<Storehouse> pageable(Integer page, Integer size, String sortField, String sortOrder,
      String code, String name);

  Storehouse addProductToStorehouse(StorehouseProductDto dto);

  void removeProductToStorehouse(StorehouseProductDto dto);
}
