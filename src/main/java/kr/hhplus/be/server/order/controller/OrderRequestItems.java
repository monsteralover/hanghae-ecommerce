package kr.hhplus.be.server.order.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRequestItems {
    private Long productId;
    private Integer quantity;
}
