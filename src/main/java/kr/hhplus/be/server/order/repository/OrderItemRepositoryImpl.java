package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {
    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public void saveAll(final List<OrderItem> orderItems) {
        orderItemJpaRepository.saveAll(orderItems);
    }
}
