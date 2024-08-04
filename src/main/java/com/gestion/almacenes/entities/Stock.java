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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stock")
@Schema( name = "Entity Stock (Stock de producto)")
@Builder
@AllArgsConstructor
public class Stock extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne
  @JoinColumn(name = "storehouse_id")
  private Storehouse storehouse;
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
  @Column(nullable = false, columnDefinition = "NUMERIC(18, 4)")
  private Double amountInStock;
  @Column(nullable = false, columnDefinition = "NUMERIC(18, 4)")
  private Double minimumStock;
  @Column(nullable = false, columnDefinition = "NUMERIC(18, 4)")
  private Double maximumStock;
  @Column(nullable = false)
  private Boolean stockAlert;
  private Boolean validExpirationDate;

}
