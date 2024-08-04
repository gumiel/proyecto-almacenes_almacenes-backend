package com.gestion.almacenes.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Schema( name = "Entity Auditable (Auditable)")
public class Auditable {

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdDate;
  @Column(updatable = false)
  @CreatedBy
  private String createdBy;
  @LastModifiedDate
  private LocalDateTime lastModifiedDate;
  @LastModifiedBy
  private String lastModifiedBy;
  private Boolean active = true;

}