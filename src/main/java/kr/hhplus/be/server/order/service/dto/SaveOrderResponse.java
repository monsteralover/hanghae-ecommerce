package kr.hhplus.be.server.order.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SaveOrderResponse {
    private long orderId;
    private long paymentAmount;

}
