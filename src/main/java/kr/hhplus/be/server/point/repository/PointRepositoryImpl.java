package kr.hhplus.be.server.point.repository;

import kr.hhplus.be.server.point.domain.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
    private final PointJpaRepository pointJpaRepository;

    @Override
    public Optional<Point> getByUserId(final Long userId) {
        return pointJpaRepository.findByUserId(userId);
    }

    @Override
    public Point save(final Point point) {
        return pointJpaRepository.save(point);
    }

    @Override
    public void deleteAll() {
        pointJpaRepository.deleteAll();
    }
}
