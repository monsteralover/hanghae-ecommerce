package kr.hhplus.be.server.order_finished.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.BaseEntity;
import kr.hhplus.be.server.order.OrderFinishedEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class Outbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String aggregateId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private OrderFinishedEvent payload;

    @Enumerated(EnumType.STRING)
    private EventStatus status;


    public static Outbox create(EventType eventType, OrderFinishedEvent payload, EventStatus status) {
        return Outbox.builder()
                .eventType(eventType)
                .payload(payload)
                .status(status)
                .build();
    }

    public void updateStatus(EventStatus eventStatus) {
        this.status = eventStatus;
    }
}
