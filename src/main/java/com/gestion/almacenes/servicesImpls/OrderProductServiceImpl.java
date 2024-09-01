package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.enums.OrderProductTypeActionEnum;
import com.gestion.almacenes.commons.enums.StatusFlowEnum;
import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductDto;
import com.gestion.almacenes.entities.*;
import com.gestion.almacenes.repositories.*;
import com.gestion.almacenes.services.OrderProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.*;

@Service
@AllArgsConstructor
public class OrderProductServiceImpl implements
    OrderProductService {

  private final OrderProductRepository orderProductRepository;
  private final StorehouseRepository storeHouseRepository;
  private final OrderProductTypeRepository orderProductTypeRepository;
  private final GenericMapper<OrderProduct, OrderProductDto> genericMapper = new GenericMapper<>(
      OrderProduct.class);
  private final OrderProductDetailRepository orderProductDetailRepository;
  private final StockRepository stockRepository;
  private final SupplierRepository supplierRepository;

  @Override
  public List<OrderProduct> getAll() {
    return orderProductRepository.findAllByActiveIsTrue();
  }

  @Override
  public OrderProduct create(OrderProductDto orderProductdto) {

    if (orderProductdto.getCode() != null
        && orderProductRepository.existsByCodeAndActiveIsTrue(
        orderProductdto.getCode())) {
      errorProcess(
          "Ya existe el código (" + orderProductdto.getCode() + ")");
    }

    Storehouse storehouse = this.findStorehouseById(orderProductdto.getStorehouseId());
    OrderProductType orderProductType = this.findOrderProductTypeById(
        orderProductdto.getOrderProductTypeId());

    OrderProduct orderProduct = OrderProduct.builder()
        .code(
            (orderProductdto.getCode() == null) ? "S/C" : orderProductdto.getCode()
        )
        .description(orderProductdto.getDescription())
        .registrationDate(
            (orderProductdto.getRegistrationDate() == null) ? LocalDate.now()
                : orderProductdto.getRegistrationDate()
        )
        .registrationTime(
            (orderProductdto.getRegistrationTime() == null) ? LocalTime.now()
                : orderProductdto.getRegistrationTime()
        )
        .storehouse(storehouse)
        .orderProductType(orderProductType)
        .status(StatusFlowEnum.BORRADOR.name()).supplier(
                    (orderProductdto.getSupplierId()!=null)? this.findSupplierById(orderProductdto.getSupplierId()): null
            )
        .build();

    return orderProductRepository.save(orderProduct);
  }

  private Supplier findSupplierById(Integer supplierId) {
    return supplierRepository.findByIdAndActiveIsTrue(supplierId).orElseThrow(
            errorEntityNotFound(Supplier.class, supplierId)
    );
  }

  @Override
  public OrderProduct update(Integer id, OrderProductDto orderProductdto) {
    OrderProduct orderProductFound = this.findOrderProductById(id);
    if (orderProductRepository.existsByCodeAndIdNotAndActiveIsTrue(
        orderProductdto.getCode(), orderProductFound.getId())) {
      errorDuplicate(OrderProduct.class, "code", orderProductdto.getCode());
    }
    Storehouse storehouse = this.findStorehouseById(orderProductdto.getStorehouseId());
    OrderProductType orderProductType = this.findOrderProductTypeById(
        orderProductdto.getOrderProductTypeId());

    orderProductFound = genericMapper.fromDto(orderProductdto);
    orderProductFound.setStorehouse(storehouse);
    orderProductFound.setOrderProductType(orderProductType);

    return orderProductRepository.save(orderProductFound);
  }

  @Override
  public OrderProduct getById(Integer id) {
    return this.findOrderProductById(id);
  }

  @Override
  public OrderProduct getByCode(String code) {
    return orderProductRepository.findByCodeAndActiveTrue(code).orElseThrow(
        errorEntityNotFound(OrderProduct.class, "code", code)
    );
  }

  @Override
  public void delete(Integer id) {
    OrderProduct orderProduct = this.findOrderProductById(id);
    if (orderProduct.getActive()) {
      orderProduct.setActive(false);
      orderProductRepository.save(orderProduct);
    } else {
//      throw new AlreadyDeletedException(OrderProduct.class.getSimpleName(), orderProduct.getId());
      errorAlreadyDeleted(OrderProduct.class, orderProduct.getId());
    }
  }

  @Override
  public List<OrderProduct> getFiltered(String code, String name) {
    return orderProductRepository.findAll();
  }

  @Override
  public PagePojo<OrderProduct> getByPageAndFilters(Integer pageNumber, Integer pageSize,
      String sortField, String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<OrderProduct> orderProductPage = orderProductRepository.findAll(pageable);

    return genericMapper.fromEntity(orderProductPage);
  }

  @Override
  public OrderProduct executeOrderProduct(OrderProductDto dto) {

    OrderProduct orderProduct = this.findOrderProductById(dto.getOrderProductId());
    String action = orderProduct.getOrderProductType().getAction();

    List<OrderProductDetail> orderProductDetails = orderProductDetailRepository.findByOrderProduct_IdAndActiveTrue(
        dto.getOrderProductId());

    //Validacion General
    //this.validationExecuteOrderProduct(orderProductDetails, orderProduct);

    for (OrderProductDetail orderProductDetail : orderProductDetails) {
      Double amountDetail = orderProductDetail.getAmount();
      Stock stock = orderProductDetail.getStock();
      double newAmountStock = Double.parseDouble("0");

      if (Objects.equals(action, OrderProductTypeActionEnum.RECEIPT.name())) {
        newAmountStock = stock.getAmountInStock() + amountDetail;
      } else {
        if (stock.getAmountInStock() < amountDetail) {
          DecimalFormat mf = new DecimalFormat("0.00");
          mf.setMinimumFractionDigits(2);
          String stingAmountDetail = mf.format(amountDetail);
          errorProcess(
              String.format(
                  "El almacen (%s) no tiene la cantidad de (Cant. %s) Items (%s) necesarios. Solo se tienen (%s)",
                  orderProduct.getStorehouse().getName(),
                  stingAmountDetail,
                  orderProductDetail.getStock().getProduct().getName(),
                  orderProductDetail.getStock().getAmountInStock().toString()
              )
          );
        }
        newAmountStock = stock.getAmountInStock() - amountDetail;
      }

      stock.setAmountInStock(newAmountStock);
      stockRepository.save(stock);

    }

    orderProduct.setStatus(StatusFlowEnum.FINALIZADO.name());
    return orderProductRepository.save(orderProduct);

  }

  private OrderProduct findOrderProductById(Integer id) {
    return orderProductRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(OrderProduct.class, id)
    );
  }


  private Storehouse findStorehouseById(Integer storehouseId) {
    return storeHouseRepository.findByIdAndActiveIsTrue(storehouseId).orElseThrow(
        errorEntityNotFound(Storehouse.class, storehouseId)
    );
  }

  private OrderProductType findOrderProductTypeById(Integer orderProductTypeId) {
    return orderProductTypeRepository.findByIdAndActiveIsTrue(orderProductTypeId).orElseThrow(
        errorEntityNotFound(OrderProductType.class, orderProductTypeId)
    );
  }

  private void validationExecuteOrderProduct(List<OrderProductDetail> orderProductDetails,
      OrderProduct orderProduct) {
    if (orderProductDetails.isEmpty()) {
      errorProcess("No tiene items para realizar.");
    }

    if (Objects.equals(orderProduct.getStatus(), StatusFlowEnum.FINALIZADO.name())) {
      errorProcess("La orden ya fue procesada.");
    }
  }

}