package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.StorehouseType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorehouseTypeRepository extends JpaRepository<StorehouseType, Integer> {

  boolean existsByCodeAndActiveIsTrue(String code);

  boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

  Optional<StorehouseType> findByIdAndActiveIsTrue(Integer id);
  
  Optional<StorehouseType> findByCodeAndActiveTrue(String code);

  List<StorehouseType> findAllByActiveIsTrue();

  List<StorehouseType> findAll(Specification<StorehouseType> spec);

  Page<StorehouseType> findAll(Specification<StorehouseType> spec, Pageable pageable);

}
