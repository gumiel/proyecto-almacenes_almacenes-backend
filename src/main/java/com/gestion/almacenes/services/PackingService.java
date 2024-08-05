package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.PackingDto;
import com.gestion.almacenes.entities.Packing;
import java.util.List;


public interface PackingService {

  List<Packing> getAll();

  Packing create(PackingDto dto);

  Packing update(Integer id, PackingDto dto);

  Packing getById(Integer id);

  Packing getByCode(String code);

  void delete(Integer id);

  List<Packing> getFiltered(String code, String name);

  PagePojo<Packing> getByPageAndFilters(Integer page, Integer size, String sortField,
      String sortOrder, String code, String name);
}
