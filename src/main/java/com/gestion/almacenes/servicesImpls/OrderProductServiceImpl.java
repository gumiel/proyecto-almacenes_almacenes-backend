package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.enums.OrderProductTypeActionEnum;
import com.gestion.almacenes.commons.enums.StatusFlowEnum;
import com.gestion.almacenes.commons.exception.AlreadyDeletedException;
import com.gestion.almacenes.commons.exception.DuplicateException;
import com.gestion.almacenes.commons.exception.EntityNotFound;
import com.gestion.almacenes.commons.exception.ValidationErrorException;
import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductDto;
import com.gestion.almacenes.entities.OrderDetailPacking;
import com.gestion.almacenes.entities.OrderProduct;
import com.gestion.almacenes.entities.OrderProductDetail;
import com.gestion.almacenes.entities.OrderProductType;
import com.gestion.almacenes.entities.PackingProduct;
import com.gestion.almacenes.entities.Stock;
import com.gestion.almacenes.entities.Storehouse;
import com.gestion.almacenes.repositories.OrderDetailPackingRepository;
import com.gestion.almacenes.repositories.OrderProductDetailRepository;
import com.gestion.almacenes.repositories.OrderProductRepository;
import com.gestion.almacenes.repositories.OrderProductTypeRepository;
import com.gestion.almacenes.repositories.PackingProductRepository;
import com.gestion.almacenes.repositories.StockRepository;
import com.gestion.almacenes.repositories.StoreHouseRepository;
import com.gestion.almacenes.services.OrderProductService;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.*;

