
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
@Schema( name = "DTO StoreHouseDto (DTO Almacen)")
public class StorehouseDto {

  @NotNull(message = "El campo Código es obligatorio")
  @NotBlank(message = "El campo Código es obligatorio")
  @Size(max = 30, message = "{field} Código {cannot.have.more.than} 30 {characters}")
  @Schema(description = "Codigo del almacen.")
  private String code;

  @NotNull(message = "El campo Nombre {is.required}")
  @NotBlank(message = "El campo Nombre {cant.be.empty}")
  @Size(max = 100, message = "El campo Nombre {cannot.have.more.than} 100 {characters}")
  @Schema(description = "Nombre del almacen.")
  private String name;

  @NotNull(message = "El campo Descripción {is.required}")
  @NotBlank(message = "El campo Descripción {cant.be.empty}")
  @Size(max = 500, message = "El campo Descripción {cannot.have.more.than} 500 {characters}")
  @Schema(description = "Descripcion del almacen.")
  private String description;

  @Schema(description = "Relacionador que identifica al tipo de almacen.")
  private Integer storehouseTypeId;

}
