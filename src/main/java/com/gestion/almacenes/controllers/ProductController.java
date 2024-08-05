package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.ProductDto;
import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.services.ProductService;
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
@RequestMapping("/product")
@Tag(name = "Gestión de productos (ProductController)")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "Obtener todos los registros")
  @GetMapping
  public ResponseEntity<List<Product>> getAll() {
    List<Product> products = productService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(products);
  }

  @Operation(summary = "Creación del registro")
  @PostMapping
  public ResponseEntity<Product> create(@Valid @RequestBody ProductDto dto) {
    Product productSaved = productService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
  }

  @Operation(summary = "Edición del registro")
  @PutMapping("/{id}")
  public ResponseEntity<Product> update(@PathVariable Integer id,
      @Valid @RequestBody ProductDto dto) {
    Product productUpdated = productService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(productUpdated);
  }

  @Operation(summary = "Obtención de los datos del registro por el identificador")
  @GetMapping("/{id}")
  public ResponseEntity<Product> getById(@PathVariable Integer id) {
    Product product = productService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @Operation(summary = "Obtención de los datos del registro por el código identificador")
  @GetMapping("/{code}")
  public ResponseEntity<Product> getById(@PathVariable String code) {
      Product product = productService.getByCode(code);
      return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @Operation(summary = "Eliminación del registro por el identificador")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    productService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de registros por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<Product>> search(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<Product> productListFiltered = productService.search(code, name);

    return ResponseEntity.status(HttpStatus.OK).body(productListFiltered);
  }

  @Operation(summary = "Paginador y buscador de registros por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<Product>> pageable(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name
  ) {
    PagePojo<Product> productPagePojoFiltered = productService.pageable(page, size, sortField,
        sortOrder, code, name);
    return ResponseEntity.status(HttpStatus.OK).body(productPagePojoFiltered);
  }


}
