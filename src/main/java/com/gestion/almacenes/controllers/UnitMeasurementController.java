package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.UnitMeasurementDto;
import com.gestion.almacenes.entities.UnitMeasurement;
import com.gestion.almacenes.services.UnitMeasurementService;
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
@RequestMapping("/unitMeasurement")
@Tag(name = "")
@Tag(name = "Gestión de unidad de medida (UnitMeasurementController)")
public class UnitMeasurementController {

  private final UnitMeasurementService unitMeasurementService;

  @GetMapping
  @Operation(summary = "Obtener todos los registros")
  public ResponseEntity<List<UnitMeasurement>> getAll() {
    List<UnitMeasurement> unitMeasurements = unitMeasurementService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(unitMeasurements);
  }

  @PostMapping
  @Operation(summary = "Creación del registro")
  public ResponseEntity<UnitMeasurement> create(@Valid @RequestBody UnitMeasurementDto dto) {
    UnitMeasurement unitMeasurementSaved = unitMeasurementService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(unitMeasurementSaved);
  }

  @Operation(summary = "Edición del registro")
  @PutMapping("/{id}")
  public ResponseEntity<UnitMeasurement> update(@PathVariable Integer id,
      @Valid @RequestBody UnitMeasurementDto dto) {
    UnitMeasurement unitMeasurementUpdated = unitMeasurementService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(unitMeasurementUpdated);
  }

  @Operation(summary = "Obtención de los datos del registro por el identificador")
  @GetMapping("/{id}")
  public ResponseEntity<UnitMeasurement> getById(@PathVariable Integer id) {
    UnitMeasurement unitMeasurement = unitMeasurementService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(unitMeasurement);
  }

  @Operation(summary = "Obtención de los datos del registro por el código identificador")
  @GetMapping("/getByCode/{code}")
  public ResponseEntity<UnitMeasurement> getByCode(@PathVariable String code) {
      UnitMeasurement unitMeasurement = unitMeasurementService.getByCode(code);
      return ResponseEntity.status(HttpStatus.OK).body(unitMeasurement);
  }

  @Operation(summary = "Eliminación del registro por el identificador")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    unitMeasurementService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de registros por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<UnitMeasurement>> getFiltered(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<UnitMeasurement> unitMeasurementListFiltered = unitMeasurementService.getFiltered(code,
        name);

    return ResponseEntity.status(HttpStatus.OK).body(unitMeasurementListFiltered);
  }

  @Operation(summary = "Paginador y buscador de registros por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<UnitMeasurement>> getAllPagination(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name

  ) {
    PagePojo<UnitMeasurement> unitMeasurementPagePojoFiltered = unitMeasurementService.getByPageAndFilters(
        page, size, sortField, sortOrder, code, name);

    return ResponseEntity.status(HttpStatus.OK).body(unitMeasurementPagePojoFiltered);
  }


}
