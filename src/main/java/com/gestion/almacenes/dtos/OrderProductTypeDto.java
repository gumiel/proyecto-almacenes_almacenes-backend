package com.gestion.almacenes.dtos;

import com.gestion.almacenes.commons.customValidations.ValueOfEnum;
import com.gestion.almacenes.commons.enums.OrderProductTypeActionEnum;
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
@Schema( name = "DTO OrderProductTypeDto (DTO Tipo de Orden)")
public class OrderProductTypeDto {

  @NotNull(message = "{field} Código {is.required}")
  @NotBlank(message = "{field} Código {cant.be.empty}")
  @Size(max = 30, message = "{field} Código {cannot.have.more.than} 30 {characters}")
  @Schema(description = "Codigo del tipo de orden.")
  private String code;

  @NotNull(message = "{field} Nombre {is.required}")
  @NotBlank(message = "{field} Nombre {cant.be.empty}")
  @Size(max = 100, message = "{field} Nombre {cannot.have.more.than} 100 {characters}")
  @Schema(description = "Nombre del timpo de orden.")
  private String name;

  @Size(max = 500, message = "{field} Descripción {cannot.have.more.than} 500 {characters}")
  @Schema(description = "Descripcion del tipo de orden.")
  private String description;

  @ValueOfEnum(enumClass = OrderProductTypeActionEnum.class, message = "Debe ser cualquier dato entre RECEIPT (Ingreso) o DISPATCH (Salida)")
  @NotNull(message = "{field} Accion {is.required}")
  @Schema(description = "Indica la accion que puede tener que son solo 2 de ingreso (RECEIPT) o salida (DISPATCH).")
  private String action;


}
