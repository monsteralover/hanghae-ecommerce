package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.coupon.domain.CouponIssue;
import kr.hhplus.be.server.coupon.service.CouponIssueReadService;
import kr.hhplus.be.server.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CouponFacade {
    private final UserReadService userReadService;
    private final CouponIssueReadService couponIssueReadService;

    public List<CouponUserResponse> getUsableCouponsForUser(Long userId) {
        userReadService.checkUserExistsById(userId);
        final List<CouponIssue> couponIssues = couponIssueReadService.getUsableCouponsForUser(userId);
        return CouponUserResponse.toCouponResponse(couponIssues);
    }
}
