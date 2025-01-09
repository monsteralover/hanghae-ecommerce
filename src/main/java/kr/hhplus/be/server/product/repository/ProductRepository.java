package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository {
    Page<Product> getProductsPagination(Pageable pageable);

    List<Product> getTopFiveProducts();

}
