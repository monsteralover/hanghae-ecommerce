package kr.hhplus.be.server.point.api;

import kr.hhplus.be.server.point.facade.PointChargeFacadeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
