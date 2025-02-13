package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponCommandService {
    private final CouponRepository couponRepository;
    private final RedisTemplate<String, Integer> redisTemplate;
    private static final String CACHE_KEY_PREFIX = "remainingCoupon::";

    @Transactional
    public int decrementCouponQuantity(long couponId) {
        Coupon coupon = couponRepository.getCouponWithLock(couponId);
        coupon.validateCouponIssue();
        coupon.deductQuantity();
        couponRepository.save(coupon);

        String cacheKey = CACHE_KEY_PREFIX + couponId;
        redisTemplate.opsForValue().set(cacheKey, coupon.getRemainingQuantity());

        return coupon.getRemainingQuantity();
    }

    public Integer getCachedQuantity(long couponId) {
        String cacheKey = CACHE_KEY_PREFIX + couponId;
        Integer cachedQuantity = redisTemplate.opsForValue().get(cacheKey);

        if (cachedQuantity == null) {
            Coupon coupon = couponRepository.getCouponById(couponId);
            cachedQuantity = coupon.getRemainingQuantity();
            redisTemplate.opsForValue().set(cacheKey, cachedQuantity);
        }

        return cachedQuantity;
    }

    public void deductQuantity(final Long couponId) {
        final Coupon coupon = couponRepository.getCouponById(couponId);
        coupon.validateCouponIssue();
        coupon.deductQuantity();
        couponRepository.save(coupon);
    }
}
