package com.gestion.almacenes.controllers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.ConfigDto;
import com.gestion.almacenes.entities.Config;
import com.gestion.almacenes.services.ConfigService;
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
@RequestMapping("/config")
@Tag(name = "Gestión de parametros generales del sistema (ConfigController)", description = "Estas son variables que pueden ser modificadas en cualquier momento")
public class ConfigController {

  private final ConfigService configService;

  @Operation(summary = "Obtener todos los registros")
  @GetMapping
  public ResponseEntity<List<Config>> getAll() {
    List<Config> configs = configService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(configs);
  }

  @Operation(summary = "Creación del registro")
  @PostMapping
  public ResponseEntity<Config> create(@Valid @RequestBody ConfigDto dto) {
    Config configSaved = configService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(configSaved);
  }

  @Operation(summary = "Edición del registro")
  @PutMapping("/{id}")
  public ResponseEntity<Config> update(@PathVariable Integer id,
      @Valid @RequestBody ConfigDto dto) {
    Config configUpdated = configService.update(id, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(configUpdated);
  }

  @Operation(summary = "Obtención de los datos del registro por el identificador")
  @GetMapping("/{id}")
  public ResponseEntity<Config> getById(@PathVariable Integer id) {
    Config config = configService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(config);
  }

  @Operation(summary = "Obtención de los datos del registro por el código identificador")
  @GetMapping("/{code}")
  public ResponseEntity<Config> getById(@PathVariable String code) {
      Config config = configService.getByCode(code);
      return ResponseEntity.status(HttpStatus.OK).body(config);
  }

  @Operation(summary = "Eliminación del registro por el identificador")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    configService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Buscador de registros por atributos")
  @GetMapping("/search")
  public ResponseEntity<List<Config>> getFiltered(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name) {
    List<Config> configListFiltered = configService.getFiltered(code, name);
    return ResponseEntity.status(HttpStatus.OK).body(configListFiltered);
  }

  @Operation(summary = "Paginador y buscador de registros por atributos")
  @GetMapping("/pageable")
  public ResponseEntity<PagePojo<Config>> getAllPagination(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortOrder,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String value
  ) {
    PagePojo<Config> configPagePojoFiltered = configService.getByPageAndFilters(page, size,
        sortField, sortOrder, code, value);
    return ResponseEntity.status(HttpStatus.OK).body(configPagePojoFiltered);
  }


}
