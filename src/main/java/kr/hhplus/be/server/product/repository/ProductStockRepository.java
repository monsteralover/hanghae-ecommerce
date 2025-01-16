package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.product.domain.ProductStock;

public interface ProductStockRepository {
    void resetAccumulatedSoldCountEveryThreeDays();

    ProductStock getByProductId(Long productId);

    void save(ProductStock productStock);

    void deleteAll();
}
