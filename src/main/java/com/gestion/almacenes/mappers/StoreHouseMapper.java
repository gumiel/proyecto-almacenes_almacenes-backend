package com.gestion.almacenes.mappers;

import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StorehouseDto;
import com.gestion.almacenes.entities.Storehouse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class StoreHouseMapper {

  public Storehouse fromDto(StorehouseDto dto, Storehouse storeHouseFound) {
    Storehouse storeHouse = new Storehouse();
    if (storeHouseFound != null) {
      storeHouse = storeHouseFound;
    }
    storeHouse.setCode(dto.getCode());
    storeHouse.setName(dto.getName());
    storeHouse.setDescription(dto.getDescription());

    return storeHouse;
  }

  public PagePojo<Storehouse> fromEntity(Page<Storehouse> page) {
    PagePojo<Storehouse> dto = new PagePojo<>();
    dto.setContent(page.getContent());
    dto.setLast(page.isLast());
    dto.setPageNumber(page.getNumber());
    dto.setPageSize(page.getSize());
    dto.setTotalPages(page.getTotalPages());
    dto.setTotalElements(page.getTotalElements());
    return dto;
  }
}
