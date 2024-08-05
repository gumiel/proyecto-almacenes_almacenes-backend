package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.UnitMeasurementDto;
import com.gestion.almacenes.entities.UnitMeasurement;
import java.util.List;


public interface UnitMeasurementService {

  List<UnitMeasurement> getAll();

  UnitMeasurement create(UnitMeasurementDto dto);

  UnitMeasurement update(Integer id, UnitMeasurementDto dto);

  UnitMeasurement getById(Integer id);

  UnitMeasurement getByCode(String code);

  void delete(Integer id);

  List<UnitMeasurement> getFiltered(String code, String name);

  PagePojo<UnitMeasurement> getByPageAndFilters(Integer page, Integer size, String sortField,
      String sortOrder, String code, String name);
}
