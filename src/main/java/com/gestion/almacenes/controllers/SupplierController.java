package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.SupplierDto;
import com.gestion.almacenes.entities.Supplier;
import com.gestion.almacenes.services.SupplierService;
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
@RequestMapping("/supplier")
@Tag(name = "SupplierController")
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "Obtener todos los registros")
    @GetMapping
    public ResponseEntity<List<Supplier>> getAll() {
        List<Supplier> suppliers = supplierService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(suppliers);
    }

    @Operation(summary = "Creación del registro")
    @PostMapping
    public ResponseEntity<Supplier> create(@Valid @RequestBody SupplierDto dto) {
        Supplier supplierSaved = supplierService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierSaved);
    }

    @Operation(summary = "Edición del registro")
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable Integer id, @Valid @RequestBody SupplierDto dto) {
        Supplier supplierUpdated = supplierService.update(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierUpdated);
    }

    @Operation(summary = "Obtención de los datos del registro por el identificador")
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable Integer id) {
        Supplier supplier = supplierService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(supplier);
    }
    
    @Operation(summary = "Obtención de los datos del registro por el código identificador")
    @GetMapping("/getBySupplierCode/{supplierCode}")
    public ResponseEntity<Supplier> getBySupplierCode(@PathVariable String supplierCode) {
        Supplier supplier = supplierService.getBySupplierCode(supplierCode);
        return ResponseEntity.status(HttpStatus.OK).body(supplier);
    }

    @Operation(summary = "Eliminación del registro por el identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        supplierService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Buscador de registros por atributos")
    @GetMapping("/search")
    public ResponseEntity<List<Supplier>> getFiltered(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {
        List<Supplier> supplierListFiltered = supplierService.getFiltered(code, name);

        return ResponseEntity.status(HttpStatus.OK).body(supplierListFiltered);
    }

    @Operation(summary = "Paginador y buscador de registros por atributos")
    @GetMapping("/pageable")
    public ResponseEntity<PagePojo<Supplier>> getAllPagination(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "asc") String sortOrder,
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String name
    ) {
        PagePojo<Supplier> supplierPagePojoFiltered = supplierService.getByPageAndFilters(page, size, sortField, sortOrder, code, name);

        return ResponseEntity.status(HttpStatus.OK).body(supplierPagePojoFiltered);
    }


}