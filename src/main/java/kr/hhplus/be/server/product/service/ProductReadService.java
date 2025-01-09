package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.repository.ProductRepository;
import kr.hhplus.be.server.product.service.dto.ProductMostSoldResponse;
import kr.hhplus.be.server.product.service.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductReadService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getProducts(final int page, final int size) {
        final Pageable pageable = PaginationVerification.toPageable(page, size);
        final Page<Product> productsPagination = productRepository.getProductsPagination(pageable);
        return ProductResponse.toProductResponse(productsPagination);
    }

    public List<ProductMostSoldResponse> getMostSoldProducts() {
        final List<Product> topFiveProducts = productRepository.getTopFiveProducts();
        return ProductMostSoldResponse.toProductMostSoldResponse(topFiveProducts);
    }

}
