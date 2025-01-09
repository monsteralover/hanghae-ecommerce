package kr.hhplus.be.server.point.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "포인트 응답 정보")
public class PointResponse {
    @Schema(description = "사용자 ID")
    private Long userId;
    @Schema(description = "현재 포인트 잔액")
    private Long userPoint;

    @Builder
    public PointResponse(final Long userId, final Long userPoint) {
        this.userId = userId;
        this.userPoint = userPoint;
    }
}
