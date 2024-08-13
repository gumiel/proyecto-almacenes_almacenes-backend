package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductTypeDto;
import com.gestion.almacenes.entities.OrderProductType;
import com.gestion.almacenes.services.OrderProductTypeService;
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
@RequestMapping("/orderProductType")
@Tag(name = "Gestión de Tipo de ordenes (OrderTypeController)", description = "Estos tipos son las ordenes que puede tener por Ejemplo Orden de Ingreso a almacen, Orden de Salida del almacen")
public class OrderProductTypeController {

  private final OrderProductTypeService orderProductTypeService;

  @Operation(summary = "Obtener todos los registros")
  @GetMapping
  public ResponseEntity<List<OrderProductType>> getAll() {
    List<OrderProductType> orderProductTypes = orderProductTypeService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(orderProductTypes);
  }

  @Operation(summary = "Creación del registro")
  @PostMapping
  public ResponseEntity<OrderProductType> create(@Valid @RequestBody OrderProductTypeDto dto) {
    OrderProductType orderProductTypeSaved = orderProductTypeService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderProductTypeSaved);
  }

  @Operation(summary = "Edición del registro")
  @PutMapping("/{id}")
  public ResponseEntity<OrderProductType> update(@PathVariable Integer id,
      @Valid @RequestBody OrderProductTypeDto dto) {
    OrderProductType orderProductTypeUpdated = orderProductTypeService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderProductTypeUpdated);
  }

  @Operation(summary = "Obtención de los datos del registro por el identificador")
  @GetMapping("/{id}")
  public ResponseEntity<OrderProductType> getById(@PathVariable Integer id) {
    OrderProductType orderProductType = orderProductTypeService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(orderProductType);
  }

  @Operation(summary = "Obtención de los datos del registro por el código identificador")
  @GetMapping("/getByCode/{code}")
  public ResponseEntity<OrderProductType> getByCode(@PathVariable String code) {
      OrderProductType orderProductType = orderProductTypeService.getByCode(code);
      return ResponseEntity.status(HttpStatus.OK).body(orderProductType);
  }

  @Operation(summary = "Eliminación del registro por el identificador")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    orderProductTypeService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de registros por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<OrderProductType>> getFiltered(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<OrderProductType> orderProductTypeListFiltered = orderProductTypeService.getFiltered(code,
        name);
    return ResponseEntity.status(HttpStatus.OK).body(orderProductTypeListFiltered);
  }

  @Operation(summary = "Paginador y buscador de registros por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<OrderProductType>> getAllPagination(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name
  ) {
    PagePojo<OrderProductType> orderTypePagePojoFiltered = orderProductTypeService.getByPageAndFilters(
        page, size, sortField, sortOrder, code, name);
    return ResponseEntity.status(HttpStatus.OK).body(orderTypePagePojoFiltered);
  }


}
