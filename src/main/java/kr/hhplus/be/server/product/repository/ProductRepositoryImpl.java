package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public List<Product> getAllById(final Set<Long> productIds) {
        final List<Product> foundProducts = productJpaRepository.findAllById(productIds);
        final Set<Long> missingProductIds = new HashSet<>(productIds);

        missingProductIds
                .removeAll(foundProducts.stream()
                        .map(Product::getId).collect(Collectors.toSet()));

        if (!missingProductIds.isEmpty()) {
            log.warn("missing productIds among requested productIds : %s".formatted(missingProductIds.toString()));
            throw new ApiException(ApiResponseCodeMessage.INVALID_PRODUCT_ID);
        }
        return foundProducts;
    }


}
