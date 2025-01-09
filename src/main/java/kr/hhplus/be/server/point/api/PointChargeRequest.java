package kr.hhplus.be.server.point.api;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.point.facade.PointChargeFacadeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "포인트 충전 요청")
public class PointChargeRequest {
    public PointChargeRequest() {
    }

    @Schema(description = "충전할 포인트 금액")
    private Long amount;

    public PointChargeFacadeRequest toFacadeRequest() {
        return PointChargeFacadeRequest.builder()
                .amount(this.amount)
                .build();
    }
}
