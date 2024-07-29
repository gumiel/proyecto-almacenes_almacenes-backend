package com.gestion.almacenes.dtos;

import com.gestion.almacenes.commons.customValidations.ValidCode;
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
public class ProductDto {

  @ValidCode
  @NotNull(message = "{field} Código {is.required}")
  @NotBlank(message = "{field} Código {cant.be.empty}")
  @Size(max = 30, message = "{field} Código {cannot.have.more.than} 30 {characters}")
  @Schema(description = "Código del producto")
  private String code;

  @NotNull(message = "{field} Nombre {is.required}")
  @NotBlank(message = "{field} Nombre {cant.be.empty}")
  @Size(max = 100, message = "{field} Nombre {cannot.have.more.than} 100 {characters}")
  @Schema(description = "Nombre del producto")
  private String name;

    @NotNull( message = "{field} Descripción {is.required}" )
    @NotBlank( message = "{field} Descripción {cant.be.empty}" )
    @Size(max = 500, message = "{field} Descripción {cannot.have.more.than} 500 {characters}")
    private String description;
    @NotNull( message = "{field} Identificador de Unidad de medida {is.required}" )
    private Integer unitMeasurementId;

}
