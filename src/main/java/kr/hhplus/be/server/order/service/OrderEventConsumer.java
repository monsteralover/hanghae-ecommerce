package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.order.OrderFinishedEvent;
import kr.hhplus.be.server.order_finished.domain.EventStatus;
import kr.hhplus.be.server.order_finished.domain.Outbox;
import kr.hhplus.be.server.order_finished.repository.OutBoxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class OrderEventConsumer {
    private final DataPlatform dataPlatform;
    private final OutBoxRepository outBoxRepository;

    @KafkaListener(topics = "order-finished", groupId = "order-group")
    public void listen(OrderFinishedEvent event) {
        log.info("Received order event: {}", event);
        boolean isSent = dataPlatform.sendData(event.getRequest());
        if (isSent) {
            Outbox outbox = outBoxRepository.findByAggregateId(event.getAggregateId());
            outbox.updateStatus(EventStatus.FINISHED);
            outBoxRepository.save(outbox);
        }
    }
}
