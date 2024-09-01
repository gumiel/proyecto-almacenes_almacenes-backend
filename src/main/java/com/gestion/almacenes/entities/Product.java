package com.gestion.almacenes.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
@Schema( name = "Entity Product (Producto)")
public class Product extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(length = 30, nullable = false)
  private String code;
  @Column(length = 100, nullable = false)
  private String name;
  @Column(length = 500, nullable = false)
  private String description;


  @ManyToOne
  @JoinColumn(columnDefinition = "unit_measurement_id")
  private UnitMeasurement unitMeasurement;

}
