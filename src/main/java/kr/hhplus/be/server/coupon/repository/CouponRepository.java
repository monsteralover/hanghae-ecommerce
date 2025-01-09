package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.domain.Coupon;

public interface CouponRepository {
    Coupon getCouponWithLock(Long couponId);

    Coupon save(Coupon coupon);

    Coupon getCouponById(Long couponId);
}
