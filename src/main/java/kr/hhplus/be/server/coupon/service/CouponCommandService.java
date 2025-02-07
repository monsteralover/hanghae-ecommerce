package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
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
        String cacheKey = CACHE_KEY_PREFIX + couponId;

        Long newQuantity = redisTemplate.opsForValue().decrement(cacheKey);

        if (newQuantity < 0) {
            Coupon coupon = couponRepository.getCouponById(couponId);
            int dbQuantity = coupon.getRemainingQuantity();
            redisTemplate.opsForValue().set(cacheKey, dbQuantity);

            if (dbQuantity <= 0) {
                throw new ApiException(ApiResponseCodeMessage.OUT_OF_COUPON);
            }

            newQuantity = redisTemplate.opsForValue().decrement(cacheKey);
        }

        return newQuantity.intValue();
    }

    public void rollbackQuantity(long couponId) {
        String cacheKey = CACHE_KEY_PREFIX + couponId;
        redisTemplate.opsForValue().increment(cacheKey);
    }

    public void deductQuantity(final Long couponId) {
        final Coupon coupon = couponRepository.getCouponById(couponId);
        coupon.validateCouponIssue();
        coupon.deductQuantity();
        couponRepository.save(coupon);
    }
}
