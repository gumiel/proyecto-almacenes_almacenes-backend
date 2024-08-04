package com.gestion.almacenes.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema( name = "DTO OrderDetailPackingDto (DTO Detalle de Orden con Empaque)")
public class OrderDetailPackingDto {

  @Schema(description = "Codigo del empaque que tienen los productos.")
  private String code;
  @Schema(description = "Relacionador que identifica el detalle de la orden.")
  private Integer orderProductDetailId;
  @Schema(description = "Relacionador que identifica el empaque.")
  private Integer packingId;
  @Schema(description = "Monto que contiene el empaque.")
  private Double amount;
  @Schema(description = "Fecha de expiracion que tiene el empaque o producto")
  private LocalDate expirationDate;
  private Integer packingProductId;

}
