package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StorehouseDto;
import com.gestion.almacenes.entities.Storehouse;
import com.gestion.almacenes.services.StorehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/storehouse")
@Tag(name = "Gestión de Almacenes (StoreHouseController)")
public class StorehouseController {

  private final StorehouseService storehouseService;

  @GetMapping
  @Operation(summary = "Obtener todos los registros")
  public ResponseEntity<List<Storehouse>> getAll() {
    List<Storehouse> storeHouses = storehouseService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(storeHouses);
  }

  @PostMapping
  @Operation(summary = "Creación del registro")
  public ResponseEntity<Storehouse> create(@Valid @RequestBody StorehouseDto dto) {
    Storehouse storeHouseSaved = storehouseService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeHouseSaved);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Edición del registro")
  public ResponseEntity<Storehouse> update(@PathVariable Integer id,
      @Valid @RequestBody StorehouseDto dto) {
    Storehouse storeHouseUpdated = storehouseService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeHouseUpdated);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtención de los datos del registro por el identificador")
  public ResponseEntity<Storehouse> getById(@PathVariable Integer id) {
    Storehouse storeHouse = storehouseService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(storeHouse);
  }
  
  @Operation(summary = "Obtención de los datos del registro por el código identificador")
  @GetMapping("/getByCode/{code}")
  public ResponseEntity<Storehouse> getByCode(@PathVariable String code) {
      Storehouse storehouse = storehouseService.getByCode(code);
      return ResponseEntity.status(HttpStatus.OK).body(storehouse);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminación del registro por el identificador")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    storehouseService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de registros por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<Storehouse>> search(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<Storehouse> storeHouseListFiltered = storehouseService.search(code, name);

    return ResponseEntity.status(HttpStatus.OK).body(storeHouseListFiltered);
  }

  @Operation(summary = "Paginador y buscador de registros por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<Storehouse>> pageable(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name
  ) {
    PagePojo<Storehouse> storeHousePagePojoFiltered = storehouseService.pageable(page, size,
        sortField, sortOrder, code, name);

    return ResponseEntity.status(HttpStatus.OK).body(storeHousePagePojoFiltered);
  }

}
