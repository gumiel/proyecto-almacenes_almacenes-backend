package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductDetailDto;
import com.gestion.almacenes.entities.OrderProductDetail;
import com.gestion.almacenes.services.OrderProductDetailService;
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
@RequestMapping("/orderProductDetail")
@Tag(name = "Gestión de los detalles de una orden (OrderProductDetailController)")
public class OrderProductDetailController {

  private final OrderProductDetailService orderProductDetailService;

  @Operation(summary = "Obtener todos los detalles de todas las ordenes")
  @GetMapping
  public ResponseEntity<List<OrderProductDetail>> getAll() {
    List<OrderProductDetail> orderProductDetails = orderProductDetailService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(orderProductDetails);
  }

  @Operation(summary = "Creación del detalle de una orden")
  @PostMapping
  public ResponseEntity<OrderProductDetail> create(@Valid @RequestBody OrderProductDetailDto dto) {
    OrderProductDetail orderProductDetailSaved = orderProductDetailService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderProductDetailSaved);
  }

  @Operation(summary = "Creación del detalle de una orden")
  @PostMapping("/createList")
  public ResponseEntity<Void> createList(@Valid @RequestBody List<OrderProductDetailDto> orderProductDetailDtos) {
    orderProductDetailService.createList(orderProductDetailDtos);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Edición del detalle de una orden")
  @PutMapping("/{id}")
  public ResponseEntity<OrderProductDetail> update(@PathVariable Integer id,
      @Valid @RequestBody OrderProductDetailDto dto) {
    OrderProductDetail orderProductDetailUpdated = orderProductDetailService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderProductDetailUpdated);
  }

  @Operation(summary = "Obtención del detalle de una orden")
  @GetMapping("/{id}")
  public ResponseEntity<OrderProductDetail> getById(@PathVariable Integer id) {
    OrderProductDetail orderProductDetail = orderProductDetailService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(orderProductDetail);
  }

  @Operation(summary = "Eliminación del detalle de una orden")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    orderProductDetailService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de detalles de las ordenes por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<OrderProductDetail>> getFiltered(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<OrderProductDetail> orderProductDetailListFiltered = orderProductDetailService.getFiltered(
        code, name);
    return ResponseEntity.status(HttpStatus.OK).body(orderProductDetailListFiltered);
  }

  @Operation(summary = "Paginador y buscador de los detalles de las ordenes por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<OrderProductDetail>> getAllPagination(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name
  ) {
    PagePojo<OrderProductDetail> orderProductDetailPagePojoFiltered = orderProductDetailService.getByPageAndFilters(
        page, size, sortField, sortOrder, code, name);
    return ResponseEntity.status(HttpStatus.OK).body(orderProductDetailPagePojoFiltered);
  }


}
