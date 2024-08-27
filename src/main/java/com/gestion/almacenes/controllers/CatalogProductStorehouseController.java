package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.CatalogProductStorehouseDto;
import com.gestion.almacenes.entities.CatalogProductStorehouse;
import com.gestion.almacenes.services.CatalogProductStorehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/catalogProductStorehouse")
@Tag(name = "CatalogProductStorehouseController")
public class CatalogProductStorehouseController {

    private final CatalogProductStorehouseService catalogProductStorehouseService;

    @Operation(summary = "Obtener todos los registros")
    @GetMapping
    public ResponseEntity<List<CatalogProductStorehouse>> getAll() {
        List<CatalogProductStorehouse> catalogProductStorehouses = catalogProductStorehouseService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(catalogProductStorehouses);
    }

    @Operation(summary = "Creación del registro")
    @PostMapping
    public ResponseEntity<CatalogProductStorehouse> create(@Valid @RequestBody CatalogProductStorehouseDto dto) {
        CatalogProductStorehouse catalogProductStorehouseSaved = catalogProductStorehouseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogProductStorehouseSaved);
    }

    @Operation(summary = "Edición del registro")
    @PutMapping("/{id}")
    public ResponseEntity<CatalogProductStorehouse> update(@PathVariable Integer id, @Valid @RequestBody CatalogProductStorehouseDto dto) {
        CatalogProductStorehouse catalogProductStorehouseUpdated = catalogProductStorehouseService.update(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogProductStorehouseUpdated);
    }

    @Operation(summary = "Obtención de los datos del registro por el identificador")
    @GetMapping("/{id}")
    public ResponseEntity<CatalogProductStorehouse> getById(@PathVariable Integer id) {
        CatalogProductStorehouse catalogProductStorehouse = catalogProductStorehouseService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(catalogProductStorehouse);
    }

    @Operation(summary = "Eliminación del registro por el identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        catalogProductStorehouseService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Eliminación del registro por el identificador")
    @DeleteMapping("/deleteByProductAndStorehouse")
    public ResponseEntity<Void> deleteByProductAndStorehouse(@RequestBody CatalogProductStorehouseDto dto) {
        catalogProductStorehouseService.deleteByProductAndStorehouse(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Buscador de registros por atributos")
    @GetMapping("/search")
    public ResponseEntity<List<CatalogProductStorehouse>> getFiltered() {
        List<CatalogProductStorehouse> catalogProductStorehouseListFiltered = catalogProductStorehouseService.getFiltered();

        return ResponseEntity.status(HttpStatus.OK).body(catalogProductStorehouseListFiltered);
    }

    @Operation(summary = "Paginador y buscador de registros por atributos")
    @GetMapping("/pageable")
    public ResponseEntity<PagePojo<CatalogProductStorehouse>> getAllPagination(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        PagePojo<CatalogProductStorehouse> catalogProductStorehousePagePojoFiltered = catalogProductStorehouseService.getByPageAndFilters(page, size, sortField, sortOrder);

        return ResponseEntity.status(HttpStatus.OK).body(catalogProductStorehousePagePojoFiltered);
    }

    @Operation(summary = "Agrega todos los productos al catalogo de un almacén.")
    @PostMapping("/addAllProductsToStorehouse")
    public ResponseEntity<Void> addAllProductsToStorehouse(@Valid @RequestBody CatalogProductStorehouseDto dto) {
        catalogProductStorehouseService.addAllProductsToStorehouse(dto.getStorehouseId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
