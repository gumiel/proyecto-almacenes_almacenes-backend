package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.ProductDto;
import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.entities.UnitMeasurement;
import com.gestion.almacenes.mappers.ProductMapper;
import com.gestion.almacenes.repositories.ProductRepository;
import com.gestion.almacenes.repositories.UnitMeasurementRepository;
import com.gestion.almacenes.services.ProductService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements
    ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final UnitMeasurementRepository unitMeasurementRepository;
  private final GenericMapper<Product, ProductDto> genericMapper = new GenericMapper<>(
      Product.class);

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
    return productRepository.save(product);
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
    return productRepository.save(product);
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

}
