package com.gestion.almacenes.dtos;

import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.entities.Storehouse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class CatalogProductStorehouseDto {


    @Schema(description = "Identificador de Catalogo de productos por almacen")
    private Integer catalogProductStorehouseId;

    @Schema(description = "Identificador de Almacen")
    @NotNull( message = "{field} Identificador de Almacen {is.required}" )
    private Integer storehouseId;

    @Schema(description = "Identificador de Producto")
    @NotNull( message = "{field} Identificador de Producto {is.required}" )
    private Integer productId;

}