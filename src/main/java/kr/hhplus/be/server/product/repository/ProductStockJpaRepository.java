package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.product.domain.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockJpaRepository extends JpaRepository<ProductStock, Long>,
        ProductStockCustomRepository {
}
