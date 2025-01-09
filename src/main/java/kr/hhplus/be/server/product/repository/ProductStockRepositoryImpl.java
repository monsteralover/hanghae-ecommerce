package kr.hhplus.be.server.product.repository;

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
}
