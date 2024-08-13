package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.exception.EntityNotFound;
import com.gestion.almacenes.commons.util.GenericMapper;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.StockDto;
import com.gestion.almacenes.entities.Product;
import com.gestion.almacenes.entities.Stock;
import com.gestion.almacenes.entities.Storehouse;
import com.gestion.almacenes.repositories.ProductRepository;
import com.gestion.almacenes.repositories.StockRepository;
import com.gestion.almacenes.repositories.StorehouseRepository;
import com.gestion.almacenes.services.StockService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.errorEntityNotFound;

@Service
@AllArgsConstructor
public class StockServiceImpl implements
    StockService {

  private final StockRepository stockRepository;
  private final GenericMapper<Stock, StockDto> genericMapper = new GenericMapper<>(Stock.class);
  private final StorehouseRepository storeHouseRepository;
  private final ProductRepository productRepository;

  @Override
  public List<Stock> getAll() {
    return stockRepository.findAllByActiveIsTrue();
  }

  @Override
  public Stock create(StockDto stockdto) {

    if (stockRepository.existsByStorehouse_IdAndProduct_IdAndActiveTrue(stockdto.getStorehouseId(),
        stockdto.getProductId())) {
      errorEntityNotFound(Stock.class, stockdto.getStorehouseId());
    }

    Storehouse storehouse = this.findStorehouseById(stockdto.getStorehouseId());
    Product product = this.findProductById(stockdto.getProductId());

    return stockRepository.save(
        Stock.builder().storehouse(storehouse).product(product)
            .amountInStock(stockdto.getAmountInStock())
            .minimumStock(stockdto.getMinimumStock()).maximumStock(stockdto.getMaximumStock())
            .stockAlert(stockdto.getStockAlert()).build()
    );
  }


  @Override
  public Stock update(Integer id, StockDto stockdto) {
    Stock stockFound = this.findStockById(id);
    Storehouse storehouse = this.findStorehouseById(stockdto.getStorehouseId());
    Product product = this.findProductById(stockdto.getProductId());

    return stockRepository.save(
        Stock.builder().storehouse(storehouse).product(product)
            .amountInStock(stockdto.getAmountInStock())
            .minimumStock(stockdto.getMinimumStock()).maximumStock(stockdto.getMaximumStock())
            .stockAlert(stockdto.getStockAlert()).build()
    );
  }

  @Override
  public Stock getById(Integer id) {
    return this.findStockById(id);
  }

  @Override
  public void delete(Integer id) {
    stockRepository.delete(
        this.findStockById(id)
    );
  }

  @Override
  public List<Stock> getFiltered(String code, String name) {
    return stockRepository.findAll();
  }

  @Override
  public PagePojo<Stock> getByPageAndFilters(Integer pageNumber, Integer pageSize, String sortField,
      String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<Stock> stockPage = stockRepository.findAll(pageable);

    return genericMapper.fromEntity(stockPage);
  }

  private Stock findStockById(Integer id) {
    return stockRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(Stock.class, id)
    );
  }

  private Product findProductById(Integer productId) {

    return productRepository.findByIdAndActiveIsTrue(productId).orElseThrow(
        errorEntityNotFound(Product.class, productId)
    );
  }

  private Storehouse findStorehouseById(Integer storehouseId) {
    return storeHouseRepository.findByIdAndActiveIsTrue(storehouseId).orElseThrow(
        errorEntityNotFound(Storehouse.class, storehouseId)
    );
  }

}
