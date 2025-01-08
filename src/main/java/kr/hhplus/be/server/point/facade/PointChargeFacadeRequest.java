package kr.hhplus.be.server.point.facade;

import kr.hhplus.be.server.point.service.dto.PointChargeServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PointChargeFacadeRequest {
    private Long amount;

    @Builder
    public PointChargeFacadeRequest(final Long amount) {
        this.amount = amount;
    }

    public PointChargeServiceRequest toPointChargeServiceRequest() {
        return PointChargeServiceRequest.builder()
                .amount(this.amount)
                .build();
    }
}