@Service
@AllArgsConstructor
public class OrderProductServiceImpl implements
    OrderProductService {

  private final OrderProductRepository orderProductRepository;
  private final StoreHouseRepository storeHouseRepository;
  private final OrderProductTypeRepository orderProductTypeRepository;
  private final GenericMapper<OrderProduct, OrderProductDto> genericMapper = new GenericMapper<>(
      OrderProduct.class);
  private final OrderProductDetailRepository orderProductDetailRepository;
  private final StockRepository stockRepository;
  private final OrderDetailPackingRepository orderDetailPackingRepository;
  private final PackingProductRepository packingProductRepository;

  @Override
  public List<OrderProduct> getAll() {
    return orderProductRepository.findAllByActiveIsTrue();
  }

  @Override
  public OrderProduct create(OrderProductDto orderProductdto) {

    if (orderProductdto.getOrderCode() != null
        && orderProductRepository.existsByOrderCodeAndActiveIsTrue(
        orderProductdto.getOrderCode())) {
      errorProcess(
          "Ya existe el codigo (" + orderProductdto.getOrderCode() + ")");
    }

    Storehouse storehouse = this.findStorehouseById(orderProductdto.getStorehouseId());
    OrderProductType orderProductType = this.findOrderProductTypeById(
        orderProductdto.getOrderProductTypeId());

    OrderProduct orderProduct = OrderProduct.builder()
        .orderCode(
            (orderProductdto.getOrderCode() == null) ? "S/C" : orderProductdto.getOrderCode()
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
        .status(StatusFlowEnum.BORRADOR.name())
        .build();

    return orderProductRepository.save(orderProduct);
  }

  @Override
  public OrderProduct update(Integer id, OrderProductDto orderProductdto) {
    OrderProduct orderProductFound = this.findOrderProductById(id);
    if (orderProductRepository.existsByOrderCodeAndIdNotAndActiveIsTrue(
        orderProductdto.getOrderCode(), orderProductFound.getId())) {
      throw new DuplicateException(OrderProduct.class.getSimpleName(), "code", "");
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
  public void delete(Integer id) {
    OrderProduct orderProduct = this.findOrderProductById(id);
    if (orderProduct.getActive()) {
      orderProduct.setActive(false);
      orderProductRepository.save(orderProduct);
    } else {
      throw new AlreadyDeletedException(OrderProduct.class.getSimpleName(), orderProduct.getId());
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

      //En caso se sea un ingreso a almacen
      if(Objects.equals(orderProduct.getOrderProductType().getAction(), OrderProductTypeActionEnum.RECEIPT.name())){
        this.copyDataOrderDetailPackingToPackingProduct(orderProductDetail, stock);
      }

      //En caso se sea una salida de almacen
      if(Objects.equals(orderProduct.getOrderProductType().getAction(), OrderProductTypeActionEnum.DISPATCH.name())){
        this.subtractDataOrderDetailPackingToPackingProduct(orderProductDetail, stock);
      }

    }

    orderProduct.setStatus(StatusFlowEnum.FINALIZADO.name());
    return orderProductRepository.save(orderProduct);

  }



  private void copyDataOrderDetailPackingToPackingProduct(OrderProductDetail orderProductDetail,
                                                          Stock stock) {
    // Obteneoms de un detalle especifico todos los pedidos por paquetes
    List<OrderDetailPacking> orderDetailPackings =
        orderDetailPackingRepository.findByOrderProductDetail_IdAndActiveTrue(
            orderProductDetail.getId());

    PackingProduct packingProductNew = new PackingProduct();

    //Recorremos todos los paquetes que se ordenaron para el ingreso
    for (OrderDetailPacking orderDetailPacking : orderDetailPackings) {
      PackingProduct packingProduct = new PackingProduct();
      packingProduct.setCode(orderDetailPacking.getCode());
      packingProduct.setExpirationDate(orderDetailPacking.getExpirationDate());
      packingProduct.setAmount(orderDetailPacking.getAmount());
      packingProduct.setPacking(orderDetailPacking.getPacking());
      packingProduct.setStock(stock);
      packingProductNew = packingProductRepository.save(packingProduct);

      orderDetailPacking.setPackingProduct(packingProductNew);
      orderDetailPackingRepository.save(orderDetailPacking);

    }
  }

  private void subtractDataOrderDetailPackingToPackingProduct(OrderProductDetail orderProductDetail,
                                                              Stock stock) {
    // Obteneoms de un detalle especifico todos los pedidos por paquetes
    List<OrderDetailPacking> orderDetailPackings =
            orderDetailPackingRepository.findByOrderProductDetail_IdAndActiveTrue(
                    orderProductDetail.getId());

    PackingProduct packingProductNew = new PackingProduct();

    //Recorremos todos los paquetes que se ordenaron para la salida
    for (OrderDetailPacking orderDetailPacking : orderDetailPackings) {

      //Buscamos el empaque del producto para restar a tu cantidad
      PackingProduct packingProduct = this.findPackingProductById(orderDetailPacking.getPackingProduct().getId());
      double newAmount = packingProduct.getAmount()-orderDetailPacking.getAmount();
      if(newAmount>=0)
        packingProduct.setAmount(newAmount);
      else
        errorProcess(String.format("El producto (%s con codigo de empaque '%s') tiene un saldo disponible de (%s) y no puede despachar la cantidad de (%s) solicitada",
                packingProduct.getStock().getProduct().getName(),
                orderDetailPacking.getCode(),
                packingProduct.getAmount(),
                orderDetailPacking.getAmount() )
        );
      packingProductRepository.save(packingProduct);

    }
  }

  private PackingProduct findPackingProductById(Integer id) {
    return packingProductRepository.findByIdAndActiveIsTrue(id).orElseThrow(
            errorEntityNotFound(PackingProduct.class, id)
    );
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

  private void validationExecuteOrderProduct(List<OrderProductDetail> orderProductDetails, OrderProduct orderProduct) {
    if (orderProductDetails.isEmpty()) {
      errorProcess("No tiene items para realizar.");
    }

    if (Objects.equals(orderProduct.getStatus(), StatusFlowEnum.FINALIZADO.name())) {
      errorProcess("La orden ya fue procesada.");
    }
  }

}