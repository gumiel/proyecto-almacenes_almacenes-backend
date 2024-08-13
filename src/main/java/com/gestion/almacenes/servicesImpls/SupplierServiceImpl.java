package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.SupplierDto;
import com.gestion.almacenes.entities.Supplier;
import com.gestion.almacenes.repositories.SupplierRepository;
import com.gestion.almacenes.services.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorDuplicateInFieldCode;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements
        SupplierService {

    private final SupplierRepository supplierRepository;
    private final GenericMapper<Supplier, SupplierDto> supplierMMapper = new GenericMapper<>(
            Supplier.class);

    @Override
    public List<Supplier> getAll() {
        return supplierRepository.findAllByActiveIsTrue();
    }

    @Override
    public Supplier create(SupplierDto supplierdto) {

        if (supplierRepository.existsBySupplierCodeAndActiveIsTrue(supplierdto.getSupplierCode()))
            errorDuplicateInFieldCode(Supplier.class, "supplierCode", supplierdto.getSupplierCode());

        Supplier supplier = supplierMMapper.fromDto(supplierdto);
        supplier.setRegisterDate(LocalDateTime.now());

        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(Integer id, SupplierDto supplierdto) {

        Supplier supplierFound = this.findSupplierById(id);

        if (supplierRepository.existsBySupplierCodeAndIdNotAndActiveIsTrue(supplierdto.getSupplierCode(), supplierFound.getId()))
            errorDuplicateInFieldCode(Supplier.class, "supplierCode", supplierdto.getSupplierCode());

        Supplier supplier = supplierMMapper.fromDto(supplierdto);

        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier getById(Integer id) {
        return this.findSupplierById(id);
    }

    @Override
    public Supplier getBySupplierCode(String supplierCode) {
      return supplierRepository.findBySupplierCodeAndActiveTrue(supplierCode).orElseThrow(
        errorEntityNotFound(Supplier.class, "supplierCode", supplierCode)
      );
    }

    @Override
    public void delete(Integer id) {
        Supplier supplier = this.findSupplierById(id);
        supplierRepository.delete(supplier);
    }

    @Override
    public List<Supplier> getFiltered(String code, String name) {

        return supplierRepository.findAll();
    }

    @Override
    public PagePojo<Supplier> getByPageAndFilters(Integer pageNumber, Integer pageSize, String sortField, String sortOrder, String code, String name) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Supplier> supplierPage = supplierRepository.findAll(pageable);

        return supplierMMapper.fromEntity(supplierPage);
    }

    private Supplier findSupplierById(Integer id){
        return supplierRepository.findByIdAndActiveIsTrue(id).orElseThrow(
                errorEntityNotFound(Supplier.class, id)
        );
    }

}