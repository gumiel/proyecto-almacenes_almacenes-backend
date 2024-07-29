package com.gestion.almacenes.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Detalle de ordenes
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "order_product_detail")
@AllArgsConstructor
public class OrderProductDetail extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "order_product_id")
  private OrderProduct orderProduct;

  @ManyToOne
  @JoinColumn(name = "stock_id")
  private Stock stock;

  @Column(columnDefinition = "NUMERIC(18, 4)")
  private Double amount;

}