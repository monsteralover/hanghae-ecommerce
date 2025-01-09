package kr.hhplus.be.server.product.service.dto;

import kr.hhplus.be.server.product.domain.Product;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record ProductMostSoldResponse(
        Long productId,
        String productName,
        Integer price,
        Integer remainingStock,
        Long accumulatedSoldCount
) {
    public static List<ProductMostSoldResponse> from(final List<Product> products) {
        return products.stream().filter(Objects::nonNull).map(product -> {
            return new ProductMostSoldResponse(product.getId(), product.getName(), product.getPrice(),
                    product.getProductStock().getStockQuantity(),
                    product.getProductStock().getAccumulatedSoldCount());
        }).collect(Collectors.toList());
    }

}
