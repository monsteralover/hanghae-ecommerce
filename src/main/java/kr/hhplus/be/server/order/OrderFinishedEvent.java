package kr.hhplus.be.server.order;

import kr.hhplus.be.server.coupon.facade.OrderFacadeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderFinishedEvent {
    private String aggregateId;
    private OrderFacadeRequest request;

    public static OrderFinishedEvent of(OrderFacadeRequest request) {
        return OrderFinishedEvent.builder()
                .aggregateId(UUID.randomUUID().toString())
                .request(request)
                .build();
    }
}