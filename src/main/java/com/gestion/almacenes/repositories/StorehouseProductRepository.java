package com.gestion.almacenes.repositories;

import com.gestion.almacenes.entities.CatalogProductStorehouse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorehouseProductRepository extends JpaRepository<CatalogProductStorehouse, Integer> {


}
