package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{

    boolean existsBySupplierCodeAndActiveIsTrue(String code);

    boolean existsBySupplierCodeAndIdNotAndActiveIsTrue(String code, Integer id);

    Optional<Supplier> findByIdAndActiveIsTrue(Integer id);

    Optional<Supplier> findBySupplierCodeAndActiveTrue(String supplierCode);

    List<Supplier> findAllByActiveIsTrue();

    Page<Supplier> findAll(Specification<Supplier> spec, Pageable pageable);
    List<Supplier> findAll(Specification<Supplier> spec);

}
