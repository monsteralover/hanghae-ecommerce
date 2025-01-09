package kr.hhplus.be.server.product.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.product.domain.Product;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Schema(description = "베스트 상품 응답 정보")
public record ProductMostSoldResponse(
        @Schema(description = "상품 ID")
        Long productId,
        @Schema(description = "상품명")
        String productName,
        @Schema(description = "가격")
        Integer price,
        @Schema(description = "재고 수량")
        Integer remainingStock,
        @Schema(description = "누적 판매량")
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
