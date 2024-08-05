package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductTypeDto;
import com.gestion.almacenes.entities.OrderProductType;
import java.util.List;


public interface OrderProductTypeService {

  List<OrderProductType> getAll();

  OrderProductType create(OrderProductTypeDto dto);

  OrderProductType update(Integer id, OrderProductTypeDto dto);

  OrderProductType getById(Integer id);

  OrderProductType getByCode(String code);

  void delete(Integer id);

  List<OrderProductType> getFiltered(String code, String name);

  PagePojo<OrderProductType> getByPageAndFilters(Integer page, Integer size, String sortField,
      String sortOrder, String code, String name);
}
