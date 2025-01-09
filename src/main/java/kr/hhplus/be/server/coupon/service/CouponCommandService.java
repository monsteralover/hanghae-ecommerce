package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponCommandService {
    private final CouponRepository couponRepository;

    public void deductQuantityWithLock(final Long couponId) {
        final Coupon coupon = couponRepository.getCouponWithLock(couponId);
        coupon.validateCouponIssue();
        coupon.deductQuantity();
        couponRepository.save(coupon);
    }
}
