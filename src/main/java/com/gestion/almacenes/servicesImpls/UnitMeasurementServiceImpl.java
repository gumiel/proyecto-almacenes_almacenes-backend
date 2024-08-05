package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.exception.AlreadyDeletedException;
import com.gestion.almacenes.commons.exception.DuplicateException;
import com.gestion.almacenes.commons.exception.EntityNotFound;
import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.UnitMeasurementDto;
import com.gestion.almacenes.entities.UnitMeasurement;
import com.gestion.almacenes.repositories.UnitMeasurementRepository;
import com.gestion.almacenes.services.UnitMeasurementService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;

@Service
@AllArgsConstructor
public class UnitMeasurementServiceImpl implements
    UnitMeasurementService {

  private final UnitMeasurementRepository unitMeasurementRepository;
  private final ModelMapper modelMapper = new ModelMapper();

  private final GenericMapper<UnitMeasurement, UnitMeasurementDto> genericMapper = new GenericMapper<>(
      UnitMeasurement.class);


  @Override
  public List<UnitMeasurement> getAll() {
    return unitMeasurementRepository.findAllByActiveIsTrue();
  }

  @Override
  public UnitMeasurement create(UnitMeasurementDto unitMeasurementdto) {

    if (unitMeasurementRepository.existsByCodeAndActiveIsTrue(
        unitMeasurementdto.getCode())
    ) {
      throw new DuplicateException("UnitMeasurement", "code", "a");
    }

    UnitMeasurement unitMeasurement = new UnitMeasurement();
    modelMapper.map(unitMeasurementdto, unitMeasurement);

    return unitMeasurementRepository.save(unitMeasurement);

  }

  @Override
  public UnitMeasurement update(Integer id, UnitMeasurementDto unitMeasurementdto) {
    UnitMeasurement unitMeasurementFound = this.findUnitMeasurementById(id);
    if (unitMeasurementRepository.existsByCodeAndIdNotAndActiveIsTrue(
        unitMeasurementdto.getCode(), unitMeasurementFound.getId())) {
      throw new DuplicateException("UnitMeasurement", "code", "1");
    }
    modelMapper.map(unitMeasurementdto, unitMeasurementFound);

    return unitMeasurementRepository.save(unitMeasurementFound);
  }

  @Override
  public UnitMeasurement getById(Integer id) {
    return this.findUnitMeasurementById(id);
  }

  @Override
  public UnitMeasurement getByCode(String code) {
    return unitMeasurementRepository.findByCodeAndActiveTrue(code).orElseThrow(
      errorEntityNotFound(UnitMeasurement.class, "code", code)
    );
  }

  @Override
  public void delete(Integer id) {
    UnitMeasurement unitMeasurement = this.findUnitMeasurementById(id);
    if (unitMeasurement.getActive()) {
      unitMeasurement.setActive(false);
      unitMeasurementRepository.save(unitMeasurement);
    } else {
      throw new AlreadyDeletedException("UnitMeasurement", unitMeasurement.getId());
    }
  }

  @Override
  public List<UnitMeasurement> getFiltered(String code, String name) {

    return unitMeasurementRepository.findAll();
  }

  @Override
  public PagePojo<UnitMeasurement> getByPageAndFilters(Integer pageNumber, Integer pageSize,
      String sortField, String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<UnitMeasurement> unitMeasurementPage = unitMeasurementRepository.findAll(pageable);

    return genericMapper.fromEntity(unitMeasurementPage);
  }

  private UnitMeasurement findUnitMeasurementById(Integer id) {
    return unitMeasurementRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        () -> new EntityNotFound("UnitMeasurement", id)
    );
  }

}
