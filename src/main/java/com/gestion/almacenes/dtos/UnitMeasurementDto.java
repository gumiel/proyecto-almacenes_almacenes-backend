package com.gestion.almacenes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema( name = "DTO UnitMeasurementDto (DTO Unidad de medida)")
public class UnitMeasurementDto {

  @NotNull(message = "{field} Código de la unidad de medida {is.required}")
  @NotBlank(message = "{field} Código de la unidad de medida {cant.be.empty}")
  @Size(max = 30, message = "{field} Código de la unidad de medida {cannot.have.more.than} 30 {characters}")
  @Schema(description = "Abreviatura o codigo de la unidad de medida.")
  private String codeUnit;

  @NotNull(message = "{field} Nombre de la unidad de medida {is.required}")
  @NotBlank(message = "{field} Nombre de la unidad de medida {cant.be.empty}")
  @Size(max = 100, message = "{field} Nombre de la unidad de medida {cannot.have.more.than} 100 {characters}")
  @Schema(description = "Nombre de la unidad de medida.")
  private String nameUnit;

}
