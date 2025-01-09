package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Page<Product> getProductsPagination(final Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }

    @Override
    public List<Product> getTopFiveProducts() {
        return productJpaRepository.getTopFiveProducts();
    }

    @Override
    public Product getById(final Long productId) {
        return productJpaRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ApiResponseCodeMessage.INVALID_PRODUCT_ID));
    }

    @Override
    public Product save(final Product product) {
        return productJpaRepository.save(product);
    }


}
