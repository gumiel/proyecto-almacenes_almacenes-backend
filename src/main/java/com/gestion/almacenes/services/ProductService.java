package com.gestion.almacenes.services;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.ProductDto;
import com.gestion.almacenes.entities.Product;
import java.util.List;


public interface ProductService {

  List<Product> getAll();

  Product create(ProductDto dto);

  Product update(Integer id, ProductDto dto);

  Product getById(Integer id);

  Product getByCode(String code);

  void delete(Integer id);

  List<Product> search(String code, String name);

  PagePojo<Product> pageable(Integer page, Integer size, String sortField, String sortOrder,
      String code, String name);
}
