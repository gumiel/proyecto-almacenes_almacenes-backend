package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.exception.AlreadyDeletedException;
import com.gestion.almacenes.commons.exception.DuplicateException;
import com.gestion.almacenes.commons.exception.EntityNotFound;
import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductTypeDto;
import com.gestion.almacenes.entities.OrderProductType;
import com.gestion.almacenes.repositories.OrderProductTypeRepository;
import com.gestion.almacenes.services.OrderProductTypeService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorProcess;

@Service
@AllArgsConstructor
public class OrderProductTypeServiceImpl implements
    OrderProductTypeService {

  private final OrderProductTypeRepository orderProductTypeRepository;
  private final ModelMapper modelMapper = new ModelMapper();
  private final GenericMapper<OrderProductType, OrderProductTypeDto> genericMapper = new GenericMapper<>(
      OrderProductType.class);


  @Override
  public List<OrderProductType> getAll() {
    return orderProductTypeRepository.findAllByActiveIsTrue();
  }

  @Override
  public OrderProductType create(OrderProductTypeDto orderProductTypedto) {

    if (orderProductTypeRepository.existsByCodeAndActiveIsTrue(orderProductTypedto.getCode())) {
      throw new DuplicateException(OrderProductType.class.getSimpleName(), "code", "");
    }
    OrderProductType orderProductType = new OrderProductType();
    modelMapper.map(orderProductTypedto, orderProductType);
    return orderProductTypeRepository.save(orderProductType);
  }

  @Override
  public OrderProductType update(Integer id, OrderProductTypeDto orderProductTypedto) {
    OrderProductType orderProductTypeFound = this.findOrderTypeById(id);
    if (orderProductTypeRepository.existsByCodeAndIdNotAndActiveIsTrue(
        orderProductTypedto.getCode(), orderProductTypeFound.getId())) {
      throw new DuplicateException("OrderType", "code", "");
    }
    modelMapper.map(orderProductTypedto, orderProductTypeFound);
    return orderProductTypeRepository.save(orderProductTypeFound);
  }

  @Override
  public OrderProductType getById(Integer id) {
    return this.findOrderTypeById(id);
  }

  @Override
  public OrderProductType getByCode(String code) {
    return orderProductTypeRepository.findByCodeAndActiveTrue(code).orElseThrow(
      errorEntityNotFound(OrderProductType.class, "code", code)
    );
  }

  @Override
  public void delete(Integer id) {
    OrderProductType orderProductType = this.findOrderTypeById(id);
    if (orderProductType.getActive()) {
      orderProductType.setActive(false);
      orderProductTypeRepository.save(orderProductType);
    } else {
      throw new AlreadyDeletedException("OrderType", orderProductType.getId());
    }
  }

  @Override
  public List<OrderProductType> getFiltered(String code, String name) {
    return orderProductTypeRepository.findAll();
  }

  @Override
  public PagePojo<OrderProductType> getByPageAndFilters(Integer pageNumber, Integer pageSize,
      String sortField, String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<OrderProductType> orderTypePage = orderProductTypeRepository.findAll(pageable);

    return genericMapper.fromEntity(orderTypePage);
  }

  private OrderProductType findOrderTypeById(Integer id) {
    return orderProductTypeRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        () -> new EntityNotFound(OrderProductType.class.getSimpleName(), id)
    );
  }

}
