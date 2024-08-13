package com.gestion.almacenes.servicesImpls;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;

import com.gestion.almacenes.commons.enums.StatusFlowEnum;
import com.gestion.almacenes.commons.exception.ValidationErrorException;
import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.OrderDetailPackingDto;
import com.gestion.almacenes.dtos.OrderProductDetailDto;
import com.gestion.almacenes.entities.OrderDetailPacking;
import com.gestion.almacenes.entities.OrderProduct;
import com.gestion.almacenes.entities.OrderProductDetail;
import com.gestion.almacenes.entities.Packing;
import com.gestion.almacenes.entities.PackingProduct;
import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.entities.Stock;
import com.gestion.almacenes.repositories.OrderDetailPackingRepository;
import com.gestion.almacenes.repositories.OrderProductDetailRepository;
import com.gestion.almacenes.repositories.OrderProductRepository;
import com.gestion.almacenes.repositories.PackingProductRepository;
import com.gestion.almacenes.repositories.PackingRepository;
import com.gestion.almacenes.repositories.ProductRepository;
import com.gestion.almacenes.repositories.StockRepository;
import com.gestion.almacenes.services.OrderProductDetailService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class OrderProductDetailServiceImpl implements
    OrderProductDetailService {

  private final OrderProductDetailRepository orderProductDetailRepository;
  private final OrderProductRepository orderProductRepository;
  private final StockRepository stockRepository;
  private final ProductRepository productRepository;
  private final PackingRepository packingRepository;
  private final GenericMapper<OrderProductDetail, OrderProductDetailDto> genericMapper = new GenericMapper<>(
      OrderProductDetail.class);
  private final OrderDetailPackingRepository orderDetailPackingRepository;
  private final PackingProductRepository packingProductRepository;

  @Override
  public List<OrderProductDetail> getAll() {
    return orderProductDetailRepository.findAllByActiveIsTrue();
  }

  @Override
  public OrderProductDetail create(OrderProductDetailDto orderProductDetaildto) {

    OrderProduct orderProduct = this.findOrderProductById(
        orderProductDetaildto.getOrderProductId());

    this.checkIfOrderIsFinalized(orderProduct);

    if (orderProductDetailRepository.existsByOrderProduct_IdAndStock_Storehouse_IdAndStock_Product_IdAndActiveTrue(
        orderProductDetaildto.getOrderProductId(),
        orderProduct.getStorehouse().getId(),
        orderProductDetaildto.getProductId())
    ) {
      //Product product = this.findProductById(orderProductDetaildto.getProductId());
      //throw new ValidationErrorException("Ya fue registrado en la orden el Item ("+product.getName()+")");
    }

    OrderProductDetail orderProductDetail = OrderProductDetail.builder()
        .stock(
            this.findStockByStorehouseIdAndProductId(orderProduct.getStorehouse().getId(),
                orderProductDetaildto.getProductId())
        )
        .amount(orderProductDetaildto.getAmount())
        .orderProduct(
            orderProduct
        )
        .build();

    OrderProductDetail orderProductDetailNew = orderProductDetailRepository.save(
        orderProductDetail);

    //Creamos el OrderDetailPacking de todos los paquetes por lote que creamos
    Double amountTotal = this.registerBatchOfOrderDetailByPackage(orderProductDetaildto,
        orderProductDetailNew);

    // Verificamos si el total del detalle es igual a la cantidad de paquetes por lote
    this.checkIfTotalQuantityIsEqualToQuantityBatch(amountTotal, orderProductDetaildto);

    return orderProductDetailNew;
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
    orderProductDetailFound.setAmount(orderProductDetailDto.getAmount());
    orderProductDetailFound.setOrderProduct(orderProduct);

    // Eliminamos todos los empaques que estan en el lote del detalle de orden
    orderDetailPackingRepository.deleteByOrderProductDetail(orderProductDetailFound);

    //Creamos el OrderDetailPacking de todos los paquetes por lote que creamos
    Double amountTotal = this.registerBatchOfOrderDetailByPackage(orderProductDetailDto,
        orderProductDetailFound);

    // Verificamos si el total del detalle es igual a la cantidad de paquetes por lote
    this.checkIfTotalQuantityIsEqualToQuantityBatch(amountTotal, orderProductDetailDto);

    return orderProductDetailRepository.save(orderProductDetailFound);
  }

  @Override
  public void delete(Integer id) {
    OrderProductDetail orderProductDetail = this.findOrderProductDetailById(id);
    orderProductDetailRepository.delete(orderProductDetail);

    // Eliminamos todos los empaques que estan en el lote del detalle de orden
    orderDetailPackingRepository.deleteByOrderProductDetail(orderProductDetail);
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

  private Packing findPackingById(Integer packingId) {
    return packingRepository.findByIdAndActiveIsTrue(packingId).orElseThrow(
        errorEntityNotFound(Packing.class, packingId)
    );
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

  /**
   * Creamos el OrderDetailPacking de todos los paquetes por lote que creamos
   *
   * @param orderProductDetailDto Los paquetes por detalle del lote
   * @param orderProductDetail    El detalle de orden
   * @return La cantidad de productos registrados en el lote
   */
  private Double registerBatchOfOrderDetailByPackage(OrderProductDetailDto orderProductDetailDto,
      OrderProductDetail orderProductDetail) {
    double amountTotal = Double.parseDouble("0");

    for (OrderDetailPackingDto orderDetailPackingDto : orderProductDetailDto.getOrderDetailPackingDtos()) {

      OrderDetailPacking orderDetailPacking = new OrderDetailPacking();
      Packing packing = this.findPackingById(orderDetailPackingDto.getPackingId());
      if (packing.getAmount() < orderDetailPackingDto.getAmount()) {
        throw new ValidationErrorException(
            String.format(
                "El empaque (%s) con fecha de vencimiento (%s) solo puede contener maximo (%s) y se envio (%s)",
                packing.getName(), orderDetailPackingDto.getExpirationDate(),
                packing.getAmount(), orderDetailPackingDto.getAmount())
        );
      }
      orderDetailPacking.setCode(
          (orderDetailPackingDto.getCode() == null) ? "" : orderDetailPackingDto.getCode()
      );
      orderDetailPacking.setOrderProductDetail(orderProductDetail);
      orderDetailPacking.setAmount(orderDetailPackingDto.getAmount());
      orderDetailPacking.setExpirationDate(orderDetailPackingDto.getExpirationDate());
      orderDetailPacking.setPacking(packing);
      if (orderDetailPackingDto.getPackingProductId() != null) {
        orderDetailPacking.setPackingProduct(
            this.findPackingProductById(orderDetailPackingDto.getPackingProductId())
        );
      }
      orderDetailPackingRepository.save(orderDetailPacking);
      amountTotal = amountTotal + orderDetailPackingDto.getAmount();
    }
    return amountTotal;
  }

  private PackingProduct findPackingProductById(Integer id) {
    return packingProductRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(PackingProduct.class, id)
    );
  }

}
