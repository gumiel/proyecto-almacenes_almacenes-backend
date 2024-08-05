package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

  boolean existsByCodeAndActiveIsTrue(String code);

  boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

  Optional<Product> findById(Integer id);
  
  Optional<Product> findByCodeAndActiveTrue(String code);

  Optional<Product> findByIdAndActiveIsTrue(Integer id);

  List<Product> findAll();

  Page<Product> findAll(Specification<Product> spec, Pageable pageable);

  List<Product> findAll(Specification<Product> spec);
}
