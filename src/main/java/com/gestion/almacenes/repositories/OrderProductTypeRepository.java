package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.OrderProductType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductTypeRepository extends JpaRepository<OrderProductType, Integer> {

  boolean existsByCodeAndActiveIsTrue(String code);

  boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

  Optional<OrderProductType> findByIdAndActiveIsTrue(Integer id);
  
  Optional<OrderProductType> findByCodeAndActiveTrue(String code);

  List<OrderProductType> findAllByActiveIsTrue();

  List<OrderProductType> findAll(Specification<OrderProductType> spec);

  Page<OrderProductType> findAll(Specification<OrderProductType> spec, Pageable pageable);

}
