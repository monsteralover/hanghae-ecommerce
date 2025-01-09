package kr.hhplus.be.server.product.service.dto;

public record ProductResponse(
        Long productId,
        String productName,
        Integer price,
        Integer remainingStock
) {
}
