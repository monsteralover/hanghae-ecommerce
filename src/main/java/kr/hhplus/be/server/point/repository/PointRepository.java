package kr.hhplus.be.server.point.repository;

import kr.hhplus.be.server.point.domain.Point;

import java.util.Optional;

public interface PointRepository {
    Optional<Point> getByUserId(Long userId);

    Point save(Point point);
}
