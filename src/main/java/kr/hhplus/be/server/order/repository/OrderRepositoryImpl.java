package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Long save(final Order finalOrder) {
        return orderJpaRepository.save(finalOrder).getId();
    }

    @Override
    public void flush() {
        orderJpaRepository.flush();
    }

    @Override
    public Order findLatestOrderByUserId(final Long id) {
        return orderJpaRepository.findAllByUserIdOrderByCreatedDateDesc(id).get(0);
    }
}
