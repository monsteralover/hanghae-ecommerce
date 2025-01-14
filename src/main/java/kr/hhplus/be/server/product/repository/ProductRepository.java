package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ProductRepository {
    Page<Product> getProductsPagination(Pageable pageable);

    List<Product> getTopFiveProducts();

    Product getById(Long productId);

    Product save(Product product);

    List<Product> getAllById(Set<Long> productIds);
}
