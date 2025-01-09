package kr.hhplus.be.server.product.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductStock;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Schema(description = "상품 응답 정보")
public record ProductResponse(
        @Schema(description = "상품 ID")
        Long productId,
        @Schema(description = "상품명")
        String productName,
        @Schema(description = "가격")
        Integer price,
        @Schema(description = "재고 수량")
        Integer remainingStock
) {
    public static List<ProductResponse> from(final Page<Product> products) {
        return products.stream().filter(Objects::nonNull).map(product -> {
            final Integer quantity = Optional.of(product)
                    .map(Product::getProductStock)
                    .map(ProductStock::getStockQuantity)
                    .orElse(0);
            return new ProductResponse(product.getId(), product.getName(), product.getPrice(), quantity);
        }).collect(Collectors.toList());
    }
}
