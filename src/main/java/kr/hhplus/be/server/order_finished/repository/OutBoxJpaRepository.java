package kr.hhplus.be.server.order_finished.repository;

import kr.hhplus.be.server.order_finished.domain.EventStatus;
import kr.hhplus.be.server.order_finished.domain.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxJpaRepository extends JpaRepository<Outbox, Long> {
    Outbox findByAggregateId(String id);

    List<Outbox> findAllByEventStatus(EventStatus status);
}
