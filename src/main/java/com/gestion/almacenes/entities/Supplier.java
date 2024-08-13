package com.gestion.almacenes.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name= "supplier" )
@Schema( name = "Entity Supplier (Proveedor)")
public class Supplier  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Fecha de registro")
    private LocalDateTime registerDate;

    @Schema(description = "Codigo de proveedor")
    @Column(length = 30, nullable = false)
    private String supplierCode;

    @Schema(description = "Numero de telefono del proveedor")
    @Column(length = 30)
    private String supplierPhoneNumber;

    @Schema(description = "Numero de celular del proveedor")
    @Column(length = 30)
    private String supplierCelNumber;

    @Schema(description = "Nombre de la compañia o persona proveedora")
    @Column(length = 100)
    private String companyName;

    @Schema(description = "Direccion de la compañia o persona proveedora")
    @Column(length = 300)
    private String address;

    @Schema(description = "Descripcion del la compañia")
    @Column(length = 500)
    private String companyDescription;

    @Schema(description = "Estado del proveedor")
    @Column(nullable = false)
    private Boolean enable;

    @Schema(description = "Nombre del propietario")
    @Column(length = 100)
    private String ownerNames;

    @Schema(description = "Apellido del propietario")
    @Column(length = 100)
    private String ownerSurname;

    @Schema(description = "Correo electronico")
    @Column(length = 100)
    private String email;
    
}
