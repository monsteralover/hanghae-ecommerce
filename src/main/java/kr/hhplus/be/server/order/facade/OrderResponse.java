package kr.hhplus.be.server.order.facade;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "주문 응답 정보")
public class OrderResponse {
    @Schema(description = "생성된 주문 ID")
    private Long orderId;
}
