package kr.hhplus.be.server.point.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PointResponse {
    private Long userId;
    private Long userPoint;

    @Builder
    public PointResponse(final Long userId, final Long userPoint) {
        this.userId = userId;
        this.userPoint = userPoint;
    }
}
