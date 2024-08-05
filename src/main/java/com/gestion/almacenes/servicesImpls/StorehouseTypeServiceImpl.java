package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.exception.AlreadyDeletedException;
import com.gestion.almacenes.commons.exception.DuplicateException;
import com.gestion.almacenes.commons.exception.EntityNotFound;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StorehouseTypeDto;
import com.gestion.almacenes.entities.StorehouseType;
import com.gestion.almacenes.mappers.StorehouseTypeMapper;
import com.gestion.almacenes.repositories.StorehouseTypeRepository;
import com.gestion.almacenes.services.StorehouseTypeService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorProcess;

@Service
@AllArgsConstructor
public class StorehouseTypeServiceImpl implements
    StorehouseTypeService {

  private final StorehouseTypeRepository storehouseTypeRepository;
  private final StorehouseTypeMapper storehouseTypeMapper;

  @Override
  public List<StorehouseType> getAll() {
    return storehouseTypeRepository.findAllByActiveIsTrue();
  }

  @Override
  public StorehouseType create(StorehouseTypeDto storehouseTypedto) {

    if (storehouseTypeRepository.existsByCodeAndActiveIsTrue(storehouseTypedto.getCode())) {
      throw new DuplicateException("StorehouseType", "code", storehouseTypedto.getCode());
    }

    StorehouseType storehouseType = storehouseTypeMapper.fromDto(storehouseTypedto, null);
    return storehouseTypeRepository.save(storehouseType);
  }

  @Override
  public StorehouseType update(Integer id, StorehouseTypeDto storehouseTypedto) {
    StorehouseType storehouseTypeFound = this.findStorehouseTypeById(id);
    if (storehouseTypeRepository.existsByCodeAndIdNotAndActiveIsTrue(storehouseTypedto.getCode(),
        storehouseTypeFound.getId())) {
      throw new DuplicateException("StorehouseType", "code", storehouseTypedto.getCode());
    }
    StorehouseType storehouseType = storehouseTypeMapper.fromDto(storehouseTypedto,
        storehouseTypeFound);
    //storehouseType.setId(id);
    return storehouseTypeRepository.save(storehouseType);
  }

  @Override
  public StorehouseType getById(Integer id) {
    return this.findStorehouseTypeById(id);
  }

  @Override
  public StorehouseType getByCode(String code) {
    return storehouseTypeRepository.findByCodeAndActiveTrue(code).orElseThrow(
      errorEntityNotFound(StorehouseType.class, "code", code)
    );
  }

  @Override
  public void delete(Integer id) {
    StorehouseType storehouseType = this.findStorehouseTypeById(id);
    if (storehouseType.getActive()) {
      storehouseType.setActive(false);
      storehouseTypeRepository.save(storehouseType);
    } else {
      throw new AlreadyDeletedException("StorehouseType", storehouseType.getId());
    }
  }

  @Override
  public List<StorehouseType> getFiltered(String code, String name) {
    return storehouseTypeRepository.findAll();
  }

  @Override
  public PagePojo<StorehouseType> getByPageAndFilters(Integer pageNumber, Integer pageSize,
      String sortField, String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<StorehouseType> storehouseTypePage = storehouseTypeRepository.findAll(pageable);

    return storehouseTypeMapper.fromEntity(storehouseTypePage);
  }

  private StorehouseType findStorehouseTypeById(Integer id) {
    return storehouseTypeRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        () -> new EntityNotFound("StorehouseType", id)
    );
  }

}
