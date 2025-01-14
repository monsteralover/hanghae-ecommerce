package kr.hhplus.be.server.order.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.coupon.facade.OrderFacadeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 요청 정보")
public class OrderRequest {
    @Schema(description = "주문할 상품 목록")
    @JsonProperty("orderItems")
    @NotNull
    private List<OrderRequestItems> orderItems;

    @Schema(description = "적용할 쿠폰 ID")
    @JsonProperty("couponId")
    private Long couponId;

    public OrderFacadeRequest toFacadeRequest(long userId) {
        return OrderFacadeRequest.builder().orderItems(orderItems)
                .couponId(couponId)
                .userId(userId)
                .build();
    }
}
