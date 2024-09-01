package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductDetailDto;
import com.gestion.almacenes.entities.OrderProductDetail;
import java.util.List;


public interface OrderProductDetailService {

  List<OrderProductDetail> getAll();

  OrderProductDetail create(OrderProductDetailDto dto);

  OrderProductDetail update(Integer id, OrderProductDetailDto dto);

  OrderProductDetail getById(Integer id);

  void delete(Integer id);

  List<OrderProductDetail> getFiltered(String code, String name);

  PagePojo<OrderProductDetail> getByPageAndFilters(Integer page, Integer size, String sortField,
      String sortOrder, String code, String name);

    void createList(List<OrderProductDetailDto> orderProductDetailDtos);
}
