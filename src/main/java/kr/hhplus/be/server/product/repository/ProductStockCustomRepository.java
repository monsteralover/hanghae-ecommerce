package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.product.domain.ProductStock;

import java.util.List;

public interface ProductStockCustomRepository {
    void resetAccumulatedSoldCountEveryThreeDays();
}
