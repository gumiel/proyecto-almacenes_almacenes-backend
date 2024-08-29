package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StockDto;
import com.gestion.almacenes.entities.Stock;
import java.util.List;


public interface StockService {

  List<Stock> getAll();

  Stock create(StockDto dto);

  Stock update(Integer id, StockDto dto);

  Stock getById(Integer id);

  void delete(Integer id);

  List<Stock> getFiltered(String code, String name);

  PagePojo<Stock> getByPageAndFilters(Integer page, Integer size, String sortField,
      String sortOrder, String code, String name);

  Stock getStockByStorehouseIdAndProductId(Integer storehouseId, Integer productId);
}
