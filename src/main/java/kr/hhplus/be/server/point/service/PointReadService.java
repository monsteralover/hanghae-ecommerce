package kr.hhplus.be.server.point.service;

import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.repository.PointRepository;
import kr.hhplus.be.server.point.service.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointReadService {
    private final PointRepository pointRepository;

    public PointResponse getUserPoint(final Long userId) {
        final Point point = pointRepository.getByUserId(userId)
                .orElse(Point.create(userId));

        return PointResponse.builder()
                .userId(point.getUserId())
                .userPoint(point.getPoint())
                .build();
    }
}
