package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductDto;
import com.gestion.almacenes.entities.OrderProduct;
import java.util.List;


public interface OrderProductService {

  List<OrderProduct> getAll();

  OrderProduct create(OrderProductDto dto);

  OrderProduct update(Integer id, OrderProductDto dto);

  OrderProduct getById(Integer id);

  OrderProduct getByCode(String code);

  void delete(Integer id);

  List<OrderProduct> getFiltered(String code, String name);

  PagePojo<OrderProduct> getByPageAndFilters(Integer page, Integer size, String sortField,
      String sortOrder, String code, String name);

  OrderProduct executeOrderProduct(OrderProductDto dto);
}
