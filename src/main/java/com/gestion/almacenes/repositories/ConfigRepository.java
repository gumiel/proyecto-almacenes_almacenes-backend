package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfigRepository extends JpaRepository<Config, Integer> {

  boolean existsByCodeAndActiveIsTrue(String code);

  boolean existsByCodeAndIdNotAndActiveIsTrue(String code, Integer id);

  Optional<Config> findByIdAndActiveIsTrue(Integer id);

  List<Config> findAllByActiveIsTrue();

  List<Config> findAll();

  Page<Config> findAll(Pageable pageable);

  Optional<Config> findByCodeAndActiveTrue(String code);
}
