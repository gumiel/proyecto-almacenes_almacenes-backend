package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.UnitMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitMeasurementRepository extends JpaRepository<UnitMeasurement, Integer> {

  boolean existsByCodeAndActiveIsTrue(String code);

  boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

  Optional<UnitMeasurement> findByIdAndActiveIsTrue(Integer id);
  Optional<UnitMeasurement> findByCodeAndActiveTrue(String code);

  List<UnitMeasurement> findAllByActiveIsTrue();

  Page<UnitMeasurement> findAll(Specification<UnitMeasurement> spec, Pageable pageable);

  List<UnitMeasurement> findAll(Specification<UnitMeasurement> spec);

}
