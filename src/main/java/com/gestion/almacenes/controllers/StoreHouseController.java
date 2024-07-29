package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StoreHouseDto;
import com.gestion.almacenes.dtos.StorehouseProductDto;
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
public class StoreHouseController {

  private final StorehouseService storeHouseService;

  @GetMapping
  @Operation(summary = "Obtener todos los registros")
  public ResponseEntity<List<Storehouse>> getAll() {
    List<Storehouse> storeHouses = storeHouseService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(storeHouses);
  }

  @PostMapping
  @Operation(summary = "Creación del registro")
  public ResponseEntity<Storehouse> create(@Valid @RequestBody StoreHouseDto dto) {
    Storehouse storeHouseSaved = storeHouseService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeHouseSaved);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Edición del registro")
  public ResponseEntity<Storehouse> update(@PathVariable Integer id,
      @Valid @RequestBody StoreHouseDto dto) {
    Storehouse storeHouseUpdated = storeHouseService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(storeHouseUpdated);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtención de los datos del registro por el identificador")
  public ResponseEntity<Storehouse> getById(@PathVariable Integer id) {
    Storehouse storeHouse = storeHouseService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(storeHouse);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminación del registro por el identificador")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    storeHouseService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de registros por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<Storehouse>> search(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<Storehouse> storeHouseListFiltered = storeHouseService.search(code, name);

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
    PagePojo<Storehouse> storeHousePagePojoFiltered = storeHouseService.pageable(page, size,
        sortField, sortOrder, code, name);

    return ResponseEntity.status(HttpStatus.OK).body(storeHousePagePojoFiltered);
  }

  @Operation(summary = "Relacionar un producto con un almancen")
  @PostMapping("/addProductToStorehouse")
  public ResponseEntity<Storehouse> addProductToStorehouse(
      @Valid @RequestBody StorehouseProductDto dto) {
    Storehouse storehouseSaved = storeHouseService.addProductToStorehouse(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(storehouseSaved);
  }

  @Operation(summary = "Eliminar relacion de un producto con un almancen")
  @DeleteMapping("/removeProductToStorehouse")
  public ResponseEntity<Storehouse> removeProductToStorehouse(
      @Valid @RequestBody StorehouseProductDto dto) {
    storeHouseService.removeProductToStorehouse(dto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
