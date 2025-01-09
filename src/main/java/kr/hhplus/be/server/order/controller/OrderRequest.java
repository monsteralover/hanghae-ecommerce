package kr.hhplus.be.server.order.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OrderRequest {
    @JsonProperty("orderItems")
    private List<OrderRequestItems> orderItems;
    @JsonProperty("couponId")
    private long couponId;

    public OrderFacadeRequest toFacadeRequest(long userId) {
        return OrderFacadeRequest.builder().orderItems(orderItems)
                .couponId(couponId)
                .userId(userId)
                .build();
    }
}
