package kr.hhplus.be.server.order;

import kr.hhplus.be.server.coupon.facade.OrderFacadeRequest;

public record OrderCalledEvent(OrderFacadeRequest request) {
}