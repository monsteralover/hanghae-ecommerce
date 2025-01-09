package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.product.controller.ProductResponse;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductStock;
import kr.hhplus.be.server.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductReadService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getProducts(final int page, final int size) {
        final Pageable pageable = PaginationVerification.toPageable(page, size);
        final Page<Product> productsPagination = productRepository.getProductsPagination(pageable);
        return productsPagination.stream().filter(Objects::nonNull).map(product -> {
            final Integer quantity = Optional.of(product)
                    .map(Product::getProductStock)
                    .map(ProductStock::getStockQuantity)
                    .orElse(0);
            return new ProductResponse(product.getId(), product.getName(), product.getPrice(), quantity);
        }).collect(Collectors.toList());
    }


}
