package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StockDto;
import com.gestion.almacenes.entities.Stock;
import com.gestion.almacenes.services.StockService;
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
@RequestMapping("/stock")
@Tag(name = "Gestión de la Existencia de productos por almacen (StockController)", description = "Es solo para el caso de que un almacen ya tenga un stock al iniciar el sistema, los datos de ingreso o salida se gestionan por las Ordenes")
public class StockController {

  private final StockService stockService;

  @Operation(summary = "Obtener todos los registros")
  @GetMapping
  public ResponseEntity<List<Stock>> getAll() {
    List<Stock> stocks = stockService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(stocks);
  }

  @Operation(summary = "Creación del registro")
  @PostMapping
  public ResponseEntity<Stock> create(@Valid @RequestBody StockDto dto) {
    Stock stockSaved = stockService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(stockSaved);
  }

  @Operation(summary = "Edición del registro")
  @PutMapping("/{id}")
  public ResponseEntity<Stock> update(@PathVariable Integer id, @Valid @RequestBody StockDto dto) {
    Stock stockUpdated = stockService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(stockUpdated);
  }

  @Operation(summary = "Obtención de los datos del registro por el identificador")
  @GetMapping("/{id}")
  public ResponseEntity<Stock> getById(@PathVariable Integer id) {
    Stock stock = stockService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(stock);
  }

  @Operation(summary = "Obtención del stock segun el identificador de producto y almacen")
  @GetMapping("/getStockByStorehouseIdAndProductId")
  public ResponseEntity<Stock> getStockByStorehouseIdAndProductId(
          @RequestParam(defaultValue = "0") int storehouseId,
          @RequestParam(defaultValue = "0") int productId ) {
    Stock stock = stockService.getStockByStorehouseIdAndProductId(storehouseId, productId);
    return ResponseEntity.status(HttpStatus.OK).body(stock);
  }

  @Operation(summary = "Eliminación del registro por el identificador")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    stockService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de registros por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<Stock>> getFiltered(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<Stock> stockListFiltered = stockService.getFiltered(code, name);
    return ResponseEntity.status(HttpStatus.OK).body(stockListFiltered);
  }

  @Operation(summary = "Paginador y buscador de registros por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<Stock>> getAllPagination(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name
  ) {
    PagePojo<Stock> stockPagePojoFiltered = stockService.getByPageAndFilters(page, size, sortField,
        sortOrder, code, name);
    return ResponseEntity.status(HttpStatus.OK).body(stockPagePojoFiltered);
  }

}
