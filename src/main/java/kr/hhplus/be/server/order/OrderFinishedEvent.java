package kr.hhplus.be.server.order;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private String aggregateId;
    @JsonProperty
    private OrderFacadeRequest request;

    public static OrderFinishedEvent of(OrderFacadeRequest request) {
        return OrderFinishedEvent.builder()
                .aggregateId(UUID.randomUUID().toString())
                .request(request)
                .build();
    }
}