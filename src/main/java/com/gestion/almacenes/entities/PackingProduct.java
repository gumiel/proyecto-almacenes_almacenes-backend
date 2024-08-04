package com.gestion.almacenes.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name= "packing_product" )
@Schema( name = "Entity PackingProduct (Producto empacado)")
public class PackingProduct extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String code;
  @ManyToOne
  @JoinColumn(name="stock_id")
  private Stock stock;
  @ManyToOne
  @JoinColumn(name="packing_id")
  private Packing packing;
  private Double amount;
  private LocalDate expirationDate;

}
