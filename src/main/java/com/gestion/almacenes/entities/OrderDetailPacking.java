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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_detail_packing")
@Schema( name = "Entity OrderDetailPacking (Detalle de Orden con Empaque)")
public class OrderDetailPacking extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String code;

  @ManyToOne
  @JoinColumn(columnDefinition = "order_product_detail_id")
  private OrderProductDetail orderProductDetail;

  @ManyToOne
  @JoinColumn(columnDefinition = "packing_id")
  private Packing packing;

  @Column(columnDefinition = "NUMERIC(18, 4)")
  private Double amount;

  private LocalDate expirationDate;

  @ManyToOne
  @JoinColumn(columnDefinition = "product_packing_id")
  private PackingProduct packingProduct;

}
