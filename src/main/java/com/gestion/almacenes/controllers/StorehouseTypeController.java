package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StorehouseTypeDto;
import com.gestion.almacenes.entities.StorehouseType;
import com.gestion.almacenes.services.StorehouseTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/storehouseType")
@Tag(name = "Gestión de Tipo de almacenes (StorehouseTypeController)")
public class StorehouseTypeController {

  private final StorehouseTypeService storehouseTypeService;

  @GetMapping
  @Operation(summary = "Obtener todos los registros")
  public ResponseEntity<List<StorehouseType>> getAll() {

    List<StorehouseType> storehouseTypes = storehouseTypeService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(storehouseTypes);
  }

  @Operation(summary = "Creación del registro")
  @PostMapping
  public ResponseEntity<StorehouseType> create(@Valid @RequestBody StorehouseTypeDto dto){
    StorehouseType storehouseTypeSaved = storehouseTypeService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(storehouseTypeSaved);
  }

  @Operation(summary = "Edición del registro")
  @PutMapping("/{id}")
  public ResponseEntity<StorehouseType> update(@PathVariable Integer id,
      @Valid @RequestBody StorehouseTypeDto dto) {
    StorehouseType storehouseTypeUpdated = storehouseTypeService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(storehouseTypeUpdated);
  }

  @Operation(summary = "Obtención de los datos del registro por el identificador")
  @GetMapping("/{id}")
  public ResponseEntity<StorehouseType> getById(@PathVariable Integer id) {
    StorehouseType storehouseType = storehouseTypeService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(storehouseType);
  }

  @Operation(summary = "Obtención de los datos del registro por el código identificador")
  @GetMapping("/getByCode/{code}")
  public ResponseEntity<StorehouseType> getByCode(@PathVariable String code) {
    StorehouseType storehouseType = storehouseTypeService.getByCode(code);
    return ResponseEntity.status(HttpStatus.OK).body(storehouseType);
  }

  @Operation(summary = "Eliminación del registro por el identificador")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    storehouseTypeService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de registros por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<StorehouseType>> getFiltered(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<StorehouseType> storehouseTypeListFiltered = storehouseTypeService.getFiltered(code, name);
    return ResponseEntity.status(HttpStatus.OK).body(storehouseTypeListFiltered);
  }

  @Operation(summary = "Paginador y buscador de registros por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<StorehouseType>> getAllPagination(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name
  ) {
    PagePojo<StorehouseType> storehouseTypePagePojoFiltered = storehouseTypeService.getByPageAndFilters(
        page, size, sortField, sortOrder, code, name);
    return ResponseEntity.status(HttpStatus.OK).body(storehouseTypePagePojoFiltered);
  }


}
