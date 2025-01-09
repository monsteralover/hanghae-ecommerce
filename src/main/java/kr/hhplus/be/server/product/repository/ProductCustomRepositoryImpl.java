package kr.hhplus.be.server.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.product.domain.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.product.domain.QProduct.product;

@Repository
@AllArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> getTopFiveProducts() {
        return queryFactory.selectFrom(product)
                .leftJoin(product.productStock).fetchJoin()
                .orderBy(product.productStock.accumulatedSoldCount.desc())
                .limit(5)
                .fetch();
    }

}
