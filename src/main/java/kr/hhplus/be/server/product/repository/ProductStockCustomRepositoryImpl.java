package kr.hhplus.be.server.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.product.domain.QProductStock.productStock;

@Repository
@AllArgsConstructor
public class ProductStockCustomRepositoryImpl implements ProductStockCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public void resetAccumulatedSoldCountEveryThreeDays() {
        queryFactory
                .update(productStock)
                .set(productStock.accumulatedSoldCount, 0L)
                .where(productStock.accumulatedSoldCount.ne(0L))
                .execute();
    }
}
