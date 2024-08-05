package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductDto;
import com.gestion.almacenes.entities.OrderProduct;
import com.gestion.almacenes.services.OrderProductService;
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
@RequestMapping("/orderProduct")
@Tag(name = "Gestion de Ordenes para ingresos o salidas de almacen (OrderProductController)", description = "Segun las ordenes que se realizan pueden ser del tipo de ingreso o salida y estas son las unicas que gestionaran los movimientos que tendra los almacenes")
public class OrderProductController {

  private final OrderProductService orderProductService;

  @Operation(summary = "Obtener todos los registros")
  @GetMapping
  public ResponseEntity<List<OrderProduct>> getAll() {
    List<OrderProduct> orderProducts = orderProductService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(orderProducts);
  }

  @Operation(summary = "Creación del registro para una orden de ingreso o salida")
  @PostMapping
  public ResponseEntity<OrderProduct> create(@Valid @RequestBody OrderProductDto dto) {
    OrderProduct orderProductSaved = orderProductService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderProductSaved);
  }

  @Operation(summary = "Edición del registro para una orden de ingreso o salida")
  @PutMapping("/{id}")
  public ResponseEntity<OrderProduct> update(@PathVariable Integer id,
      @Valid @RequestBody OrderProductDto dto) {
    OrderProduct orderProductUpdated = orderProductService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderProductUpdated);
  }

  @Operation(summary = "Obtención de los datos de la orden por el identificador")
  @GetMapping("/{id}")
  public ResponseEntity<OrderProduct> getById(@PathVariable Integer id) {
    OrderProduct orderProduct = orderProductService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(orderProduct);
  }

  @Operation(summary = "Obtención de los datos del registro por el código identificador")
  @GetMapping("/{code}")
  public ResponseEntity<OrderProduct> getById(@PathVariable String code) {
      OrderProduct orderProduct = orderProductService.getByCode(code);
      return ResponseEntity.status(HttpStatus.OK).body(orderProduct);
  }

  @Operation(summary = "Eliminación del registro para una orden de ingreso o salida")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    orderProductService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de ordenes por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<OrderProduct>> getFiltered(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<OrderProduct> orderProductListFiltered = orderProductService.getFiltered(code, name);

    return ResponseEntity.status(HttpStatus.OK).body(orderProductListFiltered);
  }

  @Operation(summary = "Paginador y buscador de ordenes por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<OrderProduct>> getAllPagination(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name
  ) {
    PagePojo<OrderProduct> orderProductPagePojoFiltered = orderProductService.getByPageAndFilters(
        page, size, sortField, sortOrder, code, name);

    return ResponseEntity.status(HttpStatus.OK).body(orderProductPagePojoFiltered);
  }

  @Operation(summary = "Ejecuta una orden de ingreso o salida para que sume o disminuya el stock del almacen")
  @PostMapping("/executeOrderProduct")
  public ResponseEntity<OrderProduct> executeOrderProduct(@RequestBody OrderProductDto dto) {
    OrderProduct orderProductSaved = orderProductService.executeOrderProduct(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderProductSaved);
  }


}
