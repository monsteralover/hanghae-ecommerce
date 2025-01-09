package kr.hhplus.be.server.order.controller;

import kr.hhplus.be.server.coupon.facade.OrderFacadeRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private List<OrderRequestItems> orderItems;
    private long couponId;

    public OrderFacadeRequest toFacadeRequest(long userId) {
        return OrderFacadeRequest.builder().orderItems(orderItems)
                .couponId(couponId)
                .userId(userId)
                .build();
    }
}
