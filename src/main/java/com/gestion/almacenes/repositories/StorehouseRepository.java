package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.Storehouse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorehouseRepository extends JpaRepository<Storehouse, Integer> {

  boolean existsByCodeAndActiveIsTrue(String code);

  boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

  Optional<Storehouse> findByIdAndActiveIsTrue(Integer id);

  Optional<Storehouse> findByCodeAndActiveTrue(String code);
  
  List<Storehouse> findAll(Specification<Storehouse> spec);

  Page<Storehouse> findAll(Specification<Storehouse> spec, Pageable pageable);
}