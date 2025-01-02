package kr.hhplus.be.server.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductRequest {
    private Long productId;
    private Integer quantity;
}
