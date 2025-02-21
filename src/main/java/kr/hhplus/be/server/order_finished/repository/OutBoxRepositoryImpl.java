package kr.hhplus.be.server.order_finished.repository;

import kr.hhplus.be.server.order_finished.domain.EventStatus;
import kr.hhplus.be.server.order_finished.domain.Outbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
@RequiredArgsConstructor
public class OutBoxRepositoryImpl implements OutBoxRepository {
    private final OutBoxJpaRepository outBoxJpaRepository;

    @Override
    public void save(Outbox outbox) {
        outBoxJpaRepository.save(outbox);
    }

    @Override
    public Outbox findByAggregateId(String id) {
        return outBoxJpaRepository.findByAggregateId(id);
    }

    public List<Outbox> findAllByEventStatus(EventStatus status) {
        return outBoxJpaRepository.findAllByEventStatus(status);
    }
}
