package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.Packing;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackingRepository extends JpaRepository<Packing, Integer> {

  boolean existsByCodeAndActiveIsTrue(String code);

  boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

  Optional<Packing> findByIdAndActiveIsTrue(Integer id);
  
  Optional<Packing> findByCodeAndActiveTrue(String code);

  List<Packing> findAllByActiveIsTrue();

  Page<Packing> findAll(Specification<Packing> spec, Pageable pageable);

  List<Packing> findAll(Specification<Packing> spec);

}
