package com.gestion.almacenes.servicesImpls;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorAlreadyDeleted;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorDuplicate;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;

import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StoreHouseDto;
import com.gestion.almacenes.dtos.StorehouseProductDto;
import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.entities.Storehouse;
import com.gestion.almacenes.entities.StorehouseProduct;
import com.gestion.almacenes.entities.StorehouseType;
import com.gestion.almacenes.repositories.ProductRepository;
import com.gestion.almacenes.repositories.StorehouseProductRepository;
import com.gestion.almacenes.repositories.StorehouseRepository;
import com.gestion.almacenes.repositories.StorehouseTypeRepository;
import com.gestion.almacenes.services.StorehouseService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StorehouseServiceImpl implements
    StorehouseService {


  private final StorehouseRepository storehouseRepository;
  private final ModelMapper modelMapper = new ModelMapper();
  private final GenericMapper<Storehouse, StoreHouseDto> genericMapper = new GenericMapper<>(
      Storehouse.class);
  private final StorehouseProductRepository storehouseProductRepository;
  private final ProductRepository productRepository;
  private final StorehouseTypeRepository storehouseTypeRepository;

  @Override
  public List<Storehouse> getAll() {
    return storehouseRepository.findAll();
  }

  @Override
  public Storehouse create(StoreHouseDto storeHousedto) {

    if (storehouseRepository.existsByCodeAndActiveIsTrue(storeHousedto.getCode())) {
      errorDuplicate(Storehouse.class, "code", storeHousedto.getCode());

    }

    StorehouseType storehouseType = this.findStorehouseTypeById(
        storeHousedto.getStorehouseTypeId());

    Storehouse storehouse = genericMapper.fromDto(storeHousedto);
    storehouse.setStorehouseType(storehouseType);

    return storehouseRepository.save(storehouse);
  }

  private StorehouseType findStorehouseTypeById(Integer storehouseTypeId) {
    return storehouseTypeRepository.findByIdAndActiveIsTrue(storehouseTypeId).orElseThrow(
        errorEntityNotFound(StorehouseType.class, storehouseTypeId)
    );
  }

  @Override
  public Storehouse update(Integer id, StoreHouseDto storeHousedto) {
    Storehouse storehouseFound = this.findStoreHouseById(id);
    if (storehouseRepository.existsByCodeAndIdNotAndActiveIsTrue(storeHousedto.getCode(),
        storehouseFound.getId())) {
      errorDuplicate(Storehouse.class, "code", storeHousedto.getCode());
    }

    modelMapper.map(storeHousedto, storehouseFound);

    return storehouseRepository.save(storehouseFound);
  }

  @Override
  public Storehouse getById(Integer id) {
    return this.findStoreHouseById(id);
  }

  @Override
  public Storehouse getByCode(String code) {
    return storehouseRepository.findByCodeAndActiveTrue(code).orElseThrow(
        errorEntityNotFound(Storehouse.class, "code", code)
    );
  }

  @Override
  public void delete(Integer id) {
    Storehouse storeHouse = this.findStoreHouseById(id);
    if (storeHouse.getActive()) {
      storeHouse.setActive(false);
      storehouseRepository.save(storeHouse);
    } else {
      errorAlreadyDeleted(Storehouse.class, storeHouse.getId());
    }
  }

  @Override
  public List<Storehouse> search(String code, String name) {
    return storehouseRepository.findAll();
  }

  @Override
  public PagePojo<Storehouse> pageable(Integer pageNumber, Integer pageSize, String sortField,
      String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<Storehouse> storeHousePage = storehouseRepository.findAll(pageable);

    return genericMapper.fromEntity(storeHousePage);

  }

//    private final PagePojo<Storehouse> search(Page<Storehouse> storeHousePage){
//
//        TypeMap<Page, PagePojo> propertyMapper = modelMapper.createTypeMap(Page.class, PagePojo.class);
//        propertyMapper.addMapping(Page::getContent, PagePojo::setContent);
//        PagePojo storehousePagePojo = modelMapper.map(storeHousePage, PagePojo.class);
//        return storehousePagePojo;
//    }

  private Storehouse findStoreHouseById(Integer id) {
    return storehouseRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(Storehouse.class, id)
    );
  }

  @Override
  public Storehouse addProductToStorehouse(StorehouseProductDto dto) {

    if (storehouseProductRepository.existsByStorehouseId_IdAndProductId_Id(dto.getStorehouseId(),
        dto.getProductId())) {
      errorDuplicate(StorehouseProduct.class, "Almacen",
          dto.getStorehouseId().toString());
    }

    StorehouseProduct storehouseProduct = new StorehouseProduct();
    storehouseProduct.setStorehouseId(this.findStoreHouseById(dto.getStorehouseId()));
    storehouseProduct.setProduct(this.findProductById(dto.getProductId()));
    storehouseProductRepository.save(storehouseProduct);
    return null;
  }

  @Override
  public void removeProductToStorehouse(StorehouseProductDto dto) {
    StorehouseProduct storehouseProduct = storehouseProductRepository.findByStorehouseId_IdAndProductId_Id(
        dto.getStorehouseId(), dto.getProductId());
    storehouseProductRepository.delete(storehouseProduct);
  }

  private Product findProductById(Integer id) {
    return productRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(Product.class, id)
    );
  }

}
