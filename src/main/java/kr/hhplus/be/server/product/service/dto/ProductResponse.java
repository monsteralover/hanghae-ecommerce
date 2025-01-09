package kr.hhplus.be.server.product.service.dto;

import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductStock;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public record ProductResponse(
        Long productId,
        String productName,
        Integer price,
        Integer remainingStock
) {
    public static List<ProductResponse> toProductResponse(final Page<Product> products) {
        return products.stream().filter(Objects::nonNull).map(product -> {
            final Integer quantity = Optional.of(product)
                    .map(Product::getProductStock)
                    .map(ProductStock::getStockQuantity)
                    .orElse(0);
            return new ProductResponse(product.getId(), product.getName(), product.getPrice(), quantity);
        }).collect(Collectors.toList());
    }
}
