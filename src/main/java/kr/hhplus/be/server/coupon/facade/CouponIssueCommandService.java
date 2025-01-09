package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponIssue;
import kr.hhplus.be.server.coupon.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import kr.hhplus.be.server.coupon.service.dto.CouponIssueResponse;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueCommandService {
    private final CouponIssueRepository couponIssueRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    public CouponIssueResponse saveCouponIssue(final Long userId, final Long couponId) {
        final User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new ApiException(ApiResponseCodeMessage.INVALID_USER));
        final Coupon coupon = couponRepository.getCouponById(couponId);
        final CouponIssue couponIssue = CouponIssue.create(user, coupon);
        final CouponIssue savedCouponIssue = couponIssueRepository.save(couponIssue);
        return CouponIssueResponse.from(savedCouponIssue);
    }
}
