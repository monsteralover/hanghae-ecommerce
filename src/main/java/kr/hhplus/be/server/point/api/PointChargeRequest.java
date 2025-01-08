package kr.hhplus.be.server.point.api;

import kr.hhplus.be.server.point.facade.PointChargeFacadeRequest;
import lombok.Getter;

@Getter
public class PointChargeRequest {
    public PointChargeRequest() {
    }

    private Long amount;

    public PointChargeFacadeRequest toFacadeRequest() {
        return PointChargeFacadeRequest.builder()
                .amount(this.amount)
                .build();
    }
}
