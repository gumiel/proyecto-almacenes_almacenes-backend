package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.PackingProduct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PackingProductRepository extends JpaRepository<PackingProduct, Integer>{

    boolean existsByCodeAndActiveIsTrue(String code);

    boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

    Optional<PackingProduct> findByIdAndActiveIsTrue(Integer id);

    List<PackingProduct> findAllByActiveIsTrue();

    Optional<PackingProduct> findByCodeAndActiveTrue(String code);
}
