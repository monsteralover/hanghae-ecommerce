package kr.hhplus.be.server.coupon.facade;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.hhplus.be.server.order.controller.OrderRequestItems;
import kr.hhplus.be.server.order.service.dto.OrderServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderFacadeRequest {
    @JsonProperty
    private List<OrderRequestItems> orderItems;
    @JsonProperty
    private Long couponId;
    @JsonProperty
    private long userId;

    public OrderServiceRequest toOrderServiceRequest(final int discountAmount) {
        return OrderServiceRequest.builder().orderItems(orderItems)
                .couponId(couponId)
                .userId(userId)
                .discountAmount(discountAmount)
                .build();
    }

}
