package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.enums.PackingCodeEnum;
import com.gestion.almacenes.commons.enums.StatusFlowEnum;
import com.gestion.almacenes.commons.exception.ValidationErrorException;
import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderProductDetailDto;
import com.gestion.almacenes.entities.OrderProduct;
import com.gestion.almacenes.entities.OrderProductDetail;
import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.entities.Stock;
import com.gestion.almacenes.repositories.*;
import com.gestion.almacenes.services.OrderProductDetailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorProcess;

@Service
@AllArgsConstructor
@Transactional
public class OrderProductDetailServiceImpl implements
    OrderProductDetailService {

  private final OrderProductDetailRepository orderProductDetailRepository;
  private final OrderProductRepository orderProductRepository;
  private final StockRepository stockRepository;
  private final ProductRepository productRepository;
  private final GenericMapper<OrderProductDetail, OrderProductDetailDto> genericMapper = new GenericMapper<>(
      OrderProductDetail.class);

  @Override
  public List<OrderProductDetail> getAll() {
    return orderProductDetailRepository.findAllByActiveIsTrue();
  }

  @Override
  public OrderProductDetail create(OrderProductDetailDto orderProductDetaildto) {

    // Obtenemos la cabecera que es la orden de producto
    OrderProduct orderProduct = this.findOrderProductById(
        orderProductDetaildto.getOrderProductId());

    // Verificamos si no se finalizo la solicitud de orden de ingreso
    this.checkIfOrderIsFinalized(orderProduct);

    // Verificamos si no fue ya ingresado el mismo producto al almacen
    if (orderProductDetailRepository.existsByOrderProduct_IdAndStock_Storehouse_IdAndStock_Product_IdAndActiveTrue(
        orderProductDetaildto.getOrderProductId(),
        orderProduct.getStorehouse().getId(),
        orderProductDetaildto.getProductId())
    ) {
      //Product product = this.findProductById(orderProductDetaildto.getProductId());
      //throw new ValidationErrorException("Ya fue registrado en la orden el Item ("+product.getName()+")");
    }

    // Creamos la entidad con los datos de stock (producto y almacen), cantidad ingresada y cabecera
    OrderProductDetail orderProductDetail = OrderProductDetail.builder()
            .stock(
            this.findStockByStorehouseIdAndProductId(orderProduct.getStorehouse().getId(),
                orderProductDetaildto.getProductId()))
            .amount(orderProductDetaildto.getAmount())
            .orderProduct(orderProduct)
            .codeProduct(orderProductDetaildto.getCodeProduct())
            .expirationDateProduct(orderProductDetaildto.getExpirationDateProduct())
        .build();

    // Guardamos la entidad el detalle
    OrderProductDetail orderProductDetailNew = orderProductDetailRepository.save(
        orderProductDetail);

    return orderProductDetailNew;
  }

  @Override
  public void createList(List<OrderProductDetailDto> orderProductDetailDtos) {
    for (OrderProductDetailDto orderProductDetailDto: orderProductDetailDtos){
      create(orderProductDetailDto);
    }
  }

  @Override
  public OrderProductDetail update(Integer id, OrderProductDetailDto orderProductDetailDto) {

    OrderProduct orderProduct = this.findOrderProductById(
        orderProductDetailDto.getOrderProductId());

    this.checkIfOrderIsFinalized(orderProduct);

    OrderProductDetail orderProductDetailFound = this.findOrderProductDetailById(id);

    orderProductDetailFound.setStock(
        this.findStockByStorehouseIdAndProductId(orderProduct.getStorehouse().getId(),
            orderProductDetailDto.getProductId())
    );
    orderProductDetailFound.setOrderProduct(orderProduct);



    return orderProductDetailRepository.save(orderProductDetailFound);
  }

  @Override
  public void delete(Integer id) {
    OrderProductDetail orderProductDetail = this.findOrderProductDetailById(id);
    orderProductDetailRepository.delete(orderProductDetail);

  }

  @Override
  public OrderProductDetail getById(Integer id) {
    return this.findOrderProductDetailById(id);
  }

  @Override
  public List<OrderProductDetail> getFiltered(String code, String name) {
    return orderProductDetailRepository.findAll();
  }

  @Override
  public PagePojo<OrderProductDetail> getByPageAndFilters(Integer pageNumber, Integer pageSize,
      String sortField, String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<OrderProductDetail> orderProductDetailPage = orderProductDetailRepository.findAll(
        pageable);

    return genericMapper.fromEntity(orderProductDetailPage);
  }

  private OrderProductDetail findOrderProductDetailById(Integer id) {

    return orderProductDetailRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(OrderProductDetail.class, id)
    );
  }

  private OrderProduct findOrderProductById(Integer id) {
    return orderProductRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(OrderProduct.class, id)
    );
  }

  private Stock findStockByStorehouseIdAndProductId(Integer storehouseId, Integer productId) {

    return stockRepository.findByStorehouse_IdAndProduct_IdAndActiveTrue(storehouseId, productId)
        .orElseThrow(
            errorEntityNotFound(Stock.class, storehouseId)
        );
  }

  private void checkIfOrderIsFinalized(OrderProduct orderProduct) {
    if (Objects.equals(orderProduct.getStatus(), StatusFlowEnum.FINALIZADO.name())) {
      throw new ValidationErrorException(
          String.format(
              "Actualmente el estado de la orden esta en estado (%s) y no puede realizar esta operación",
              StatusFlowEnum.FINALIZADO.name())
      );
    }
  }

  private Product findProductById(Integer productId) {

    return productRepository.findByIdAndActiveIsTrue(productId).orElseThrow(
        errorEntityNotFound(Product.class, productId)
    );
  }

  /**
   * Verificar si el total del detalle es igual a la cantidad de paquetes por lote
   *
   * @param amountTotal           Cantidad de productos de todos los paquetes por lote que se
   *                              registrara
   * @param orderProductDetailDto Cantidad total del detalle de orden
   */
  private void checkIfTotalQuantityIsEqualToQuantityBatch(Double amountTotal,
      OrderProductDetailDto orderProductDetailDto) {
    if (!Objects.equals(amountTotal, orderProductDetailDto.getAmount())) {
      throw new ValidationErrorException(
          String.format(
              "La cantidad total (%s) es distinta a la cantidad por empaques (%s) que se envio.",
              orderProductDetailDto.getAmount(), amountTotal)
      );
    }
  }




}
