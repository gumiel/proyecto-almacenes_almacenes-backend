package com.gestion.almacenes.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_product_type")
public class OrderProductType extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 30, nullable = false)
  private String code;

  @Column(length = 100, nullable = false)
  private String name;

  @Column(length = 500, nullable = false)
  private String description;

  private String action; // Puede ser de ingreso (receipt) o de salida (dispatch)

}
