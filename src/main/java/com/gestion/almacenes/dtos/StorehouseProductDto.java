package com.gestion.almacenes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema( name = "DTO StorehouseProductDto (DTO Productos por almacen)")
public class StorehouseProductDto {

  @NotNull(message = "{field} Idetinficador de Almacen {is.required}")
  @Schema(description = "Relacionador que identifica al almacen.")
  private Integer storehouseId;
  @NotNull(message = "{field} Idetinficador de Producto {is.required}")
  @Schema(description = "Relacionador que identifica al producto.")
  private Integer productId;

}
