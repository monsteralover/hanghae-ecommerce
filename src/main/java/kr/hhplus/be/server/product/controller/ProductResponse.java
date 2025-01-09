package kr.hhplus.be.server.product.controller;

public record ProductResponse(
        Long productId,
        String productName,
        Integer price,
        Integer remainingStock
) {
}
