package com.gestion.almacenes.dtos;

import com.gestion.almacenes.commons.customValidations.ValidCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "DTO ProductDto (DTO Producto)")
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
  @Schema(description = "Nombre del producto.")
  private String name;

  @NotNull(message = "{field} Descripción {is.required}")
  @NotBlank(message = "{field} Descripción {cant.be.empty}")
  @Size(max = 500, message = "{field} Descripción {cannot.have.more.than} 500 {characters}")
  @Schema(description = "Descripción del producto.")
  private String description;

  @NotNull(message = "{field} Identificador de Unidad de medida {is.required}")
  private Integer unitMeasurementId;

  @Schema(description = "Atributo que identifica si se asociara a todos los almacenes.")
  private Boolean selectAllStorehouse = false;

  @Schema(description = "Lista de identificadores de almacenesalmacenes.")
  private List<Integer> storehouseIds;

}
