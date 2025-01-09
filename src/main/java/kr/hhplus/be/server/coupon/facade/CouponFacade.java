package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.coupon.service.CouponCommandService;
import kr.hhplus.be.server.coupon.service.CouponIssueReadService;
import kr.hhplus.be.server.coupon.service.dto.CouponIssueResponse;
import kr.hhplus.be.server.coupon.service.dto.CouponUserResponse;
import kr.hhplus.be.server.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CouponFacade {
    private final UserReadService userReadService;
    private final CouponIssueReadService couponIssueReadService;
    private final CouponCommandService couponCommandService;

    private final CouponIssueCommandService couponIssueCommandService;

    public List<CouponUserResponse> getUsableCouponsForUser(Long userId) {
        userReadService.checkUserExistsById(userId);
        return couponIssueReadService.getUsableCouponsForUser(userId);
    }

    @Transactional
    public CouponIssueResponse issueCouponForUser(Long userId, Long couponId) {
        couponCommandService.deductQuantityWithLock(couponId);
        return couponIssueCommandService.saveCouponIssue(userId, couponId);
    }
}
