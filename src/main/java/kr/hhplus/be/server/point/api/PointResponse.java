package kr.hhplus.be.server.point.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointResponse {
    private Long userId;
    private Long userPoint;
}
