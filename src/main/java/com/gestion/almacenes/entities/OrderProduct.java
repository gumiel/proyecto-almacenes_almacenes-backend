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
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "order_product")
@Schema( name = "Entity OrderProduct (Orden)")
@AllArgsConstructor
public class OrderProduct extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String orderCode;

  private LocalDate registrationDate;
  private LocalTime registrationTime;

  @ManyToOne
  @JoinColumn(name = "storehouse_id")
  private Storehouse storehouse;

  @Column(length = 500)
  private String description;

  @ManyToOne
  @JoinColumn(name = "order_product_type_id")
  private OrderProductType orderProductType;

  private String status;

}