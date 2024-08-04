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
@Schema( name = "DTO ConfigDto (DTO Configuraciones)")
public class ConfigDto {

  @NotNull(message = "{field} Código {is.required}")
  @NotBlank(message = "{field} Código {cant.be.empty}")
  @Size(max = 30, message = "{field} Código {cannot.have.more.than} 30 {characters}")
  @Schema(description = "Codigo del parametro de configuracion.")
  private String code;

  @NotNull(message = "{field} valor {is.required}")
  @NotBlank(message = "{field} valor {cant.be.empty}")
  @Size(max = 100, message = "{field} valor {cannot.have.more.than} 100 {characters}")
  @Schema(description = "Valor del parametro de configuracion.")
  private String value;

  @NotNull(message = "{field} Descripción {is.required}")
  @NotBlank(message = "{field} Descripción {cant.be.empty}")
  @Size(max = 500, message = "{field} Descripción {cannot.have.more.than} 500 {characters}")
  @Schema(description = "Descripción del parametro de configuracion.")
  private String description;


}
