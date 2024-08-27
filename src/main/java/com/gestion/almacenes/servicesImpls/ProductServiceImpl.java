package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.enums.PackingCodeEnum;
import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.ProductDto;
import com.gestion.almacenes.entities.CatalogProductStorehouse;
import com.gestion.almacenes.entities.Packing;
import com.gestion.almacenes.entities.PackingProduct;
import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.entities.Storehouse;
import com.gestion.almacenes.entities.UnitMeasurement;
import com.gestion.almacenes.mappers.ProductMapper;
import com.gestion.almacenes.repositories.CatalogProductStorehouseRepository;
import com.gestion.almacenes.repositories.PackingRepository;
import com.gestion.almacenes.repositories.ProductRepository;
import com.gestion.almacenes.repositories.StorehouseRepository;
import com.gestion.almacenes.repositories.UnitMeasurementRepository;
import com.gestion.almacenes.services.ProductService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.*;

@Transactional
@Service
@AllArgsConstructor
public class ProductServiceImpl implements
    ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final UnitMeasurementRepository unitMeasurementRepository;
  private final GenericMapper<Product, ProductDto> genericMapper = new GenericMapper<>(
      Product.class);
  private final StorehouseRepository storehouseRepository;
  private final CatalogProductStorehouseRepository catalogProductStorehouseRepository;
  private final PackingRepository packingRepository;

  @Override
  public List<Product> getAll() {
    return productRepository.findAll();
  }

  @Override
  public Product create(ProductDto productdto) {

    if (productRepository.existsByCodeAndActiveIsTrue(productdto.getCode())) {
      errorDuplicateInFieldCode(ProductDto.class, "code", productdto.getCode());
    }

    Product product = productMapper.fromDto(productdto, null);
    product.setUnitMeasurement(
        this.findUnitMeasurementById(
            productdto.getUnitMeasurementId())
    );

    Product productNew = productRepository.save(product);



    if(productdto.getSelectAllStorehouse()){

      for(Storehouse storehouse: storehouseRepository.findByActiveTrue()){
        this.createCatalogProductStorehouse(productNew, storehouse);
      }

    }else{
      if(productdto.getStorehouseIds()!=null) {
        for (Integer storehouseId : productdto.getStorehouseIds()) {
          Storehouse storehouse = storehouseRepository.findByIdAndActiveIsTrue(storehouseId).orElseThrow(
                  errorEntityNotFound(Storehouse.class, storehouseId)
          );
          this.createCatalogProductStorehouse(productNew, storehouse);
        }
      }else{
        errorProcess("La lista de almacenes esta vacía.");
      }
    }

    Optional<Packing> packingOptional = packingRepository.findByCodeAndActiveTrue(PackingCodeEnum.NA.getCode());
    if(packingOptional.isEmpty())
      errorProcess("El código " + PackingCodeEnum.NA.getCode() + " no existe");

    PackingProduct packingProduct = new PackingProduct();
    packingProduct.setPacking(packingOptional.get());
    packingProduct.set


    return productNew;
  }


  @Override
  public Product update(Integer id, ProductDto productdto) {
    Product productFound = this.findProductById(id);
    if (productRepository.existsByCodeAndIdNotAndActiveIsTrue(productdto.getCode(),
        productFound.getId())) {
      errorDuplicateInFieldCode(ProductDto.class, "code", productdto.getCode());
    }
    Product product = productMapper.fromDto(productdto, productFound);
    product.setUnitMeasurement(
        this.findUnitMeasurementById(
            productdto.getUnitMeasurementId())
    );
    Product productEdited = productRepository.save(product);



    if(productdto.getSelectAllStorehouse()){

      List<Integer> storehouseIdsNotId = catalogProductStorehouseRepository.findStorehouseIdNotInByProductId(productEdited.getId());
      for(Integer storehouseId: storehouseIdsNotId){
        Storehouse storehouse = this.findStorehouseById(storehouseId);
        this.createCatalogProductStorehouse(productEdited, storehouse);
      }

    }else{

      List<Integer> storehouseIds = catalogProductStorehouseRepository.findStorehouseIdByProductId(productEdited.getId());

      // Crear una copia de las listas para no modificar las originales
      List<Integer> toDeleteStorehouse  = new ArrayList<>(storehouseIds);
      List<Integer> toCreateStorehouse  = new ArrayList<>(productdto.getStorehouseIds());

      toDeleteStorehouse.removeAll(productdto.getStorehouseIds());

      for(Integer storehouseId: toDeleteStorehouse){
        Storehouse storehouse = this.findStorehouseById(storehouseId);
        catalogProductStorehouseRepository.deleteByStorehouseAndProduct(storehouse, productEdited);
      }

      toCreateStorehouse.removeAll(storehouseIds);

      for(Integer storehouseId: toCreateStorehouse){
        Storehouse storehouse = this.findStorehouseById(storehouseId);
        this.createCatalogProductStorehouse(productEdited, storehouse);
      }

    }

    return productEdited;

  }



  @Override
  public Product getById(Integer id) {
    return this.findProductById(id);
  }

  @Override
  public Product getByCode(String code) {
    return productRepository.findByCodeAndActiveTrue(code).orElseThrow(
        errorEntityNotFound(Product.class, "code", code)
    );
  }

  @Override
  public void delete(Integer id) {
    Product product = this.findProductById(id);
    productRepository.delete(product);
  }

  @Override
  public List<Product> search(String code, String name) {
    return productRepository.findAll();
  }

  @Override
  public PagePojo<Product> pageable(Integer pageNumber, Integer pageSize, String sortField,
      String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<Product> productPage = productRepository.findAll(pageable);

    return genericMapper.fromEntity(productPage);
  }

  private Product findProductById(Integer id) {
    return productRepository.findById(id).orElseThrow(
        errorEntityNotFound(Product.class, id)
    );
  }

  private UnitMeasurement findUnitMeasurementById(Integer unitMeasurementId) {
    return unitMeasurementRepository.findByIdAndActiveIsTrue(unitMeasurementId).orElseThrow(
        errorEntityNotFound(UnitMeasurement.class, unitMeasurementId)
    );
  }

  private void createCatalogProductStorehouse(Product productEdited, Storehouse storehouse) {
    CatalogProductStorehouse catalogProductStorehouse = new CatalogProductStorehouse();
    catalogProductStorehouse.setProduct(productEdited);
    catalogProductStorehouse.setStorehouse(storehouse);
    catalogProductStorehouseRepository.save(catalogProductStorehouse);
  }

  private Storehouse findStorehouseById(Integer storehouseId) {
    return storehouseRepository.findById(storehouseId).orElseThrow(
            errorEntityNotFound(Storehouse.class, storehouseId)
    );
  }

}
