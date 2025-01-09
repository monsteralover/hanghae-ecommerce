package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.coupon.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Coupon getCouponWithLock(final Long couponId) {
        return couponJpaRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new ApiException(ApiResponseCodeMessage.INVALID_COUPON));
    }

    @Override
    public Coupon save(final Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public Coupon getCouponById(final Long couponId) {
        return couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(ApiResponseCodeMessage.INVALID_COUPON));
    }

}
