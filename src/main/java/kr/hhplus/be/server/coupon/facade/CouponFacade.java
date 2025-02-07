package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.coupon.service.CouponCommandService;
import kr.hhplus.be.server.coupon.service.CouponIssueReadService;
import kr.hhplus.be.server.coupon.service.dto.CouponIssueResponse;
import kr.hhplus.be.server.coupon.service.dto.CouponUserResponse;
import kr.hhplus.be.server.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class CouponFacade {
    private final UserReadService userReadService;
    private final CouponIssueReadService couponIssueReadService;

    private final RedissonClient redissonClient;
    private static final String LOCK_PREFIX = "COUPON_LOCK:";
    private static final long WAIT_TIME = 3L;
    private static final long LEASE_TIME = 5L;

    private final CouponIssueFacade couponIssueFacade;

    private final CouponCommandService couponCommandService;

    public List<CouponUserResponse> getUsableCouponsForUser(Long userId) {
        userReadService.checkUserExistsById(userId);
        return couponIssueReadService.getUsableCouponsForUser(userId);
    }

    public CouponIssueResponse issueCouponForUser(Long userId, Long couponId) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + couponId);
        boolean decremented = false;
        try {
            boolean isLocked = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new ApiException(ApiResponseCodeMessage.FAILED_TO_ISSUE_COUPON);
            }
            // 캐시에서 차감
            int remainingQuantity = couponCommandService.decrementCouponQuantity(couponId);
            decremented = true;

            if (remainingQuantity < 0) {
                throw new ApiException(ApiResponseCodeMessage.OUT_OF_COUPON);
            }
            return couponIssueFacade.issueCoupon(userId, couponId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(ApiResponseCodeMessage.FAILED_TO_ISSUE_COUPON);

        } catch (Exception e) {
            if (decremented) {
                couponCommandService.rollbackQuantity(couponId);
            }
            throw new ApiException(ApiResponseCodeMessage.FAILED_TO_ISSUE_COUPON);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
