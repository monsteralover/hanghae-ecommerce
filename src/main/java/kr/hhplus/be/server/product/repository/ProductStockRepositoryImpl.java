package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.product.domain.ProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStockRepositoryImpl implements ProductStockRepository {
    private final ProductStockJpaRepository jpaRepository;

    @Override
    public void resetAccumulatedSoldCountEveryThreeDays() {
        jpaRepository.resetAccumulatedSoldCountEveryThreeDays();
    }

    @Override
    public ProductStock getByProductId(final Long productId) {
        return jpaRepository.findByProductId(productId)
                .orElseThrow(() -> new ApiException(ApiResponseCodeMessage.INVALID_PRODUCT_ID));
    }

    @Override
    public void save(final ProductStock productStock) {
        jpaRepository.save(productStock);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
