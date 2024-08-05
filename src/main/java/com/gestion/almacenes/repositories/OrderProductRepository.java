package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.OrderProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {

  boolean existsByCodeAndActiveIsTrue(String code);

  boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

  Optional<OrderProduct> findByIdAndActiveIsTrue(Integer id);
  
  Optional<OrderProduct> findByCodeAndActiveTrue(String code);

  List<OrderProduct> findAllByActiveIsTrue();

  Page<OrderProduct> findAll(Specification<OrderProduct> spec, Pageable pageable);

  List<OrderProduct> findAll(Specification<OrderProduct> spec);

}
