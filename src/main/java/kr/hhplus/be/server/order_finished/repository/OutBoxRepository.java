package kr.hhplus.be.server.order_finished.repository;

import kr.hhplus.be.server.order_finished.domain.EventStatus;
import kr.hhplus.be.server.order_finished.domain.Outbox;

import java.util.List;

public interface OutBoxRepository {
    void save(Outbox outbox);

    Outbox findByAggregateId(String id);

    List<Outbox> findAllByEventStatus(EventStatus status);
}
