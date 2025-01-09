package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    void saveAll(List<OrderItem> orderItems);
}
