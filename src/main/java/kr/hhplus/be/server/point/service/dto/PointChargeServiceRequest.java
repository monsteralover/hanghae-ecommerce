package kr.hhplus.be.server.point.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PointChargeServiceRequest {
    private Long amount;

    @Builder
    public PointChargeServiceRequest(final Long amount) {
        this.amount = amount;
    }
}
