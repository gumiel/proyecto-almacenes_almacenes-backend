package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.CatalogProductStorehouseDto;
import com.gestion.almacenes.entities.CatalogProductStorehouse;
import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.entities.Storehouse;
import com.gestion.almacenes.repositories.CatalogProductStorehouseRepository;
import com.gestion.almacenes.repositories.ProductRepository;
import com.gestion.almacenes.repositories.StorehouseProductRepository;
import com.gestion.almacenes.repositories.StorehouseRepository;
import com.gestion.almacenes.services.CatalogProductStorehouseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorDuplicate;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;

@Service
@AllArgsConstructor
public class CatalogProductStorehouseServiceImpl implements
        CatalogProductStorehouseService {

    private final CatalogProductStorehouseRepository catalogProductStorehouseRepository;
    private final GenericMapper<CatalogProductStorehouse, CatalogProductStorehouseDto> catalogProductStorehouseMMapper = new GenericMapper<>(
            CatalogProductStorehouse.class);
    private final StorehouseRepository storehouseRepository;
    private final ProductRepository productRepository;
    private final StorehouseProductRepository storehouseProductRepository;

    @Override
    public List<CatalogProductStorehouse> getAll() {
        return catalogProductStorehouseRepository.findAllByActiveIsTrue();
    }

    @Override
    public CatalogProductStorehouse create(CatalogProductStorehouseDto dto) {

        if (catalogProductStorehouseRepository.existsByStorehouseId_IdAndProductId_Id(dto.getStorehouseId(),
                dto.getProductId())) {
            errorDuplicate(CatalogProductStorehouse.class, "Almacen",
                    dto.getStorehouseId().toString());
        }

        CatalogProductStorehouse catalogProductStorehouse = new CatalogProductStorehouse();
        catalogProductStorehouse.setStorehouse(this.findStoreHouseById(dto.getStorehouseId()));
        catalogProductStorehouse.setProduct(this.findProductById(dto.getProductId()));
        catalogProductStorehouseRepository.save(catalogProductStorehouse);
        return catalogProductStorehouse;

    }

    @Override
    public CatalogProductStorehouse update(Integer id, CatalogProductStorehouseDto catalogProductStorehousedto) {

        CatalogProductStorehouse catalogProductStorehouseFound = this.findCatalogProductStorehouseById(id);

        catalogProductStorehouseFound = catalogProductStorehouseMMapper.fromDto(catalogProductStorehousedto);

        return catalogProductStorehouseRepository.save(catalogProductStorehouseFound);
    }

    @Override
    public CatalogProductStorehouse getById(Integer id) {
        return this.findCatalogProductStorehouseById(id);
    }

    @Override
    public void delete(Integer id) {
        CatalogProductStorehouse catalogProductStorehouseFound = this.findCatalogProductStorehouseById(id);
        catalogProductStorehouseRepository.delete(catalogProductStorehouseFound);
    }

    @Override
    public void deleteByProductAndStorehouse(CatalogProductStorehouseDto dto) {
        CatalogProductStorehouse catalogProductStorehouse = catalogProductStorehouseRepository.findByStorehouseId_IdAndProductId_Id(
                dto.getStorehouseId(), dto.getProductId());
        catalogProductStorehouseRepository.delete(catalogProductStorehouse);
    }

    @Override
    public List<CatalogProductStorehouse> getFiltered() {

        return catalogProductStorehouseRepository.findAll();
    }

    @Override
    public PagePojo<CatalogProductStorehouse> getByPageAndFilters(Integer pageNumber, Integer pageSize, String sortField, String sortOrder) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<CatalogProductStorehouse> catalogProductStorehousePage = catalogProductStorehouseRepository.findAll(pageable);

        return catalogProductStorehouseMMapper.fromEntity(catalogProductStorehousePage);
    }

    private CatalogProductStorehouse findCatalogProductStorehouseById(Integer id) {
        return catalogProductStorehouseRepository.findByIdAndActiveIsTrue(id).orElseThrow(
                errorEntityNotFound(CatalogProductStorehouse.class, id)
        );
    }

    private Storehouse findStoreHouseById(Integer id) {
        return storehouseRepository.findByIdAndActiveIsTrue(id).orElseThrow(
                errorEntityNotFound(Storehouse.class, id)
        );
    }

    private Product findProductById(Integer id) {
        return productRepository.findByIdAndActiveIsTrue(id).orElseThrow(
                errorEntityNotFound(Product.class, id)
        );
    }

    @Override
    public void addAllProductsToStorehouse(Integer storehouseId) {
        Storehouse storehouse = this.findStoreHouseById(storehouseId);
        catalogProductStorehouseRepository.addAllProductsToStorehouse(storehouse.getId());
    }
}
