package com.gestion.almacenes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema( name = "DTO OrderProductDetailDto (DTO Detalle de Orden)")
public class OrderProductDetailDto {

  @Schema(description = "Relacionador que identifica el stock.")
  private Integer stockId;

  @NotNull(message = "{field} Identificador de producto {is.required}")
  @Schema(description = "Relacionador que identifica el producto")
  private Integer productId;

  @NotNull(message = "{field} cantidad {is.required}")
  @Min(0)
  @Schema(description = "Cantidad que se esta solicitando")
  private Double amount;

  @NotNull(message = "{field} Identificador de Orden {is.required}")
  @Schema(description = "Relacionador que identifica la orden de producto")
  private Integer orderProductId;

  @Schema(description = "Codigo del producto que ingresa o sale de almacen")
  private String codeProduct;

  @Schema(description = "Fecha de expiración del producto que ingresa o sale de almacen")
  private LocalDate expirationDateProduct;

}
