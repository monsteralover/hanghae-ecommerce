package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserIdOrderByCreatedDateDesc(Long userId);

    List<Order> findAllByUserId(long savedUserId);
}
