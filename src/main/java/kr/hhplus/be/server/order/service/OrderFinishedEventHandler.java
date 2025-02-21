package kr.hhplus.be.server.order.service;


import kr.hhplus.be.server.order.OrderFinishedEvent;
import kr.hhplus.be.server.order_finished.domain.EventStatus;
import kr.hhplus.be.server.order_finished.domain.EventType;
import kr.hhplus.be.server.order_finished.domain.Outbox;
import kr.hhplus.be.server.order_finished.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderFinishedEventHandler {
    private final OutBoxRepository outBoxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "order-finished";

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void saveOutbox(OrderFinishedEvent event) {
        Outbox outbox = Outbox.create(
                EventType.ORDER_FINISHED_EVENT,
                event,
                EventStatus.INITIATED
        );

        outBoxRepository.save(outbox);

    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void sendOrderInfo(OrderFinishedEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }

}