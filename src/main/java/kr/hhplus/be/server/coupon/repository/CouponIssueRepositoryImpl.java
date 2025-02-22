package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.coupon.domain.CouponIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {
    private final CouponIssueJpaRepository couponIssueJpaRepository;

    @Override
    public List<CouponIssue> getUsableCouponsForUser(final Long userId) {
        return couponIssueJpaRepository.findAllByUserIdAndUsed(userId, false);
    }

    @Override
    public CouponIssue save(final CouponIssue couponIssue) {
        return couponIssueJpaRepository.save(couponIssue);
    }

    @Override
    public CouponIssue getById(final long couponId) {
        return couponIssueJpaRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(ApiResponseCodeMessage.INVALID_COUPON));
    }

    @Override
    public long countByCouponId(final Long couponId) {
        return couponIssueJpaRepository.countByCouponId(couponId);
    }

    @Override
    public void deleteAll() {
        couponIssueJpaRepository.deleteAll();
    }
}
