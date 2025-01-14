package kr.hhplus.be.server.order.service.dto;

import kr.hhplus.be.server.order.controller.OrderRequestItems;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderServiceRequest {
    private List<OrderRequestItems> orderItems;
    private Long couponId;
    private long userId;
    private int discountAmount;


}

