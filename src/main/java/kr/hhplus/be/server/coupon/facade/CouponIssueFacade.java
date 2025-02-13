package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.coupon.service.CouponCommandService;
import kr.hhplus.be.server.coupon.service.CouponIssueCommandService;
import kr.hhplus.be.server.coupon.service.dto.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CouponIssueFacade {
    private final CouponCommandService couponCommandService;
    private final CouponIssueCommandService couponIssueCommandService;

    @Transactional
    CouponIssueResponse issueCoupon(final Long userId, final Long couponId) {
        return couponIssueCommandService.saveCouponIssue(userId, couponId);
    }
}
