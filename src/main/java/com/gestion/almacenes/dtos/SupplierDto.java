package com.gestion.almacenes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
public class SupplierDto {

    @Schema(description = "Fecha de registro")
    private LocalDateTime registerDate;

    @Schema(description = "Codigo de proveedor")
    @NotNull( message = "{field} Código del proveedor {is.required}" )
    @NotBlank( message = "{field} Código del proveedor {cant.be.empty}" )
    @Size(max = 30, message = "{field} Código del proveedor {cannot.have.more.than} 30 {characters}")
    private String supplierCode;

    @Schema(description = "Numero de telefono del proveedor")
    @Size(max = 30, message = "{field} Numero de telefono {cannot.have.more.than} 30 {characters}")
    private String supplierPhoneNumber;

    @Schema(description = "Numero de celular del proveedor")
    @Size(max = 30, message = "{field} Numero de celular {cannot.have.more.than} 30 {characters}")
    private String supplierCelNumber;

    @Schema(description = "Nombre de la compañia o persona proveedora")
    @Size(max = 100, message = "{field} Nombre de la compañia o persona proveedora {cannot.have.more.than} 100 {characters}")
    private String companyName;

    @Schema(description = "Direccion de la compañia o persona proveedora")
    @Size(max = 300, message = "{field} Direccion de la compañia o persona proveedora {cannot.have.more.than} 300 {characters}")
    private String address;

    @Schema(description = "Descripcion del la compañia")
    @Size(max = 500, message = "{field} Descripcion del la compañia {cannot.have.more.than} 500 {characters}")
    private String companyDescription;

    @Schema(description = "Estado del proveedor")
    @NotNull( message = "{field} Estado del proveedor {is.required}" )
    private Boolean enable;

    @Schema(description = "Nombre del propietario")
    @Size(max = 100, message = "{field} Nombre del propietario {cannot.have.more.than} 100 {characters}")
    private String ownerNames;

    @Schema(description = "Apellido del propietario")
    @Size(max = 100, message = "{field} Apellido del propietario {cannot.have.more.than} 100 {characters}")
    private String ownerSurname;

    @Schema(description = "Correo electronico")
    @Size(max = 100, message = "{field} Correo electronico {cannot.have.more.than} 100 {characters}")
    private String email;
    
}