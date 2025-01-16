package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.order.controller.OrderRequestItems;
import kr.hhplus.be.server.order.service.dto.OrderServiceRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderFacadeRequest {
    private List<OrderRequestItems> orderItems;
    private Long couponId;
    private long userId;

    public OrderServiceRequest toOrderServiceRequest(final int discountAmount) {
        return OrderServiceRequest.builder().orderItems(orderItems)
                .couponId(couponId)
                .userId(userId)
                .discountAmount(discountAmount)
                .build();
    }

}
