package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.product.domain.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductStockJpaRepository extends JpaRepository<ProductStock, Long>,
        ProductStockCustomRepository {
    Optional<ProductStock> findByProductId(Long ProductId);
}
