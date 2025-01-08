package kr.hhplus.be.server.point.facade;

import kr.hhplus.be.server.point.service.PointCommandService;
import kr.hhplus.be.server.point.service.PointReadService;
import kr.hhplus.be.server.point.service.dto.PointResponse;
import kr.hhplus.be.server.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {
    private final PointReadService pointReadService;
    private final PointCommandService pointCommandService;
    private final UserReadService userReadService;

    public PointResponse getUserPoint(final Long userId) {
        userReadService.checkUserExistsById(userId);
        return pointReadService.getUserPoint(userId);
    }

    public PointResponse chargeUserPoint(final Long userId, PointChargeFacadeRequest request) {
        userReadService.checkUserExistsById(userId);
        return pointCommandService.chargeUserPoint(userId, request.toPointChargeServiceRequest());
    }
}
