package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.PackingDto;
import com.gestion.almacenes.entities.Packing;
import com.gestion.almacenes.repositories.PackingRepository;
import com.gestion.almacenes.services.PackingService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.*;


@Service
@AllArgsConstructor
public class PackingServiceImpl implements
    PackingService {

  private final PackingRepository packingRepository;
  private final ModelMapper modelMapper = new ModelMapper();
  private final GenericMapper<Packing, PackingDto> genericMapper = new GenericMapper<>(
      Packing.class);

  @Override
  public List<Packing> getAll() {
    return packingRepository.findAllByActiveIsTrue();
  }

  @Override
  public Packing create(PackingDto packingdto) {

    if (packingRepository.existsByCodeAndActiveIsTrue(packingdto.getCode())) {
      errorDuplicateInFieldCode(PackingDto.class, "code", packingdto.getCode());
    }
    Packing packing = new Packing();
    modelMapper.map(packingdto, packing);
    return packingRepository.save(packing);
  }

  @Override
  public Packing update(Integer id, PackingDto packingdto) {
    Packing packingFound = this.findPackingById(id);
    if (packingRepository.existsByCodeAndIdNotAndActiveIsTrue(packingdto.getCode(),
        packingFound.getId())) {
      errorDuplicateInFieldCode(PackingDto.class, "code", packingdto.getCode());
    }
    modelMapper.map(packingdto, packingFound);
    return packingRepository.save(packingFound);
  }

  @Override
  public Packing getById(Integer id) {
    return this.findPackingById(id);
  }

  @Override
  public Packing getByCode(String code) {
    return packingRepository.findByCodeAndActiveTrue(code).orElseThrow(
        errorEntityNotFound(Packing.class, "code", code)
    );
  }

  @Override
  public void delete(Integer id) {
    Packing packing = this.findPackingById(id);
    if (packing.getActive()) {
      packing.setActive(false);
      packingRepository.save(packing);
    } else {
      errorAlreadyDeleted(Packing.class, packing.getId());
    }
  }

  @Override
  public List<Packing> getFiltered(String code, String name) {
    return packingRepository.findAll();
  }

  @Override
  public PagePojo<Packing> getByPageAndFilters(Integer pageNumber, Integer pageSize,
      String sortField, String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<Packing> packingPage = packingRepository.findAll(pageable);

    return genericMapper.fromEntity(packingPage);
  }

  private Packing findPackingById(Integer id) {

    return packingRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(Packing.class, id)
    );
  }

}
