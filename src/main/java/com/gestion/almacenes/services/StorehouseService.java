package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StorehouseDto;
import com.gestion.almacenes.entities.Storehouse;
import java.util.List;


public interface StorehouseService {

  List<Storehouse> getAll();

  Storehouse create(StorehouseDto dto);

  Storehouse update(Integer id, StorehouseDto dto);

  Storehouse getById(Integer id);

  Storehouse getByCode(String code);

  void delete(Integer id);

  List<Storehouse> search(String code, String name);

  PagePojo<Storehouse> pageable(Integer page, Integer size, String sortField, String sortOrder,
      String code, String name);


}
