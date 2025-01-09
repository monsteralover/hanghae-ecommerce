package kr.hhplus.be.server.point.service;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.repository.PointRepository;
import kr.hhplus.be.server.point.service.dto.PointChargeServiceRequest;
import kr.hhplus.be.server.point.service.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointCommandService {
    private final PointRepository pointRepository;

    public PointResponse chargeUserPoint(final Long userId, final PointChargeServiceRequest request) {
        final Point point = pointRepository.getByUserId(userId)
                .orElse(Point.create(userId));

        point.charge(request.getAmount());
        final Point savedPoint = pointRepository.save(point);

        return PointResponse.builder()
                .userId(savedPoint.getUserId())
                .userPoint(savedPoint.getPoint())
                .build();
    }

    public void usePoint(final long userId, final long amount) {
        final Point point = pointRepository.getByUserId(userId)
                .orElseThrow(() -> new ApiException(ApiResponseCodeMessage.CHARGE_RECORD_NOT_EXIST));
        point.usePoint(amount);
        pointRepository.save(point);
    }
}
